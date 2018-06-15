
package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Crew implements Parcelable {

  @Json(name = "credit_id") private String mCreditId;
  @Json(name = "department") private String mDepartment;
  @Json(name = "gender") private Long mGender;
  @Json(name = "id") private Long mId;
  @Json(name = "job") private String mJob;
  @Json(name = "name") private String mName;
  @Json(name = "profile_path") private Object mProfilePath;

  private Crew(Parcel in) {
    mCreditId = in.readString();
    mDepartment = in.readString();
    if (in.readByte() == 0) {
      mGender = null;
    } else {
      mGender = in.readLong();
    }
    if (in.readByte() == 0) {
      mId = null;
    } else {
      mId = in.readLong();
    }
    mJob = in.readString();
    mName = in.readString();
  }

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

  public String getCreditId() {
    return mCreditId;
  }

  public String getDepartment() {
    return mDepartment;
  }

  public Long getGender() {
    return mGender;
  }

  public Long getId() {
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
    if (mGender == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(mGender);
    }
    if (mId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(mId);
    }
    dest.writeString(mJob);
    dest.writeString(mName);
  }
}
