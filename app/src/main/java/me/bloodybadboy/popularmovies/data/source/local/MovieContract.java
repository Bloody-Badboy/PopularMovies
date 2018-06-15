package me.bloodybadboy.popularmovies.data.source.local;

import android.net.Uri;
import me.bloodybadboy.popularmovies.Constants;

public final class MovieContract {

  public MovieContract() {
    throw new AssertionError();
  }

  public static final String CONTENT_AUTHORITY = Constants.PACKAGE;

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
