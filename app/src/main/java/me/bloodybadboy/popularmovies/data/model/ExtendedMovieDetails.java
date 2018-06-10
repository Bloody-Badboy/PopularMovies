package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class ExtendedMovieDetails {

  @Json(name = "adult") private boolean mAdult;
  @Json(name = "backdrop_path") private String mBackdropPath;
  @Json(name = "belongs_to_collection") private Object mBelongsToCollection;
  @Json(name = "budget") private long mBudget;
  @Json(name = "genres") private List<Genre> mGenres;
  @Json(name = "homepage") private String mHomepage;
  @Json(name = "id") private long mId;
  @Json(name = "imdb_id") private String mImdbId;
  @Json(name = "original_language") private String mOriginalLanguage;
  @Json(name = "original_title") private String mOriginalTitle;
  @Json(name = "overview") private String mOverview;
  @Json(name = "popularity") private double mPopularity;
  @Json(name = "poster_path") private String mPosterPath;
  @Json(name = "production_companies") private List<ProductionCompany> mProductionCompanies;
  @Json(name = "production_countries") private List<ProductionCountry> mProductionCountries;
  @Json(name = "release_date") private String mReleaseDate;
  @Json(name = "revenue") private long mRevenue;
  @Json(name = "reviews") private Reviews mReviews;
  @Json(name = "runtime") private long mRuntime;
  @Json(name = "spoken_languages") private List<SpokenLanguage> mSpokenLanguages;
  @Json(name = "status") private String mStatus;
  @Json(name = "tagline") private String mTagLine;
  @Json(name = "title") private String mTitle;
  @Json(name = "video") private boolean mVideo;
  @Json(name = "videos") private Videos mVideos;
  @Json(name = "vote_average") private double mVoteAverage;
  @Json(name = "vote_count") private long mVoteCount;

  public boolean isAdult() {
    return mAdult;
  }

  public String getBackdropPath() {
    return mBackdropPath;
  }

  public Object getBelongsToCollection() {
    return mBelongsToCollection;
  }

  public long getBudget() {
    return mBudget;
  }

  public List<Genre> getGenres() {
    return mGenres;
  }

  public String getHomepage() {
    return mHomepage;
  }

  public long getId() {
    return mId;
  }

  public String getImdbId() {
    return mImdbId;
  }

  public String getOriginalLanguage() {
    return mOriginalLanguage;
  }

  public String getOriginalTitle() {
    return mOriginalTitle;
  }

  public String getOverview() {
    return mOverview;
  }

  public double getPopularity() {
    return mPopularity;
  }

  public String getPosterPath() {
    return mPosterPath;
  }

  public List<ProductionCompany> getProductionCompanies() {
    return mProductionCompanies;
  }

  public List<ProductionCountry> getProductionCountries() {
    return mProductionCountries;
  }

  public String getReleaseDate() {
    return mReleaseDate;
  }

  public long getRevenue() {
    return mRevenue;
  }

  public Reviews getMovieReviews() {
    return mReviews;
  }

  public long getRuntime() {
    return mRuntime;
  }

  public List<SpokenLanguage> getSpokenLanguages() {
    return mSpokenLanguages;
  }

  public String getStatus() {
    return mStatus;
  }

  public String getTagLine() {
    return mTagLine;
  }

  public String getTitle() {
    return mTitle;
  }

  public boolean isVideo() {
    return mVideo;
  }

  public Videos getMovieVideos() {
    return mVideos;
  }

  public double getVoteAverage() {
    return mVoteAverage;
  }

  public long getVoteCount() {
    return mVoteCount;
  }

  @Override public String toString() {
    return "ExtendedMovieDetails{" +
        "mAdult=" + mAdult +
        ", mBackdropPath='" + mBackdropPath + '\'' +
        ", mBelongsToCollection=" + mBelongsToCollection +
        ", mBudget=" + mBudget +
        ", mGenres=" + mGenres +
        ", mHomepage='" + mHomepage + '\'' +
        ", mId=" + mId +
        ", mImdbId='" + mImdbId + '\'' +
        ", mOriginalLanguage='" + mOriginalLanguage + '\'' +
        ", mOriginalTitle='" + mOriginalTitle + '\'' +
        ", mOverview='" + mOverview + '\'' +
        ", mPopularity=" + mPopularity +
        ", mPosterPath='" + mPosterPath + '\'' +
        ", mProductionCompanies=" + mProductionCompanies +
        ", mProductionCountries=" + mProductionCountries +
        ", mReleaseDate='" + mReleaseDate + '\'' +
        ", mRevenue=" + mRevenue +
        ", mReviews=" + mReviews +
        ", mRuntime=" + mRuntime +
        ", mSpokenLanguages=" + mSpokenLanguages +
        ", mStatus='" + mStatus + '\'' +
        ", mTagLine='" + mTagLine + '\'' +
        ", mTitle='" + mTitle + '\'' +
        ", mVideo=" + mVideo +
        ", mVideos=" + mVideos +
        ", mVoteAverage=" + mVoteAverage +
        ", mVoteCount=" + mVoteCount +
        '}';
  }
}
