package com.example.annadroid.moviesapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.annadroid.moviesapp.models.MovieInfo;
import com.example.annadroid.moviesapp.models.Review;
import com.example.annadroid.moviesapp.utils.URLFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewsLoader extends AsyncTaskLoader<MovieInfo> {
    private final String LOG_TAG = MovieReviewsLoader.class.getSimpleName();

    //response params
    final String REVIEW_LIST = "results";
    final String AUTHOR = "author";
    final String CONTENT = "content";
    private final MovieInfo movie;


    public MovieReviewsLoader(Context context, MovieInfo movie) {
        super(context);
        this.movie = movie;
    }

    private MovieInfo getMoviesDataFromJson(final String reviewsJsonStr) throws JSONException,
            ParseException {
        final JSONObject movieJson = new JSONObject(reviewsJsonStr);
        final JSONArray reviewsArray = movieJson.getJSONArray(REVIEW_LIST);

        final List<Review> results = new ArrayList<>();
        for (int i = 0; i < reviewsArray.length(); i++) {
            final JSONObject review = reviewsArray.getJSONObject(i);

            final Review reviewInfo = new Review(review.getString(AUTHOR), review.getString(CONTENT));
            results.add(reviewInfo);
        }
        movie.getReviews().clear();
        movie.getReviews().addAll(results);

        return movie;
    }

    @Override
    public MovieInfo loadInBackground() {
        try {
            final URL url = URLFactory.buildReviewURL(movie.getId());
            final String reviewsJsonStr = Proxy.send(url);
            return getMoviesDataFromJson(reviewsJsonStr);
        } catch (IOException | JSONException | ParseException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
}
