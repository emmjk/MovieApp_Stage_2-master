package com.example.mtecgwa_jr.movieapp.Data;

/**
 * Created by mtecgwa-jr on 5/12/17.
 */

public class Reviews {

    String reviewerName , review ;

    public Reviews(String reviewerName , String review)
    {
        this.reviewerName = reviewerName;
        this.review = review;
    }

    public String getReviewerName() { return reviewerName; }

    public String getReview() { return review; }

}
