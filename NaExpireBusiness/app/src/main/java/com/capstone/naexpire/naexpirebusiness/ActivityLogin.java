package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivityLogin extends AppCompatActivity {

    private SharedPreferences sharedPref;

    EditText txtemail, txtpassword;
    String email, password, confirmationCode, loginStatus, userData;
    AlertDialog.Builder dialogBuilder;
    View dialogView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtemail = (EditText) findViewById(R.id.txtLoginEmail);
        txtpassword = (EditText) findViewById(R.id.txtLoginPassword);
        loginStatus = "nope";

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        if(1 == sharedPref.getInt("fromRegister", 0)){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("fromRegister", 0); //set that the user didn't just come from register
            editor.commit();

            txtemail.setText(sharedPref.getString("email", ""));
            txtpassword.setText(sharedPref.getString("password", ""));
        }

        dialogBuilder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_confirmation_code, null);

        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
    }

    public void clickLogin(View view) {
        email = txtemail.getText().toString();
        password = txtpassword.getText().toString();

        Intent intent = new Intent(ActivityLogin.this.getBaseContext(), NavDrawer.class);
        //intent.putExtra("userData", userData);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        /*if (email.isEmpty() || password.isEmpty()) Toast.makeText(this, "Enter Username & Password",
                Toast.LENGTH_LONG).show();
        else {
            new ActivityLogin.HttpAsyncTask().execute("http://138.197.33.88/api/business/login/");
        }*/
    }

    public void clickForgot(View view) {
        if(!txtemail.getText().toString().isEmpty()) {
            Toast.makeText(this, "A Recovery E-Mail Has Been Sent", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Enter Username", Toast.LENGTH_LONG).show();
    }

    public void clickRegister(View view) {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
        //String y = readFromFile(this, password.getText().toString());
        //Toast.makeText(this, y, Toast.LENGTH_SHORT).show();
    }

    private class HttpAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            loginStatus = "nope";
            String line = null;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                URL requestURL = new URL(urls[0]);
                String outputString = toJsonString();

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Length", "" + outputString.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(outputString);
                writer.flush();
                writer.close();

                int HttpResult = connection.getResponseCode();
                android.util.Log.w(this.getClass().getSimpleName(),
                        "Response Code: "+HttpResult);

                if(HttpResult == HttpURLConnection.HTTP_FORBIDDEN){
                    //first login ever
                    loginStatus = "first";
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"
                    ));
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    try{
                        JSONObject obj = new JSONObject(sb.toString());
                        userData = obj.getString("sessionID");
                    }catch (Exception e){}

                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+sb.toString());
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "session: "+userData);
                }
                else if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"
                    ));
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    try{
                        JSONObject obj = new JSONObject(sb.toString());
                        loginStatus = obj.getString("sessionID");
                    }catch (Exception e){}

                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+sb.toString());
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Session ID: "+loginStatus);
                }
                else{
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+connection.getResponseMessage());
                }
            }
            catch (MalformedURLException ex){ ex.printStackTrace(); }
            catch (IOException e){ e.printStackTrace(); }
            finally{ connection.disconnect(); }

            return loginStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("first") && !result.equals("nope")){ //not first login
                userData = result;

                Intent intent = new Intent(ActivityLogin.this.getBaseContext(), NavDrawer.class);
                //intent.putExtra("userData", userData);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(result.equals("nope")){ //incorrect login attempt
                Toast.makeText(ActivityLogin.this, "Email or Password Incorrect", Toast.LENGTH_SHORT).show();
            }
            else if(loginStatus.equals("first")){ //correct credentials, first login
                next();
            }
        }
    }

    public void next(){
        final EditText code = (EditText) dialogView.findViewById(R.id.txtConfirmCode);
        Button submit = (Button) dialogView.findViewById(R.id.btnConfirm);

        dialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationCode = code.getText().toString();
                new ActivityLogin.ConfirmCode().execute("http://138.197.33.88/api/business/register/confirm/");
            }
        });
    }

    private class ConfirmCode extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            String confirmStatus = "wrong";
            String line = null;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                URL requestURL = new URL(urls[0]);
                String outputString = confirmationJson();

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Length", "" + outputString.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(outputString);
                writer.flush();
                writer.close();

                int HttpResult = connection.getResponseCode();
                android.util.Log.w(this.getClass().getSimpleName(), "Response Code: "+HttpResult);
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    confirmStatus = "false";
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"
                    ));
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    try{
                        JSONObject obj = new JSONObject(sb.toString());
                        confirmStatus = obj.getString("ok");
                    }catch (Exception e){}

                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+sb.toString());
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Session ID: "+loginStatus);
                }
                else{
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+connection.getResponseMessage());
                }
            }
            catch (MalformedURLException ex){ ex.printStackTrace(); }
            catch (IOException e){ e.printStackTrace(); }
            finally{ connection.disconnect(); }

            return confirmStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("true")){ //correct code
                //putextra loginStatus
                Intent intent = new Intent(ActivityLogin.this.getBaseContext(), ActivityRegFirstLogin.class);
                //intent.putExtra("userData", userData);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
            else{ //incorrect code
                Toast.makeText(ActivityLogin.this, "Confirmation Code Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String toJsonString() {//creates a new JSON string from stored movie data
        String returnJ = "";
        try{
            JSONObject js = new JSONObject();
            js.put("email", email);
            js.put("password", password);
            returnJ = js.toString();
            android.util.Log.w(this.getClass().getSimpleName(),returnJ);
        }
        catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return returnJ;
    }

    public String confirmationJson() {//creates a new JSON string from stored movie data
        String returnJ = "";
        try{
            JSONObject js = new JSONObject();
            js.put("emailAddress", email);
            js.put("confirmationCode", confirmationCode);
            returnJ = js.toString();
            android.util.Log.w(this.getClass().getSimpleName(),returnJ);
        }
        catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return returnJ;
    }
}
