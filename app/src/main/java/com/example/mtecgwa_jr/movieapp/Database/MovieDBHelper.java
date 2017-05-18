package com.example.mtecgwa_jr.movieapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mtecgwa-jr on 5/13/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DB_VERSION = 1;

    public MovieDBHelper(Context context)
    {
        super(context , DATABASE_NAME , null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+
                "("+ MovieContract.MovieEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                MovieContract.MovieEntry.MOVIE_ID_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.MOVIE_NAME_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.COVER_IMAGE_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.BACKDROP_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.RELEASE_DATE_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.RATINGS_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.LANGUAGE_COLUMN+" TEXT NOT NULL ,"+
                MovieContract.MovieEntry.OVERVIEW_COLUMN+" TEXT NOT NULL "+ ");";

                db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
