package com.example.mtecgwa_jr.movieapp.Data;

/**
 * Created by mtecgwa-jr on 5/12/17.
 */

public class Trailer {

    String trailerName , type , vidKey;

    public Trailer(String trailerName , String type , String vidKey)
    {
        this.trailerName = trailerName;
        this.type = type;
        this.vidKey = vidKey;
    }

    public String getTrailerName() { return trailerName; }

    public String getType() { return type; }

    public String getVidKey() { return vidKey; }
}
