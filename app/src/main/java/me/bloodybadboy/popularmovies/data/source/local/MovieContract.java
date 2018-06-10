package me.bloodybadboy.popularmovies.data.source.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import me.bloodybadboy.popularmovies.Constants;

public final class MovieContract {

  public MovieContract() {
    throw new AssertionError();
  }

  public static final String CONTENT_AUTHORITY = Constants.PACKAGE;

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_MOVIE = "movie";

  public static final class MovieEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
  }
}
