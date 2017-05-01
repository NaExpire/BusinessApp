package com.capstone.naexpire.naexpirebusiness;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//fragment for viewing and editing account info
public class FragmentAccountInfo extends Fragment {

    private SharedPreferences sharedPref;

    private EditText restName, restAddr, restPhone, restDesc, usn, email, personalPhone, pass, cPass;
    private String name, description, address, city, state, pickupTime, phoneNumber, items;

    public FragmentAccountInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);

        FragmentAccountInfo.this.getActivity().setTitle("Account Information"); //set activity title

        restName = (EditText) view.findViewById(R.id.txtRestName);
        restAddr = (EditText) view.findViewById(R.id.txtRestAddr);
        restPhone = (EditText) view.findViewById(R.id.txtRestPhone);
        restDesc = (EditText) view.findViewById(R.id.txtRestDesc);
        usn = (EditText) view.findViewById(R.id.txtUsn);
        email = (EditText) view.findViewById(R.id.txtEm);
        personalPhone = (EditText) view.findViewById(R.id.txtPPhone);
        pass = (EditText) view.findViewById(R.id.txtNewPass);
        cPass = (EditText) view.findViewById(R.id.txtCNewPass);
        Button foodTypes = (Button) view.findViewById(R.id.btnFoodInfo);
        Button save = (Button) view.findViewById(R.id.btnSaveAcctEdits);

        sharedPref = getActivity().getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        restName.setText(sharedPref.getString("restaurantName", ""));
        restAddr.setText(sharedPref.getString("addressLine1", ""));
        restPhone.setText(sharedPref.getString("restaurantPhone", ""));
        restDesc.setText(sharedPref.getString("restaurantDescription", ""));
        usn.setText(sharedPref.getString("username", ""));
        email.setText(sharedPref.getString("email", ""));
        personalPhone.setText(sharedPref.getString("userPhone", ""));

        foodTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);

                FragmentEditFoodTypes fragmentEditFoodTypes = new FragmentEditFoodTypes();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, fragmentEditFoodTypes).commit();
            }
        });

        //save changes button tapped
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = restName.getText().toString();
                description = restDesc.getText().toString();
                address = restAddr.getText().toString();
                phoneNumber = restPhone.getText().toString();

                //get info from shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("restaurantName", name);
                editor.putString("restaurantAddress", address);
                editor.putString("restaurantPhone", phoneNumber);
                editor.putString("restaurantDescription", description);
                editor.putString("username", usn.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("userPhone", personalPhone.getText().toString());

                int rId = Integer.parseInt(sharedPref.getString("restaurantId", "0"));
                new updateRestaurant().execute("http://138.197.33.88/api/business/restaurant/restaurantID:"+rId+"/update/");

                String enteredpass = pass.getText().toString();
                String enteredcpass = cPass.getText().toString();
                if(isValidPassword(enteredpass)){ //if password is valid
                    if(enteredcpass.equals(enteredpass)){ //if passwords match, make it the new one
                        editor.putString("password", enteredpass);
                    }
                    else Toast.makeText(FragmentAccountInfo.this.getContext(),
                            "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(FragmentAccountInfo.this.getContext(),
                        "Password must have at least:\n\t8 Characters\n\t1 Capital\n\t1 Number\n\t1 Special Character",
                        Toast.LENGTH_SHORT).show();
                editor.commit();

                //update restaurant name in the navigation drawer
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                TextView onw = (TextView) header.findViewById(R.id.lblNavOwner);
                TextView rest = (TextView) header.findViewById(R.id.lblNavRest);

                rest.setText(sharedPref.getString("restaurantName", ""));
                onw.setText(sharedPref.getString("username", ""));
            }
        });

        view.findViewById(R.id.layAccount).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        view.findViewById(R.id.layAccountScroll).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideKeyboard(view);
            }
        });

        return view;
    }

    //async call using endpoint to update restaurant info
    private class updateRestaurant extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
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
                android.util.Log.w(this.getClass().getSimpleName(), "Response Code: "+HttpResult);

                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"
                    ));
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();

                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+sb.toString());
                    line = "true";
                }
                else{
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Response Message: "+connection.getResponseMessage());
                }
            }
            catch (MalformedURLException ex){ ex.printStackTrace(); }
            catch (IOException e){ e.printStackTrace(); }
            finally{ connection.disconnect(); }

            return line;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("true")){ //updated successfully
                Toast.makeText(FragmentAccountInfo.this.getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(FragmentAccountInfo.this.getContext(), "Error saving changes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String toJsonString() {//creates a new JSON string from stored movie data
        String returnJ = "";
        try{
            JSONObject js = new JSONObject();
            js.put("name", name);
            js.put("description", description);
            js.put("address", address);
            js.put("city", "");
            js.put("state", "");
            js.put("pickupTime", "");
            js.put("phoneNumber", phoneNumber);
            js.put("items", "");
            returnJ = js.toString();
            android.util.Log.w(this.getClass().getSimpleName(),returnJ);
        }
        catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return returnJ;
    }

    protected void hideKeyboard(View view)
    {
        view.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
