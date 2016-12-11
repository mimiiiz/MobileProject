package com.example.buftest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.buftest.model.Code;
import com.example.buftest.model.Menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private ListView lv_listMenu;
    private DatabaseReference mDatabase;
    private ArrayList<Menu> menuLs;
    private ArrayList<String> menuNames;
    private Menu editedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        lv_listMenu = (ListView) findViewById(R.id.lv_listMenu);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Menu");
        mQ.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         menuLs = new ArrayList<Menu>();
                                         menuNames = new ArrayList<String>();
                                         for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                                             Menu menuObj = new Menu();
                                             menuObj = mSnap.getValue(Menu.class);
                                             menuLs.add(menuObj);
                                             menuNames.add(menuObj.getMenuName());
                                         }
                                         Log.d("Menu, onCreate", "onDataChange: Size of arrayList Menu " + menuLs.size());

                                         lv_listMenu.setAdapter(new ArrayAdapter<String>(MenuActivity.this, android.R.layout.simple_list_item_1, menuNames));
                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 }

        );

        lv_listMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                showDeleteMenuDialog(position);
                return true;
            }
        });

        lv_listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                editMenuDialog(position);
            }
        });

    }

    private void editMenuDialog(final int position) {

        final Dialog menuDialog = new Dialog(MenuActivity.this);
        menuDialog.setContentView(R.layout.dialog_edit_menu);

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, num);

        final EditText et_dialog_menuName = (EditText) menuDialog.findViewById(R.id.dialog_menuName);
        final Spinner spn_dialog_max = (Spinner) menuDialog.findViewById(R.id.dialog_spn_setMax);

        Button btn_dialog_edit_btn_done = (Button) menuDialog.findViewById(R.id.dialog_edit_btn_done);

        et_dialog_menuName.setText(menuLs.get(position).getMenuName());
        spn_dialog_max.setAdapter(adapter);

        btn_dialog_edit_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editedMenu = new Menu();
                editedMenu.setMenuName(et_dialog_menuName.getText().toString());
                editedMenu.setMax(Integer.parseInt(spn_dialog_max.getSelectedItem().toString()));

                updateMenu(position);
                menuDialog.dismiss();
            }
        });

        menuDialog.show();

    }

    protected void gotoAddMenu(View v) {
        Intent gotoAddMenu = new Intent(this, AddMenuActivity.class);
        startActivity(gotoAddMenu);
    }

    public boolean showDeleteMenuDialog(final Integer position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
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