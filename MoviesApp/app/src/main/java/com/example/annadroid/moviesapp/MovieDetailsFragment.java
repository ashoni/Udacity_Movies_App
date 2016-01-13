package com.example.annadroid.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annadroid.moviesapp.adapters.ReviewAdapter;
import com.example.annadroid.moviesapp.adapters.TrailerAdapter;
import com.example.annadroid.moviesapp.data.MovieContract;
import com.example.annadroid.moviesapp.loaders.MovieFavouriteCheck;
import com.example.annadroid.moviesapp.loaders.MovieReviewsLoader;
import com.example.annadroid.moviesapp.loaders.MovieTrailersLoader;
import com.example.annadroid.moviesapp.models.MovieInfo;
import com.example.annadroid.moviesapp.models.Trailer;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<MovieInfo> {
    public static final int REVIEW_LOADER_ID = 0;
    public static final int TRAILER_LOADER_ID = 1;
    public static final int CHECK_FAVOURITE_LOADER_ID = 2;
    private MovieInfo movie;
    private ReviewAdapter mReviewsAdapter;
    private TrailerAdapter mTrailersAdapter;
    public static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #MoviesApp";
    public static final String MOVIE_INFO = "movie";
    private Toast toast;
    private MovieDeletion movieDeletion;
    private MovieInsertion movieInsertion;
    private View favouriteView;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (movie == null)
            return;
        inflater.inflate(R.menu.menu_details, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        } else {
            Log.w(LOG_TAG, "Share Action Provider is null?");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        restartLoaders();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(MovieDetailsFragment.MOVIE_INFO);
        }


        View rootView = inflater.inflate(R.layout.movie_details_fragment, container, false);
        favouriteView = rootView.findViewById(R.id.favorite_icon);
        View descriptionCard = rootView.findViewById(R.id.description_card);
        View trailersCard = rootView.findViewById(R.id.trailers_card);
        View reviewsCard = rootView.findViewById(R.id.reviews_card);
        if (movie != null) {

            descriptionCard.setVisibility(View.VISIBLE);
            trailersCard.setVisibility(View.VISIBLE);
            reviewsCard.setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.movie_name))
                    .setText(movie.getOriginalTitle());
            ((TextView) rootView.findViewById(R.id.movie_overview))
                    .setText(movie.getOverview());
            ((TextView) rootView.findViewById(R.id.movie_rating))
                    .setText(String.format("%.1f/10", movie.getVoteAverage()));
            ((TextView) rootView.findViewById(R.id.release_date))
                    .setText(movie.getReleaseDate());
            ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster_details);
            Picasso.with(getActivity()).load(movie.getPosterPath())
                    .placeholder(R.drawable.waiting).error(R.drawable.no_poster).into(imageView);

            mReviewsAdapter =
                    new ReviewAdapter(getActivity(), R.layout.movie_review, movie.getReviews());
            mTrailersAdapter =
                    new TrailerAdapter(getActivity(), R.layout.movie_trailer, movie.getTrailerLinks());

            final ListView reviewsView = (ListView) rootView.findViewById(R.id.listview_reviews);
            reviewsView.setAdapter(mReviewsAdapter);

            final ListView trailersView = (ListView) rootView.findViewById(R.id.listview_trailers);
            trailersView.setAdapter(mTrailersAdapter);

            trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trailer trailer = movie.getTrailerLinks().get(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(trailer.getLink()));
                    startActivity(intent);
                }
            });


            movieDeletion = new MovieDeletion(favouriteView, getActivity());
            movieInsertion = new MovieInsertion(favouriteView, getActivity());
            favouriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movie.isFavourited()) {
                        movieDeletion.execute(movie.getId());
                    } else {
                        movieInsertion.execute(movie);
                    }
                }
            });
        } else {
            Log.d(LOG_TAG, "No movie found");
            reviewsCard.setVisibility(View.INVISIBLE);
            trailersCard.setVisibility(View.INVISIBLE);
            descriptionCard.setVisibility(View.INVISIBLE);
            ((TextView) rootView.findViewById(R.id.movie_name))
                    .setText("Choose movie");
        }


        return rootView;
    }


    @Override
    public Loader<MovieInfo> onCreateLoader(int id, Bundle args) {
        if (movie == null) {
            return null;
        }

        if (id == REVIEW_LOADER_ID) {
            return new MovieReviewsLoader(getActivity(), movie);
        } else if (id == TRAILER_LOADER_ID) {
            return new MovieTrailersLoader(getActivity(), movie);
        } else if (id == CHECK_FAVOURITE_LOADER_ID) {
            return new MovieFavouriteCheck(getActivity(), movie);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<MovieInfo> loader, MovieInfo data) {
        if (data == null) {
            return;
        }

        if (loader.getId() == REVIEW_LOADER_ID) {
            mReviewsAdapter.notifyDataSetChanged();
        } else if (loader.getId() == TRAILER_LOADER_ID) {
            mTrailersAdapter.notifyDataSetChanged();
        } else if (loader.getId() == CHECK_FAVOURITE_LOADER_ID) {
            movie.setFavourited(data.isFavourited());
            if (data.isFavourited())
                favouriteView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.yellow_star));
            else
                favouriteView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.empty_star));
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieInfo> loader) {
    }


    public void restartLoaders() {
        if (movie == null)
            return;
        getLoaderManager().restartLoader(TRAILER_LOADER_ID, null, this).forceLoad();
        getLoaderManager().restartLoader(REVIEW_LOADER_ID, null, this).forceLoad();
        if (movie != null && movie.isFavourited() == null) {
            getLoaderManager().restartLoader(CHECK_FAVOURITE_LOADER_ID, null, this).forceLoad();
        }
    }


    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        final String trailer = movie.getTrailerLinks().isEmpty() ? "" :
                movie.getTrailerLinks().get(0).getLink();
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                String.format("Have you seen it? %s: %s %s", movie.getOriginalTitle(), trailer,
                        FORECAST_SHARE_HASHTAG));
        return shareIntent;
    }


    public class MovieDeletion extends AsyncTask<String, Void, Integer> {
        private View star;
        private Context context;

        public MovieDeletion(View star, Context context) {
            this.star = star;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            return getActivity().getContentResolver().delete(
                    MovieContract.FavouriteMovieEntry.CONTENT_URI,
                    MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{params[0]}
            );
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                if (toast != null) {
                    toast.cancel();
                }
                movie.setFavourited(false);
                star.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                toast = Toast.makeText(getActivity(), getString(R.string.onRemoveFavourite), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    public class MovieInsertion extends AsyncTask<MovieInfo, Void, Uri> {
        private View star;
        private Context context;

        public MovieInsertion(View star, Context context) {
            this.star = star;
            this.context = context;
        }

        @Override
        protected Uri doInBackground(MovieInfo... params) {
            if (params.length == 0) {
                return null;
            }

            ContentValues values = new ContentValues();

            values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
            values.put(MovieContract.FavouriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            values.put(MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieContract.FavouriteMovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
            values.put(MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

            return getActivity().getContentResolver().insert(MovieContract.FavouriteMovieEntry.CONTENT_URI,
                    values);
        }

        @Override
        protected void onPostExecute(Uri result) {
            if (result != null) {
                if (toast != null) {
                    toast.cancel();
                }
                movie.setFavourited(true);
                star.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.yellow_star));
                toast = Toast.makeText(getActivity(), getString(R.string.onAddFavourite), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}