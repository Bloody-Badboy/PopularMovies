package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings({ "unused", "WeakerAccess" }) public class SpokenLanguage implements Parcelable {

  @Json(name = "iso_639_1") private String mIso6391;
  @Json(name = "name") private String mName;

  protected SpokenLanguage(Parcel in) {
    mIso6391 = in.readString();
    mName = in.readString();
  }

  public static final Creator<SpokenLanguage> CREATOR = new Creator<SpokenLanguage>() {
    @Override
    public SpokenLanguage createFromParcel(Parcel in) {
      return new SpokenLanguage(in);
    }

    @Override
    public SpokenLanguage[] newArray(int size) {
      return new SpokenLanguage[size];
    }
  };

  public String getIso6391() {
    return mIso6391;
  }

  public String getName() {
    return mName;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mIso6391);
    dest.writeString(mName);
  }
}
