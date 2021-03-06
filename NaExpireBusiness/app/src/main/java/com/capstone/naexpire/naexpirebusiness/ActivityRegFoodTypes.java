package com.capstone.naexpire.naexpirebusiness;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

//activity for setting what types of foods the restaurant serves during registration
public class ActivityRegFoodTypes extends AppCompatActivity {
    DatabaseHelperFoods dbHelper = null;

    ArrayList<String> foods = new ArrayList<String>(); //list of all food types
    ArrayList<String> checked = new ArrayList<String>(); //list of currently selected food types
    ListAdapterFoodType adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_food_types);

        setTitle("Register"); //set activity title

        //base food types that the app starts with
        foods.add("Mexican");
        foods.add("Cajun");
        foods.add("Vietnamese");
        foods.add("Chinese");
        foods.add("Mediterranean");
        foods.add("Japanese");
        foods.add("Indian");
        foods.add("Korean");
        foods.add("Italian");
        foods.add("Thai");
        foods.add("Greek");
        foods.add("Lebanese");
        foods.add("Moroccan");
        foods.add("French");
        foods.add("Spanish");
        foods.add("German");
        foods.add("Turkish");
        foods.add("Caribbean");

        View footer =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_add, null);

        //set layout & data for the list's adapter
        adapter = new ListAdapterFoodType(this, foods, checked);
        final ListView listView = (ListView) findViewById(R.id.lstFoodType);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        //when add new food type footer is tapped
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

                //add new food type to list of food types if name isn't empty
                saveFoodType.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String newFood = newFoodType.getText().toString();
                        if(!newFood.isEmpty()){
                            checked = adapter.getChecked();
                            foods.add(newFood);
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

    public void clickFoodTypesNext(View View){
        //send registration info to database

        //to local database
        checked = adapter.getChecked();

        dbHelper = new DatabaseHelperFoods(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("foods", null, null);

        ContentValues value = new ContentValues();
        for(int i = 0; i < checked.size(); i++){
            value.put("checked", checked.get(i));
            db.insert("foods", null, value);
        }
        db.close();

        Intent intent = new Intent(getBaseContext(), ActivityRegChoose.class);
        startActivity(intent);
    }
}
