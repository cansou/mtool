package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ModelAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] models;

    public ModelAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.models = objects;
        this.context = context;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(17367048, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(16908308);
        tv.setText(this.models[position]);
        tv.setGravity(17);
        tv.setTextColor(-16777216);
        tv.setTextSize(20.0f);
        tv.setHeight(50);
        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(17367048, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(16908308);
        tv.setHeight(35);
        tv.setText(this.models[position]);
        tv.setGravity(17);
        tv.setTextColor(-16777216);
        tv.setTextSize(18.0f);
        return convertView;
    }
}
