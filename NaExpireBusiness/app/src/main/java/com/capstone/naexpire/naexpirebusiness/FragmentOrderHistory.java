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
import java.util.Random;


public class FragmentOrderHistory extends Fragment {

    ArrayList<String> orderid = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> total = new ArrayList<String>();
    ListAdapterPastOrders adapter;

    public FragmentOrderHistory() {
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
        total.add("$5.67");
        total.add("$8.43");

        Random rnd = new Random();
        orderid.add(""+(100000 + rnd.nextInt(900000)));
        orderid.add(""+(100000 + rnd.nextInt(900000)));


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        FragmentOrderHistory.this.getActivity().setTitle("Order History"); //set activity title

        adapter = new ListAdapterPastOrders(FragmentOrderHistory.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstOrderHistory);
        listView.setAdapter(adapter);

        for(int i = 0; i < name.size(); i++){
            adapter.newOrder(orderid.get(i), name.get(i), date.get(i), total.get(i), items.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentOrderHistory.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_inbox, null);
                final TextView orderID = (TextView) dialogView.findViewById(R.id.lblOrderInfo);
                final TextView custName = (TextView) dialogView.findViewById(R.id.lblCustName);
                final TextView datePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlacedLabel);
                final TextView timePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlaced);
                final TextView total = (TextView) dialogView.findViewById(R.id.lblOrderTotal);
                final TextView orderItems = (TextView) dialogView.findViewById(R.id.lblItems);
                Button okay = (Button) dialogView.findViewById(R.id.btnDismiss);

                datePlaced.setText("Date Placed");
                okay.setText("Okay");

                orderID.setText("Order #" + adapter.getOrderId(position));
                custName.setText(adapter.getCustName(position));
                timePlaced.setText(adapter.getTime(position));
                total.setText(adapter.getTotal(position));
                orderItems.setText(adapter.getFood(position));

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
