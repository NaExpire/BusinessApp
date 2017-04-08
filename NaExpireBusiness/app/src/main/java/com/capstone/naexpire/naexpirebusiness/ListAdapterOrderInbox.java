package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterOrderInbox extends BaseAdapter {
    ArrayList<String> orderId, foods, custName, timePlaced, total;
    Context context;

    private static LayoutInflater inflater = null;

    public ListAdapterOrderInbox(Context c){
        orderId = new ArrayList<>();
        foods = new ArrayList<>();
        custName = new ArrayList<>();
        timePlaced = new ArrayList<>();
        total = new ArrayList<>();
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return foods.size();
    }

    @Override
    public Object getItem(int position){ return position; }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getOrderId(int position) { return orderId.get(position); }
    public String getFood(int position) {
        String f = foods.get(position).replaceAll(", ", "\n");
        return f;
    }
    public String getCustName(int position) { return custName.get(position); }
    public String getTime(int position) { return timePlaced.get(position); }
    public String getTotal(int position) { return total.get(position); }

    public void newOrder(String id, String name, String time, String price, String food){
        orderId.add(id);
        custName.add(name);
        timePlaced.add(time);
        total.add(price);
        foods.add(food);
        notifyDataSetChanged();
    }

    public void fulfilled(int position){
        foods.remove(position);
        custName.remove(position);
        total.remove(position);
        notifyDataSetChanged();
    }

    public class Holder{
        TextView cn, t, fd;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ListAdapterOrderInbox.Holder holder = new ListAdapterOrderInbox.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_order_inbox, null);
        holder.fd=(TextView) rowView.findViewById(R.id.txtInboxFoods);
        holder.cn=(TextView) rowView.findViewById(R.id.txtInboxCustName);
        holder.t=(TextView) rowView.findViewById(R.id.txtInboxTotal);

        holder.fd.setText(foods.get(position));
        holder.cn.setText(custName.get(position));
        holder.t.setText(total.get(position));

        return rowView;
    }
}