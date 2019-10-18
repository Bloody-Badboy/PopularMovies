package me.bloodybadboy.popularmovies.ui.movies.adapters;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import me.bloodybadboy.popularmovies.data.model.Movie;

public class MovieListDiffCallback extends DiffUtil.Callback {
  private final List<Movie> mOldMovieList;
  private final List<Movie> mNewMovieList;

  MovieListDiffCallback(List<Movie> oldMovieList, List<Movie> newMovieList) {
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
    if (mOldMovieList.get(oldItemPosition) == null || mNewMovieList.get(newItemPosition) == null) {
      return true;
    }
    return mOldMovieList.get(oldItemPosition).getMovieId() == mNewMovieList.get(newItemPosition)
        .getMovieId();
  }

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final Movie oldMovie = mOldMovieList.get(oldItemPosition);
    final Movie newMovie = mNewMovieList.get(newItemPosition);
    return oldMovie.equals(newMovie);
  }
}
