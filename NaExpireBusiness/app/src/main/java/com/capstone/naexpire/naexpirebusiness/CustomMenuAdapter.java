package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomMenuAdapter extends BaseAdapter {
    ArrayList<String> names, prices, descriptions;
    Context context;

    private static LayoutInflater inflater = null;

    public CustomMenuAdapter(Context c,
                             ArrayList<String> n, ArrayList<String> p, ArrayList<String> d){
        names = n;
        prices = p;
        descriptions = d;
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return names.size();
    }

    @Override
    public Object getItem(int position){
        return position;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class Holder{
        TextView nm, pr, ds;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_menu, null);
        holder.nm=(TextView) rowView.findViewById(R.id.txtFoodName);
        holder.pr=(TextView) rowView.findViewById(R.id.txtFoodPrice);
        holder.ds=(TextView) rowView.findViewById(R.id.txtFoodDescription);

        holder.nm.setText(names.get(position));
        holder.pr.setText(prices.get(position));
        holder.ds.setText(descriptions.get(position));

        return rowView;
    }
}
