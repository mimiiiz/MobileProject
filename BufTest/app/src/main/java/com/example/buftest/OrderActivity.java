package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    NumberPicker numPick = (NumberPicker) findViewById(R.id.numPick);


    public void setNumPick(NumberPicker numPick) {
        this.numPick = numPick;
        numPick.setMaxValue(5);
        numPick.setMinValue(0);
    }
}
