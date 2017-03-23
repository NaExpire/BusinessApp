package com.capstone.naexpire.naexpirebusiness;


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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        //get passed string
        String rInfo = "";
        //Bundle bundle = this.getArguments();
        //if(bundle !=null){ rInfo = bundle.getString("restrauntData", ""); }
        rInfo = "Mexican,Cajun,yyyyyy,Beef Taco,1.23,Taco with beef," +
                "Chicken Taco,2.34,Taco with chicken,Shrimp Taco,3.45,Taco with shrimp";
        String[] ar = rInfo.split(",");
        ArrayList<String> arr = new ArrayList<String>();

        //place menu item info in arraylist arr to be loaded into listview
        Boolean found = false;
        for(int i = 0; i < ar.length;i++){
            if(found){
                arr.add(ar[i]+" - $"+ ar[i+1]+" | "+ar[i+2]);
                i+=2;
            }
            if(ar[i].equals("yyyyyy")) found = true;
        }

        MenuFragment.this.getActivity().setTitle("Menu"); //set activity title

        //spinner to select filter method for menu items
        Spinner spinner = (Spinner) view.findViewById(R.id.spnFilter);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                MenuFragment.this.getContext(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuFragment.this.getContext(), android.R.layout.simple_list_item_1, arr);
        final ListView listView = (ListView) view.findViewById(R.id.lstRestrauntMenu);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(MenuFragment.this.getContext());
                View dView = getActivity().getLayoutInflater().inflate(R.layout.dialog_discount, null);
                final EditText quantity = (EditText) dView.findViewById(R.id.txtQuantity);
                final EditText dPrice = (EditText) dView.findViewById(R.id.txtDiscountPrice);
                Button saveDiscount = (Button) dView.findViewById(R.id.btnNewDiscount);

                dBuilder.setView(dView);
                final AlertDialog dialog = dBuilder.create();
                dialog.show();

                saveDiscount.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String q = quantity.getText().toString();
                        String p = dPrice.getText().toString();

                        if(q.isEmpty() || p.isEmpty()){
                            Toast.makeText(MenuFragment.this.getActivity(),"Fill All Fields", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MenuFragment.this.getActivity(),"Discount Created", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }
}
