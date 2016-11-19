package com.example.buftest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buftest.model.Code;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {

    protected ListView listCode;
    protected DatabaseReference mDatabase;
    protected ArrayList<String> codeLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        listCode = (ListView) findViewById(R.id.lv_listCode);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Code").limitToLast(25);
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                codeLs = new ArrayList<String>();
                for (DataSnapshot mSnap : dataSnapshot.getChildren()){
                    Code codeObj = mSnap.getValue(Code.class);
//                    Log.d(">>>>>", codeObj.getCode());
                    codeLs.add(codeObj.getTableNo() + ": \t\t\t\t"+ codeObj.getCode() +  "\t\t\t\t" + codeObj.getTimestamp());
                }
                listCode.setAdapter(new ArrayAdapter<String>(CodeActivity.this, android.R.layout.simple_list_item_1, codeLs));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }

    public void gotoAddNewCode(View v){
        Intent gotoGenCode = new Intent(this, GenCodeActivity.class);
        startActivity(gotoGenCode);
    }
}
