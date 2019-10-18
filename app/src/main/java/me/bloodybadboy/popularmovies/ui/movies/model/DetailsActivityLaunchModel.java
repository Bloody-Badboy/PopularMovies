package me.bloodybadboy.popularmovies.ui.movies.model;

import androidx.core.util.Pair;
import android.view.View;
import me.bloodybadboy.popularmovies.data.model.Movie;

public class DetailsActivityLaunchModel {
  private Movie mMovie;
  private Pair<View, String>[] mSharedElementPairs;
  private int position;

  @SafeVarargs
  public DetailsActivityLaunchModel(Movie movie,int position, Pair<View, String>... sharedElements) {
    this.mMovie = movie;
    this.position = position;
    this.mSharedElementPairs = sharedElements;
  }

  public Movie getMovie() {
    return mMovie;
  }

  public int getPosition() {
    return position;
  }

  public Pair<View, String>[] getSharedElements() {
    return mSharedElementPairs;
  }
}
