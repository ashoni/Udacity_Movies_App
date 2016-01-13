package com.example.annadroid.moviesapp.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.annadroid.moviesapp.data.MovieContract;
import com.example.annadroid.moviesapp.models.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class MoviesFavouriteLoader extends AsyncTaskLoader<List<MovieInfo>> {
    private Context context;

    public MoviesFavouriteLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<MovieInfo> loadInBackground() {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.FavouriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        List<MovieInfo> movies = new ArrayList<>();
        if (cursor == null)
            return movies;
        while (cursor.moveToNext()) {
            movies.add(new MovieInfo(cursor));
        }
        cursor.close();
        return movies;
    }
}
