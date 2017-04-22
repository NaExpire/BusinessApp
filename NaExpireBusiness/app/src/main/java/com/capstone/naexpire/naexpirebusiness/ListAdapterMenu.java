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

import org.json.JSONObject;

import java.text.DecimalFormat;
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

    public Integer getId(int position){
        return menu.get(position).getId();
    }
    public String getName(int position){ return menu.get(position).getName(); }
    public Double getPrice(int position){ return menu.get(position).getPrice(); }
    public String getDescription(int position){ return menu.get(position).getDescription(); }
    public Integer getQuantity(int position){ return menu.get(position).getQuantity(); }
    public Double getDeal(int position){ return menu.get(position).getDeal(); }
    public String getImage(int position){ return menu.get(position).getImage(); }
    public String getJson(int position) {return menu.get(position).getJson();}

    public void setQuantity(int position, int quantity){ menu.get(position).setQuantity(quantity);}
    public void setDeal(int position, double deal){ menu.get(position).setDeal(deal);}
    public void setPrice(int position, double price){ menu.get(position).setPrice(price);}

    public void newItem(int id, String name, Double price, String description, int quantity,
                        double deal, String image){
        menu.add(new MenuItem(id, name, price, description, quantity, deal, image));
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
                        return -1*o1.getDeal().compareTo(o2.getDeal());
                    }
                });
                notifyDataSetChanged();
                break;
            case 2: //low to high
                Collections.sort(menu, new Comparator<MenuItem>(){
                    public int compare( MenuItem o1, MenuItem o2){
                        return o1.getDeal().compareTo(o2.getDeal());
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
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if(getQuantity(position) > 0) holder.pr.setText("$"+decimalFormat.format(getDeal(position)));
        else holder.pr.setText("");
        holder.ds.setText(menu.get(position).getDescription());
        Glide.with(context).load(menu.get(position).getImage()).into(holder.im);

        return rowView;
    }

    public class MenuItem{
        private String name, description, image;
        private double price, deal;
        private int id, quantity;

        MenuItem(){
            id = 0;
            name = "";
            price = 0.00;
            description = "";
            quantity = 0;
            deal = 0.0;
            image = "";
        }

        MenuItem(int id, String name, Double price, String description, int quantity, double deal, String image){
            this.id = id;
            this.name = name;
            this.price  = price;
            this.description = description;
            this.quantity = quantity;
            this.deal = deal;
            this.image = image;
        }
        Integer getId(){return id;}
        String getName(){return name;}
        Double getPrice(){return price;}
        String getDescription(){return description;}
        Integer getQuantity(){return quantity;}
        Double getDeal(){return deal;}
        String getImage(){return image;}
        String getJson(){return toJsonString(id, name, description, image, price);}

        void setName(String n){name = n;}
        void setPrice(Double p){price = p;}
        void setDescription(String d){description = d;}
        void setQuantity(Integer quantity){this.quantity = quantity;}
        void setDeal(Double deal){this.deal = deal;}
        void setImage(String i){image = i;}
    }

    public String toJsonString(int id, String name, String description, String image, double price) {
        String returnJ = "";
        try{
            JSONObject js = new JSONObject();
            js.put("id", id);
            js.put("name", name);
            js.put("description", description);
            js.put("image", image);
            js.put("price", price);
            returnJ = js.toString();
            android.util.Log.w(this.getClass().getSimpleName(),returnJ);
        }
        catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return returnJ;
    }
}
