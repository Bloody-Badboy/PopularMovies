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

  public enum SortOrder {
    POPULARITY, TOP_RATED
  }
}