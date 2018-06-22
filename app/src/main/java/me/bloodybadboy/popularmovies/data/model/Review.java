package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Review implements Parcelable {
  public static final Creator<Review> CREATOR = new Creator<Review>() {
    @Override
    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    @Override
    public Review[] newArray(int size) {
      return new Review[size];
    }
  };
  @Json(name = "author") private String mAuthor;
  @Json(name = "content") private String mContent;
  @Json(name = "id") private String mId;
  @Json(name = "url") private String mUrl;
  private Review(Parcel in) {
    mAuthor = in.readString();
    mContent = in.readString();
    mId = in.readString();
    mUrl = in.readString();
  }

  @Override public String toString() {
    return "Review{" +
        "mAuthor='" + mAuthor + '\'' +
        ", mContent='" + mContent + '\'' +
        ", mId='" + mId + '\'' +
        ", mUrl='" + mUrl + '\'' +
        '}';
  }

  public String getAuthor() {
    return mAuthor;
  }

  public void setAuthor(String author) {
    mAuthor = author;
  }

  public String getContent() {
    return mContent;
  }

  public void setContent(String content) {
    mContent = content;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    mUrl = url;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mAuthor);
    dest.writeString(mContent);
    dest.writeString(mId);
    dest.writeString(mUrl);
  }
}
