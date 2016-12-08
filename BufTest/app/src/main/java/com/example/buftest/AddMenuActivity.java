package com.example.buftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.buftest.model.Menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddMenuActivity extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected String menuName;
    protected Spinner spnMax, spnMin;
    protected EditText et_MenuName;
    protected  Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, num);
        spnMax = (Spinner) findViewById(R.id.spn_setMax);
        spnMax.setAdapter(adapter);
//        spnMin = (Spinner) findViewById(R.id.spn_setMin);
//        spnMin.setAdapter(adapter);

        //if name != null , set btn enable
        et_MenuName = (EditText) findViewById(R.id.et_menuName);
        et_MenuName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                menuName = et_MenuName.getText().toString();
                if (!menuName.equals("")){
                    btn_add.setEnabled(true);
                }else {
                    btn_add.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_MenuName = (EditText) findViewById(R.id.et_menuName);
                spnMax = (Spinner) findViewById(R.id.spn_setMax);
                Menu menuObj = new Menu();
                menuObj.setMenuName(et_MenuName.getText().toString());
                menuObj.setMax(Integer.parseInt(spnMax.getSelectedItem().toString()));

                mDatabase = FirebaseDatabase.getInstance().getReference();
                String keyGen = mDatabase.push().getKey();
                mDatabase.child("Menu").child(keyGen).setValue(menuObj);

            }
        });
    }

}
