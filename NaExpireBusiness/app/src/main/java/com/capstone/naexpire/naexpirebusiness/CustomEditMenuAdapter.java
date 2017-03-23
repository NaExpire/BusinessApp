package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by graemedrucker on 3/23/17.
 */

public class CustomEditMenuAdapter extends BaseAdapter {
    ArrayList<String> names, prices, descriptions;
    Context context;

    private static LayoutInflater inflater = null;

    public CustomEditMenuAdapter(Context c,
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
        ImageButton bt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_edit_menu, null);
        holder.nm=(TextView) rowView.findViewById(R.id.txtEditFoodName);
        holder.pr=(TextView) rowView.findViewById(R.id.txtEditFoodPrice);
        holder.ds=(TextView) rowView.findViewById(R.id.txtEditFoodDescription);
        holder.bt=(ImageButton) rowView.findViewById(R.id.imgbtnEditMenu);

        holder.nm.setText(names.get(position));
        holder.pr.setText(prices.get(position));
        holder.ds.setText(descriptions.get(position));

        holder.bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                names.remove(position);
                prices.remove(position);
                descriptions.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}