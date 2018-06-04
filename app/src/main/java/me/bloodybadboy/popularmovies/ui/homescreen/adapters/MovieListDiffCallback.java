package me.bloodybadboy.popularmovies.ui.homescreen.adapters;

import android.support.v7.util.DiffUtil;
import java.util.List;
import me.bloodybadboy.popularmovies.data.model.Movie;

public class MovieListDiffCallback extends DiffUtil.Callback {
  private final List<Movie> mOldMovieList;
  private final List<Movie> mNewMovieList;

  public MovieListDiffCallback(List<Movie> oldMovieList, List<Movie> newMovieList) {
    this.mOldMovieList = oldMovieList;
    this.mNewMovieList = newMovieList;
  }

  @Override public int getOldListSize() {
    return mOldMovieList == null ? 0 : mOldMovieList.size();
  }

  @Override public int getNewListSize() {
    return mNewMovieList == null ? 0 : mNewMovieList.size();
  }

  @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return mOldMovieList.get(oldItemPosition).getMovieId() == mNewMovieList.get(newItemPosition)
        .getMovieId();
  }

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final Movie oldMovie = mOldMovieList.get(oldItemPosition);
    final Movie newMovie = mNewMovieList.get(newItemPosition);
    return oldMovie.equals(newMovie);
  }
}
