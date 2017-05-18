package com.example.mtecgwa_jr.movieapp.ViewPagerFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mtecgwa_jr.movieapp.Adapter.OverviewAdapter;
import com.example.mtecgwa_jr.movieapp.Data.Movie;
import com.example.mtecgwa_jr.movieapp.MainActivity;
import com.example.mtecgwa_jr.movieapp.MovieActivity;
import com.example.mtecgwa_jr.movieapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }

    private static Movie movie;
    private RecyclerView recyclerView;
    private OverviewAdapter overviewAdapter;

    ArrayList<String> overviews = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info, container, false);

        movie = MovieActivity.getMovie();
        initialiseViews(movie , view);

        return view;
    }

    public void initialiseViews(Movie movie , View view)
    {

        recyclerView = (RecyclerView) view.findViewById(R.id.overview);

        overviews.add(movie.getOverview());

        overviewAdapter = new OverviewAdapter(overviews);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(overviewAdapter);
    }

}
