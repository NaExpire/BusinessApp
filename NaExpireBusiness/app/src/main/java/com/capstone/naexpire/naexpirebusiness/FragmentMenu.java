package com.capstone.naexpire.naexpirebusiness;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.DecimalFormat;
import java.util.ArrayList;


public class FragmentMenu extends Fragment {

    private DatabaseHelperMenu dbHelper = null;
    private Cursor current = null;

    ListAdapterMenu adapter;
    ImageView newItemImage;
    String foodImage;
    ArrayList<Integer> itemId = new ArrayList<Integer>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<Double> price = new ArrayList<Double>();
    ArrayList<String> description = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();

    public FragmentMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        FragmentMenu.this.getActivity().setTitle("Menu"); //set activity title

        dbHelper = new DatabaseHelperMenu(getActivity().getApplicationContext());

        //spinner to select filter method for menu items
        Spinner spinner = (Spinner) view.findViewById(R.id.spnFilter);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                FragmentMenu.this.getContext(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        adapter = new ListAdapterMenu(FragmentMenu.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstRestrauntMenu);
        listView.setAdapter(adapter);

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

                if(numDeals > 0){
                    quantity.setText(""+adapter.getQuantity(position));
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    dPrice.setText(""+decimalFormat.format(adapter.getDeal(position)));
                    saveDiscount.setText("Save");
                }
                else{
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

                saveDiscount.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String q = quantity.getText().toString();
                        String p = dPrice.getText().toString();

                        if(q.isEmpty() && p.isEmpty()){
                            dialog.dismiss();
                        }
                        else if(q.isEmpty() || p.isEmpty()){
                            Toast.makeText(FragmentMenu.this.getActivity(),"Fill All Fields", Toast.LENGTH_SHORT).show();
                        }
                        else{
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

                            if(numDeals ==0) Toast.makeText(FragmentMenu.this.getActivity(),"Deal Created", Toast.LENGTH_SHORT).show();
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
