package com.example.annadroid.moviesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.annadroid.moviesapp.R;
import com.example.annadroid.moviesapp.models.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Loads images for gridview
 */
public class GridViewAdapter extends ArrayAdapter<MovieInfo> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<MovieInfo> data = new ArrayList<>();


    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<MovieInfo> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.movie_poster_grid);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }

        Picasso.with(context).load(data.get(position).getPosterPath()).
                placeholder(R.drawable.waiting).error(R.drawable.no_poster)
                .into(imageView);
        return convertView;
    }
}
