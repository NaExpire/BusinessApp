package com.capstone.naexpire.naexpirebusiness;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragmentAccountInfo extends Fragment {

    private SharedPreferences sharedPref;

    private EditText restName, restAddr, restPhone, restDesc, usn, email, personalPhone, pass, cPass;

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
        pass = (EditText) view.findViewById(R.id.txtNewPass);
        cPass = (EditText) view.findViewById(R.id.txtCNewPass);
        Button foodTypes = (Button) view.findViewById(R.id.btnFoodInfo);
        Button save = (Button) view.findViewById(R.id.btnSaveAcctEdits);

        sharedPref = getActivity().getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        restName.setText(sharedPref.getString("restaurantName", ""));
        restAddr.setText(sharedPref.getString("addressLine1", ""));
        restPhone.setText(sharedPref.getString("restaurantPhone", ""));
        restDesc.setText(sharedPref.getString("restaurantDescription", ""));
        usn.setText(sharedPref.getString("username", ""));
        email.setText(sharedPref.getString("email", ""));
        personalPhone.setText(sharedPref.getString("userPhone", ""));

        foodTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);

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
                String enteredpass = pass.getText().toString();
                String enteredcpass = cPass.getText().toString();
                if(isValidPassword(enteredpass)){
                    if(enteredcpass.equals(enteredpass)){
                        editor.putString("password", enteredpass);
                    }
                    else Toast.makeText(FragmentAccountInfo.this.getContext(),
                            "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(FragmentAccountInfo.this.getContext(),
                        "Password must have at least:\n\t8 Characters\n\t1 Capital\n\t1 Number\n\t1 Special Character",
                        Toast.LENGTH_SHORT).show();
                editor.commit();

                //update restaurant name in the navigation drawer
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                TextView rest = (TextView) header.findViewById(R.id.lblNavRest);

                rest.setText(sharedPref.getString("restaurantName", ""));

                Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.layAccount).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        view.findViewById(R.id.layAccountScroll).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideKeyboard(view);
            }
        });

        return view;
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
