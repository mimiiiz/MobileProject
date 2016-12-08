package com.example.buftest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.buftest.model.Code;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewCodeActivity extends AppCompatActivity {

    private ListView lv_listCode;
    private DatabaseReference mDatabase;
    private ArrayList<Code> codeLs;
    private ArrayList<String> code_n_tableNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_code);

        lv_listCode = (ListView) findViewById(R.id.lv_listCode);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Code").orderByChild("timestamp").limitToLast(25);
        mQ.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                codeLs = new ArrayList<Code>();
                code_n_tableNames = new ArrayList<String>();
                lv_listCode = (ListView) findViewById(R.id.lv_listCode);
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    Code codeObj = new Code();
                    codeObj = mSnap.getValue(Code.class);
                    codeLs.add(codeObj);
                    code_n_tableNames.add(codeObj.getTableNo() + " : " + codeObj.getCode() + " , " + codeObj.getTimestamp());
                }
                Log.d("ViewCode, onCreate", "onDataChange: Size of arrayList Code " + codeLs.size());
                lv_listCode.setAdapter(new ArrayAdapter<String>(ViewCodeActivity.this, android.R.layout.simple_list_item_1, code_n_tableNames));

            }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            }

            );
        }

    public void gotoAddNewCode(View view) {
        startActivity(new Intent(ViewCodeActivity.this, GenCodeActivity.class));
    }



}
