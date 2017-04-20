package com.capstone.naexpire.naexpirebusiness;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;


public class FragmentMenu extends Fragment {

    private DatabaseHelperMenu dbHelper = null;
    private DatabaseHelperActiveDiscounts dbDiscountsHelper = null;
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

        if (current==null) {
            dbHelper = new DatabaseHelperMenu(getActivity().getApplicationContext());
            dbDiscountsHelper = new DatabaseHelperActiveDiscounts(getActivity().getApplicationContext());
        }

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

        Cursor result = db.rawQuery("SELECT id, name, price, description, image FROM menu", null);

        while(result.moveToNext()){
            itemId.add(Integer.parseInt(result.getString(0)));
            name.add(result.getString(1));
            price.add(Double.parseDouble(result.getString(2)));
            description.add(result.getString(3));
            image.add(result.getString(4));
        }

        db.close();
        result.close();

        for(int i = 0; i < name.size(); i++){
            adapter.newItem(itemId.get(i), name.get(i),price.get(i),description.get(i), image.get(i));
        }
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(FragmentMenu.this.getContext());
                View dView = getActivity().getLayoutInflater().inflate(R.layout.dialog_discount, null);
                ImageView image = (ImageView) dView.findViewById(R.id.imgDiscountPic);
                final TextView name = (TextView) dView.findViewById(R.id.lblDiscount);
                final EditText quantity = (EditText) dView.findViewById(R.id.txtQuantity);
                final EditText dPrice = (EditText) dView.findViewById(R.id.txtDiscountPrice);
                Button saveDiscount = (Button) dView.findViewById(R.id.btnNewDiscount);

                final int spot = position;

                name.setText(adapter.getName(position)+" Deal");
                Glide.with(FragmentMenu.this.getContext()).load(adapter.getImage(position)).into(image);

                dBuilder.setView(dView);
                final AlertDialog dialog = dBuilder.create();
                dialog.show();

                saveDiscount.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String q = quantity.getText().toString();
                        String p = dPrice.getText().toString();

                        if(q.isEmpty() || p.isEmpty()){
                            Toast.makeText(FragmentMenu.this.getActivity(),"Fill All Fields", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            SQLiteDatabase db = dbDiscountsHelper.getWritableDatabase();

                            ContentValues values = new ContentValues();

                            values.put("id", "1");
                            values.put("name", adapter.getName(spot));
                            values.put("price", p);
                            values.put("quantity", q);
                            values.put("image", adapter.getImage(spot));
                            db.insert("activeDiscounts", null, values);

                            db.close();

                            Toast.makeText(FragmentMenu.this.getActivity(),"Deal Created", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }
    @Override
    public void onDestroy() {
        dbHelper.close();
        dbDiscountsHelper.close();
        super.onDestroy();
    }
}
