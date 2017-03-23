package com.capstone.naexpire.naexpirebusiness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ActiveDiscountsFragment extends Fragment {

    CustomMenuAdapter adapter;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();

    public ActiveDiscountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_discounts, container, false);

        ActiveDiscountsFragment.this.getActivity().setTitle("Active Discounts"); //set activity title

        adapter = new CustomMenuAdapter(ActiveDiscountsFragment.this.getContext(), name, price, quantity);
        final ListView listView = (ListView) view.findViewById(R.id.lstOrderInbox);
        listView.setAdapter(adapter);

        return view;
    }

}
