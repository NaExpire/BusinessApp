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
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> items = new ArrayList<String>();
    ListAdapterMenu adapter;

    public FragmentOrderInbox() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //test data
        name.add("Jerry");
        name.add("Tom");
        time.add("4:56pm");
        time.add("5:10pm");
        items.add("Beef Taco, Chicken Taco");
        items.add("Shrimp Taco, Beef Taco, Chicken Taco, Fries");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_inbox, container, false);

        FragmentOrderInbox.this.getActivity().setTitle("Order Inbox"); //set activity title

        adapter = new ListAdapterMenu(FragmentOrderInbox.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstOrderInbox);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentOrderInbox.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_inbox, null);
                final TextView orderID = (TextView) dialogView.findViewById(R.id.lblOrderInfo);
                final TextView custName = (TextView) dialogView.findViewById(R.id.lblCustName);
                final TextView timePlaced = (TextView) dialogView.findViewById(R.id.lblTimePlaced);
                final TextView total = (TextView) dialogView.findViewById(R.id.lblOrderTotal);
                final TextView orderItems = (TextView) dialogView.findViewById(R.id.lblItems);
                Button fulfilled = (Button) dialogView.findViewById(R.id.btnFulfilled);

                final int spot = position;

                orderID.setText("Order #"+position);
                custName.setText(name.get(position));
                timePlaced.setText(time.get(position));
                total.setText("$6.54");
                orderItems.setText("2x Beef Taco\n3x Chicken Taco\n5x Shrimp Taco");

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                fulfilled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        name.remove(spot);
                        time.remove(spot);
                        items.remove(spot);
                        adapter.notifyDataSetChanged(); //redraw the list on the screen
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
