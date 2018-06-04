package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Genre {

  @Json(name = "id")
  private int mId;

  @Json(name = "name")
  private String mName;

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }
}
