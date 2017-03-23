package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RegCreateMenu extends AppCompatActivity {

    MenuItem mItem;
    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> prices = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<String> foodTypes = new ArrayList<String>(); //chosen food types from previous activity
    CustomMenuAdapter adapter;
    String[] rInfo = new String[5]; //restaurant information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_create_menu);

        Intent intent = getIntent();
        rInfo = intent.getStringArrayExtra("restInfo");
        foodTypes = getIntent().getExtras().getStringArrayList("foodTypes");

        setTitle("Register"); //set activity title

        adapter = new CustomMenuAdapter(this, names, prices, descriptions);
        final ListView listView = (ListView) findViewById(R.id.lstMenu);
        listView.setAdapter(adapter);

        Button mButton = (Button) findViewById(R.id.btnNewItem);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegCreateMenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialogue_menu, null);
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
                            Toast.makeText(RegCreateMenu.this, "Fill all Fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String n = mName.getText().toString();
                            Double p = Double.parseDouble(mPrice.getText().toString());
                            String d = mDescription.getText().toString();
                            mItem = new MenuItem(n, p, d);
                            items.add(mItem);
                            updateNames();
                            adapter.notifyDataSetChanged(); //redraw the list on the screen
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String n = items.get(position).getName();
                Double p = items.get(position).getPrice();
                String d = items.get(position).getDescription();
                final int spot = position;

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegCreateMenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialogue_menu, null);
                final EditText mName = (EditText) mView.findViewById(R.id.txtItemName);
                final EditText mPrice = (EditText) mView.findViewById(R.id.txtPrice);
                final EditText mDescription = (EditText) mView.findViewById(R.id.txtDescription);
                Button mSave = (Button) mView.findViewById(R.id.btnSaveItem);

                mName.setText(n);
                mPrice.setText(p.toString());
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
                            Toast.makeText(RegCreateMenu.this, "Fill all Fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String n = mName.getText().toString();
                            Double p = Double.parseDouble(mPrice.getText().toString());
                            String d = mDescription.getText().toString();
                            mItem = new MenuItem(n, p, d);
                            items.set(spot, mItem);
                            updateNames();
                            adapter.notifyDataSetChanged(); //redraw the list on the screen
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    void updateNames(){
        names.clear();
        prices.clear();
        descriptions.clear();
        for(int i = 0; i < items.size();i++){
            String row = items.get(i).getName();
            names.add(row);
            row = "$"+items.get(i).getPrice();
            prices.add(row);
            row = items.get(i).getDescription();
            descriptions.add(row);
        }
    }

    public class MenuItem{
        private String name;
        private double price;
        private String description;

        MenuItem(){
            name = "";
            price = 0.00;
            description = "";
        }

        MenuItem(String n, Double p, String d){
            name = n;
            price  = p;
            description = d;
        }
        String getName(){return name;}
        Double getPrice(){return price;}
        String getDescription(){return description;}

        void setName(String n){name = n;}
        void setPrice(Double p){price = p;}
        void setDescription(String d){description = d;}
    }

    public void clickFinishRegistration(View view){

        //send restraunt info to server

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
