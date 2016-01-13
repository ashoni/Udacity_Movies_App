package com.example.annadroid.moviesapp.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;


/**
 * I decided not to store reviews and trailers. Anyway you can't watch trailers without internet.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.annadroid.moviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";


    public static final class FavouriteMovieEntry implements BaseColumns {
        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String TABLE_NAME = "favourite_movies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
