package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Crew implements Parcelable {

  public static final Creator<Crew> CREATOR = new Creator<Crew>() {
    @Override
    public Crew createFromParcel(Parcel in) {
      return new Crew(in);
    }

    @Override
    public Crew[] newArray(int size) {
      return new Crew[size];
    }
  };
  @Json(name = "credit_id") private String mCreditId;
  @Json(name = "department") private String mDepartment;
  @Json(name = "gender") private long mGender;
  @Json(name = "id") private long mId;
  @Json(name = "job") private String mJob;
  @Json(name = "name") private String mName;
  @Json(name = "profile_path") private Object mProfilePath;

  private Crew(Parcel in) {
    mCreditId = in.readString();
    mDepartment = in.readString();
    mGender = in.readLong();
    mId = in.readLong();
    mJob = in.readString();
    mName = in.readString();
  }

  @Override public String toString() {
    return "Crew{" +
        "mCreditId='" + mCreditId + '\'' +
        ", mDepartment='" + mDepartment + '\'' +
        ", mGender=" + mGender +
        ", mId=" + mId +
        ", mJob='" + mJob + '\'' +
        ", mName='" + mName + '\'' +
        ", mProfilePath=" + mProfilePath +
        '}';
  }

  public String getCreditId() {
    return mCreditId;
  }

  public String getDepartment() {
    return mDepartment;
  }

  public long getGender() {
    return mGender;
  }

  public long getId() {
    return mId;
  }

  public String getJob() {
    return mJob;
  }

  public String getName() {
    return mName;
  }

  public Object getProfilePath() {
    return mProfilePath;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mCreditId);
    dest.writeString(mDepartment);
    dest.writeLong(mGender);
    dest.writeLong(mId);
    dest.writeString(mJob);
    dest.writeString(mName);
  }
}
