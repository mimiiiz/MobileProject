package com.example.buftest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected Button btnAddMenu, btnQueue, btnGenCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btnAddMenu = (Button) findViewById(R.id.btn_addMenu);
//        btnQueue = (Button) findViewById(R.id.btn_queue);
//        btnGenCus = (Button) findViewById(R.id.btn_genCustomer);

    }


    protected void gotoMenu(View v){
        Intent gotoMenu = new Intent(this, MenuActivity.class);
        startActivity(gotoMenu);
    }

    protected void gotoOrder(View v){
        startActivity(new Intent(this, OrderActivity.class));
    }

    protected void gotoGenCustomer(View v){
        Toast.makeText(this, "go to gen customer", Toast.LENGTH_SHORT).show();
        Intent gotoGenCustomer = new Intent(this, ViewCodeActivity.class);
        startActivity(gotoGenCustomer);
    }


}
