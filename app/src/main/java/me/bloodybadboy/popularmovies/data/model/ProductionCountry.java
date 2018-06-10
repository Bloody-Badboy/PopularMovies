package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class ProductionCountry {

  @Json(name = "iso_3166_1") private String mIso31661;
  @Json(name = "name") private String mName;

  public String getIso31661() {
    return mIso31661;
  }

  public String getName() {
    return mName;
  }
}
