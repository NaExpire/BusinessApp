package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityRegFirstLogin extends AppCompatActivity {
    private SharedPreferences sharedPref;

    EditText phone, addressOne, addressTwo, city, zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_first_login);

        phone = (EditText) findViewById(R.id.txtRegPhone);
        addressOne = (EditText) findViewById(R.id.txtRegAddressOne);
        addressTwo = (EditText) findViewById(R.id.txtRegAddressTwo);
        city = (EditText) findViewById(R.id.txtRegCity);
        zip = (EditText) findViewById(R.id.txtRegZip);

        Spinner spinner = (Spinner) findViewById(R.id.spnState);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this, R.array.states_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
    }

    public void clickRegisterNext(View view){

        //checks to make sure all text fields are filled
        Boolean ready = !phone.getText().toString().isEmpty() && !addressOne.getText().toString().isEmpty()
                && !city.getText().toString().isEmpty() && !zip.getText().toString().isEmpty();

        if(ready){
            //put values in shared preferences
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("restaurantPhone", phone.getText().toString());
            editor.putString("addressLine1", addressOne.getText().toString());
            editor.putString("addressLine2", addressTwo.getText().toString());
            editor.putString("city", city.getText().toString());
            editor.putString("zip", zip.getText().toString()); //needs to be encrypted
            editor.commit();

            Intent intent = new Intent(this, ActivityRegFoodTypes.class);
            startActivity(intent);
        }
        else Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
    }
}
