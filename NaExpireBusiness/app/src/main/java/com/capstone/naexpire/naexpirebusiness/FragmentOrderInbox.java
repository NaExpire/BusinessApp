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


public class FragmentOrderInbox extends Fragment {

    ArrayList<String> name = new ArrayList<String>();
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
        name.add("Jerry");
        name.add("Tom");
        total.add("$17.54");
        total.add("$13.55");
        items.add("Beef Taco, Chicken Taco");
        items.add("Shrimp Taco, Beef Taco, Chicken Taco, Fries");

        for(int i = 0; i<name.size(); i++){
            adapter.newOrder(items.get(i), name.get(i), total.get(i));
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
                Button fulfilled = (Button) dialogView.findViewById(R.id.btnFulfilled);


                String[] s = adapter.getItem(position);

                orderID.setText("Order #" + s[0]);
                custName.setText(s[1]);
                timePlaced.setText(s[2]);
                total.setText(s[3]);
                orderItems.setText(s[4]);

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                fulfilled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.fulfilled(position);
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
