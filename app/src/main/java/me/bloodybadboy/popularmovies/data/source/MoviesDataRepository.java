package me.bloodybadboy.popularmovies.data.source;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.MovieGenreList;
import me.bloodybadboy.popularmovies.data.model.MovieList;

public class MoviesDataRepository implements MoviesDataSource {

  private static MoviesDataRepository INSTANCE = null;
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

  @Override public Single<MovieGenreList> getMovieGenreList() {
    return mRemoteDataSource.getMovieGenreList();
  }

  @Override public Single<MovieList> getPopularMovieList(Map<String, String> options) {
    return mRemoteDataSource.getPopularMovieList(options);
  }

  @Override public Single<MovieList> getTopRatedMovieList(Map<String, String> options) {
    return mRemoteDataSource.getTopRatedMovieList(options);
  }
}
