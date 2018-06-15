
package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;
import java.util.List;

@SuppressWarnings("unused") public class Casts {

  @Json(name = "cast") private List<Cast> mCast;
  @Json(name = "crew") private List<Crew> mCrew;

  public List<Cast> getCast() {
    return mCast;
  }

  public List<Crew> getCrew() {
    return mCrew;
  }
}
