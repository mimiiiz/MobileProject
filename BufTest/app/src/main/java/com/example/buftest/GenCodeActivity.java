package com.example.buftest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buftest.model.Code;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GenCodeActivity extends AppCompatActivity {

    private EditText et_tableName;
    private Button btn_genCode;
    protected final String alphaSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected final String numSet = "0123456789";
    protected SecureRandom secRandomNum, secRandomAlpha;
    private String randomNum, tableName;
    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_code);


        //check table no cannot null
        btn_genCode = (Button) findViewById(R.id.btn_genCode);
        et_tableName = (EditText) findViewById(R.id.et_tableName);
        et_tableName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tableName = et_tableName.getText().toString();
                if (!tableName.equals("")) {
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

    protected String randomString(int len) {
        secRandomAlpha = new SecureRandom();
        StringBuilder sb_alpha = new StringBuilder(len);
        for (int i = 0; i < 4; i++)
            sb_alpha.append(alphaSet.charAt(secRandomAlpha.nextInt(alphaSet.length())));

        secRandomNum = new SecureRandom();
        StringBuilder sb_num = new StringBuilder(len);
        for (int i = 0; i < 4; i++)
            sb_num.append(numSet.charAt(secRandomNum.nextInt(numSet.length())));

        Log.d("GenCode, randomString" , "random num = " + sb_num.toString() + sb_alpha.toString());
        return sb_num.toString() + sb_alpha.toString();
    }

    public void genCode(View v) {

//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMM yyyy ");
//        Date resultdate = new Date(System.currentTimeMillis());

        Code newCode = new Code();
        newCode.setTableNo(tableName);
//        newCode.setTimestamp(sdf.format(resultdate) + "");
        newCode.setTimestamp(new Date());
        newCode.setCode(randomString(8));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //push to firebase
        String keyGen = mDatabase.push().getKey();
        try {
            mDatabase.child("Code").child(keyGen).setValue(newCode);
            et_tableName.setText("");
            Toast.makeText(GenCodeActivity.this, newCode.getCode(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(GenCodeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }



}
