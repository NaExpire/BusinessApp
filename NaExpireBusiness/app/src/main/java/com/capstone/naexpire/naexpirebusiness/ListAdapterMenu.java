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

import java.util.*;

public class ListAdapterMenu extends BaseAdapter {
    ArrayList<MenuItem> menu;
    Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterMenu(Context c){
        menu = new ArrayList<>();
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return menu.size();
    }

    @Override
    public Object getItem(int position){
        return position;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getName(int position){ return menu.get(position).getName(); }
    public String getPrice(int position){ return "$"+menu.get(position).getPrice(); }
    public String getDescription(int position){ return menu.get(position).getDescription(); }
    public String getImage(int position){ return menu.get(position).getImage(); }

    public void setItem(int position, String name, Double price, String description, String image){
        menu.get(position).getName();
        menu.get(position).getPrice();
        menu.get(position).getDescription();
        menu.get(position).getImage();
        notifyDataSetChanged();
    }
    public void newItem(String name, Double price, String description, String image){
        menu.add(new MenuItem(name, price, description, image));
        notifyDataSetChanged();
    }

    public class Holder{
        TextView nm, pr, ds;
        ImageView im;
    }

    public void sortMenu(int sortBy){
        switch (sortBy){
            case 0: //alphabetical
                Collections.sort(menu, new Comparator<MenuItem>(){
                    public int compare( MenuItem o1, MenuItem o2){
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                notifyDataSetChanged();
                break;
            case 1: //high to low
                Collections.sort(menu, new Comparator<MenuItem>(){
                    public int compare( MenuItem o1, MenuItem o2){
                        return -1*o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                notifyDataSetChanged();
                break;
            case 2: //low to high
                Collections.sort(menu, new Comparator<MenuItem>(){
                    public int compare( MenuItem o1, MenuItem o2){
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                notifyDataSetChanged();
                break;
        }
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

        holder.nm.setText(menu.get(position).getName());
        holder.pr.setText("$"+menu.get(position).getPrice());
        holder.ds.setText(menu.get(position).getDescription());
        Glide.with(context).load(menu.get(position).getImage()).into(holder.im);

        return rowView;
    }

    public class MenuItem{
        private String name, description, image;
        private double price;

        MenuItem(){
            name = "";
            price = 0.00;
            description = "";
            image = "";
        }

        MenuItem(String n, Double p, String d, String i){
            name = n;
            price  = p;
            description = d;
            image = i;
        }
        String getName(){return name;}
        Double getPrice(){return price;}
        String getDescription(){return description;}
        String getImage(){return image;}

        void setName(String n){name = n;}
        void setPrice(Double p){price = p;}
        void setDescription(String d){description = d;}
        void setImage(String i){image = i;}
    }
}
