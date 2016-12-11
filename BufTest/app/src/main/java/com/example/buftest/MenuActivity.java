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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.example.buftest.adapter.MenuCustomList;
import com.example.buftest.model.Code;
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

public class MenuActivity extends AppCompatActivity {

    private ListView lv_listMenu;
    private DatabaseReference mDatabase;
    private ArrayList<Menu> menuLs;
    private ArrayList<String> menuNames;

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
                 menuLs = new ArrayList();
                 menuNames = new ArrayList();
                 for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                     Menu menuObj = mSnap.getValue(Menu.class);
                     menuLs.add(menuObj);
                     menuNames.add(menuObj.getMenuName());
                 }
                 MenuCustomList adapter = new MenuCustomList(MenuActivity.this,menuLs);
                 lv_listMenu.setAdapter(adapter);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         }
        );
    }

    public void gotoEditMenu(View view){
        Intent gotoEditMenu = new Intent(MenuActivity.this, EditMenuActivity.class);
        gotoEditMenu.putExtra("menuLs", menuLs);
        startActivity(gotoEditMenu);
    }


}