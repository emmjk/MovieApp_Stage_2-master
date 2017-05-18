package com.example.mtecgwa_jr.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.MovieActivity;
import com.example.mtecgwa_jr.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 5/8/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.AdapterViewHolder> {


    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    private Context context;


    private static final String COVER_BASE_URL = "http://image.tmdb.org/t/p/w780";

    public MovieAdapter(ArrayList<Movie> movieList , Context context )
    {
        this.movieList = movieList;
        this.context = context;

    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder
    {
        ImageView cover_image;

        TextView movieName;

        TextView releasedDate;

        CardView movieCard;


        public AdapterViewHolder(View view)
        {
            super(view);

            cover_image = (ImageView) view.findViewById(R.id.movie_cover);
            movieName = (TextView) view.findViewById(R.id.movie_name);
            releasedDate = (TextView) view.findViewById(R.id.release_date);
            movieCard = (CardView) view.findViewById(R.id.movie_card);
            context = view.getContext();
        }


    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent ,false );
        AdapterViewHolder viewHolder = new AdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.AdapterViewHolder holder, int position) {
        final Movie movie = movieList.get(position);

        holder.movieName.setText(movie.getMovieName());
        holder.releasedDate.setText("Released : "+movie.getReleaseDate());

        String className = this.getClass().getName();

        Picasso.with(context)
                .load(COVER_BASE_URL+movie.getCover_url())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(holder.cover_image);

        Log.v(className, "cover url is " + movie.getCover_url());

        holder.movieCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context , "You clicked movie : "+movie.getMovieName() , Toast.LENGTH_SHORT).show();

                String movieId = String.valueOf(movie.getMovieId());

                Intent intent = new Intent(context , MovieActivity.class);
                intent.putExtra("movie" , movie);

                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}


