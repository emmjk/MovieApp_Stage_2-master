package com.example.mtecgwa_jr.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mtecgwa_jr.movieapp.Data.Trailer;
import com.example.mtecgwa_jr.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtecgwa-jr on 5/12/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private Context context;
    private ArrayList<Trailer> trailerList = new ArrayList<>();
    private static final String YOUTUBE_VIDEO_COVER_BASE_URL = "https://img.youtube.com/vi/";
    private String finalUrl = null;

    public TrailersAdapter(Context context , ArrayList<Trailer> trailerList)
    {
        this.context = context;
        this.trailerList = trailerList;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView trailerTitle , trailerCaption;
        ImageView trailerCover;
        RelativeLayout relativeLayout;


        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerTitle = (TextView) itemView.findViewById(R.id.trailer_name);
            trailerCaption = (TextView) itemView.findViewById(R.id.trailer_caption);
            trailerCover = (ImageView) itemView.findViewById(R.id.youtube_video_cover);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.trailer_container);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_trailer ,
                viewGroup, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder myViewHolder, int i) {
        final Trailer trailer = trailerList.get(i);
        finalUrl = YOUTUBE_VIDEO_COVER_BASE_URL+trailer.getVidKey()+"/0.jpg";

        myViewHolder.trailerTitle.setText(trailer.getTrailerName());
        myViewHolder.trailerCaption.setText(trailer.getType());

        Picasso.with(context)
                .load(finalUrl)
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie)
                .into(myViewHolder.trailerCover);

        myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoKey = trailer.getVidKey();
                String videoUrl = "https://www.youtube.com/watch?v="+videoKey;
                Intent viewTrailer = new Intent(Intent.ACTION_VIEW , Uri.parse(videoUrl));

                context.startActivity(viewTrailer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


}
