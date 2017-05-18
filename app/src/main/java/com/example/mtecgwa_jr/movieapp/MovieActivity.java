package com.example.mtecgwa_jr.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtecgwa_jr.movieapp.Adapter.FragmentAdapter;
import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.Database.MovieContract;
import com.example.mtecgwa_jr.movieapp.Database.MovieDBHelper;
import com.example.mtecgwa_jr.movieapp.ViewPagerFragments.InfoFragment;
import com.example.mtecgwa_jr.movieapp.ViewPagerFragments.ReviewFragment;
import com.example.mtecgwa_jr.movieapp.ViewPagerFragments.TrailerFragment;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager ;
    private FragmentAdapter fragmentAdapter ;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView backdrop , cover , favorite;
    private TextView movieName , originalLanguage , releaseDate , rate ;

    private static final String MOVIE_COVER_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static final String MOVIE_BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w780";

    private static Movie movie;

    private int isFavorite = 0;

    private SQLiteDatabase mdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent movieIntent = getIntent();
        movie = movieIntent.getParcelableExtra("movie");

        initialiseCollapsingHeaderContent();

        setupTabLayoutWithFragment();
        setuptCollapsingHeadContent(movie);


    }

    public static Movie getMovie()
    {
        return movie;
    }

    public void initialiseCollapsingHeaderContent()
    {
        backdrop = (ImageView) findViewById(R.id.backdrop);
        cover = (ImageView) findViewById(R.id.cover);
        movieName = (TextView) findViewById(R.id.movie_name);
        originalLanguage = (TextView) findViewById(R.id.original_language);
        releaseDate = (TextView) findViewById(R.id.release_date);
        rate = (TextView) findViewById(R.id.rate);
        favorite = (ImageView) findViewById(R.id.favourate);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);
        collapsingToolbarLayout.setTitleEnabled(false);

        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cursor movieReturned = selectAllFavorites();
        isFavorite = isMovieFavorite(movieReturned);

        switch (isFavorite)
        {
            case 0:
                favorite.setImageResource(R.drawable.star);
                break;
            case 1:
                favorite.setImageResource(R.drawable.starpink);
                break;
            default :
        }


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (isFavorite)
                {
                    case 0:
                        isFavorite = 1;
                        favorite.setImageResource(R.drawable.starpink);
                        //Toast.makeText(getApplicationContext() , "You have added this movie to your favorites" , Toast.LENGTH_SHORT).show();
                        //now the movie is made favourate by the user....
                        Uri returnedUri = insertFavorite(movie);
                        if(returnedUri != null)
                        {
                            Toast.makeText(getApplicationContext() , "the value returned is "+returnedUri.toString() , Toast.LENGTH_SHORT).show();

                        }

                        break;
                    case 1:
                        isFavorite = 0;
                        favorite.setImageResource(R.drawable.star);
                        boolean isRemoved = removeFavorite(movie);
                        if(isRemoved = true)
                        {
                            Toast.makeText(getApplicationContext() , "the delete was succesfull and returned "+isRemoved , Toast.LENGTH_LONG).show();

                        }
                        else {
                            Log.v(MovieActivity.class.getName() , "Delete operation was unsuccessful");
                        }


                        //now the movie is no longer the favourate ........

                        break;

                    default :

                        break;
                }
            }
        });
    }

    public void movieTitle()
    {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    public void setupTabLayoutWithFragment()
    {


        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragments(new InfoFragment() , "INFO");
        fragmentAdapter.addFragments(new TrailerFragment() , "TRAILERS");
        fragmentAdapter.addFragments(new ReviewFragment() , "REVIEWS");

        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void setuptCollapsingHeadContent(Movie movie)
    {
        Picasso.with(this)
                .load(MOVIE_BACKDROP_BASE_URL+movie.getCover_url())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(backdrop);

        Picasso.with(this)
                .load(MOVIE_COVER_BASE_URL+movie.getCover_url())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(cover);


        movieName.setText(movie.getMovieName());
        originalLanguage.append(movie.getOriginaLanguage());
        releaseDate.append(movie.getReleaseDate());
        rate.setText(Double.toString(movie.getVoteRatings()));
    }

    public Uri insertFavorite(Movie movie)
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.MOVIE_ID_COLUMN , Long.toString(movie.getMovieId()));
        cv.put(MovieContract.MovieEntry.MOVIE_NAME_COLUMN , movie.getMovieName());
        cv.put(MovieContract.MovieEntry.COVER_IMAGE_COLUMN , movie.getCover_url());
        cv.put(MovieContract.MovieEntry.BACKDROP_COLUMN , movie.getBackdropUrl());
        cv.put(MovieContract.MovieEntry.RELEASE_DATE_COLUMN , movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.RATINGS_COLUMN , Double.toString(movie.getVoteRatings()));
        cv.put(MovieContract.MovieEntry.LANGUAGE_COLUMN , movie.getOriginaLanguage());
        cv.put(MovieContract.MovieEntry.OVERVIEW_COLUMN , movie.getOverview());


        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI , cv);

        return uri;
    }

    public boolean removeFavorite(Movie movie)
    {

        String[] args = new String[] {Long.toString(movie.getMovieId())};
        return getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI , MovieContract.MovieEntry.MOVIE_ID_COLUMN+"=?" ,args) > 0;

    }

    public Cursor selectAllFavorites()
    {
        Cursor selectAllCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI ,
                null ,
                null ,
                null ,
                null);

        return selectAllCursor;
    }

    public int isMovieFavorite(Cursor cursor)
    {
        int movieIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID_COLUMN);

        String movieId;
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                movieId = cursor.getString(movieIdIndex);
                if(movieId.equals(Long.toString(movie.getMovieId())))
                {
                    return 1;
                }

                Log.v(MovieActivity.class.getName() , "movie id retrieved is "+movieId+" and this movie movie Id is "+Long.toString(movie.getMovieId()));

            }
        }
        else
        {
            Toast.makeText(getApplicationContext() , "No data was retrieved " , Toast.LENGTH_SHORT).show();
        }


        return 0;
    }

}
