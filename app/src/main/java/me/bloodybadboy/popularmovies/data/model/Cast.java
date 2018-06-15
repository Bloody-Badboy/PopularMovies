
package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Cast implements Parcelable {

  @Json(name = "cast_id") private Long mCastId;
  @Json(name = "character") private String mCharacter;
  @Json(name = "credit_id") private String mCreditId;
  @Json(name = "gender") private Long mGender;
  @Json(name = "id") private Long mId;
  @Json(name = "name") private String mName;
  @Json(name = "order") private Long mOrder;
  @Json(name = "profile_path") private String mProfilePath;

  private Cast(Parcel in) {
    if (in.readByte() == 0) {
      mCastId = null;
    } else {
      mCastId = in.readLong();
    }
    mCharacter = in.readString();
    mCreditId = in.readString();
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
    mName = in.readString();
    if (in.readByte() == 0) {
      mOrder = null;
    } else {
      mOrder = in.readLong();
    }
    mProfilePath = in.readString();
  }

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

  public Long getCastId() {
    return mCastId;
  }

  public String getCharacter() {
    return mCharacter;
  }

  public String getCreditId() {
    return mCreditId;
  }

  public Long getGender() {
    return mGender;
  }

  public Long getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public Long getOrder() {
    return mOrder;
  }

  public String getProfilePath() {
    return mProfilePath;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    if (mCastId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(mCastId);
    }
    dest.writeString(mCharacter);
    dest.writeString(mCreditId);
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
    dest.writeString(mName);
    if (mOrder == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(mOrder);
    }
    dest.writeString(mProfilePath);
  }
}
