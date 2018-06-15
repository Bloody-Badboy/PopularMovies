package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Movies {

  @Json(name = "page") private int mPage;
  @Json(name = "total_results") private int mTotalResults;
  @Json(name = "total_pages") private int mTotalPages;
  @Json(name = "results") private List<Movie> mMovies;

  Movies(Builder builder) {
    mPage = builder.mPage;
    mTotalResults = builder.mTotalResults;
    mTotalPages = builder.mTotalPages;
    mMovies = builder.mMovies;
  }

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

  public static class Builder {
    private int mPage;
    private int mTotalResults;
    private int mTotalPages;
    private List<Movie> mMovies;

    public Builder withPage(int page) {
      this.mPage = page;
      return this;
    }

    public Builder withTotalResults(int totalResults) {
      this.mTotalResults = totalResults;
      return this;
    }

    public Builder withTotalPages(int totalPages) {
      this.mTotalPages = totalPages;
      return this;
    }

    public Builder withMovies(List<Movie> movies) {
      this.mMovies = movies;
      return this;
    }

    public Movies build(){
      return new Movies(this);
    }
  }
}
