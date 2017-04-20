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


public class FragmentOrderInbox extends Fragment {

    ArrayList<String> orderId = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> timePlaced = new ArrayList<String>();
    ArrayList<String> total = new ArrayList<String>();
    ArrayList<String> items = new ArrayList<String>();
    ListAdapterOrderInbox adapter;

    public FragmentOrderInbox() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_inbox, container, false);

        FragmentOrderInbox.this.getActivity().setTitle("Order Inbox"); //set activity title

        adapter = new ListAdapterOrderInbox(FragmentOrderInbox.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstOrderInbox);
        listView.setAdapter(adapter);

        //test data
        Random rnd = new Random();
        orderId.add(""+(100000 + rnd.nextInt(900000)));
        orderId.add(""+(100000 + rnd.nextInt(900000)));
        name.add("Jerry");
        name.add("Tom");
        timePlaced.add("3:24pm");
        timePlaced.add("4:56pm");
        total.add("$17.54");
        total.add("$13.55");
        items.add("Beef Taco, Chicken Taco");
        items.add("Shrimp Taco, Beef Taco, Chicken Taco, Fries");

        for(int i = 0; i<name.size(); i++){
            adapter.newOrder(orderId.get(i), name.get(i), timePlaced.get(i), total.get(i), items.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentOrderInbox.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_inbox, null);
                final TextView orderID = (TextView) dialogView.findViewById(R.id.lblOrderInfo);
                final TextView custName = (TextView) dialogView.findViewById(R.id.lblCustName);
                final TextView timePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlaced);
                final TextView total = (TextView) dialogView.findViewById(R.id.lblOrderTotal);
                final TextView orderItems = (TextView) dialogView.findViewById(R.id.lblItems);
                Button dismiss = (Button) dialogView.findViewById(R.id.btnDismiss);


                orderID.setText("Order #" + adapter.getOrderId(position));
                custName.setText(adapter.getCustName(position));
                timePlaced.setText(adapter.getTime(position));
                total.setText(adapter.getTotal(position));
                orderItems.setText(adapter.getFood(position));

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                dismiss.setOnClickListener(new View.OnClickListener() {
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
