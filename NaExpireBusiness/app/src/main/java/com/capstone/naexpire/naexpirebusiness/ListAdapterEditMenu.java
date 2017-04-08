package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by graemedrucker on 3/23/17.
 */

public class ListAdapterEditMenu extends BaseAdapter {
    private ArrayList<String> names, prices, descriptions;
    private ArrayList<Uri> images;
    private Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterEditMenu(Context c){
        names = new ArrayList<>();
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
        images = new ArrayList<>();
        context = c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void newItem(String name, String price, String description, Uri image){
        names.add(name);
        prices.add(price);
        descriptions.add(description);
        images.add(image);
        notifyDataSetChanged();
    }

    public void setItem(int position, String name, String price, String description, Uri image){
        names.set(position, name);
        prices.set(position, price);
        descriptions.set(position, description);
        images.set(position, image);
        notifyDataSetChanged();
    }

    public String getName(int position){ return names.get(position); }
    public String getPrice(int position){ return prices.get(position); }
    public String getDescription(int position){ return descriptions.get(position); }
    public Uri getImage(int position){ return images.get(position); }

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
        ImageView im;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_menu, null);
        holder.nm=(TextView) rowView.findViewById(R.id.txtFoodName);
        holder.pr=(TextView) rowView.findViewById(R.id.txtFoodPrice);
        holder.ds=(TextView) rowView.findViewById(R.id.txtFoodDescription);
        holder.im=(ImageView) rowView.findViewById(R.id.imgFoodPic);
        holder.bt=(ImageButton) rowView.findViewById(R.id.imgbtnClear);

        holder.nm.setText(names.get(position));
        holder.pr.setText(prices.get(position));
        holder.ds.setText(descriptions.get(position));
        holder.im.setImageURI(images.get(position));

        holder.bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                names.remove(position);
                prices.remove(position);
                descriptions.remove(position);
                images.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}