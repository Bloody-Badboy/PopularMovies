package me.bloodybadboy.popularmovies.utils;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.data.model.Genre;

public final class Utils {
  private Utils() {
    throw new AssertionError();
  }

  public static String getPosterUrl(String posterPath) {
    if (posterPath == null) return "";
    return Constants.IMAGE_BASE_URL
        + Constants.POSTER_IMAGE_SIZE
        + posterPath;
  }

  public static String getBackdropUrl(String backdropPath) {
    if (backdropPath == null) return "";
    return Constants.IMAGE_BASE_URL
        + Constants.BACKDROP_IMAGE_SIZE
        + backdropPath;
  }

  public static String getDisplayableReadableDate(Date date) {
    return new SimpleDateFormat("dd MMM, yyyy - EEEE", Locale.ENGLISH).format(date);
  }

  public static Map<String, String> getGenresMap(List<Genre> genreList) {
    Map<String, String> genresMap = new HashMap<>();
    if (genreList != null) {
      for (Genre genre : genreList) {
        genresMap.put(String.valueOf(genre.getId()), genre.getName());
      }
    }
    return genresMap;
  }

  public static String getDisplayableGenreList(Map<String, String> genresMap,
      List<Integer> genreIds) {
    if (genresMap == null || genreIds == null) {
      return "";
    }
    List<String> displayableGenres = new ArrayList<>();
    for (Integer genreId : genreIds) {
      if (genresMap.containsKey(String.valueOf(genreId))) {
        displayableGenres.add(genresMap.get(String.valueOf(genreId)));
      }
    }
    return TextUtils.join(" | ", displayableGenres);
  }

  public static Map<String, String> getQueryMapForMovieList(int page) {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(Constants.REQUEST_QUERY_PAGE, String.valueOf(page));
    return queryMap;
  }
}
