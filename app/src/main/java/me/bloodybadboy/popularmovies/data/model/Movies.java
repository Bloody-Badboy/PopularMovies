package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Movies {

  @Json(name = "page") private int mPage;
  @Json(name = "total_results") private int mTotalResults;
  @Json(name = "total_pages") private int mTotalPages;
  @Json(name = "results") private List<Movie> mMovies;

  public int getPage() {
    return mPage;
  }

  public int getTotalResults() {
    return mTotalResults;
  }

  public int getTotalPages() {
    return mTotalPages;
  }

  public List<Movie> getResults() {
    return mMovies;
  }

  @Override public String toString() {
    return "Movies{" +
        "Page=" + mPage +
        ", TotalResults=" + mTotalResults +
        ", TotalPages=" + mTotalPages +
        ", Movies=" + mMovies +
        '}';
  }
}
