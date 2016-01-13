package com.example.annadroid.moviesapp.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.example.annadroid.moviesapp.data.MovieContract;
import com.example.annadroid.moviesapp.models.MovieInfo;

public class MovieFavouriteCheck extends AsyncTaskLoader<MovieInfo> {
    private Context context;
    private MovieInfo movie;

    public MovieFavouriteCheck(Context context, MovieInfo movie) {
        super(context);
        this.context = context;
        this.movie = movie;
    }

    @Override
    public MovieInfo loadInBackground() {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.FavouriteMovieEntry.CONTENT_URI,
                null,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movie.getId()},
                null
        );

        movie.setFavourited(cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }

        return movie;
    }
}
