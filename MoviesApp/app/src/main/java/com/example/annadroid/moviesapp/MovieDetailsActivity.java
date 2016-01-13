package com.example.annadroid.moviesapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MovieDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailsFragment.MOVIE_INFO,
                    getIntent().getParcelableExtra(MovieDetailsFragment.MOVIE_INFO));

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container, fragment)
                    .commit();
        }
    }
}
