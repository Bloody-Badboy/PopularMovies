package me.bloodybadboy.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Genre;

public final class Utils {


  private Utils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  @Nullable public static String getPosterUrl(@Nullable String posterPath) {
    if (posterPath == null || TextUtils.isEmpty(posterPath)) return null;
    return Constants.IMAGE_BASE_URL
        + Constants.POSTER_IMAGE_SIZE
        + posterPath;
  }

  @Nullable public static String getBackdropUrl(@Nullable String backdropPath) {
    if (backdropPath == null || TextUtils.isEmpty(backdropPath)) return null;
    return Constants.IMAGE_BASE_URL
        + Constants.BACKDROP_IMAGE_SIZE
        + backdropPath;
  }

  @Nullable public static String getYouTubeThumbnailUrl(@Nullable String videoId) {
    if (videoId == null || TextUtils.isEmpty(videoId)) return null;
    return Constants.YOUTUBE_THUMBNAIL_BASE_URL
        + videoId
        + "/"
        + Constants.YOUTUBE_THUMBNAIL_IMAGE_SIZE;
  }

  @Nullable public static String getProfilePictureUrl(@Nullable String profilePath) {
    if (profilePath == null || TextUtils.isEmpty(profilePath)) return null;
    return Constants.IMAGE_BASE_URL
        + Constants.PROFILE_IMAGE_SIZE
        + profilePath;
  }

  public static String getDisplayableReadableDate(@NonNull Date date) {
    return new SimpleDateFormat("MMMM dd, YYYY", Locale.getDefault()).format(date);
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

  @Nullable public static String getDisplayableGenreList(@Nullable SparseArray<String> genres,
      @Nullable List<Integer> genreIds) {
    if (genres == null || genreIds == null) {
      return null;
    }
    List<String> displayableGenres = new ArrayList<>();
    for (Integer genreId : genreIds) {
      if (genres.indexOfKey(genreId) >= 0) {
        displayableGenres.add(genres.get(genreId));
      }
    }
    return TextUtils.join(" | ", displayableGenres);
  }

  public static Map<String, String> getQueryMapForMovieList(int page) {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(Constants.REQUEST_QUERY_PAGE, String.valueOf(page));
    return queryMap;
  }

  public static int calculateNoOfColumns(Context context) {
    DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
    float itemWidth = context.getResources().getDimension(R.dimen.movie_list_grid_item_min_width);
    int col = (int) (mDisplayMetrics.widthPixels / itemWidth) < 0 ? context.getResources()
        .getInteger(R.integer.movie_list_grid_min_cols)
        : (int) (mDisplayMetrics.widthPixels / itemWidth);
    int maxCols = context.getResources().getInteger(R.integer.movie_list_grid_max_cols);
    return col > maxCols ? maxCols : col;
  }

  public static int getStatusBarHeight(@NonNull Context context) {
    int result = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static boolean canPerformIntent(@NonNull Context context, @NonNull Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    List<ResolveInfo> resolveInfos =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    return resolveInfos.size() > 0;
  }
}
