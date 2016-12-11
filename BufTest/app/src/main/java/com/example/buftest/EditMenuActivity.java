package com.example.buftest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.example.buftest.model.Menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class EditMenuActivity extends AppCompatActivity {

    private ListView lv_listEditMenu;
    private DatabaseReference mDatabase;
    private ArrayList<Menu> menuLs;
    private ArrayList<String> menuNames;
    private Menu editedMenu;
    private ImageView dialog_imgBtn_pickFromAlbum;
    private FirebaseStorage mStorage;
    private ArrayList<Image> imagesPic;
    private String keygen = "";
    private Uri uriFile;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("Edit menu");

        Intent getIntent = getIntent();
        menuLs = (ArrayList<Menu>) getIntent.getSerializableExtra("menuLs");
        menuNames = new ArrayList<>();

        /** Get Database Ref **/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        /** Get Storage Ref **/
        storageRef = storage.getReferenceFromUrl("gs://leanbillbuffet.appspot.com");

        lv_listEditMenu = (ListView) findViewById(R.id.lv_listMenuEdit);
        for (Menu menu0: menuLs ) {
            menuNames.add(menu0.getMenuName());
        }
        lv_listEditMenu.setAdapter(new ArrayAdapter<String>(EditMenuActivity.this, android.R.layout.simple_list_item_1, menuNames));


        lv_listEditMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                showDeleteMenuDialog(position);
                return true;
            }
        });

        lv_listEditMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                editMenuDialog(position);
            }
        });
    }

    private void editMenuDialog(final int position) {

        final Dialog menuDialog = new Dialog(EditMenuActivity.this);
        menuDialog.setContentView(R.layout.dialog_edit_menu);

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, num);

        final EditText et_dialog_menuName = (EditText) menuDialog.findViewById(R.id.dialog_menuName);
        final Spinner spn_dialog_max = (Spinner) menuDialog.findViewById(R.id.dialog_spn_setMax);

        Button btn_dialog_edit_btn_done = (Button) menuDialog.findViewById(R.id.dialog_edit_btn_done);

        et_dialog_menuName.setText(menuLs.get(position).getMenuName());
        keygen = menuLs.get(position).getKey();


        //get pet img
        mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReferenceFromUrl("gs://leanbillbuffet.appspot.com");
        storageRef.child("Menu/" + menuLs.get(position).getKey() + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).fitCenter().centerCrop().into(dialog_imgBtn_pickFromAlbum);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Slid", exception.getMessage());
            }
        });

        spn_dialog_max.setAdapter(adapter);
        spn_dialog_max.setSelection(menuLs.get(position).getMax());

        dialog_imgBtn_pickFromAlbum = (ImageView) menuDialog.findViewById(R.id.dialog_imgBtn_pickFromAlbum);
        dialog_imgBtn_pickFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImgFromAlbumMenu();
            }
        });

        btn_dialog_edit_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editedMenu = new Menu();
                editedMenu.setMenuName(et_dialog_menuName.getText().toString());
                editedMenu.setStatus(menuLs.get(position).getStatus());
                editedMenu.setKey(menuLs.get(position).getKey());
                editedMenu.setMax(Integer.parseInt(spn_dialog_max.getSelectedItem().toString()));

                updateMenu(position);
                menuDialog.dismiss();
            }
        });

        menuDialog.show();

    }

    private void pickImgFromAlbumMenu() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            imagesPic = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            storeImage(keygen, imagesPic);
        }
    }

    private void storeImage(String menuKey, ArrayList<Image> images) {
        for (int i = 0; i < images.size(); i++) {
            Log.i("pathhhhhhhhhhh", images.get(i).path);
            uriFile = Uri.fromFile(new File(images.get(i).path));
            StorageReference imgRef = storageRef.child("Menu/" + menuKey + "/" + i);
            UploadTask uploadTask = imgRef.putFile(uriFile);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }

        /*** Set image view ***/

        Glide.with(getApplicationContext()).load(uriFile).fitCenter().centerCrop().into(dialog_imgBtn_pickFromAlbum);
//        dialog_imgBtn_pickFromAlbum.setVisibility(View.VISIBLE);
    }

    public boolean showDeleteMenuDialog(final Integer position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(EditMenuActivity.this);
        alert.setTitle("Delete this menu?");
        alert.setMessage("Do you want to delete this menu? \n It make this menu cannot order");

        alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteMenu(position);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            //user click cancle
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
        return true;
    }

    protected void deleteMenu(final int position) {
        mDatabase.child("Menu").orderByChild("menuName").equalTo(menuLs.get(position).getMenuName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                    Log.d("firstChild", firstChild.getRef() + "");
                    firstChild.getRef().removeValue();
                    menuLs.remove(position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    protected void updateMenu(final int position) {
        mDatabase.child("Menu").orderByChild("menuName").equalTo(menuLs.get(position).getMenuName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                    firstChild.getRef().setValue(editedMenu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
