package com.example.annadroid.moviesapp.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.annadroid.moviesapp.R;
import com.example.annadroid.moviesapp.models.Review;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Review> data = new ArrayList<>();

    public ReviewAdapter(Context context, int layoutResourceId, ArrayList<Review> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewViewHolder reviewViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);//R.layout.movie_review
            reviewViewHolder = new ReviewViewHolder(convertView);
            convertView.setTag(reviewViewHolder);
        } else {
            reviewViewHolder = (ReviewViewHolder) convertView.getTag();
        }


        final Review review = data.get(position);
        reviewViewHolder.getAuthorView().setText(review.getAuthor());
        reviewViewHolder.getContentView().setText(review.getText());

        return convertView;
    }

    public static class ReviewViewHolder {
        private final TextView authorView;
        private final TextView contentView;

        public ReviewViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.movie_review_author);
            contentView = (TextView) view.findViewById(R.id.movie_review_content);
        }

        public TextView getAuthorView() {
            return authorView;
        }

        public TextView getContentView() {
            return contentView;
        }
    }
}
