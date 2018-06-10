package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Videos {

  @Json(name = "results") private List<Video> mVideos;

  public List<Video> getResults() {
    return mVideos;
  }
}
