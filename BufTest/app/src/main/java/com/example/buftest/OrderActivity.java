package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buftest.adapter.OrderListAdapter;
import com.example.buftest.model.Menu;
import com.example.buftest.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<Order> orderLs;

    ValueEventListener orderListener;
    Query mQ;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQ = mDatabase.child("Order").orderByChild("timestamp");
        mRecyclerView = (RecyclerView) findViewById(R.id.order_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderLs = new ArrayList();
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    Order orderObj = mSnap.getValue(Order.class);
                    orderLs.add(orderObj);
                }
                /*
                for (Order order1: orderLs  ) {
                    Map<String, Menu> map = order1.getMenus();
                    for (Map.Entry<String, Menu> entry : map.entrySet())
                    {
                        Log.d("entryyyy ", entry.getKey() + "/" + entry.getValue().getMenuName());
                    }
                }
                */
                mAdapter = new OrderListAdapter(orderLs, OrderActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mQ.addValueEventListener(orderListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderListener != null){
            mQ.removeEventListener(orderListener);
        }
    }
}
