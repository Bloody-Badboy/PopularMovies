package me.bloodybadboy.popularmovies.data.source.local;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.MovieGenreList;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;

public class MoviesLocalDataSource implements MoviesDataSource {

  private static MoviesLocalDataSource INSTANCE;

  private MoviesLocalDataSource() {
  }

  public static MoviesDataSource getInstance() {
    if (INSTANCE == null) {
      synchronized (MoviesLocalDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new MoviesLocalDataSource();
        }
      }
    }
    return INSTANCE;
  }

  @Override public Single<MovieGenreList> getMovieGenreList() {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public Single<MovieList> getPopularMovieList(Map<String, String> options) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public Single<MovieList> getTopRatedMovieList(Map<String, String> options) {
    throw new UnsupportedOperationException("Not supported");
  }
}
