package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Video implements Parcelable {

  public static final Creator<Video> CREATOR = new Creator<Video>() {
    @Override
    public Video createFromParcel(Parcel in) {
      return new Video(in);
    }

    @Override
    public Video[] newArray(int size) {
      return new Video[size];
    }
  };
  @Json(name = "id") private String mId;
  @Json(name = "iso_3166_1") private String mIso31661;
  @Json(name = "iso_639_1") private String mIso6391;
  @Json(name = "key") private String mKey;
  @Json(name = "name") private String mName;
  @Json(name = "site") private String mSite;
  @Json(name = "size") private long mSize;
  @Json(name = "type") private String mType;

  private Video(Parcel in) {
    mId = in.readString();
    mIso31661 = in.readString();
    mIso6391 = in.readString();
    mKey = in.readString();
    mName = in.readString();
    mSite = in.readString();
    mSize = in.readLong();
    mType = in.readString();
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getIso31661() {
    return mIso31661;
  }

  public void setIso31661(String iso31661) {
    mIso31661 = iso31661;
  }

  public String getIso6391() {
    return mIso6391;
  }

  public void setIso6391(String iso6391) {
    mIso6391 = iso6391;
  }

  public String getKey() {
    return mKey;
  }

  public void setKey(String key) {
    mKey = key;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public String getSite() {
    return mSite;
  }

  public void setSite(String site) {
    mSite = site;
  }

  public long getSize() {
    return mSize;
  }

  public void setSize(long size) {
    mSize = size;
  }

  public String getType() {
    return mType;
  }

  public void setType(String type) {
    mType = type;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mId);
    dest.writeString(mIso31661);
    dest.writeString(mIso6391);
    dest.writeString(mKey);
    dest.writeString(mName);
    dest.writeString(mSite);
    dest.writeLong(mSize);
    dest.writeString(mType);
  }

  @Override public String toString() {
    return "Video{" + "mId='" + mId + '\''
        + ", mIso31661='" + mIso31661 + '\''
        + ", mIso6391='" + mIso6391 + '\''
        + ", mKey='" + mKey + '\''
        + ", mName='" + mName + '\''
        + ", mSite='" + mSite + '\''
        + ", mSize=" + mSize
        + ", mType='" + mType + '\''
        + '}';
  }
}
