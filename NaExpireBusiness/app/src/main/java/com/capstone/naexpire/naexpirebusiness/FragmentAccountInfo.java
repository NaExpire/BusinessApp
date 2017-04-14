package com.capstone.naexpire.naexpirebusiness;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentAccountInfo extends Fragment {

    private SharedPreferences sharedPref;

    private EditText restName, restAddr, restPhone, restDesc, usn, email, personalPhone;

    public FragmentAccountInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);

        FragmentAccountInfo.this.getActivity().setTitle("Account Information"); //set activity title

        restName = (EditText) view.findViewById(R.id.txtRestName);
        restAddr = (EditText) view.findViewById(R.id.txtRestAddr);
        restPhone = (EditText) view.findViewById(R.id.txtRestPhone);
        restDesc = (EditText) view.findViewById(R.id.txtRestDesc);
        usn = (EditText) view.findViewById(R.id.txtUsn);
        email = (EditText) view.findViewById(R.id.txtEm);
        personalPhone = (EditText) view.findViewById(R.id.txtPPhone);
        Button foodTypes = (Button) view.findViewById(R.id.btnFoodInfo);
        Button save = (Button) view.findViewById(R.id.btnSaveAcctEdits);

        sharedPref = getActivity().getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        restName.setText(sharedPref.getString("restaurantName", ""));
        restAddr.setText(sharedPref.getString("restaurantAddress", ""));
        restPhone.setText(sharedPref.getString("restaurantPhone", ""));
        restDesc.setText(sharedPref.getString("restaurantDescription", ""));
        usn.setText(sharedPref.getString("username", ""));
        email.setText(sharedPref.getString("email", ""));
        personalPhone.setText(sharedPref.getString("userPhone", ""));

        foodTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEditFoodTypes fragmentEditFoodTypes = new FragmentEditFoodTypes();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, fragmentEditFoodTypes).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("restaurantName", restName.getText().toString());
                editor.putString("restaurantAddress", restAddr.getText().toString());
                editor.putString("restaurantPhone", restPhone.getText().toString());
                editor.putString("restaurantDescription", restDesc.getText().toString());
                editor.putString("username", usn.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("userPhone", personalPhone.getText().toString());
                editor.commit();

                //update restaurant name in the navigation drawer
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                TextView rest = (TextView) header.findViewById(R.id.lblNavRest);

                rest.setText(sharedPref.getString("restaurantName", ""));

                Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
