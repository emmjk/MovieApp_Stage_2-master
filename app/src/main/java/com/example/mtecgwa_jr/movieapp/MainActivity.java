package com.example.mtecgwa_jr.movieapp;


import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mtecgwa_jr.movieapp.Adapter.MovieAdapter;
import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.Database.MovieContract;
import com.example.mtecgwa_jr.movieapp.NetworkUtility.NetworkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String QUERY_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String QUERY_TOP_RATED_URL = " https://api.themoviedb.org/3/movie/top_rated";

    private String queryTypeCheck = "popular";
    private static final String QUERY_CHECKER = "check_query";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            queryTypeCheck = savedInstanceState.getString(QUERY_CHECKER);
            MovieQueryTask movieAsync = new MovieQueryTask();
            switch (queryTypeCheck)
            {

                case "popular":
                    movieAsync.execute(QUERY_POPULAR_URL);
                    queryTypeCheck = "popular";
                    setTitle();
                    break;
                case "top_rated":
                    movieAsync.execute(QUERY_TOP_RATED_URL);
                    queryTypeCheck = "top_rated";
                    setTitle();
                    break;
                case "favorite":
                    MovieFromDbTask movieFromDbTask = new MovieFromDbTask();
                    movieFromDbTask.execute();
                    setTitle();
                    break;
                default:
                    movieAsync.execute(QUERY_POPULAR_URL);
                    queryTypeCheck = "popular";
                    setTitle();
                    break;
            }

        }
        else
        {
            MovieQueryTask movieAsync = new MovieQueryTask();
            movieAsync.execute(QUERY_POPULAR_URL);
            queryTypeCheck = "popular";
            setTitle();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(QUERY_CHECKER , queryTypeCheck);

    }

    public class MovieQueryTask extends AsyncTask<String , Movie , Void>
    {
        private RecyclerView recyclerView;
        private MovieAdapter movieAdapter;
        private ArrayList<Movie> movieList = new ArrayList<>();
        private ProgressBar progressBar;
        private Resources resources = getResources();
        private int numberOfColumn;


        public void showProgressBar()
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        public void hidePrigressBar()
        {
            progressBar.setVisibility(View.INVISIBLE);
        }


        @Override
        protected void onPreExecute() {

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            movieAdapter = new MovieAdapter(movieList , MainActivity.this);
            recyclerView.setHasFixedSize(true);
            numberOfColumn = resources.getInteger(R.integer.number_of_column);

            Log.v(MainActivity.class.getName() , "number of column should be "+numberOfColumn);


            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this , numberOfColumn);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(movieAdapter);

            progressBar = (ProgressBar) findViewById(R.id.progress_bar);

            showProgressBar();


        }

        @Override
        protected Void doInBackground(String... params) {

            String queryUrl = params[0];
            URL url = NetworkRequest.buildUrl(queryUrl);
            String coverImage , movieName , backdropUrl , releaseDate , overview , lang;
            String jsonResult = "";
            long movieId;
            double voteRatings;
            try
            {
                jsonResult = NetworkRequest.getJson(url);
                JSONObject json = new JSONObject(jsonResult);
                JSONArray resultArray = json.getJSONArray("results");

                for(int i = 0 ; i< resultArray.length() ; i++)
                {
                    JSONObject movie = resultArray.getJSONObject(i);
                    coverImage = movie.getString("poster_path");
                    movieName = movie.getString("original_title");
                    backdropUrl = movie.getString("backdrop_path");
                    releaseDate = movie.getString("release_date");
                    overview = movie.getString("overview");
                    lang = movie.getString("original_language");
                    voteRatings = movie.getDouble("vote_average");
                    movieId = movie.getLong("id");

                    Movie movieQueried = new Movie();

                    movieQueried.setCover_url(coverImage);
                    movieQueried.setBackdropUrl(backdropUrl);
                    movieQueried.setMovieName(movieName);
                    movieQueried.setReleaseDate(releaseDate);
                    movieQueried.setOverview(overview);
                    movieQueried.setOriginaLanguage(lang);
                    movieQueried.setVoteRatings(voteRatings);
                    movieQueried.setMovieId(movieId);

                    publishProgress(movieQueried);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            movieList.add(values[0]);
            movieAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hidePrigressBar();
        }
    }

    public class MovieFromDbTask extends AsyncTask<Void , Movie , Void>
    {
        private RecyclerView recyclerView;
        private MovieAdapter movieAdapter;
        private ArrayList<Movie> movieList = new ArrayList<>();
        private ProgressBar progressBar;
        private int numberOfColumn;
        private Resources resources = getResources();

        public void showProgressBar()
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        public void hidePrigressBar()
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            recyclerView.setHasFixedSize(true);
            numberOfColumn = resources.getInteger(R.integer.number_of_column);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this , numberOfColumn);

            recyclerView.setLayoutManager(layoutManager);
            movieAdapter = new MovieAdapter(movieList , MainActivity.this);
            recyclerView.setAdapter(movieAdapter);


            showProgressBar();

        }

        @Override
        protected Void doInBackground(Void... params) {

            Cursor movieCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI ,
                    null ,
                    null ,
                    null ,
                    null);
            Log.v(MainActivity.class.getName() , "Cursor is called next statement will check for null in cursor");

            if(movieCursor != null)
            {
                Log.v(MainActivity.class.getName() , "Cursor is not null , next is iterating through cursor");

                while(movieCursor.moveToNext())
                {
                    Movie movieQueried = new Movie();

                    movieQueried.setMovieName(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_NAME_COLUMN)));
                    long movieId = Long.valueOf(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID_COLUMN))).longValue();
                    movieQueried.setMovieId(movieId);
                    movieQueried.setCover_url(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.COVER_IMAGE_COLUMN)));
                    movieQueried.setBackdropUrl(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.BACKDROP_COLUMN)));
                    movieQueried.setReleaseDate(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE_COLUMN)));
                    movieQueried.setOverview(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW_COLUMN)));
                    movieQueried.setOriginaLanguage(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.LANGUAGE_COLUMN)));
                    double movieRatings = Double.parseDouble(movieCursor.getString(
                            movieCursor.getColumnIndex(MovieContract.MovieEntry.RATINGS_COLUMN)));

                    movieQueried.setVoteRatings(movieRatings);

                    Log.v(MainActivity.class.getName() , "Cursor position is now at "+movieCursor.getPosition());

                    publishProgress(movieQueried);



                }
                Log.v(MainActivity.class.getName() , "Cursor has been retrieved the size of cursor is "+movieCursor.getCount());

            }
            else
            {
                Log.v(MainActivity.class.getName() , "Cursor was not received");
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            super.onProgressUpdate(values);
            movieList.add(values[0]);
            movieAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hidePrigressBar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedMenu = item.getItemId();
        switch(clickedMenu)
        {
            case R.id.query_popular:
                if(!queryTypeCheck.equals("popular"))
                {

                    queryTypeCheck = "popular";
                    MovieQueryTask asyncTask = new MovieQueryTask();
                    asyncTask.execute(QUERY_POPULAR_URL);
                    setTitle();
                }
                break;
            case R.id.query_top_rated:
                if(!queryTypeCheck.equals("top_rated"))
                {
                    queryTypeCheck = "top_rated";
                    MovieQueryTask asyncTask = new MovieQueryTask();
                    asyncTask.execute(QUERY_TOP_RATED_URL);
                    setTitle();

                }
                break;
            case R.id.query_favorites:
                if(!queryTypeCheck.equals("favorite"))
                {
                    queryTypeCheck = "favorite";

                    Log.v(MainActivity.class.getName() , "*************************query for favorite movies******************");
                    MovieFromDbTask movieFromDbTask = new MovieFromDbTask();
                    movieFromDbTask.execute();
                    setTitle();
                    //asyc task to query data from the database .....
                }

                break;
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitle()
    {
        switch(queryTypeCheck)
        {
            case "popular":
                getSupportActionBar().setTitle("Popular Movies");
                break;
            case "top_rated":
                getSupportActionBar().setTitle("Top Rated Movies");
                break;
            case "favorite":
                getSupportActionBar().setTitle("Favorite Movies");
                break;
            default :
                getSupportActionBar().setTitle("Movie App");
                break;
        }
    }
}
