package com.capstone.naexpire.naexpirebusiness;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment {


    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);

        AccountInfoFragment.this.getActivity().setTitle("Account Information"); //set activity title

        Button foodTypes = (Button) view.findViewById(R.id.btnFoodInfo);

        foodTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFoodTypesFragment editFoodTypesFragment = new EditFoodTypesFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, editFoodTypesFragment).commit();
            }
        });

        return view;
    }

}
