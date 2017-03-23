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


public class ActiveDiscountsFragment extends Fragment {

    CustomMenuAdapter adapter;
    ArrayList<String> item = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();

    public ActiveDiscountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_discounts, container, false);

        ActiveDiscountsFragment.this.getActivity().setTitle("Active Discounts"); //set activity title

        item.add("Beef Taco");
        item.add("Shrimp Taco");
        price.add("$3.00");
        price.add("$3.50");
        quantity.add("x4");
        quantity.add("x2");

        adapter = new CustomMenuAdapter(ActiveDiscountsFragment.this.getContext(), item, quantity, price);
        final ListView listView = (ListView) view.findViewById(R.id.lstActiveDiscounts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActiveDiscountsFragment.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_active_discount, null);
                final TextView header = (TextView) dialogView.findViewById(R.id.lblActiveDiscount);
                final EditText itemQuantity = (EditText) dialogView.findViewById(R.id.txtActiveQuantity);
                final EditText itemPrice = (EditText) dialogView.findViewById(R.id.txtActiveDiscountPrice);
                Button save = (Button) dialogView.findViewById(R.id.btnActiveSave);
                Button delete = (Button) dialogView.findViewById(R.id.btnActiveDelete);

                header.setText(item.get(position) + " Discount");
                itemQuantity.setText(quantity.get(position).substring(1));
                itemPrice.setText(price.get(position));

                final int spot = position;

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price.set(spot, itemPrice.getText().toString());
                        quantity.set(spot, "x"+itemQuantity.getText().toString());
                        adapter.notifyDataSetChanged(); //redraw the list on the screen
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item.remove(spot);
                        price.remove(spot);
                        quantity.remove(spot);
                        adapter.notifyDataSetChanged(); //redraw the list on the screen
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
