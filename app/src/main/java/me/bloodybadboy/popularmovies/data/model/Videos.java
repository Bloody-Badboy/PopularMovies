package me.bloodybadboy.popularmovies.data.model;

import android.os.Parcel;
import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Videos {

  @Json(name = "results") private List<Video> mVideos;

  private Videos(Parcel in) {
    mVideos = in.createTypedArrayList(Video.CREATOR);
  }

  public List<Video> getResults() {
    return mVideos;
  }
}
