package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityRegRestrauntInfo extends AppCompatActivity {

    EditText txtFirstName, txtLastName, txtrestName, txtaddress1, txtAddress2, txtbusPhone, txtdescription, txtusername, txtemail,
            txtpersonalPhone, txtpassword, txtcPassword, txtcity, txtZip;
    Spinner spinner;
    String firstName, lastName, email, password, confirmPass, personalPhone, restaurantName,
            address1, address2, city, state, zip, businessPhone, description, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_restraunt_info);
        setTitle("Register"); //set activity title

        txtFirstName = (EditText) findViewById(R.id.txtRegFirstName);
        txtLastName = (EditText) findViewById(R.id.txtRegLastName);
        txtrestName = (EditText) findViewById(R.id.txtRestaurantName);
        txtaddress1 = (EditText) findViewById(R.id.txtAddress1);
        txtAddress2 = (EditText) findViewById(R.id.txtAddress2);
        txtbusPhone = (EditText) findViewById(R.id.txtPhone);
        txtdescription = (EditText) findViewById(R.id.txtRestDescription);
        txtusername = (EditText) findViewById(R.id.txtRegUsername);
        txtemail = (EditText) findViewById(R.id.txtEmail);
        txtpersonalPhone = (EditText) findViewById(R.id.txtPersonalPhone);
        txtpassword = (EditText) findViewById(R.id.txtRegPassword);
        txtcPassword = (EditText) findViewById(R.id.txtRegConfirmPass);
        txtcity = (EditText) findViewById(R.id.txtCity);
        txtZip = (EditText) findViewById(R.id.txtZip);

        spinner = (Spinner) findViewById(R.id.spnState);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this, R.array.states_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);
    }

    public void clickNext(View view){
        firstName = txtFirstName.getText().toString();
        lastName = txtLastName.getText().toString();
        restaurantName = txtrestName.getText().toString();
        address1 = txtaddress1.getText().toString();
        address2 = txtAddress2.getText().toString();
        city = txtcity.getText().toString();
        state = spinner.getSelectedItem().toString();
        zip = txtZip.getText().toString();
        businessPhone = txtbusPhone.getText().toString();
        description = txtdescription.getText().toString();
        username = txtusername.getText().toString();
        email = txtemail.getText().toString();
        personalPhone = txtpersonalPhone.getText().toString();
        password = txtpassword.getText().toString();
        confirmPass = txtcPassword.getText().toString();

        //makes sure all fields are filled
        Boolean ready = true; //!n.isEmpty() && !a.isEmpty() && !p.isEmpty()
                //&& !d.isEmpty() && !u.isEmpty() && !e.isEmpty()
                //&& !pp.isEmpty() && !pass.isEmpty() && !cPass.isEmpty();
        Boolean same = true;// pass.equals(cPass); //checks password is entered the same twice

        if(ready && same){ //if all fields are filled and passwords match
            //rInfo is Rest. Name, Address, Rest. Phone, Description, Username, Email,
            //Personal Phone, Password
            //String[] rInfo = {n, a, p, d, u, e, pp, pass};
            //toJsonString();

            //JSONObject js = makeJson();

            /*try {
                URL requestURL = new URL("http://138.197.33.88/api/business/register/");

                HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();

                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);

                String outputString = toJsonString();
                connection.setRequestProperty("Content-Length", "" + outputString.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json");
                android.util.Log.w(this.getClass().getSimpleName(),"four");

                //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                android.util.Log.w(this.getClass().getSimpleName(),"five");
                //writer.write(outputString);
                android.util.Log.w(this.getClass().getSimpleName(),"six");
                //writer.flush();
                //writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line+ "\n");
                }
                android.util.Log.w(this.getClass().getSimpleName(),"POST Response: "+sb.toString());
                connection.disconnect();
            }
            catch (Exception ex){
                android.util.Log.w(this.getClass().getSimpleName(), ex.getMessage());
            }*/


            new HttpAsyncTask().execute("http://138.197.33.88/api/business/register/");

            Intent intent = new Intent(this, ActivityRegFoodTypes.class);
            //intent.putExtra("restInfo", rInfo);
            startActivity(intent);
        }
        else if(!ready) Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
        else if(!same) Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ActivityRegRestrauntInfo.this, "error", Toast.LENGTH_SHORT).show();
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
            js.put("personalPhoneNumber", personalPhone);
            js.put("restaurantName", restaurantName);
            js.put("addressLine1", address1);
            js.put("addressLine2", address2);
            js.put("city", city);
            js.put("state", state);
            js.put("zip", zip);
            js.put("businessPhoneNumber", businessPhone);
            js.put("description", description);
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
