package com.example.buftest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.buftest.R;
import com.example.buftest.model.Menu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mark on 12/11/2016.
 */

public class MenuCustomList extends ArrayAdapter<Menu> {
    ArrayList<Menu> menus;
    Activity context;
    public MenuCustomList(Activity context, ArrayList<Menu> menus) {
        super(context, R.layout.custom_list_item, menus);
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_item, null, true);
        final Switch rowSwitch = (Switch) rowView.findViewById(R.id.menu_switch);

        rowSwitch.setText(menus.get(position).getMenuName());

        rowSwitch.setChecked(false);
        if(menus.get(position).getStatus() != 0){
            rowSwitch.setChecked(true);
        }

        rowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String menuKey = menus.get(position).getKey();
                DatabaseReference menuTarget = FirebaseDatabase.getInstance().getReference().child("Menu").child(menuKey);
                if (b){
                    menuTarget.child("status").setValue(1);
                }else {
                    menuTarget.child("status").setValue(0);
                }

            }
        });

        return rowView;
    }
}
