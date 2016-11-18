package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMenuActivity extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected String keygen;
    protected Spinner spnMax, spnMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        String[] num = {"0","1","2","3","4","5","6","7","8","9","10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, num);
        spnMax = (Spinner) findViewById(R.id.spn_setMax);
        spnMax.setAdapter(adapter);
//        spnMin = (Spinner) findViewById(R.id.spn_setMin);
//        spnMin.setAdapter(adapter);

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.setValue("mi1");

    }
}
