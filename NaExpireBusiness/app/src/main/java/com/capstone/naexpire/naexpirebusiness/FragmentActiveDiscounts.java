package com.capstone.naexpire.naexpirebusiness;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class FragmentActiveDiscounts extends Fragment {

    ListAdapterEditMenu adapter;
    ArrayList<String> item = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();

    public FragmentActiveDiscounts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_discounts, container, false);

        FragmentActiveDiscounts.this.getActivity().setTitle("Active Discounts"); //set activity title

        adapter = new ListAdapterEditMenu(FragmentActiveDiscounts.this.getContext(), FragmentActiveDiscounts.this);
        final ListView listView = (ListView) view.findViewById(R.id.lstActiveDiscounts);
        listView.setAdapter(adapter);

        //fill with dummy data
        item.add("Beef Taco");
        item.add("Chicken Taco");
        price.add("$3.00");
        price.add("$3.50");
        quantity.add("x4");
        quantity.add("x2");
        image.add("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos");
        image.add("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos2");
        for(int i = 0; i < item.size(); i++){
            adapter.newItem(item.get(i), price.get(i), quantity.get(i), image.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentActiveDiscounts.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_active_discount, null);
                final TextView header = (TextView) dialogView.findViewById(R.id.lblActiveDiscount);
                final EditText itemQuantity = (EditText) dialogView.findViewById(R.id.txtActiveQuantity);
                final EditText itemPrice = (EditText) dialogView.findViewById(R.id.txtActiveDiscountPrice);
                ImageView image = (ImageView) dialogView.findViewById(R.id.imgDiscountPic);
                Button save = (Button) dialogView.findViewById(R.id.btnActiveSave);

                itemQuantity.setText(adapter.getDescription(position).substring(1));
                itemPrice.setText(adapter.getPrice(position).substring(1));
                final String holdName = adapter.getName(position);

                header.setText(adapter.getName(position)+" Discount");
                Glide.with(FragmentActiveDiscounts.this.getContext()).load(adapter.getImage(position)).into(image);

                final int spot = position;

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemQuantity.getText().toString().equals("0")) adapter.deleteItem(position);
                        else adapter.setItem(position, holdName, "$"+itemPrice.getText().toString(),
                                "x"+itemQuantity.getText().toString(), adapter.getImage(position));
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
