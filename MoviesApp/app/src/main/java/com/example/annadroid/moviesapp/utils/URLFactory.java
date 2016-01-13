package com.example.annadroid.moviesapp.utils;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class URLFactory {
    private static final String MOVIES_TOP_URL = "http://api.themoviedb.org/3/discover/movie";

    private static final String MOVIES_DETAILS_URL = "http://api.themoviedb.org/3/movie";

    private static final String SORT_ORDER = "sort_by";

    private static final String KEY_PARAM = "api_key";

    private static final String KEY = "ae87a3b8b9d8533b180e10e1aa4cc10f";

    private static final String REVIEWS = "reviews";

    private static final String TRAILERS = "videos";


    private URLFactory() {

    }


    public static URL buildSortedMoviesURL(final String sortOrder) throws MalformedURLException {
        final Uri builtUri = Uri.parse(MOVIES_TOP_URL).buildUpon()
                .appendQueryParameter(SORT_ORDER, sortOrder)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();

        return new URL(builtUri.toString());
    }


    public static URL buildReviewURL(final String id) throws MalformedURLException {
        final Uri builtUri = Uri.parse(String.format("%s/%s/%s", MOVIES_DETAILS_URL, id, REVIEWS))
                .buildUpon()
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();
        return new URL(builtUri.toString());
    }


    public static URL buildTrailerURL(final String id) throws MalformedURLException {
        final Uri builtUri = Uri.parse(String.format("%s/%s/%s", MOVIES_DETAILS_URL, id, TRAILERS))
                .buildUpon()
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();
        return new URL(builtUri.toString());
    }
}
