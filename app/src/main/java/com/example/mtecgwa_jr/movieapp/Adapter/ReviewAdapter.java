package com.example.mtecgwa_jr.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mtecgwa_jr.movieapp.Data.Reviews;

import com.example.mtecgwa_jr.movieapp.R;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 5/12/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    private ArrayList<Reviews> reviewList = new ArrayList<>();


    public ReviewAdapter(ArrayList<Reviews> reviewList)
    {

        this.reviewList = reviewList;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerName , review;



        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
            review = (TextView) itemView.findViewById(R.id.review);

        }
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_review,
                viewGroup, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        Reviews review = reviewList.get(position);


        holder.reviewerName.setText(review.getReviewerName());
        holder.review.setText(review.getReview());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


}
