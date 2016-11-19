package com.example.buftest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buftest.model.Code;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {

    protected ListView listCode;
    protected DatabaseReference mDatabase;
    protected ArrayList<String> codeLs;
    protected Code newCode;
    protected String tableNo;
    protected EditText et_tableNo;
    protected final String alphaSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected SecureRandom secRandom;
    protected Button btn_genCode;


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
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    Code codeObj = mSnap.getValue(Code.class);
//                    Log.d(">>>>>", codeObj.getCode());
                    codeLs.add(codeObj.getTableNo() + ": \t\t\t\t" + codeObj.getCode() + "\t\t\t\t" + codeObj.getTimestamp());
                }
                listCode.setAdapter(new ArrayAdapter<String>(CodeActivity.this, android.R.layout.simple_list_item_1, codeLs));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn_genCode = (Button) findViewById(R.id.btn_genCode);
        et_tableNo = (EditText) findViewById(R.id.et_tableNo);
        et_tableNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tableNo = et_tableNo.getText().toString();
                if (!tableNo.equals("")) {
                    btn_genCode.setEnabled(true);
                } else {
                    btn_genCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void genCode(View v) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMM yyyy ");
        Date resultdate = new Date(System.currentTimeMillis());

        Code newCode = new Code();
        newCode.setTableNo(tableNo);
        newCode.setTimestamp(sdf.format(resultdate) + "");
        newCode.setCode(randomString(8));

        //push to firebase
        String keyGen = mDatabase.push().getKey();
        try {
            mDatabase.child("Code").child(keyGen).setValue(newCode);
            Toast.makeText(CodeActivity.this, newCode.getCode(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(CodeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    protected String randomString ( int len){
        secRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < 8; i++)
            sb.append(alphaSet.charAt(secRandom.nextInt(alphaSet.length())));
        return sb.toString();
    }
}
