package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityRegister extends AppCompatActivity {

    EditText txtFirstName, txtLastName, txtrestName, txtemail, txtpassword, txtcPassword;
    String firstName, lastName, email, password, confirmPass, restaurantName;


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
        Boolean same = password.equals(confirmPass); //checks password is entered the same twice

        if(ready && same){ //if all fields are filled and passwords match

            //new HttpAsyncTask().execute("http://138.197.33.88/api/business/register/");

            Toast.makeText(getBaseContext(), "A verification link has been sent to your email", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ActivityLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(!ready) Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
        else if(!same) Toast.makeText(this, "passwords do not match", Toast.LENGTH_LONG).show();
    }

    private class HttpAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            String line = null;
            try {
                URL requestURL = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();



                while((line = reader.readLine()) != null){
                    sb.append(line+ "\n");
                }
                android.util.Log.w(this.getClass().getSimpleName(),"POST Response: "+sb.toString());
                connection.disconnect();
            }
            catch (Exception ex){
                Toast.makeText(ActivityRegister.this, "error", Toast.LENGTH_SHORT).show();
            }

            return line;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
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
            js.put("personalPhoneNumber", "");
            js.put("restaurantName", restaurantName);
            js.put("addressLine1", "");
            js.put("addressLine2", "");
            js.put("city", "");
            js.put("state", "");
            js.put("zip", "");
            js.put("businessPhoneNumber", "");
            js.put("description", "");
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
