package me.bloodybadboy.popularmovies.storage;

import java.util.List;
import me.bloodybadboy.popularmovies.data.model.Movie;

class HomeActivityDataStore {
  private static volatile HomeActivityDataStore sInstance = null;
  private List<Movie> movies;

  private HomeActivityDataStore() {
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + HomeActivityDataStore.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static HomeActivityDataStore getInstance() {
    if (sInstance == null) {
      synchronized (HomeActivityDataStore.class) {
        if (sInstance == null) {
          sInstance = new HomeActivityDataStore();
        }
      }
    }
    return sInstance;
  }
}