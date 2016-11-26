package com.example.buftest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buftest.model.Code;
import com.example.buftest.model.Order;
import com.example.buftest.model.Queues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueueActivity extends AppCompatActivity {

    protected ListView lv_listQueue;
    protected DatabaseReference mDatabase;
    protected ArrayList<String> orderLs;
    protected ArrayList<Order> orderArrayList;
    protected Queues queuesObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        lv_listQueue = (ListView) findViewById(R.id.lv_listQueue);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query mQ = mDatabase.child("Order").orderByChild("timestamp");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderLs = new ArrayList<String>();
                for (DataSnapshot mSnap : dataSnapshot.getChildren()){
                    Log.d("mSnap", mSnap.toString());
                    queuesObj = mSnap.getValue(Queues.class);
                    orderLs.add(queuesObj.getTableNo() + "\n" + queuesObj.getCode()+ "\n" + queuesObj.getTimestamp());
//                    orderLs.add(queuesObj.getTableNo());
                    queuesObj.setMenuOrdered(orderArrayList);
//                    Log.d("-----------", "--------------------------");
//                    Log.d("menu children",mSnap.child("menus").hasChildren()+"");
//                    orderArrayList = new ArrayList<Order>();
//                    for (DataSnapshot newSnap : mSnap.child("menus").getChildren()){
//                        Log.d("newSnap>>>>", newSnap.toString());
//                        Order orderObj = newSnap.getValue(Order.class);
//                        orderArrayList.add(orderObj);
////                        Log.d("OrderObj : " , orderObj.getMenuName());
                    }
//                    queuesObj.setMenuOrdered(orderArrayList);
//                    for (Order order:orderArrayList) {
//                        Log.d("orderrrrrr", order.getMenuName());
//                        Log.d("orderrrrrr", order.getAmount());
//                        Log.d("====", "========");
//                    }
//                }
//                Log.d("orderArrayList(0)",orderArrayList.get(0).getMenuName());
//                Log.d("orderArrayList(1)",orderArrayList.get(1).getMenuName());
                lv_listQueue.setAdapter(new ArrayAdapter<String>(QueueActivity.this, android.R.layout.simple_list_item_1, orderLs));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv_listQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String[] itemFromLV = lv_listQueue.getItemAtPosition(position).toString().split("\n");
                Toast.makeText(QueueActivity.this, itemFromLV[0] + ", " + itemFromLV[1], Toast.LENGTH_SHORT).show();

                Intent gotoOrderDetail = new Intent(QueueActivity.this, OrderDetailActivity.class);
                gotoOrderDetail.putExtra("TableNo", itemFromLV[0]);
                gotoOrderDetail.putExtra("Code", itemFromLV[1]);
                gotoOrderDetail.putExtra("Timestamp", itemFromLV[2]);

                startActivity(gotoOrderDetail);

            }
        });

    }
}
