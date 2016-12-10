package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    private ListView lv_listOrder;
    private DatabaseReference mDatabase;
    private ArrayList<Order> orderLs;
    private ArrayList<String> order_string;
    private Map<String, Menu> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        lv_listOrder = (ListView) findViewById(R.id.lv_listOrder);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query mQ = mDatabase.child("Order").orderByChild("timestamp");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderLs = new ArrayList<Order>();
                order_string = new ArrayList<String>();
                lv_listOrder = (ListView) findViewById(R.id.lv_listCode);
                Log.d("dataSnap", dataSnapshot.toString());
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    Log.d("mSnap", mSnap.toString());
                    Order orderObj = new Order();
                    orderObj = mSnap.getValue(Order.class);
                    orderLs.add(orderObj);
                }
                Log.d("Order, onCreate", "onDataChange: Size of arrayList order " + orderLs.size());
                Log.d("Order, onCreate", "onDataChange:menu name  " + orderLs.get(0).getMenus().get("-KYUfikpF_-2gsAoZS0i").getMenuName()); // beef

                for (Order order1: orderLs  ) {
                    Map<String, Menu> map = order1.getMenus();
                    for (Map.Entry<String, Menu> entry : map.entrySet())
                    {
                        Log.d("entryyyy ", entry.getKey() + "/" + entry.getValue().getMenuName());
                    }
                }


            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        }
        );
    }
}
