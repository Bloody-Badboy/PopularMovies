package me.bloodybadboy.popularmovies.data.source.local;

import android.net.Uri;
import me.bloodybadboy.popularmovies.Constants;

@SuppressWarnings("WeakerAccess") public final class MovieContract {

  public static final String CONTENT_AUTHORITY = Constants.PACKAGE;
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public MovieContract() {
    throw new AssertionError();
  }
}
