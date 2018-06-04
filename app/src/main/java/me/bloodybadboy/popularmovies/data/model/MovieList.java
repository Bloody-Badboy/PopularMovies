package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

public class MovieList {

  @Json(name = "page")
  private int mPage;

  @Json(name = "total_results")
  private int mTotalResults;

  @Json(name = "total_pages")
  private int mTotalPages;

  @Json(name = "results")
  private List<Movie> mMovies;

  public int getPage() {
    return mPage;
  }

  public void setPage(int page) {
    this.mPage = page;
  }

  public int getTotalResults() {
    return mTotalResults;
  }

  public void setTotalResults(int totalResults) {
    this.mTotalResults = totalResults;
  }

  public int getTotalPages() {
    return mTotalPages;
  }

  public void setTotalPages(int totalPages) {
    this.mTotalPages = totalPages;
  }

  public List<Movie> getResults() {
    return mMovies;
  }

  public void setResults(List<Movie> movies) {
    this.mMovies = movies;
  }

  @Override public String toString() {
    return "MovieList{" +
        "Page=" + mPage +
        ", TotalResults=" + mTotalResults +
        ", TotalPages=" + mTotalPages +
        ", Movies=" + mMovies +
        '}';
  }
}
