package me.bloodybadboy.popularmovies;

@SuppressWarnings("WeakerAccess") public final class Constants {
  public static final String PACKAGE = "me.bloodybadboy.popularmovies";

  public static final String API_SERVICE_END_POINT = "http://api.themoviedb.org/3/";
  public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

  public static final String REQUEST_QUERY_PAGE = "page";
  public static final String POSTER_IMAGE_SIZE = "w185";
  public static final String BACKDROP_IMAGE_SIZE = "w780";

  public static final float POSTER_IMAGE_ASPECT_RATIO = 1.4f;

  public static final String MOVIE_DATA_EXTRA = PACKAGE + ".MOVIE_DATA_EXTRA";
  public static final String REVIEW_LIST_EXTRA = PACKAGE + ".REVIEW_LIST_EXTRA";
  public static final String VIDEO_LIST_EXTRA = PACKAGE + ".VIDEO_LIST_EXTRA";
  public static final String CAST_CREW_LIST_EXTRA = PACKAGE + ".CAST_CREW_LIST_EXTRA";

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