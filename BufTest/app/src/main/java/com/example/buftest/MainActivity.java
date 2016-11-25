package com.example.buftest;

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

    protected void gotoOrder(View v){
        Intent gotoOrder = new Intent(this, OrderActivity.class);
        startActivity(gotoOrder);
    }

    protected void gotoMenu(View v){
        Intent gotoMenu = new Intent(this, MenuActivity.class);
        startActivity(gotoMenu);
    }

    protected void gotoQueue(View v){
        Intent gotoQueue = new Intent(this, QueueActivity.class);
        startActivity(gotoQueue);
    }

    protected void gotoCode(View v){
        Intent gotoCode = new Intent(this, CodeActivity.class);
        startActivity(gotoCode);
    }


}
