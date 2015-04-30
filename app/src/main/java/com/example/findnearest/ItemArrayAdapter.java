package com.example.findnearest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemArrayAdapter extends  ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ItemArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_main, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_main, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);


        String s = values[position];


        if (s.equals("Gas Station")) {
            imageView.setImageResource(R.drawable.gasstation);
        } else if (s.equals("Cafe")) {
            imageView.setImageResource(R.drawable.coffeeicon);
        } else {
            imageView.setImageResource(R.drawable.pharmacy);
        }

        return rowView;
    }
}