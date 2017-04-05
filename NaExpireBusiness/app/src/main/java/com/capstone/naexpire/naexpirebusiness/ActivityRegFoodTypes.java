package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityRegFoodTypes extends AppCompatActivity {

    ArrayList<String> fTypes = new ArrayList<String>(); //list of all food types
    ArrayList<String> checked = new ArrayList<String>(); //list of currently selected food types
    String[] rInfo = new String[5];
    ListAdapterFoodType adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_food_types);

        Intent intent = getIntent();
        rInfo = intent.getStringArrayExtra("restInfo");

        setTitle("Register"); //set activity title

        //initial food types for testing
        fTypes.add("Mexican");
        fTypes.add("Italian");
        fTypes.add("Indian");
        fTypes.add("Cajun");
        fTypes.add("Thai");
        fTypes.add("Greek");

        View footer =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_add, null);

        //set layout & data for the list's adapter
        adapter = new ListAdapterFoodType(this, fTypes, checked);
        final ListView listView = (ListView) findViewById(R.id.lstFoodType);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRegFoodTypes.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_new_food_type, null);
                final EditText newFoodType = (EditText) mView.findViewById(R.id.txtNewFoodType);
                Button saveFoodType = (Button) mView.findViewById(R.id.btnNewFoodType);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                saveFoodType.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String newFood = newFoodType.getText().toString();
                        if(!newFood.isEmpty()){
                            checked = adapter.getChecked();
                            fTypes.add(newFood);
                            checked.add(newFood);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(ActivityRegFoodTypes.this,
                                    "food type name cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*
    public void clickNewFood(View view){
        EditText newFood = (EditText) findViewById(R.id.txtNewFood);
        String nFood = newFood.getText().toString();
        if(nFood.length() != 0){ //if food type string is not empty
            list.add(nFood); //add the food type to the arraylist
            adapter.notifyDataSetChanged(); //redraw the list on the screen
            newFood.setText("");
            Toast toast = Toast.makeText(getApplicationContext(), nFood+" added", Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/

    public void clickFoodTypesNext(View View){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRegFoodTypes.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_skip_menu, null);
        Button yes = (Button) mView.findViewById(R.id.btnCreateMenuYes);
        Button later = (Button) mView.findViewById(R.id.btnCreateMenuLater);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), ActivityRegCreateMenu.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        later.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //send registration info to database

                Toast.makeText(getBaseContext(), "Account Created", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                dialog.dismiss();
            }
        });


    }
}
