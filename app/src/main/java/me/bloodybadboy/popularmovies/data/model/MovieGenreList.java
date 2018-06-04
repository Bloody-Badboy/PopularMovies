package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

public class MovieGenreList {
  @Json(name = "genres")
  private List<Genre> mGenres;

  public List<Genre> getGenres() {
    return mGenres;
  }

  public void setGenres(List<Genre> genres) {
    this.mGenres = genres;
  }
}
