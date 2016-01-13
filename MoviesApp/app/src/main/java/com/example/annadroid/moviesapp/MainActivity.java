package com.example.annadroid.moviesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.annadroid.moviesapp.models.MovieInfo;


public class MainActivity extends AppCompatActivity implements MoviesTopFragment.Callback {

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twoPane = (findViewById(R.id.movie_details_container) != null);

        if (twoPane && savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, new MovieDetailsFragment(),
                            MovieDetailsFragment.LOG_TAG)
                    .commit();
        }
    }


    @Override
    public void onMovieSelected(MovieInfo movieInfo) {
        if (twoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailsFragment.MOVIE_INFO, movieInfo);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, MovieDetailsFragment.LOG_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class)
                    .putExtra(MovieDetailsFragment.MOVIE_INFO, movieInfo);
            startActivity(intent);
        }
    }
}
