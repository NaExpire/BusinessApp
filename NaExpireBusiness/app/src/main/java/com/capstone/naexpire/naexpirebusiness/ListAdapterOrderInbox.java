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
        foods = new ArrayList<>();
        custName = new ArrayList<>();
        total = new ArrayList<>();
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return foods.size();
    }

    @Override
    public String[] getItem(int position){
        String[] s = {"123456", custName.get(position), "9:45pm 2/12/17", total.get(position), foods.get(position)};
        return s;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public void newOrder(String f, String n, String t){
        foods.add(f);
        custName.add(n);
        total.add(t);
        notifyDataSetChanged();
    }

    public void fulfilled(int position){
        foods.remove(position);
        custName.remove(position);
        total.remove(position);
        notifyDataSetChanged();
    }

    public class Holder{
        TextView fd, cn, tl;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ListAdapterOrderInbox.Holder holder = new ListAdapterOrderInbox.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_order_inbox, null);
        holder.fd=(TextView) rowView.findViewById(R.id.txtInboxFoods);
        holder.cn=(TextView) rowView.findViewById(R.id.txtInboxCustName);
        holder.tl=(TextView) rowView.findViewById(R.id.txtInboxTotal);

        holder.fd.setText(foods.get(position));
        holder.cn.setText(custName.get(position));
        holder.tl.setText(total.get(position));

        return rowView;
    }
}