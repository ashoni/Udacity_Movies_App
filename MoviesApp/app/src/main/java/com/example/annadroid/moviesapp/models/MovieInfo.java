package com.example.annadroid.moviesapp.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.annadroid.moviesapp.data.MovieContract;

import java.util.ArrayList;

public class MovieInfo implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String PICTURE_SIZE = "w185/";

    private String originalTitle;

    private String releaseDate;

    private String posterPath;

    private double voteAverage;

    private String overview;

    private String id;

    private Boolean favourited;

    private ArrayList<Review> reviews = new ArrayList<>();

    private ArrayList<Trailer> trailerLinks = new ArrayList<>();


    public MovieInfo(String id, String posterPath) {
        this.id = id;
        this.posterPath = BASE_URL + PICTURE_SIZE + posterPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        if (favourited == null)
            parcel.writeInt(0);
        else if (!favourited)
            parcel.writeInt(1);
        else
            parcel.writeInt(2);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR
            = new Parcelable.Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    private MovieInfo(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        int favouriteMark = in.readInt();
        if (favouriteMark == 1)
            favourited = false;
        else if (favouriteMark == 2)
            favourited = true;
    }

    public MovieInfo(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID));
        originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_TITLE));
        releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE));
        posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PATH));
        voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE));
        overview = cursor.getString(cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_OVERVIEW));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Boolean isFavourited() {
        return favourited;
    }

    public void setFavourited(Boolean isFavourited) {
        this.favourited = isFavourited;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Trailer> getTrailerLinks() {
        return trailerLinks;
    }

    public void setTrailerLinks(ArrayList<Trailer> trailerLinks) {
        this.trailerLinks = trailerLinks;
    }
}
