package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;
import java.util.List;
import me.bloodybadboy.popularmovies.utils.ArrayUtils;

@SuppressWarnings("unused") public class Movie implements Parcelable {

  public static final Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };
  @Json(name = "vote_count")
  private int mVoteCount;
  @Json(name = "id")
  private int mMovieId;
  @Json(name = "video")
  private boolean mVideo;
  @Json(name = "vote_average")
  private double mVoteAverage;
  @Json(name = "title")
  private String mTitle;
  @Json(name = "popularity")
  private double mPopularity;
  @Json(name = "poster_path")
  private String mPosterPath;
  @Json(name = "original_language")
  private String mOriginalLanguage;
  @Json(name = "original_title")
  private String mOriginalTitle;
  @Json(name = "genre_ids")
  private List<Integer> mGenreIds;
  @Json(name = "backdrop_path")
  private String mBackdropPath;
  @Json(name = "adult")
  private boolean mAdult;
  @Json(name = "overview")
  private String mOverview;
  @Json(name = "release_date")
  private String mReleaseDate;

  protected Movie(Parcel in) {
    mVoteCount = in.readInt();
    mMovieId = in.readInt();
    mVideo = in.readByte() != 0;
    mVoteAverage = in.readDouble();
    mTitle = in.readString();
    mPopularity = in.readDouble();
    mPosterPath = in.readString();
    mOriginalLanguage = in.readString();
    mOriginalTitle = in.readString();
    mGenreIds = ArrayUtils.toIntegerList(in.createIntArray());
    mBackdropPath = in.readString();
    mAdult = in.readByte() != 0;
    mOverview = in.readString();
    mReleaseDate = in.readString();
  }

  @Override public String toString() {
    return "Movie{" +
        "VoteCount=" + mVoteCount +
        ", MovieId=" + mMovieId +
        ", Video=" + mVideo +
        ", VoteAverage=" + mVoteAverage +
        ", Title='" + mTitle + '\'' +
        ", Popularity=" + mPopularity +
        ", PosterPath='" + mPosterPath + '\'' +
        ", OriginalLanguage='" + mOriginalLanguage + '\'' +
        ", OriginalTitle='" + mOriginalTitle + '\'' +
        ", GenreIds=" + mGenreIds +
        ", BackdropPath='" + mBackdropPath + '\'' +
        ", Adult=" + mAdult +
        ", Overview='" + mOverview + '\'' +
        ", ReleaseDate='" + mReleaseDate + '\'' +
        '}';
  }

  public int getVoteCount() {
    return mVoteCount;
  }

  public int getMovieId() {
    return mMovieId;
  }

  public boolean isVideo() {
    return mVideo;
  }

  public double getVoteAverage() {
    return mVoteAverage;
  }

  public String getTitle() {
    return mTitle;
  }

  public double getPopularity() {
    return mPopularity;
  }

  public String getPosterPath() {
    return mPosterPath;
  }

  public String getOriginalLanguage() {
    return mOriginalLanguage;
  }

  public String getOriginalTitle() {
    return mOriginalTitle;
  }

  public List<Integer> getGenreIds() {
    return mGenreIds;
  }

  public String getBackdropPath() {
    return mBackdropPath;
  }

  public boolean isAdult() {
    return mAdult;
  }

  public String getOverview() {
    return mOverview;
  }

  public String getReleaseDate() {
    return mReleaseDate;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Movie movie = (Movie) o;

    if (mVoteCount != movie.mVoteCount) return false;
    if (mMovieId != movie.mMovieId) return false;
    if (mVideo != movie.mVideo) return false;
    if (Double.compare(movie.mVoteAverage, mVoteAverage) != 0) return false;
    if (Double.compare(movie.mPopularity, mPopularity) != 0) return false;
    if (mAdult != movie.mAdult) return false;
    if (mTitle != null ? !mTitle.equals(movie.mTitle) : movie.mTitle != null) return false;
    if (mPosterPath != null ? !mPosterPath.equals(movie.mPosterPath) : movie.mPosterPath != null) {
      return false;
    }
    if (mOriginalLanguage != null ? !mOriginalLanguage.equals(movie.mOriginalLanguage)
        : movie.mOriginalLanguage != null) {
      return false;
    }
    if (mOriginalTitle != null ? !mOriginalTitle.equals(movie.mOriginalTitle)
        : movie.mOriginalTitle != null) {
      return false;
    }
    if (mGenreIds != null ? !mGenreIds.equals(movie.mGenreIds) : movie.mGenreIds != null) {
      return false;
    }
    if (mBackdropPath != null ? !mBackdropPath.equals(movie.mBackdropPath)
        : movie.mBackdropPath != null) {
      return false;
    }
    if (mOverview != null ? !mOverview.equals(movie.mOverview) : movie.mOverview != null) {
      return false;
    }
    return mReleaseDate != null ? mReleaseDate.equals(movie.mReleaseDate)
        : movie.mReleaseDate == null;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mVoteCount);
    dest.writeInt(mMovieId);
    dest.writeByte((byte) (mVideo ? 1 : 0));
    dest.writeDouble(mVoteAverage);
    dest.writeString(mTitle);
    dest.writeDouble(mPopularity);
    dest.writeString(mPosterPath);
    dest.writeString(mOriginalLanguage);
    dest.writeString(mOriginalTitle);
    dest.writeIntArray(ArrayUtils.toPrimitiveIntArray(mGenreIds));
    dest.writeString(mBackdropPath);
    dest.writeByte((byte) (mAdult ? 1 : 0));
    dest.writeString(mOverview);
    dest.writeString(mReleaseDate);
  }
}
