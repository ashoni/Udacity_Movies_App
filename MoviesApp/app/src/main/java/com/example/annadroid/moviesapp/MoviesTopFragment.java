package com.example.annadroid.moviesapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.annadroid.moviesapp.adapters.GridViewAdapter;
import com.example.annadroid.moviesapp.loaders.MoviesFavouriteLoader;
import com.example.annadroid.moviesapp.loaders.MoviesTopLoader;
import com.example.annadroid.moviesapp.models.MovieInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for movies top-list
 */
public class MoviesTopFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieInfo>> {
    private GridViewAdapter gridViewAdapter;
    GridView gridView;
    private ArrayList<MovieInfo> movies;
    private Callback movieSelectedCallback;
    private Bundle bundle;
    public static final String MOVIES = "movies_list";
    public static final int FAVORITE_LOADER_ID = 1;
    public static final int TOP_LOADER_ID = 0;
    public static final String LOG_TAG = MoviesTopFragment.class.getSimpleName();
    private int currentLoaderId = 0;
    private String sortOrder;


    public interface Callback {
        void onMovieSelected(MovieInfo movieInfo);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            movieSelectedCallback = (Callback) activity;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Can't attach callback");
        }
    }

    @Override
    public void onResume() {
        restartLoader();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            bundle = savedInstanceState;

        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(!item.isChecked());
        switch (id) {
            case R.id.action_top_popular:
                sortOrder = getString(R.string.pref_sort_popularity);
                currentLoaderId = TOP_LOADER_ID;
                break;
            case R.id.action_top_voted:
                sortOrder = getString(R.string.pref_sort_votes);
                currentLoaderId = TOP_LOADER_ID;
                break;
            case R.id.action_favourites:
                currentLoaderId = FAVORITE_LOADER_ID;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        restartLoader();
        return true;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        restartLoader();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES, movies);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);

        sortOrder = getString(R.string.pref_default_sort_order);
        gridView = (GridView) rootView.findViewById(R.id.movies_grid);

        movies = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, movies);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo movie = movies.get(position);
                if (currentLoaderId == FAVORITE_LOADER_ID) {
                    movie.setFavourited(true);
                }
                movieSelectedCallback.onMovieSelected(movie);
            }
        });
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }

        return rootView;
    }


    @Override
    public Loader<List<MovieInfo>> onCreateLoader(int id, Bundle args) {
        if (id == FAVORITE_LOADER_ID) {
            return new MoviesFavouriteLoader(getActivity());
        } else if (id == TOP_LOADER_ID) {
            return new MoviesTopLoader(getActivity(), sortOrder);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<MovieInfo>> loader, List<MovieInfo> data) {
        if (data != null) {
            movies.clear();
            movies.addAll(data);
            gridViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieInfo>> loader) {
    }


    public void restartLoader() {
        getLoaderManager().restartLoader(currentLoaderId, bundle, this).forceLoad();
    }
}
