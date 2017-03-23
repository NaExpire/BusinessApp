package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomFoodTypeAdapter extends BaseAdapter {

    ArrayList<String> types;
    Context context;

    private static LayoutInflater inflater = null;

    public CustomFoodTypeAdapter(Context c, ArrayList<String> t){
        types = t;
        context = c;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return types.size();
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
        TextView tp;
        ImageButton bt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        final View rowView = inflater.inflate(R.layout.list_edit_food_types, null);
        holder.tp=(TextView) rowView.findViewById(R.id.lblFoodType);
        holder.bt=(ImageButton) rowView.findViewById(R.id.imgbtnClear);

        holder.tp.setText(types.get(position));

        holder.bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                types.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
