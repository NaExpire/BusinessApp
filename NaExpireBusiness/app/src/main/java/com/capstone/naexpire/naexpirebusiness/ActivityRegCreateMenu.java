package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityRegCreateMenu extends AppCompatActivity {

    ArrayList<String> foodTypes = new ArrayList<String>(); //chosen food types from previous activity
    ListAdapterMenu adapter;
    String[] rInfo = new String[5]; //restaurant information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_create_menu);

        Intent intent = getIntent();
        rInfo = intent.getStringArrayExtra("restInfo");
        foodTypes = getIntent().getExtras().getStringArrayList("foodTypes");

        setTitle("Register"); //set activity title

        View footer =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_add, null);

        adapter = new ListAdapterMenu(this);
        final ListView listView = (ListView) findViewById(R.id.lstMenu);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRegCreateMenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText mName = (EditText) mView.findViewById(R.id.txtItemName);
                final EditText mPrice = (EditText) mView.findViewById(R.id.txtPrice);
                final EditText mDescription = (EditText) mView.findViewById(R.id.txtDescription);
                Button mSave = (Button) mView.findViewById(R.id.btnSaveItem);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mSave.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(mName.getText().toString().isEmpty() ||
                                mPrice.getText().toString().isEmpty() ||
                                mDescription.getText().toString().isEmpty()){
                            Toast.makeText(ActivityRegCreateMenu.this, "Fill all Fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String n = mName.getText().toString();
                            String p = "$" + Double.parseDouble(mPrice.getText().toString());
                            String d = mDescription.getText().toString();
                            adapter.newItem(n, p, d);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String n = adapter.getName(position);
                String p = adapter.getPrice(position);
                String d = adapter.getDescrip(position);
                final int spot = position;

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRegCreateMenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText mName = (EditText) mView.findViewById(R.id.txtItemName);
                final EditText mPrice = (EditText) mView.findViewById(R.id.txtPrice);
                final EditText mDescription = (EditText) mView.findViewById(R.id.txtDescription);
                Button mSave = (Button) mView.findViewById(R.id.btnSaveItem);

                mName.setText(n);
                mPrice.setText(p.substring(1));
                mDescription.setText(d);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mSave.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(mName.getText().toString().isEmpty() ||
                                mPrice.getText().toString().isEmpty() ||
                                mDescription.getText().toString().isEmpty()){
                            Toast.makeText(ActivityRegCreateMenu.this, "Fill all Fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String n = mName.getText().toString();
                            String p = "$"+Double.parseDouble(mPrice.getText().toString());
                            String d = mDescription.getText().toString();
                            adapter.editItem(position, n, p, d);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void clickFinishRegistration(View view){

        //send restraunt info to server

        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
}
