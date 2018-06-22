package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Cast implements Parcelable {

  public static final Creator<Cast> CREATOR = new Creator<Cast>() {
    @Override
    public Cast createFromParcel(Parcel in) {
      return new Cast(in);
    }

    @Override
    public Cast[] newArray(int size) {
      return new Cast[size];
    }
  };
  @Json(name = "cast_id") private long mCastId;
  @Json(name = "character") private String mCharacter;
  @Json(name = "credit_id") private String mCreditId;
  @Json(name = "gender") private long mGender;
  @Json(name = "id") private long mId;
  @Json(name = "name") private String mName;
  @Json(name = "order") private long mOrder;
  @Json(name = "profile_path") private String mProfilePath;

  private Cast(Parcel in) {
    mCastId = in.readLong();
    mCharacter = in.readString();
    mCreditId = in.readString();
    mGender = in.readLong();
    mId = in.readLong();
    mName = in.readString();
    mOrder = in.readLong();
    mProfilePath = in.readString();
  }

  @Override public String toString() {
    return "Cast{" +
        "mCastId=" + mCastId +
        ", mCharacter='" + mCharacter + '\'' +
        ", mCreditId='" + mCreditId + '\'' +
        ", mGender=" + mGender +
        ", mId=" + mId +
        ", mName='" + mName + '\'' +
        ", mOrder=" + mOrder +
        ", mProfilePath='" + mProfilePath + '\'' +
        '}';
  }

  public long getCastId() {
    return mCastId;
  }

  public String getCharacter() {
    return mCharacter;
  }

  public String getCreditId() {
    return mCreditId;
  }

  public long getGender() {
    return mGender;
  }

  public long getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public long getOrder() {
    return mOrder;
  }

  public String getProfilePath() {
    return mProfilePath;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {

    dest.writeLong(mCastId);
    dest.writeString(mCharacter);
    dest.writeString(mCreditId);
    dest.writeLong(mGender);
    dest.writeLong(mId);
    dest.writeString(mName);
    dest.writeLong(mOrder);
    dest.writeString(mProfilePath);
  }
}
