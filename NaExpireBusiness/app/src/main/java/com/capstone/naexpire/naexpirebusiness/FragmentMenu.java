package com.capstone.naexpire.naexpirebusiness;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

//fragment to display current menu items & deals
public class FragmentMenu extends Fragment {

    private DatabaseHelperMenu dbHelper = null;
    private Cursor current = null;
    private SharedPreferences sharedPref;

    private ListAdapterMenu adapter;
    private Spinner spinner;
    private ImageView newItemImage;
    private String foodImage;
    private ArrayList<Integer> itemId = new ArrayList<Integer>();
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<Double> price = new ArrayList<Double>();
    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<String> image = new ArrayList<String>();

    private int mealId, mealQuantity;
    private double dealPrice;

    public FragmentMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        sharedPref = getActivity().getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        FragmentMenu.this.getActivity().setTitle("Menu"); //set activity title

        dbHelper = new DatabaseHelperMenu(getActivity().getApplicationContext());

        //spinner to select filter method for menu items
        spinner = (Spinner) view.findViewById(R.id.spnFilter);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                FragmentMenu.this.getContext(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        adapter = new ListAdapterMenu(FragmentMenu.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstRestrauntMenu);
        listView.setAdapter(adapter);

        //get current menu items & deals from local db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM menu", null);

        //0 id
        //1 name
        //2 price
        //3 description
        //4 quantity
        //5 deal
        //6 image

        while(result.moveToNext()){
            adapter.newItem(Integer.parseInt(result.getString(0)), result.getString(1),
                    Double.parseDouble(result.getString(2)),result.getString(3),
                    Integer.parseInt(result.getString(4)), Double.parseDouble(result.getString(5)),
                    result.getString(6));
        }
        db.close();
        result.close();

        adapter.sortMenu(spinner.getSelectedItemPosition());

        //get meals from backend db if none in local
        if(adapter.getCount() == 0) {
            int rId = Integer.parseInt(sharedPref.getString("restaurantId", ""));
            String uri = "http://138.197.33.88/api/business/restaurant/"+rId+"/";
            android.util.Log.w(this.getClass().getSimpleName(), "http: "+uri);
            new getRestaurant().execute(uri);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapter.sortMenu(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //if meal in list is tapped
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(FragmentMenu.this.getContext());
                View dView = getActivity().getLayoutInflater().inflate(R.layout.dialog_deal, null);
                ImageView image = (ImageView) dView.findViewById(R.id.imgDiscountPic);
                final TextView name = (TextView) dView.findViewById(R.id.lblDiscount);
                final EditText quantity = (EditText) dView.findViewById(R.id.txtQuantity);
                final EditText dPrice = (EditText) dView.findViewById(R.id.txtDiscountPrice);
                Button saveDiscount = (Button) dView.findViewById(R.id.btnNewDiscount);
                Button cancel = (Button) dView.findViewById(R.id.btnCancel);

                final int spot = position;
                final int numDeals = adapter.getQuantity(position);

                if(numDeals > 0){ //set text fields empty if no deal exists
                    quantity.setText(""+adapter.getQuantity(position));
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    dPrice.setText(""+decimalFormat.format(adapter.getDeal(position)));
                    saveDiscount.setText("Save");
                }
                else{ //set text fields appropriately if a deal exists
                    quantity.setText("");
                    dPrice.setText("");
                }
                name.setText(adapter.getName(position)+" Deal");
                Glide.with(FragmentMenu.this.getContext()).load(adapter.getImage(position)).into(image);

                dBuilder.setView(dView);
                final AlertDialog dialog = dBuilder.create();
                dialog.show();

                dView.findViewById(R.id.layDeal).setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent ev)
                    {
                        hideKeyboard(view);
                        return false;
                    }
                });

                //save new deal info
                saveDiscount.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String q = quantity.getText().toString();
                        if(q.equals("")) q = "0"; //set quantity to 0 if no value in quantity field
                        String p = dPrice.getText().toString();

