package me.bloodybadboy.popularmovies.data.source;

import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants.SortByOrder;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movies;

public class MoviesDataRepository implements MoviesDataSource {

  private volatile static MoviesDataRepository INSTANCE = null;
  private final MoviesDataSource mLocalDataSource;
  private final MoviesDataSource mRemoteDataSource;

  private MoviesDataRepository(MoviesDataSource localDataSource,
      MoviesDataSource remoteDataSource) {
    mLocalDataSource = localDataSource;
    mRemoteDataSource = remoteDataSource;
  }

  public static MoviesDataRepository getInstance(MoviesDataSource localDataSource,
      MoviesDataSource remoteDataSource) {
    if (INSTANCE == null) {
      synchronized (MoviesDataRepository.class) {
        if (INSTANCE == null) {
          INSTANCE = new MoviesDataRepository(localDataSource, remoteDataSource);
        }
      }
    }
    return INSTANCE;
  }

  @Override public Single<Genres> getMovieGenreList() {
    return mRemoteDataSource.getMovieGenreList();
  }

  @Override public Single<Movies> getMovieList(SortByOrder sortByOrder, Map<String, String> options) {
    return mRemoteDataSource.getMovieList(sortByOrder, options);
  }

  @Override public Single<ExtendedMovieDetails> getExtendedMovieDetails(String movieId) {
    return mRemoteDataSource.getExtendedMovieDetails(movieId);
  }

  @Override public Completable addMovieToFavourites(Movie movie) {
   return mLocalDataSource.addMovieToFavourites(movie);
  }
}
