package com.capstone.naexpire.naexpirebusiness;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class FragmentEditMenu extends Fragment {
    private SharedPreferences sharedPref;
    private DatabaseHelperMenu dbHelper = null;

    private ListAdapterEditMenu adapter;
    private ImageView newItemImage;
    private String foodImage;


    public FragmentEditMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        View footer = inflater.inflate(R.layout.footer_add, null);

        sharedPref = getActivity().getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        FragmentEditMenu.this.getActivity().setTitle("Edit Menu"); //set activity title

        Button save = (Button) view.findViewById(R.id.btnEditMenu);
        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);

        adapter = new ListAdapterEditMenu(FragmentEditMenu.this.getContext(), FragmentEditMenu.this);
        final ListView listView = (ListView) view.findViewById(R.id.lstEditMenu);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        dbHelper = new DatabaseHelperMenu(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT name, price, description, image FROM menu", null);

        while (result.moveToNext()) {
            adapter.newItem(result.getString(0), result.getString(1), result.getString(2),
                    result.getString(3));
        }

        result.close();
        db.close();

        //item in the list tapped to edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu, null);
                TextView header = (TextView) dialogView.findViewById(R.id.lblNewMenuItem);
                final EditText newItemName = (EditText) dialogView.findViewById(R.id.txtItemName);
                final EditText newItemPrice = (EditText) dialogView.findViewById(R.id.txtPrice);
                final EditText newItemDesc = (EditText) dialogView.findViewById(R.id.txtDescription);
                newItemImage = (ImageView) dialogView.findViewById(R.id.imgMenuFood);
                Button chooseImage = (Button) dialogView.findViewById(R.id.btnMenuChooseImage);
                Button saveNewItem = (Button) dialogView.findViewById(R.id.btnSaveItem);
                Button cancel = (Button) dialogView.findViewById(R.id.btnCancel);

                header.setText("Edit Menu Item");
                saveNewItem.setText("Save");
                foodImage = adapter.getImage(position);

                final int spot = position;

                newItemName.setText(adapter.getName(position));
                newItemPrice.setText(adapter.getPrice(position));
                newItemDesc.setText(adapter.getDescription(position));
                newItemImage.getLayoutParams().height = 300;
                newItemImage.getLayoutParams().width = 300;
                Glide.with(FragmentEditMenu.this.getContext()).load(foodImage).into(newItemImage);

                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                dialogView.findViewById(R.id.layEditMenu).setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent ev)
                    {
                        hideKeyboard(view);
                        return false;
                    }
                });

                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                    }
                });

                //press button to save edits
                saveNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String holdNew = newItemName.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues v = new ContentValues();
                        v.put("name", newItemName.getText().toString());
                        v.put("price", newItemPrice.getText().toString());
                        v.put("description", newItemDesc.getText().toString());
                        v.put("image", foodImage);

                        String[] selectionArgs = {adapter.getName(position)};

                        db.update("menu", v, "name = ?", selectionArgs);
                        db.close();

                        DatabaseHelperActiveDiscounts dbActiveHelper = new DatabaseHelperActiveDiscounts(getActivity().getApplicationContext());
                        SQLiteDatabase dbActive = dbActiveHelper.getWritableDatabase();

                        ContentValues vals = new ContentValues();
                        vals.put("name", newItemName.getText().toString());

                        selectionArgs[0] = adapter.getName(position);

                        dbActive.update("activeDiscounts", vals, "name = ?", selectionArgs);

                        dbActive.close();
                        dbActiveHelper.close();

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
                Button cancel = (Button) mView.findViewById(R.id.btnCancel);

                foodImage = ("android.resource://com.capstone.naexpire.naexpirebusiness/drawable/tacos");

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mView.findViewById(R.id.layEditMenu).setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent ev)
                    {
                        hideKeyboard(view);
                        return false;
                    }
                });

                chooseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3645);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                    }
                });

                saveNewItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        int id = sharedPref.getInt("mealId", 0) + 1;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("mealId", id); //store a value to signify the user just finished registering
                        editor.commit();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();

                        values.put("id", id);
                        values.put("name", newItemName.getText().toString());
                        values.put("price", newItemPrice.getText().toString());
                        values.put("description", newItemDesc.getText().toString());
                        values.put("image", foodImage);
                        db.insert("menu", null, values);

                        db.close();

                        adapter.newItem(newItemName.getText().toString(),
                                newItemPrice.getText().toString(),
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
            foodImage = resultData.getData().toString();
            //newItemImage.setImageURI(foodImage);
            Glide.with(this).load(foodImage).into(newItemImage);
            newItemImage.getLayoutParams().height = 300;
            newItemImage.getLayoutParams().width = 300;
        }
    }

    private boolean deleteResult = false;
    public void delete(String name, final int position){

        final String n = name;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm_delete, null);
        Button no = (Button) mView.findViewById(R.id.btnNo);
        Button yes = (Button) mView.findViewById(R.id.btnYes);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteResult =  false;
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String[] selectionArgs = {n};

                db.delete("menu", "name = ?", selectionArgs);

                db.close();

                DatabaseHelperActiveDiscounts dbActiveHelper = new DatabaseHelperActiveDiscounts(getActivity().getApplicationContext());
                SQLiteDatabase dbActive = dbActiveHelper.getWritableDatabase();

                dbActive.delete("activeDiscounts", "name = ?", selectionArgs);

                dbActive.close();
                dbActiveHelper.close();

                adapter.deleteItem(position);
                Toast.makeText(FragmentEditMenu.this.getContext(), n + " deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    public void cantDelete(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FragmentEditMenu.this.getContext());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_cant_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mView.findViewById(R.id.btnOkay).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
    }

    protected void hideKeyboard(View view)
    {
        view.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}