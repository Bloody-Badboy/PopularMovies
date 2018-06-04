package me.bloodybadboy.popularmovies.storage;

import java.util.Map;

public class MovieGenreStore {
  private static volatile MovieGenreStore sInstance = null;
  private Map<String, String> mGenresMap;

  private MovieGenreStore() {
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + MovieGenreStore.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static MovieGenreStore getInstance() {
    if (sInstance == null) {
      synchronized (MovieGenreStore.class) {
        if (sInstance == null) {
          sInstance = new MovieGenreStore();
        }
      }
    }
    return sInstance;
  }

  public Map<String, String> getGenresMap() {
    return mGenresMap;
  }

  public void store(Map<String, String> genresMap) {
    this.mGenresMap = genresMap;
  }
}
