package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterPastOrders extends BaseAdapter {
    ArrayList<String> orderId, custName, timePlaced, total, foods;
    Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterPastOrders(Context c){
        orderId = new ArrayList<>();
        custName = new ArrayList<>();
        timePlaced = new ArrayList<>();
        total = new ArrayList<>();
        foods = new ArrayList<>();
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return foods.size();
    }

    @Override
    public String[] getItem(int position){
        String[] s = {orderId.get(position), custName.get(position), timePlaced.get(position), total.get(position), foods.get(position)};
        return s;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public void newOrder(String o, String n, String ti, String t, String f){
        orderId.add(o);
        custName.add(n);
        timePlaced.add(ti);
        total.add(t);
        foods.add(f);
        notifyDataSetChanged();
    }

    public class Holder{
        TextView id, info, tl;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ListAdapterPastOrders.Holder holder = new ListAdapterPastOrders.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_past_orders, null);
        holder.id=(TextView) rowView.findViewById(R.id.txtPastOrderId);
        holder.info=(TextView) rowView.findViewById(R.id.txtPastOrderInfo);
        holder.tl=(TextView) rowView.findViewById(R.id.txtPastTotal);

        holder.id.setText("#" + orderId.get(position));
        holder.info.setText(custName.get(position) + " - " + foods.get(position));
        holder.tl.setText(total.get(position));

        return rowView;
    }
}