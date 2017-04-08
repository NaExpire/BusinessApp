package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapterMenu extends BaseAdapter {
    ArrayList<String> names, prices, descriptions;
    private ArrayList<String> images;
    Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterMenu(Context c){
        names = new ArrayList<>();
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
        images = new ArrayList<>();
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
    public String getDescription(int position){ return descriptions.get(position); }
    public String getImage(int position){ return images.get(position); }

    public void setItem(int position, String name, String price, String description, String image){
        names.set(position, name);
        prices.set(position, price);
        descriptions.set(position, description);
        images.set(position, image);
        notifyDataSetChanged();
    }
    public void newItem(String name, String price, String description, String image){
        names.add(name);
        prices.add(price);
        descriptions.add(description);
        images.add(image);
        notifyDataSetChanged();
    }

    public class Holder{
        TextView nm, pr, ds;
        ImageView im;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_menu, null);
        holder.nm=(TextView) rowView.findViewById(R.id.txtMenuName);
        holder.pr=(TextView) rowView.findViewById(R.id.txtMenuPrice);
        holder.ds=(TextView) rowView.findViewById(R.id.txtMenuDescription);
        holder.im=(ImageView) rowView.findViewById(R.id.imgMenuPic);

        holder.nm.setText(names.get(position));
        holder.pr.setText(prices.get(position));
        holder.ds.setText(descriptions.get(position));
        Glide.with(context).load(images.get(position)).into(holder.im);

        return rowView;
    }
}
