package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class SpokenLanguage {

  @Json(name = "iso_639_1") private String mIso6391;
  @Json(name = "name") private String mName;


  public String getIso6391() {
    return mIso6391;
  }

  public String getName() {
    return mName;
  }
}
