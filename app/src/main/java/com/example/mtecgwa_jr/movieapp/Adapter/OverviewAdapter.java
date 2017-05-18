package com.example.mtecgwa_jr.movieapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mtecgwa_jr.movieapp.Data.Reviews;
import com.example.mtecgwa_jr.movieapp.R;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 5/13/17.
 */

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {


private ArrayList<String> overview = new ArrayList<>();


public OverviewAdapter(ArrayList<String> overview)
        {

        this.overview = overview;
        }

class OverviewViewHolder extends RecyclerView.ViewHolder {

    TextView overview;



    public OverviewViewHolder(View itemView) {
        super(itemView);

        overview = (TextView) itemView.findViewById(R.id.overview);

    }
}

    @Override
    public OverviewAdapter.OverviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_overview,
                viewGroup, false);
        return new OverviewAdapter.OverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OverviewAdapter.OverviewViewHolder holder, int position) {

        String sypnosis = overview.get(position);
        holder.overview.setText(sypnosis);

    }

    @Override
    public int getItemCount() {
        return 1;
    }


}

