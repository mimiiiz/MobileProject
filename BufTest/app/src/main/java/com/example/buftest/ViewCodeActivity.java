package com.example.buftest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    private ListView listCode;
    private DatabaseReference mDatabase;
    private ArrayList<Code> codeLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_code);

        listCode = (ListView) findViewById(R.id.lv_listCode);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Code").orderByChild("timestamp").limitToLast(25);
        mQ.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                 Code codeObj = new Code();
                    codeObj = dataSnapshot.getValue(Code.class);
                    codeLs.add(codeObj);
                }

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
