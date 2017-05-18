package com.example.mtecgwa_jr.movieapp.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mtecgwa-jr on 5/13/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.mtecgwa_jr.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String MOVIE_PATH = "movies";

    private MovieContract() {}

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();


        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID_COLUMN = "movieId";
        public static final String MOVIE_NAME_COLUMN = "name";
        public static final String COVER_IMAGE_COLUMN = "cover";
        public static final String BACKDROP_COLUMN = "backdrop";
        public static final String RELEASE_DATE_COLUMN = "date";
        public static final String OVERVIEW_COLUMN = "overview";
        public static final String RATINGS_COLUMN = "ratings";
        public static final String LANGUAGE_COLUMN = "language";


    }

}
