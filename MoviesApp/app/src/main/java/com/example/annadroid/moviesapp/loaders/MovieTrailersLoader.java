package com.example.annadroid.moviesapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.annadroid.moviesapp.models.MovieInfo;
import com.example.annadroid.moviesapp.models.Trailer;
import com.example.annadroid.moviesapp.utils.URLFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MovieTrailersLoader extends AsyncTaskLoader<MovieInfo> {
    private final String LOG_TAG = MovieTrailersLoader.class.getSimpleName();

    //response params
    final String TRAILER_LIST = "results";
    final String LINK = "key";
    final String NAME = "name";
    final String SITE = "site";
    private final MovieInfo movie;


    public MovieTrailersLoader(Context context, MovieInfo movie) {
        super(context);
        this.movie = movie;
    }


    private MovieInfo getTrailersDataFromJson(final String trailersJsonStr) throws JSONException,
            ParseException {
        final JSONObject movieJson = new JSONObject(trailersJsonStr);
        final JSONArray trailersArray = movieJson.getJSONArray(TRAILER_LIST);

        final List<Trailer> results = new ArrayList<>();
        for (int i = 0; i < trailersArray.length(); i++) {
            final JSONObject trailer = trailersArray.getJSONObject(i);
            final String site = trailer.getString(SITE);
            if (site == null || !site.equals("YouTube"))
                continue;

            final Trailer trailerInfo = new Trailer(trailer.getString(NAME),
                    trailer.getString(LINK));
            results.add(trailerInfo);
        }
        movie.getTrailerLinks().clear();
        movie.getTrailerLinks().addAll(results);

        return movie;
    }

    @Override
    public MovieInfo loadInBackground() {
        try {
            final URL url = URLFactory.buildTrailerURL(movie.getId());
            final String trailersJsonStr = Proxy.send(url);
            return getTrailersDataFromJson(trailersJsonStr);
        } catch (IOException | JSONException | ParseException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
}
