package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ActivityLogin extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
    }

    public void clickLogin(View view) {
        String u = username.getText().toString();
        String p = password.getText().toString();
        if (u.isEmpty() || p.isEmpty()) Toast.makeText(this, "Enter Username & Password",
                Toast.LENGTH_LONG).show();
        else {
            ///
            //validate login credentials
            ///

            Intent intent = new Intent(this, NavDrawer.class);
            startActivity(intent);
        }
    }

    public void clickForgot(View view) {
        if(!username.getText().toString().isEmpty()) {
            Toast.makeText(this, "A Recovery E-Mail Has Been Sent", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Enter Username", Toast.LENGTH_LONG).show();
    }

    public void clickRegister(View view) {
        Intent intent = new Intent(this, ActivityRegRestrauntInfo.class);
        startActivity(intent);
        //String y = readFromFile(this, password.getText().toString());
        //Toast.makeText(this, y, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context, String n) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(n+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
