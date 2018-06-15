package me.bloodybadboy.popularmovies.storage;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import java.util.HashMap;
import java.util.List;
import me.bloodybadboy.popularmovies.data.model.Genre;

public class MovieGenreStore {
  private static volatile MovieGenreStore sInstance = null;
  private final SparseArray<String> sparseArray = new SparseArray<>();
  @SuppressLint("UseSparseArrays") private final HashMap<Integer, String> mMap = new HashMap<>();

  private MovieGenreStore() {
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + MovieGenreStore.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  @NonNull public static MovieGenreStore getInstance() {
    if (sInstance == null) {
      synchronized (MovieGenreStore.class) {
        if (sInstance == null) {
          sInstance = new MovieGenreStore();
        }
      }
    }
    return sInstance;
  }

  public final void put(List<Genre> genreList) {
    if (genreList != null) {
      for (Genre genre : genreList) {
        sparseArray.put(genre.getId(), genre.getName());
      }
    }
  }

  public final void clear() {
    mMap.clear();
    sparseArray.clear();
  }

  @NonNull public SparseArray<String> get() {
    return sparseArray;
  }
}
