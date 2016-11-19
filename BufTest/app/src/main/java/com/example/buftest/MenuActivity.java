package com.example.buftest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    protected void gotoAddMenu(View v){
        Intent gotoAddMenu = new Intent(this, AddMenuActivity.class);
        startActivity(gotoAddMenu);
    }
}
