package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivityLogin extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText txtemail, txtpassword;
    private String email, password, confirmationCode, loginStatus, restaurantId;
    private AlertDialog.Builder dialogBuilder;
    private View dialogView;
    private AlertDialog dialog;
    private int first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtemail = (EditText) findViewById(R.id.txtLoginEmail);
        txtpassword = (EditText) findViewById(R.id.txtLoginPassword);
        loginStatus = "nope";
        first = 0;

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        if(1 == sharedPref.getInt("fromRegister", 0)){  //checks if the user just came from
            first = 1;                                  //the first stage of registration
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("fromRegister", 0); //set that the user didn't just come from register
            editor.commit();

            txtemail.setText(sharedPref.getString("email", ""));
            txtpassword.setText(sharedPref.getString("password", ""));
        }

        //dialog builder for confirmation code dialog
        dialogBuilder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_confirmation_code, null);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();

        findViewById(R.id.layLogin).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
    }

    protected void hideKeyboard(View view) //method to hide soft-keyboard
    {
        view.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void clickLogin(View view) {
        email = txtemail.getText().toString();
        password = txtpassword.getText().toString();

        new login().execute("http://138.197.33.88/api/business/login/");
    }

    public void clickForgot(View view) { //when forgot password is tapped
        if(!txtemail.getText().toString().isEmpty()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
            TextView header = (TextView) mView.findViewById(R.id.lblText);
            header.setText("A recovery email has been sent to "+txtemail.getText().toString());
            Button dismiss = (Button) mView.findViewById(R.id.btnDismiss);
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            dismiss.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    dialog.dismiss();
                }
            });
        }
        else Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show();
    }

    public void clickRegister(View view) { //register is tapped
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
    }

    private class login extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            loginStatus = "nope";
            restaurantId = "";
            String line = "";
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
                android.util.Log.w(this.getClass().getSimpleName(), "Response Code: "+HttpResult);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), "utf-8"
                ));

                if(HttpResult == HttpURLConnection.HTTP_FORBIDDEN){ //signifies first ever login
                    loginStatus = "first";
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
                }
                else if(HttpResult == HttpURLConnection.HTTP_OK){ //correct login credentials
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    try{
                        JSONObject obj = new JSONObject(sb.toString());
                        loginStatus = obj.getString("sessionID");
                        restaurantId = obj.getString("restaurantID");
                    }catch (Exception e){}

                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+sb.toString());
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

                //set current session id
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sessionId", result);
                editor.putString("restaurantId", restaurantId);
                editor.commit();

                //move to nav bar activity
                Intent intent = new Intent(ActivityLogin.this.getBaseContext(), NavDrawer.class);
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

    public void next(){ //show dialog to enter confirmation code
        final EditText code = (EditText) dialogView.findViewById(R.id.txtConfirmCode);
        Button submit = (Button) dialogView.findViewById(R.id.btnConfirm);

        dialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationCode = code.getText().toString();
                Intent intent = new Intent(ActivityLogin.this.getBaseContext(), ActivityRegFirstLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //new ActivityLogin.ConfirmCode().execute("http://138.197.33.88/api/business/register/confirm/");
            }
        });
    }

    //checks if confirmation code is valid
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
