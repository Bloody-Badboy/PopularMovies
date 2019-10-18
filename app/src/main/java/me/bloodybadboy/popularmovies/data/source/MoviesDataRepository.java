package me.bloodybadboy.popularmovies.data.source;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants.MoviesFilterType;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movie;
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

  @NonNull @Override public Single<Genres> getMovieGenreList() {
    return mRemoteDataSource.getMovieGenreList();
  }

  @NonNull @Override public Single<Movies> getMovieList(@NonNull MoviesFilterType moviesFilterType,
      @NonNull Map<String, String> options) {
    if (moviesFilterType == MoviesFilterType.FAVOURITES) {
      return mLocalDataSource.getMovieList(moviesFilterType, options);
    }
    return mRemoteDataSource.getMovieList(moviesFilterType, options);
  }

  @NonNull @Override
  public Single<ExtendedMovieDetails> getExtendedMovieDetails(@NonNull String movieId) {
    return mRemoteDataSource.getExtendedMovieDetails(movieId);
  }

  @NonNull @Override public Single<Boolean> isMovieInFavourites(int movieId) {
    return mLocalDataSource.isMovieInFavourites(movieId);
  }

  @NonNull @Override public Completable addMovieToFavourites(@NonNull Movie movie) {
    return mLocalDataSource.addMovieToFavourites(movie);
  }

  @NonNull @Override public Completable removeMovieFromFavourites(int movieId) {
    return mLocalDataSource.removeMovieFromFavourites(movieId);
  }
}
