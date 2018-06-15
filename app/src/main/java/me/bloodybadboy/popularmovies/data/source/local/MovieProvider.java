package me.bloodybadboy.popularmovies.data.source.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import timber.log.Timber;

public class MovieProvider extends ContentProvider {
  private static final int FAVOURITE_MOVIES = 100;
  private static final int FAVOURITE_MOVIE_ID = 200;
  private static final UriMatcher sUriMatcher;
  private MovieDB mMovieDB;

  static {
    sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, FavouriteMovieStore.PATH_FAVOURITE_MOVIE,
        FAVOURITE_MOVIES);
    sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
        FavouriteMovieStore.PATH_FAVOURITE_MOVIE + "/#",
        FAVOURITE_MOVIE_ID);
  }

  @Override public boolean onCreate() {
    mMovieDB = MovieDB.getInstance(getContext());
    return false;
  }

  @Nullable @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    Cursor cursor;
    SQLiteDatabase readableDatabase = mMovieDB.getReadableDatabase();

    switch (sUriMatcher.match(uri)) {
      case FAVOURITE_MOVIES:
        cursor = readableDatabase.query(
            FavouriteMovieStore.Columns.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      case FAVOURITE_MOVIE_ID:
        cursor = readableDatabase.query(
            FavouriteMovieStore.Columns.TABLE_NAME,
            projection,
            FavouriteMovieStore.Columns.MOVIE_ID + " = ?",
            new String[] { uri.getLastPathSegment() },
            null,
            null,
            sortOrder
        );
        break;
      default:
        throw new UnsupportedOperationException("Unknown Uri " + uri);
    }
    return cursor;
  }

  @Nullable @Override public String getType(@NonNull Uri uri) {
    switch (sUriMatcher.match(uri)) {
      case FAVOURITE_MOVIES:
        return FavouriteMovieStore.CONTENT_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Nullable @Override public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    SQLiteDatabase db = mMovieDB.getWritableDatabase();
    Uri returnUri;

    switch (sUriMatcher.match(uri)) {
      case FAVOURITE_MOVIES:
        Timber.d(values.toString());
        long insert =
            db.insert(FavouriteMovieStore.Columns.TABLE_NAME, null, values);
        if (insert > 0) {
          returnUri = ContentUris.withAppendedId(FavouriteMovieStore.CONTENT_URI, insert);
        } else {
          throw new SQLException("Failed to insert row into " + uri);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unknown Uri " + uri);
    }

    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return returnUri;
  }

  @Override public int delete(@NonNull Uri uri, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    SQLiteDatabase db = mMovieDB.getWritableDatabase();
    int rowsDeleted;
    switch (sUriMatcher.match(uri)) {
      case FAVOURITE_MOVIE_ID:
        String id = uri.getPathSegments().get(1);
        rowsDeleted = db.delete(FavouriteMovieStore.Columns.TABLE_NAME,
            FavouriteMovieStore.Columns.MOVIE_ID + " = ?",
            new String[] { id });
        break;
      default:
        throw new UnsupportedOperationException("Unknown Uri " + uri);
    }

    if (rowsDeleted != 0 && getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    SQLiteDatabase db = mMovieDB.getWritableDatabase();
    int rowsUpdated;
    switch (sUriMatcher.match(uri)) {
      case FAVOURITE_MOVIE_ID:
        String id = uri.getPathSegments().get(1);
        rowsUpdated = db.update(FavouriteMovieStore.Columns.TABLE_NAME,
            values, FavouriteMovieStore.Columns.MOVIE_ID + " = ?",
            new String[] { id });
        break;
      default:
        throw new UnsupportedOperationException("Unknown Uri " + uri);
    }

    if (rowsUpdated != 0 && getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;
  }
}
