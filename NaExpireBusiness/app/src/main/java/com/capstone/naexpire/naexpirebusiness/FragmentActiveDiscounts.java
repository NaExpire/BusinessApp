package com.capstone.naexpire.naexpirebusiness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentActiveDiscounts extends Fragment {

    ListAdapterMenu adapter;
    ArrayList<String> item = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();

    public FragmentActiveDiscounts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_discounts, container, false);

        FragmentActiveDiscounts.this.getActivity().setTitle("Active Discounts"); //set activity title

        adapter = new ListAdapterMenu(FragmentActiveDiscounts.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstActiveDiscounts);
        listView.setAdapter(adapter);

        //fill with dummy data
        item.add("Beef Taco");
        item.add("Shrimp Taco");
        price.add("$3.00");
        price.add("$3.50");
        quantity.add("x4");
        quantity.add("x2");
        for(int i = 0; i < item.size(); i++){
            adapter.newItem(item.get(i), price.get(i), quantity.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentActiveDiscounts.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_active_discount, null);
                final TextView header = (TextView) dialogView.findViewById(R.id.lblActiveDiscount);
                final EditText itemQuantity = (EditText) dialogView.findViewById(R.id.txtActiveQuantity);
                final EditText itemPrice = (EditText) dialogView.findViewById(R.id.txtActiveDiscountPrice);
                Button save = (Button) dialogView.findViewById(R.id.btnActiveSave);

                header.setText(adapter.getName(position) + " Discount");
                itemQuantity.setText(adapter.getDescrip(position).substring(1));
                itemPrice.setText(adapter.getPrice(position).substring(1));
                final String holdName = adapter.getName(position);

                final int spot = position;

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.editItem(position, holdName, "$"+itemPrice.getText().toString(),"x"+itemQuantity.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}