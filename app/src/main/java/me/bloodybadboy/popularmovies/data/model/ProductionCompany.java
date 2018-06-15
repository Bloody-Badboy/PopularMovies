package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;

@SuppressWarnings("unused")
public class ProductionCompany {

  @Json(name = "id") private long mId;
  @Json(name = "logo_path") private String mLogoPath;
  @Json(name = "name") private String mName;
  @Json(name = "origin_country") private String mOriginCountry;

  public long getId() {
    return mId;
  }

  public String getLogoPath() {
    return mLogoPath;
  }

  public String getName() {
    return mName;
  }

  public String getOriginCountry() {
    return mOriginCountry;
  }
}
