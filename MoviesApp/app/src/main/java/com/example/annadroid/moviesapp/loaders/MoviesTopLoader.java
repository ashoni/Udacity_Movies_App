package com.example.annadroid.moviesapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.annadroid.moviesapp.models.MovieInfo;
import com.example.annadroid.moviesapp.utils.URLFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoviesTopLoader extends AsyncTaskLoader<List<MovieInfo>> {
    private String sortOrder;

    public MoviesTopLoader(Context context, String sortOrder) {
        super(context);
        this.sortOrder = sortOrder;
    }

    private final String LOG_TAG = MoviesTopLoader.class.getSimpleName();

    //response params
    final String MOVIE_LIST = "results";
    final String MOVIE_POSTER = "poster_path";
    final String MOVIE_ID = "id";
    final String MOVIE_RELEASE_DATE = "release_date";
    final String MOVIE_TITLE = "original_title";
    final String MOVIE_OVERVIEW = "overview";
    final String MOVIE_VOTES = "vote_average";


    @Override
    public List<MovieInfo> loadInBackground() {
        try {
            final URL url = URLFactory.buildSortedMoviesURL(sortOrder);
            final String moviesJsonStr = Proxy.send(url);
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (IOException | JSONException | ParseException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }


    private List<MovieInfo> getMoviesDataFromJson(String moviesJsonStr) throws JSONException,
            ParseException {
        JSONObject movieJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray(MOVIE_LIST);

        List<MovieInfo> results = new ArrayList<>();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            MovieInfo movieInfo = new MovieInfo(movie.getString(MOVIE_ID),
                    movie.getString(MOVIE_POSTER));
            movieInfo.setVoteAverage(movie.getDouble(MOVIE_VOTES));
            movieInfo.setOriginalTitle(movie.getString(MOVIE_TITLE));

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(movie.getString(MOVIE_RELEASE_DATE));
                sdf.applyPattern("MMM, yyyy");
                movieInfo.setReleaseDate(sdf.format(d));
            } catch (ParseException e) {
                Log.w(LOG_TAG, "Wrong date format: " + movie.getString(MOVIE_RELEASE_DATE));
                movieInfo.setReleaseDate("");
            }

            movieInfo.setOverview(movie.getString(MOVIE_OVERVIEW));

            results.add(movieInfo);
        }

        return results;
    }
}
