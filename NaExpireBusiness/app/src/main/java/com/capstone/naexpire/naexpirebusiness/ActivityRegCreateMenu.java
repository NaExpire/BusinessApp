package com.capstone.naexpire.naexpirebusiness;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//activity to create regular menu items during initial registration
public class ActivityRegCreateMenu extends AppCompatActivity {
    private DatabaseHelperMenu dbHelper = null;
    private SharedPreferences sharedPref;

    ListAdapterCreateMenu adapter;
    ImageView newItemImage;
    String foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_create_menu);

        setTitle("Register"); //set activity title

        View footer =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_add, null);

        adapter = new ListAdapterCreateMenu(this, ActivityRegCreateMenu.this);
        final ListView listView = (ListView) findViewById(R.id.lstMenu);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelperMenu(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT name, price, description, image FROM menu", null);

        while (result.moveToNext()) { //only add non grab-bag items
            if(!(result.getString(0)).equals(sharedPref.getString("restaurantName", "")+" Grab-Bag Meal"))
                adapter.newItem(result.getString(0), result.getString(1), result.getString(2),
                        result.getString(3));
        }

        result.close();
        db.close();

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ //new item footer tapped
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRegCreateMenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText newItemName = (EditText) mView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) mView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) mView.findViewById(R.id.txtDescription);
                newItemImage = (ImageView) mView.findViewById(R.id.imgMenuFood);
                Button chooseImage = (Button) mView.findViewById(R.id.btnMenuChooseImage);
                Button saveNewItem = (Button) mView.findViewById(R.id.btnSaveItem);

                foodImage = ("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos");

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //allow user to choose image from system image picker
                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                //put entered data in local db
                //looks like it's putting data in the wrong db
                //probably should be deals
                saveNewItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();

                        values.put("id", "1");
                        values.put("name", newItemName.getText().toString());
                        values.put("price", newItemPrice.getText().toString());
                        values.put("description", newItemDesc.getText().toString());
                        values.put("image", foodImage);
                        db.insert("menu", null, values);

                        db.close();

                        adapter.newItem(newItemName.getText().toString(),
                                newItemPrice.getText().toString(),
                                newItemDesc.getText().toString(),
                                foodImage);
                        dialog.dismiss();
                    }
                });
            }
        });

        //when user taps an existing item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityRegCreateMenu.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText newItemName = (EditText) dialogView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) dialogView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) dialogView.findViewById(R.id.txtDescription);
                newItemImage = (ImageView) dialogView.findViewById(R.id.imgMenuFood);
                Button chooseImage = (Button) dialogView.findViewById(R.id.btnMenuChooseImage);
                Button saveNewItem = (Button) dialogView.findViewById(R.id.btnSaveItem);

                foodImage = adapter.getImage(position);

                final int spot = position;

                newItemName.setText(adapter.getName(position));
                newItemPrice.setText(adapter.getPrice(position).substring(1));
                newItemDesc.setText(adapter.getDescription(position));
                Glide.with(getBaseContext()).load(foodImage).into(newItemImage);

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                //allow user to choose new image
                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                //update item info in local db
                //should probably be changed to deals db
                saveNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String holdNew = newItemName.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues v = new ContentValues();
                        v.put("id", "1");
                        v.put("name", newItemName.getText().toString());
                        v.put("price", newItemPrice.getText().toString());
                        v.put("description", newItemDesc.getText().toString());
                        v.put("image", foodImage);

                        String[] selectionArgs = {adapter.getName(position)};

                        db.update("menu", v, "name = ?", selectionArgs);
                        db.close();

                        adapter.setItem(spot, newItemName.getText().toString(),
                                newItemPrice.getText().toString(),newItemDesc.getText().toString(),
                                foodImage);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void clickFinishRegistration(View view){

        //send registration info to database
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("madeMenu", 1);
        editor.commit();

        Intent intent = new Intent(this, ActivityRegChoose.class);
        startActivity(intent);
    }

    //load image into global variable when user returns from selecting an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == 3645 && resultData != null) {
            foodImage = resultData.getData().toString();
            Glide.with(this).load(foodImage).into(newItemImage);
            newItemImage.getLayoutParams().height = 300;
            newItemImage.getLayoutParams().width = 300;
        }
    }

    //delete the item from the local db
    public void delete(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] selectionArgs = {name};

        db.delete("menu", "name = ?", selectionArgs);

        db.close();

        Toast.makeText(this, name + " deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
