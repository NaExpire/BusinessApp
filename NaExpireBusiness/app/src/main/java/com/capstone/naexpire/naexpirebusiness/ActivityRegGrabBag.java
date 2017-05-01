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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

//activity for creating a grab-bag meal during registration
public class ActivityRegGrabBag extends AppCompatActivity {
    private DatabaseHelperMenu dbHelper = null;
    private SharedPreferences sharedPref;

    EditText ingredients, price;
    ImageView newItemImage;
    private String foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_grab_bag);

        ingredients = (EditText) findViewById(R.id.txtGrabIngredients);
        price = (EditText) findViewById(R.id.txtGrabPrice);
        newItemImage = (ImageView) findViewById(R.id.imgFoodPicture);

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelperMenu(getApplicationContext());

        //if a grab bag meal has been made already then get previously entered info
        if(1 == sharedPref.getInt("madeGrabBag", 0)){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor result = db.rawQuery("SELECT price, description, image FROM menu WHERE TRIM(name) = '"+(sharedPref.getString("restaurantName", "")+" Grab-Bag Meal").trim()+"'", null);
            if(result.moveToNext()){
                price.setText(result.getString(0));
                ingredients.setText(result.getString(1));
                foodImage = result.getString(2);
                newItemImage.getLayoutParams().height = 300;
                newItemImage.getLayoutParams().width = 300;
                Glide.with(this).load(result.getString(2)).into(newItemImage);
            }
            db.close();
            result.close();
        }
    }

    //show description of what grab bag is
    public void clickWhatsThis(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_grab_bag_descrip, null);
        Button ok = (Button) mView.findViewById(R.id.btnGrabOk);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
    }

    //opens system image picker
    public void clickChooseImage(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 3645);
    }

    //next button is tapped
    public void clickGrabNext(View view){
        //ensure all fields are filled
        if(!ingredients.getText().toString().isEmpty() && !price.getText().toString().isEmpty()){
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Toast.makeText(this, sharedPref.getInt("madeGrabBag", 0)+"", Toast.LENGTH_SHORT).show();

            //if one has been created already: update it
            if(1 == sharedPref.getInt("madeGrabBag", 0)){
                ContentValues v = new ContentValues();
                v.put("price", price.getText().toString());
                v.put("description", ingredients.getText().toString());
                v.put("image", foodImage);

                String[] selectionArgs = {sharedPref.getString("restaurantName", "")+" Grab-Bag Meal"};

                db.update("menu", v, "name = ?", selectionArgs);
                db.close();
            }
            else{ //insert meal into db
                ContentValues values = new ContentValues();

                values.put("id", "1");
                values.put("name", sharedPref.getString("restaurantName", "")+" Grab-Bag Meal");
                values.put("price", price.getText().toString());
                values.put("description", ingredients.getText().toString());
                values.put("image", foodImage);
                db.insert("menu", null, values);
                db.close();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("madeGrabBag", 1);
                editor.commit();
            }

            Intent intent = new Intent(this, ActivityRegChoose.class);
            startActivity(intent);
        }
        else Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
    }

    //place selected image in a global variable
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == 3645 && resultData != null) {
            foodImage = resultData.getData().toString();
            Glide.with(this).load(foodImage).into(newItemImage);
            newItemImage.getLayoutParams().height = 300;
            newItemImage.getLayoutParams().width = 300;
        }
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
