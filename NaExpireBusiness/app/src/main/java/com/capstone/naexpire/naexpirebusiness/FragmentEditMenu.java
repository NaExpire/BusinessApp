package com.capstone.naexpire.naexpirebusiness;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;


public class FragmentEditMenu extends Fragment {

    ListAdapterEditMenu adapter;
    ImageView newItemImage;
    Uri foodImage;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();
    ArrayList<Uri> image = new ArrayList<Uri>();


    public FragmentEditMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        View footer = inflater.inflate(R.layout.footer_add, null);

        FragmentEditMenu.this.getActivity().setTitle("Edit Menu"); //set activity title

        Button save = (Button) view.findViewById(R.id.btnEditMenu);
        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        adapter = new ListAdapterEditMenu(FragmentEditMenu.this.getContext());
        final ListView listView = (ListView) view.findViewById(R.id.lstEditMenu);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        //test data
        name.add("Beef Taco");
        name.add("Chicken Taco");
        name.add("Cheeseburger");
        price.add("$1.23");
        price.add("$2.34");
        price.add("$3.45");
        description.add("Taco with beef");
        description.add("Taco with chicken");
        description.add("Burger with cheese");
        image.add(Uri.parse("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos"));
        image.add(Uri.parse("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos2"));
        image.add(Uri.parse("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/burger"));

        for(int i = 0; i < name.size(); i++){
            adapter.newItem(name.get(i),price.get(i),description.get(i), image.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu, null);
                final EditText newItemName = (EditText) dialogView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) dialogView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) dialogView.findViewById(R.id.txtDescription);
                newItemImage = (ImageView) dialogView.findViewById(R.id.imgMenuFood);
                Button chooseImage = (Button) dialogView.findViewById(R.id.btnMenuChooseImage);
                Button saveNewItem = (Button) dialogView.findViewById(R.id.btnSaveItem);

                foodImage = adapter.getImage(position);

                final int spot = position;

                newItemName.setText(adapter.getName(position));
                newItemPrice.setText(adapter.getPrice(position));
                newItemDesc.setText(adapter.getDescription(position));
                newItemImage.setImageURI(foodImage);

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                saveNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        name.set(spot, newItemName.getText().toString());
                        price.set(spot, newItemPrice.getText().toString());
                        description.set(spot, newItemDesc.getText().toString());
                        adapter.setItem(spot, newItemName.getText().toString(),
                                newItemPrice.getText().toString(),newItemDesc.getText().toString(),
                                foodImage);
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
                newItemImage = (ImageView) mView.findViewById(R.id.imgMenuFood);
                Button chooseImage = (Button) mView.findViewById(R.id.btnMenuChooseImage);
                Button saveNewItem = (Button) mView.findViewById(R.id.btnSaveItem);

                foodImage = Uri.parse("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos");

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                saveNewItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        name.add(newItemName.getText().toString());
                        price.add("$"+newItemPrice.getText().toString());
                        description.add(newItemDesc.getText().toString());
                        adapter.newItem(newItemName.getText().toString(),
                                "$"+newItemPrice.getText().toString(),
                                newItemDesc.getText().toString(),
                                foodImage);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == 3645 && resultData != null) {
            foodImage = resultData.getData();
            newItemImage.setImageURI(foodImage);
            newItemImage.getLayoutParams().height = 300;
            newItemImage.getLayoutParams().width = 300;
        }
    }

}
