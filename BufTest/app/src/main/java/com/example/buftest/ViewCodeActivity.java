package com.example.buftest;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buftest.model.Code;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewCodeActivity extends AppCompatActivity {

    private ListView lv_listCode;
    private DatabaseReference mDatabase;
    private ArrayList<Code> codeLs;
    private ArrayList<String> code_n_tableNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_code);

        lv_listCode = (ListView) findViewById(R.id.lv_listCode);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQ = mDatabase.child("Code").orderByChild("timestamp").limitToLast(25);
        mQ.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                codeLs = new ArrayList<Code>();
                code_n_tableNames = new ArrayList<String>();
                lv_listCode = (ListView) findViewById(R.id.lv_listCode);
                for (DataSnapshot mSnap : dataSnapshot.getChildren()) {
                    Code codeObj = new Code();
                    codeObj = mSnap.getValue(Code.class);
                    codeLs.add(codeObj);
                    code_n_tableNames.add(codeObj.getTableNo());
                }
                Log.d("ViewCode, onCreate", "onDataChange: Size of arrayList Code " + codeLs.size());
                lv_listCode.setAdapter(new ArrayAdapter<String>(ViewCodeActivity.this, android.R.layout.simple_list_item_1, code_n_tableNames));

            }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            }

            );

        lv_listCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(ViewCodeActivity.this, adapterView.getItemAtPosition(position) + "", Toast.LENGTH_SHORT).show();
                setCodeDialog(position);

            }
        });

        }

    public void gotoAddNewCode(View view) {
        startActivity(new Intent(ViewCodeActivity.this, GenCodeActivity.class));
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMM yyyy ");
        String dateString = sdf.format(date);
        return dateString;
    }

    public void setCodeDialog(Integer position){
        Dialog codeDialog = new Dialog(ViewCodeActivity.this);
        codeDialog.setContentView(R.layout.dialog_code_info);

        TextView tv_dialog_tableName = (TextView) codeDialog.findViewById(R.id.dialog_table_name);
        TextView tv_dialog_code = (TextView) codeDialog.findViewById(R.id.dialog_code);
        TextView tv_dialog_time = (TextView) codeDialog.findViewById(R.id.dialog_timestamp);
        Button btn_dialog_ok = (Button) codeDialog.findViewById(R.id.dialog_btn_ok);

        tv_dialog_tableName.setText(codeLs.get(position).getTableNo());
        tv_dialog_code.setText(codeLs.get(position).getCode());
        tv_dialog_time.setText(formatDate(codeLs.get(position).getTimestamp()));

        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        codeDialog.show();

    }



}
