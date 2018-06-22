package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Reviews {

  @Json(name = "page")
  private long mPage;
  @Json(name = "results")
  private List<Review> mReviews;
  @Json(name = "total_pages")
  private long mTotalPages;
  @Json(name = "total_results")
  private long mTotalResults;

  public long getPage() {
    return mPage;
  }

  public List<Review> getReviews() {
    return mReviews;
  }

  public long getTotalPages() {
    return mTotalPages;
  }

  public long getTotalResults() {
    return mTotalResults;
  }
}
