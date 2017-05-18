package com.example.mtecgwa_jr.movieapp.ViewPagerFragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mtecgwa_jr.movieapp.Adapter.ReviewAdapter;
import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.Data.Reviews;
import com.example.mtecgwa_jr.movieapp.MovieActivity;
import com.example.mtecgwa_jr.movieapp.NetworkUtility.NetworkRequest;
import com.example.mtecgwa_jr.movieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {


    public ReviewFragment() {
        // Required empty public constructor
    }
    private RecyclerView reviewRecylerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Reviews> reviewList = new ArrayList<>();

    private static final String QUERY_REVIEWS_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        Movie movie = MovieActivity.getMovie();
        String movieID = Long.toString(movie.getMovieId());
        String reviewsUrl = QUERY_REVIEWS_BASE_URL+movieID+"/reviews";

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_review);

        reviewRecylerView = (RecyclerView) view.findViewById(R.id.review_list);
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecylerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        reviewRecylerView.setLayoutManager(layoutManager);
        reviewRecylerView.setAdapter(reviewAdapter);

        MovieReviewTask movieReviewTask = new MovieReviewTask();
        movieReviewTask.execute(reviewsUrl);

        return view;

    }

    private class MovieReviewTask extends AsyncTask<String , Reviews , Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            String reviewsUrl = params[0];
            URL url = NetworkRequest.buildUrl(reviewsUrl);
            String reviewerName , review;
            String jsonResult = "";
            try {
                jsonResult = NetworkRequest.getJson(url);
                JSONObject reviewsObject = new JSONObject(jsonResult);
                JSONArray reviewsArray = reviewsObject.getJSONArray("results");
                for(int i = 0 ; i <= reviewsArray.length() ; i++)
                {
                    JSONObject singleReview = reviewsArray.getJSONObject(i);
                    reviewerName = singleReview.getString("author");
                    review = singleReview.getString("content");

                    Reviews reviews = new Reviews(reviewerName , review);

                    publishProgress(reviews);
                }
            }
            catch(IOException e)
            {

            }
            catch(JSONException jse)
            {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Reviews... values) {
            super.onProgressUpdate(values);

            reviewList.add(values[0]);
            reviewAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
        }
    }

}
