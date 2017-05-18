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
import com.example.mtecgwa_jr.movieapp.Adapter.TrailersAdapter;
import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.Data.Reviews;
import com.example.mtecgwa_jr.movieapp.Data.Trailer;
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
public class TrailerFragment extends Fragment {


    public TrailerFragment() {
        // Required empty public constructor
    }
    private ProgressBar progressBar;

    private RecyclerView trailerRecylerView;
    private TrailersAdapter trailersAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<>();

    private static final String QUERY_REVIEWS_BASE_URL = "https://api.themoviedb.org/3/movie/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trailer, container, false);



        Movie movie = MovieActivity.getMovie();
        String movieID = Long.toString(movie.getMovieId());
        String trailersUrl = QUERY_REVIEWS_BASE_URL+movieID+"/videos";

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_trailer);

        trailerRecylerView = (RecyclerView) view.findViewById(R.id.trailer_list);
        trailersAdapter = new TrailersAdapter(getActivity() , trailerList);
        trailerRecylerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        trailerRecylerView.setLayoutManager(layoutManager);
        trailerRecylerView.setAdapter(trailersAdapter);


        TrailerTask trailerTask = new TrailerTask();
        trailerTask.execute(trailersUrl);

        return view;
    }

    private class TrailerTask extends AsyncTask<String , Trailer , Void>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            String trailerQueryUrl = params[0];
            URL url = NetworkRequest.buildUrl(trailerQueryUrl);
            String trailerName , trailerType , trailerKey;
            String jsonResult = "";
            try {
                jsonResult = NetworkRequest.getJson(url);
                JSONObject trailersObject = new JSONObject(jsonResult);
                JSONArray trailersArray = trailersObject.getJSONArray("results");
                for(int i = 0 ; i <= trailersArray.length() ; i++)
                {
                    JSONObject singleTrailer = trailersArray.getJSONObject(i);
                    trailerName = singleTrailer.getString("name");
                    trailerKey = singleTrailer.getString("key");
                    trailerType = singleTrailer.getString("type");

                    Trailer trailer = new Trailer(trailerName , trailerType ,trailerKey);

                    publishProgress(trailer);
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
        protected void onProgressUpdate(Trailer... values) {
            super.onProgressUpdate(values);

            trailerList.add(values[0]);
            trailersAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
        }
    }
}
