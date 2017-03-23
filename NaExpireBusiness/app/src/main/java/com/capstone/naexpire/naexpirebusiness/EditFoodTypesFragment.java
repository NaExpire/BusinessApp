package com.capstone.naexpire.naexpirebusiness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class EditFoodTypesFragment extends Fragment {

    CustomFoodTypeAdapter adapter;
    ArrayList<String> fTypes = new ArrayList<String>();


    public EditFoodTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //test data
        fTypes.add("Cajun");
        fTypes.add("Mexican");
        fTypes.add("Chinese");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_food_types, container, false);
        View footer = inflater.inflate(R.layout.footer_add, null);

        adapter = new CustomFoodTypeAdapter(EditFoodTypesFragment.this.getContext(), fTypes);
        final ListView listView = (ListView) view.findViewById(R.id.lstTypes);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        Button foot = (Button) footer.findViewById(R.id.btnFooterNew);
        Button acctInfo = (Button) view.findViewById(R.id.btnacctInfo);
        Button save = (Button) view.findViewById(R.id.btnSaveFoods);

        foot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditFoodTypesFragment.this.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_food_type, null);
                final EditText newFoodType = (EditText) mView.findViewById(R.id.txtNewFoodType);
                Button saveFoodType = (Button) mView.findViewById(R.id.btnNewFoodType);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                saveFoodType.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String newFood = newFoodType.getText().toString();
                        fTypes.add(newFood);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

        acctInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AccountInfoFragment accountInfoFragment = new AccountInfoFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, accountInfoFragment).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(EditFoodTypesFragment.this.getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
