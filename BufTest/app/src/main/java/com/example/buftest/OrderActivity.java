package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        final TextView countPork = (TextView) findViewById(R.id.tv_countPork);
        NumberPicker numPick = (NumberPicker) findViewById(R.id.numPick);
        numPick.setMaxValue(5);
        numPick.setMinValue(0);
        numPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                countPork.setText(newValue);
            }
        });
    }



}
