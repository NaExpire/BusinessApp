package com.capstone.naexpire.naexpirebusiness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class OrderHistoryFragment extends Fragment {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> items = new ArrayList<String>();
    CustomMenuAdapter adapter;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //test data
        name.add("Jerry");
        name.add("Tom");
        date.add("3/4/2017");
        date.add("3/3/2017");
        items.add("Beef Taco, Chicken Taco");
        items.add("Shrimp Taco, Beef Taco, Chicken Taco, Fries");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        OrderHistoryFragment.this.getActivity().setTitle("Order History"); //set activity title

        adapter = new CustomMenuAdapter(OrderHistoryFragment.this.getContext(), name, date, items);
        final ListView listView = (ListView) view.findViewById(R.id.lstOrderHistory);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderHistoryFragment.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_inbox, null);
                final TextView orderID = (TextView) dialogView.findViewById(R.id.lblOrderInfo);
                final TextView custName = (TextView) dialogView.findViewById(R.id.lblCustName);
                final TextView datePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlacedLabel);
                final TextView timePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlaced);
                final TextView total = (TextView) dialogView.findViewById(R.id.lblOrderTotal);
                final TextView orderItems = (TextView) dialogView.findViewById(R.id.lblItems);
                Button okay = (Button) dialogView.findViewById(R.id.btnFulfilled);

                datePlaced.setText("Date Placed");
                okay.setText("Okay");

                final int spot = position;

                orderID.setText("Order #"+position);
                custName.setText(name.get(position));
                timePlaced.setText(date.get(position));
                total.setText("$6.54");
                orderItems.setText("2x Beef Taco\n3x Chicken Taco\n5x Shrimp Taco");

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
