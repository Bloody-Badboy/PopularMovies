package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings({ "unused", "WeakerAccess" }) public class ProductionCountry implements Parcelable {

  @Json(name = "iso_3166_1") private String mIso31661;
  @Json(name = "name") private String mName;

  protected ProductionCountry(Parcel in) {
    mIso31661 = in.readString();
    mName = in.readString();
  }

  public static final Creator<ProductionCountry> CREATOR = new Creator<ProductionCountry>() {
    @Override
    public ProductionCountry createFromParcel(Parcel in) {
      return new ProductionCountry(in);
    }

    @Override
    public ProductionCountry[] newArray(int size) {
      return new ProductionCountry[size];
    }
  };

  public String getIso31661() {
    return mIso31661;
  }

  public String getName() {
    return mName;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mIso31661);
    dest.writeString(mName);
  }
}
