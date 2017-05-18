package com.example.mtecgwa_jr.movieapp.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by mtecgwa-jr on 5/13/17.
 */

public class MovieContentProvider extends ContentProvider {

    private MovieDBHelper movieDBHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 124;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY , MovieContract.MOVIE_PATH , MOVIES);
        //uriMatcher.addURI(MovieContract.AUTHORITY , MovieContract.MOVIE_PATH+"/#" , MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();

        movieDBHelper = new MovieDBHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match)
        {
            case MOVIES:
                returnCursor = db.query(MovieContract.MovieEntry.TABLE_NAME ,
                        projection ,
                        selection ,
                        selectionArgs ,
                        null ,
                        null ,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Failed to query data from "+uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver() , uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri resultUri = null;
        switch (match)
        {
            case MOVIES:

                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME , null , values);

                if(id > 0)
                {
                    resultUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI , id);
                }
                else
                {
                    throw new SQLException("Failed to insert data into "+uri);
                }

                break;

            default:
                new UnsupportedOperationException();

        }

        getContext().getContentResolver().notifyChange(uri , null);

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);

        int deletedRows = 0;
        switch(matcher)
        {
            case MOVIES:
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME , selection , selectionArgs);
                break;
            default :
                throw new UnsupportedOperationException();
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
