package me.bloodybadboy.popularmovies.data.source.local;

import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;

import static me.bloodybadboy.popularmovies.Constants.SortByOrder;

public class MoviesLocalDataSource implements MoviesDataSource {

  private static volatile MoviesLocalDataSource sInstance;

  private MoviesLocalDataSource() {
  }

  public static MoviesDataSource getInstance() {
    if (sInstance == null) {
      synchronized (MoviesLocalDataSource.class) {
        if (sInstance == null) {
          sInstance = new MoviesLocalDataSource();
        }
      }
    }
    return sInstance;
  }

  @Override public Single<Genres> getMovieGenreList() {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public Single<Movies> getMovieList(SortByOrder sortByOrder, Map<String, String> options) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public Single<ExtendedMovieDetails> getExtendedMovieDetails(String movieId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public Completable addMovieToFavourites(Movie movie) {
    throw new UnsupportedOperationException("Not supported");
  }
}
