package com.example.buftest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class AddMenuActivity extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected String menuName;
    protected Spinner spnMax, spnMin;
    protected EditText et_MenuName;
    protected Button btn_add;
    private ImageView imgv_menuPhoto;
    private ArrayList<Image> imagesMenu;
    private Uri uriFile;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String keyGen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, num);
        spnMax = (Spinner) findViewById(R.id.spn_setMax);
        spnMax.setAdapter(adapter);
//        spnMin = (Spinner) findViewById(R.id.spn_setMin);
//        spnMin.setAdapter(adapter);

        //if name != null , set btn enable
        et_MenuName = (EditText) findViewById(R.id.et_menuName);
        et_MenuName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                menuName = et_MenuName.getText().toString();
                if (!menuName.equals("")){
                    btn_add.setEnabled(true);
                }else {
                    btn_add.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        imgv_menuPhoto = (ImageView) findViewById(R.id.imgv_petPhoto);
        imagesMenu = new ArrayList<>();

        /** Get Database Ref **/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        /** Get Storage Ref **/
        storageRef = storage.getReferenceFromUrl("gs://leanbillbuffet.appspot.com");
        keyGen = mDatabase.push().getKey();
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_MenuName = (EditText) findViewById(R.id.et_menuName);
                spnMax = (Spinner) findViewById(R.id.spn_setMax);
                Menu menuObj = new Menu();
                menuObj.setKey(keyGen);
                menuObj.setMenuName(et_MenuName.getText().toString());
                menuObj.setMax(Integer.parseInt(spnMax.getSelectedItem().toString()));
                menuObj.setStatus(1);

                mDatabase.child("Menu").child(keyGen).setValue(menuObj);
                Toast.makeText(AddMenuActivity.this, "add successful !", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    public void pickImgFromAlbum(View view) {
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
            imagesMenu = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Toast.makeText(AddMenuActivity.this, "Add Images Successfully", Toast.LENGTH_SHORT).show();
            storeImage(keyGen, imagesMenu);
        }
    }

    private void storeImage(String menuKey, ArrayList<Image> images) {
        for (int i = 0; i < images.size(); i++) {
            Log.i("pathhhhhhhhhhh", images.get(i).path);
            uriFile = Uri.fromFile(new File(images.get(i).path));
            StorageReference imgRef = storageRef.child("Menu/" + menuKey  + "/" + i);
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

        Glide.with(getApplicationContext()).load(uriFile).fitCenter().centerCrop().into(imgv_menuPhoto);
        imgv_menuPhoto.setVisibility(View.VISIBLE);
    }

}
