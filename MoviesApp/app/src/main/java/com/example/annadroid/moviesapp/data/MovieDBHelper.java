package com.example.annadroid.moviesapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "moviesapp.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " +
                MovieContract.FavouriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.FavouriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
