package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings({ "unused", "WeakerAccess" })
public class ProductionCompany implements Parcelable {

  @Json(name = "id") private long mId;
  @Json(name = "logo_path") private String mLogoPath;
  @Json(name = "name") private String mName;
  @Json(name = "origin_country") private String mOriginCountry;

  protected ProductionCompany(Parcel in) {
    mId = in.readLong();
    mLogoPath = in.readString();
    mName = in.readString();
    mOriginCountry = in.readString();
  }

  public static final Creator<ProductionCompany> CREATOR = new Creator<ProductionCompany>() {
    @Override
    public ProductionCompany createFromParcel(Parcel in) {
      return new ProductionCompany(in);
    }

    @Override
    public ProductionCompany[] newArray(int size) {
      return new ProductionCompany[size];
    }
  };

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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(mId);
    dest.writeString(mLogoPath);
    dest.writeString(mName);
    dest.writeString(mOriginCountry);
  }
}
