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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityRegister extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText txtFirstName, txtLastName, txtrestName, txtemail, txtpassword, txtcPassword;
    private String firstName, lastName, email, password, confirmPass, restaurantName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register"); //set activity title

        txtFirstName = (EditText) findViewById(R.id.txtRegFirstName);
        txtLastName = (EditText) findViewById(R.id.txtRegLastName);
        txtrestName = (EditText) findViewById(R.id.txtRegRestName);
        txtemail = (EditText) findViewById(R.id.txtRegEmail);
        txtpassword = (EditText) findViewById(R.id.txtRegPassword);
        txtcPassword = (EditText) findViewById(R.id.txtRegConfirmPass);

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        findViewById(R.id.layRegister).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
    }

    protected void hideKeyboard(View view)
    {
        view.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void clickNext(View view){
        firstName = txtFirstName.getText().toString();
        lastName = txtLastName.getText().toString();
        restaurantName = txtrestName.getText().toString();
        email = txtemail.getText().toString();
        password = txtpassword.getText().toString();
        confirmPass = txtcPassword.getText().toString();

        //makes sure all fields are filled
        Boolean ready = !firstName.isEmpty() && !lastName.isEmpty() && !restaurantName.isEmpty()
                && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty();
        Boolean valid = isValidPassword(password); //checks if password is valid
        Boolean same = password.equals(confirmPass); //checks password is entered the same twice

        if(ready && same && valid){ //if all fields are filled and passwords match

            new register().execute("http://138.197.33.88/api/business/register/");

            //put values in shared preferences
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("fromRegister", 1);
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.putString("restaurantName", restaurantName);
            editor.putString("email", email);
            editor.putString("password", password); //needs to be encrypted
            editor.putInt("madeGrabBag", 0);
            editor.commit();

            Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(!ready) Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
        else if(!valid){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_password, null);
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
        else if(!same) Toast.makeText(this, "passwords do not match", Toast.LENGTH_LONG).show();
    }

    private class register extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            String line = null;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                URL requestURL = new URL(urls[0]);
                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);

                String outputString = toJsonString();
                connection.setRequestProperty("Content-Length", "" + outputString.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(outputString);
                writer.flush();
                writer.close();

                int HttpResult = connection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"
                    ));
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    android.util.Log.w(this.getClass().getSimpleName(),"Response Message: "+sb.toString());
                }
                else{
                    android.util.Log.w(this.getClass().getSimpleName(),"Response Message: "+connection.getResponseMessage());
                }
            }
            catch (MalformedURLException ex){ ex.printStackTrace(); }
            catch (IOException e){ e.printStackTrace(); }
            finally{ connection.disconnect(); }

            return line;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "A verification link has been sent to your email", Toast.LENGTH_LONG).show();
        }
    }

    public String toJsonString() {//creates a new JSON string from stored movie data
        String returnJ = "";
        try{
            JSONObject js = new JSONObject();
            js.put("firstName", firstName);
            js.put("lastName", lastName);
            js.put("email", email);
            js.put("password", password);
            js.put("personalPhoneNumber", "0123456789");
            js.put("restaurantName", restaurantName);
            js.put("addressLine1", "123 Nowhere Ln");
            js.put("addressLine2", "");
            js.put("city", "Fountain Hills");
            js.put("state", "Az");
            js.put("zip", "85268");
            js.put("businessPhoneNumber", "9876543210");
            js.put("description", "gosh this place is cool");
            returnJ = js.toString();
            android.util.Log.w(this.getClass().getSimpleName(),returnJ);
        }
        catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return returnJ;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
