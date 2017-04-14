package com.capstone.naexpire.naexpirebusiness;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentEditFoodTypes extends Fragment {

    DatabaseHelperFoods dbHelper = null;

    ListAdapterFoodType adapter;
    ArrayList<String> foods = new ArrayList<String>();
    ArrayList<String> checked = new ArrayList<String>();


    public FragmentEditFoodTypes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //base food types that the app starts with
        foods.add("Mexican");
        foods.add("Italian");
        foods.add("Indian");
        foods.add("Cajun");
        foods.add("Thai");
        foods.add("Greek");

        /*f
        //initial setup for testing
        dbHelper = new DatabaseHelperFoods(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        for(int i = 0; i < fTypes.size(); i++){
            value.put("name", fTypes.get(i));
            value.put("checked", "no");
            db.insert("foods", null, value);
        }
        fTypes.clear();
        checked.clear();
        db.close();*/

        //get checked foods from db
        dbHelper = new DatabaseHelperFoods(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT checked FROM foods", null);

        while(result.moveToNext()){
            checked.add(result.getString(0));
        }
        db.close();

        updateFoods();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_food_types, container, false);
        View footer = inflater.inflate(R.layout.footer_add, null);

        adapter = new ListAdapterFoodType(FragmentEditFoodTypes.this.getContext(), foods, checked);
        final ListView listView = (ListView) view.findViewById(R.id.lstTypes);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);
        Button acctInfo = (Button) view.findViewById(R.id.btnacctInfo);
        Button save = (Button) view.findViewById(R.id.btnSaveFoods);

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FragmentEditFoodTypes.this.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_food_type, null);
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
                            foods.add(newFood);
                            checked.add(newFood);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(FragmentEditFoodTypes.this.getContext(),
                                    "food type name cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        acctInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentAccountInfo fragmentAccountInfo = new FragmentAccountInfo();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, fragmentAccountInfo).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checked = adapter.getChecked();

                dbHelper = new DatabaseHelperFoods(getActivity().getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("foods", null, null);

                ContentValues value = new ContentValues();
                for(int i = 0; i < checked.size(); i++){
                    value.put("checked", checked.get(i));
                    db.insert("foods", null, value);
                }
                db.close();
                Toast.makeText(FragmentEditFoodTypes.this.getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void updateFoods(){
        for(int i = 0; i < checked.size(); i++){
            boolean found = false;
            for(int j = 0; j < foods.size(); j++){
                if((checked.get(i).equals(foods.get(j)))) found = true;
            }
            if(!found) foods.add(checked.get(i));
        }
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
