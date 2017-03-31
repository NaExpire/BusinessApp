package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterMenu extends BaseAdapter {
    ArrayList<String> names, prices, descriptions;
    Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterMenu(Context c){
        names = new ArrayList<>();
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
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

    public String getName(int position){ return names.get(position); }
    public String getPrice(int position){ return prices.get(position); }
    public String getDescrip(int position){ return descriptions.get(position); }
    public void editItem(int position, String n, String p, String d){
        names.set(position, n);
        prices.set(position, p);
        descriptions.set(position, d);
        notifyDataSetChanged();
    }
    public void newItem(String n, String p, String d){
        names.add(n);
        prices.add(p);
        descriptions.add(d);
        notifyDataSetChanged();
    }

    public class Holder{
        TextView nm, pr, ds;
        ImageButton ex;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_menu, null);
        holder.nm=(TextView) rowView.findViewById(R.id.txtFoodName);
        holder.pr=(TextView) rowView.findViewById(R.id.txtFoodPrice);
        holder.ds=(TextView) rowView.findViewById(R.id.txtFoodDescription);
        holder.ex = (ImageButton) rowView.findViewById(R.id.imgbtnClear);

        holder.nm.setText(names.get(position));
        holder.pr.setText(prices.get(position));
        holder.ds.setText(descriptions.get(position));

        holder.ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.remove(position);
                prices.remove(position);
                descriptions.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