                        if(q.isEmpty() && p.isEmpty()){ //dismiss dialog if bot values are empty
                            dialog.dismiss();
                        }
                        else if(q.isEmpty() || p.isEmpty()){
                            Toast.makeText(FragmentMenu.this.getActivity(),"Fill All Fields", Toast.LENGTH_SHORT).show();
                        }
                        else{ //save new deal values
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("quantity", q);
                            if(Integer.parseInt(q) > 0) values.put("deal", p);
                            else values.put("deal", 0.0);
                            db.update("menu", values, "id = ?", new String[]{""+adapter.getId(position)});
                            db.close();

                            adapter.setQuantity(position, Integer.parseInt(q));
                            adapter.setDeal(position, Double.parseDouble(p));
                            adapter.notifyDataSetChanged();

                            if(numDeals == 0){ //create new deal if one doesn't already exist
                                mealId = adapter.getId(position);
                                mealQuantity = Integer.parseInt(q);
                                dealPrice = Double.parseDouble(p);
                                new newDeal().execute("http://138.197.33.88/api/business/deal/create/");
                                Toast.makeText(FragmentMenu.this.getActivity(),"Deal Created", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(FragmentMenu.this.getActivity(),"Deal Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    //async call to get restaurant info
    private class getRestaurant extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
            ArrayList<MenuItem> menu = new ArrayList<>();
            String line, name, description, image = null;
            int id, quantity;
            double price, deal;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                name = "";
                URL requestURL = new URL(urls[0]);
                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setRequestMethod("GET");

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

                    JSONArray meals, deals;
                    try{ //gets all meal and deal info
                        JSONObject obj = new JSONObject(sb.toString());
                        meals = new JSONArray(obj.getString("meals")); //array of json meals
                        deals = new JSONArray(obj.getString("deals")); //array of json deals
                        for(int i = 0; i < meals.length(); i++){
                            JSONObject tempObj = meals.getJSONObject(i);
                            id = tempObj.getInt("id");
                            name = tempObj.getString("name");
                            description = tempObj.getString("description");
                            //get restaurant id too
                            price = tempObj.getDouble("price");
                            //get menu type too
                            image = "@drawable/logo.png";
                            menu.add(new MenuItem(id, name, price, description, 0, 0.0, image));
                        }
                        for(int i = 0; i < deals.length(); i++){
                            JSONObject tempObj = deals.getJSONObject(i);
                            int mId = tempObj.getInt("mealID");
                            quantity = tempObj.getInt("quantity");
                            deal = tempObj.getDouble("dealPrice");
                            for(int j = 0; j < menu.size(); j++){
                                if(menu.get(j).getName().equals(name)){
                                    menu.get(j).setQuantity(quantity);
                                    menu.get(j).setDeal(deal);
                                }
                            }
                        }
                    }catch (Exception e){}

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    //0 id
                    //1 name
                    //2 price
                    //3 description
                    //4 quantity
                    //5 deal
                    //6 image

                   for(int i = 0; i < menu.size(); i++){ //put in local db
                        ContentValues values = new ContentValues();
                        values.put("id", menu.get(i).getId());
                        values.put("name", menu.get(i).getName());
                        values.put("price", menu.get(i).getPrice());
                        values.put("description", menu.get(i).getDescription());
                        values.put("quantity", menu.get(i).getQuantity());
                        values.put("deal", menu.get(i).getDeal());
                        values.put("image", menu.get(i).getImage());
                        db.insert("menu", null, values);
                    }

                    db.close();

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

            return "";
        }

        @Override
        protected void onPostExecute(String result) { //put menu item into list adapter
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor results = db.rawQuery("SELECT * FROM menu", null);
            //0 id
            //1 name
            //2 price
            //3 description
            //4 quantity
            //5 deal
            //6 image
            while(results.moveToNext()){
                adapter.newItem(Integer.parseInt(results.getString(0)), results.getString(1),
                        Double.parseDouble(results.getString(2)),results.getString(3),
                        Integer.parseInt(results.getString(4)), Double.parseDouble(results.getString(5)),
                        results.getString(6));
            }
            db.close();
            results.close();

            adapter.sortMenu(spinner.getSelectedItemPosition());
        }
    }

    //async call to create new deal
    private class newDeal extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls){
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

        public String toJsonString() {//creates a new JSON string from stored movie data
            String returnJ = "";
            try{
                JSONObject js = new JSONObject();
                js.put("mealID", mealId);
                js.put("dealPrice", dealPrice);
                js.put("quantity", mealQuantity);
                js.put("restaurantID", Integer.parseInt(sharedPref.getString("restaurantId", "")));
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

    protected void hideKeyboard(View view)
    {
        view.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
