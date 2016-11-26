package com.example.buftest;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buftest.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected ListView lv_orderDetail;
    protected String tableNo, code, timestamp;
    protected ArrayList<String> orderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent fromQueue = getIntent();
        tableNo = fromQueue.getStringExtra("TableNo");
        code = fromQueue.getStringExtra("Code");
        timestamp = fromQueue.getStringExtra("Timestamp");

        //set table detail
        setTitle(fromQueue.getStringExtra("TableNo"));

        TextView tv_tableNo, tv_code, tv_timestamp;
        tv_tableNo = (TextView) findViewById(R.id.tv_tableNoDetail);
        tv_code = (TextView) findViewById(R.id.tv_codeDetail);
        tv_timestamp = (TextView) findViewById(R.id.tv_timestampDetail);

        tv_tableNo.setText(tableNo);
        tv_code.setText(code);
        tv_timestamp.setText(timestamp);

        //set order detail
        lv_orderDetail = (ListView) findViewById(R.id.lv_orderDetail);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Order");

        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderArrayList = new ArrayList<String>();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    if (dataSnap.child("code").getValue().equals(code) && dataSnap.child("timestamp").getValue().equals(timestamp)) {
                        for (DataSnapshot newSnap : dataSnap.child("menus").getChildren()) {
                            Order orderObj = newSnap.getValue(Order.class);
                            orderArrayList.add(orderObj.getMenuName() + "\t\t\t\t" + orderObj.getAmount());
                        }
                        lv_orderDetail.setAdapter(new ArrayAdapter<String>(OrderDetailActivity.this, android.R.layout.simple_list_item_1, orderArrayList));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
