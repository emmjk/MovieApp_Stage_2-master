package com.example.mtecgwa_jr.movieapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mtecgwa-jr on 5/8/17.
 */

public class Movie implements Parcelable{

    private String cover_url , backdropUrl , movieName , releaseDate ,  overview , originaLanguage;
    private long movieId;
    private double voteRatings;

    public Movie()
    {
        super();
    }

    public Movie(Parcel parcel)
    {
        this.cover_url = parcel.readString();
        this.backdropUrl = parcel.readString();
        this.movieName = parcel.readString();
        this.releaseDate = parcel.readString();
        this.overview = parcel.readString();
        this.originaLanguage = parcel.readString();
        this.movieId = parcel.readLong();
        this.voteRatings = parcel.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.cover_url);
        parcel.writeString(this.backdropUrl);
        parcel.writeString(this.movieName);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.overview);
        parcel.writeString(this.originaLanguage);
        parcel.writeLong(this.movieId);
        parcel.writeDouble(this.voteRatings);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setCover_url(String cover_url)
    {
        this.cover_url = cover_url;
    }

    public void setBackdropUrl(String backdropUrl)
    {
        this.backdropUrl = backdropUrl;
    }

    public void setMovieName(String movieName)
    {
        this.movieName = movieName;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }
    public void setOriginaLanguage(String originaLanguage)
    {
        this.originaLanguage = originaLanguage;
    }
    public void setMovieId(long movieId)
    {
        this.movieId = movieId;
    }
    public void setVoteRatings(double voteRatings)
    {
        this.voteRatings = voteRatings;
    }
    public String getCover_url()
    {
        return cover_url;
    }

    public String getMovieName()
    {
        return movieName;
    }

    public String getBackdropUrl() { return backdropUrl; }

    public String getOriginaLanguage() { return originaLanguage; }

    public double getVoteRatings() { return voteRatings; }

    public String getReleaseDate() {return releaseDate; }

    public String getOverview() { return overview; }



    public long getMovieId()
    {
        return movieId;
    }



}
