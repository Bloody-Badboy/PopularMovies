package me.bloodybadboy.popularmovies.ui.homescreen.model;

import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.Constants.MoviesFilterType;
import me.bloodybadboy.popularmovies.data.model.Movie;

public class HomeActivityStore {
  public List<Movie> mMovieList = new ArrayList<>();
  public int mLastScrollPostion;
  public MoviesFilterType mCurrentMoviesFilterType;
}
