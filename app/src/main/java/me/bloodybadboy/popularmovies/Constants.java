package me.bloodybadboy.popularmovies;

@SuppressWarnings("WeakerAccess") public final class Constants {
  public static final String PACKAGE = "me.bloodybadboy.popularmovies";

  public static final int NO_POSITION = -1;

  public static final String API_SERVICE_END_POINT = "https://api.themoviedb.org/3/";
  public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
  public static final String YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";

  public static final String REQUEST_QUERY_PAGE = "page";
  public static final String POSTER_IMAGE_SIZE = "w185";
  public static final String BACKDROP_IMAGE_SIZE = "w780";
  public static final String PROFILE_IMAGE_SIZE = "w45";
  public static final String YOUTUBE_THUMBNAIL_IMAGE_SIZE = "mqdefault.jpg";

  public static final float POSTER_IMAGE_ASPECT_RATIO = 1.4f;

  public static final String MOVIE_DATA_EXTRA = PACKAGE + ".MOVIE_DATA_EXTRA";
  public static final String MOVIE_LIST_ITEM_POSITION_EXTRA =
      PACKAGE + ".MOVIE_LIST_ITEM_POSITION_EXTRA";
  public static final String REVIEW_LIST_EXTRA = PACKAGE + ".REVIEW_LIST_EXTRA";
  public static final String VIDEO_LIST_EXTRA = PACKAGE + ".VIDEO_LIST_EXTRA";
  public static final String CAST_LIST_EXTRA = PACKAGE + ".CAST_LIST_EXTRA";
  //  public static final String CREW_LIST_EXTRA = PACKAGE + ".CREW_LIST_EXTRA";
  public static final String MOVIE_INFO_EXTRA = PACKAGE + ".MOVIE_INFO_EXTRA";

  public static final String ACTION_FAVOURITE_ITEM_REMOVE =
      PACKAGE + ".ACTION_FAVOURITE_ITEM_REMOVE";

  public static final MoviesFilterType DEFAULT_SORT_BY_ORDER = MoviesFilterType.POPULARITY;

  public enum MoviesFilterType {
    POPULARITY("popular"),
    TOP_RATED("top_rated"),
    FAVOURITES("favourites");

    String mFilter;

    MoviesFilterType(String s) {
      mFilter = s;
    }

    @Override public String toString() {
      return mFilter;
    }
  }
}