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
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentEditMenu extends Fragment {

    ListAdapterEditMenu adapter;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();


    public FragmentEditMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //test data
        name.add("Beef Taco");
        name.add("Chicken Taco");
        name.add("Shrimp Taco");
        price.add("$1.23");
        price.add("$2.34");
        price.add("$3.45");
        description.add("Taco with beef");
        description.add("Taco with chicken");
        description.add("Taco with shrimp");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        View footer = inflater.inflate(R.layout.footer_add, null);

        FragmentEditMenu.this.getActivity().setTitle("Edit Menu"); //set activity title

        Button save = (Button) view.findViewById(R.id.btnEditMenu);
        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        adapter = new ListAdapterEditMenu(FragmentEditMenu.this.getContext(), name, price, description);
        final ListView listView = (ListView) view.findViewById(R.id.lstEditMenu);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText newItemName = (EditText) dialogView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) dialogView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) dialogView.findViewById(R.id.txtDescription);
                Button saveNewItem = (Button) dialogView.findViewById(R.id.btnSaveItem);

                final int spot = position;

                newItemName.setText(name.get(position));
                newItemPrice.setText(price.get(position));
                newItemDesc.setText(description.get(position));

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                saveNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        name.set(spot, newItemName.getText().toString());
                        price.set(spot, newItemPrice.getText().toString());
                        description.set(spot, newItemDesc.getText().toString());
                        adapter.notifyDataSetChanged(); //redraw the list on the screen
                        dialog.dismiss();
                    }
                });
            }
        });

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText newItemName = (EditText) mView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) mView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) mView.findViewById(R.id.txtDescription);
                Button saveNewItem = (Button) mView.findViewById(R.id.btnSaveItem);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                saveNewItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        name.add(newItemName.getText().toString());
                        price.add("$"+newItemPrice.getText().toString());
                        description.add(newItemDesc.getText().toString());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(FragmentEditMenu.this.getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
