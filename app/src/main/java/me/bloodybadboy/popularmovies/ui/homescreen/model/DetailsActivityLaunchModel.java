package me.bloodybadboy.popularmovies.ui.homescreen.model;

import android.support.v4.util.Pair;
import android.view.View;
import me.bloodybadboy.popularmovies.data.model.Movie;

public class DetailsActivityLaunchModel {
  private Movie mMovie;
  private Pair<View, String>[] mSharedElementPairs;

  @SafeVarargs
  public DetailsActivityLaunchModel(Movie movie, Pair<View, String>... sharedElements) {
    this.mMovie = movie;
    this.mSharedElementPairs = sharedElements;
  }

  public Movie getMovie() {
    return mMovie;
  }

  public Pair<View, String>[] getSharedElements() {
    return mSharedElementPairs;
  }
}
