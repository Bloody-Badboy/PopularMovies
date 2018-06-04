package me.bloodybadboy.popularmovies.data.source;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.MovieGenreList;
import me.bloodybadboy.popularmovies.data.model.MovieList;

public interface MoviesDataSource {
  Single<MovieGenreList> getMovieGenreList();

  Single<MovieList> getPopularMovieList(Map<String, String> options);

  Single<MovieList> getTopRatedMovieList(Map<String, String> options);
}
