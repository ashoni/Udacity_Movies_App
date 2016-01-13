package com.example.annadroid.moviesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.annadroid.moviesapp.R;
import com.example.annadroid.moviesapp.models.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Trailer> data = new ArrayList<>();

    public TrailerAdapter(Context context, int layoutResourceId, ArrayList<Trailer> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            textView = (TextView) convertView.findViewById(R.id.trailer_name);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        textView.setText(data.get(position).getName());
        return convertView;
    }
}
