package me.bloodybadboy.popularmovies.data.source.local;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;

import static me.bloodybadboy.popularmovies.Constants.MoviesFilterType;

public class MoviesLocalDataSource implements MoviesDataSource {

  private static volatile MoviesLocalDataSource sInstance;
  private final FavouriteMovieStore mMoviesStore;

  private MoviesLocalDataSource(FavouriteMovieStore moviesStore) {
    mMoviesStore = moviesStore;
  }

  public static MoviesDataSource getInstance(FavouriteMovieStore moviesStore) {
    if (sInstance == null) {
      synchronized (MoviesLocalDataSource.class) {
        if (sInstance == null) {
          sInstance = new MoviesLocalDataSource(moviesStore);
        }
      }
    }
    return sInstance;
  }

  @NonNull @Override public Single<Genres> getMovieGenreList() {
    throw new UnsupportedOperationException("Not supported");
  }

  @NonNull @Override
  public Single<Movies> getMovieList(@NonNull MoviesFilterType moviesFilterType,
      @NonNull Map<String, String> options) {
    return FavouriteMovieStore.getInstance().getFavouriteMovieList();
  }

  @NonNull @Override
  public Single<ExtendedMovieDetails> getExtendedMovieDetails(@NonNull String movieId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @NonNull @Override public Single<Boolean> isMovieInFavourites(int movieId) {
    return mMoviesStore.isMovieInFavourites(movieId);
  }

  @NonNull @Override public Completable addMovieToFavourites(@NonNull Movie movie) {
    return mMoviesStore.addMovieToFavourites(movie);
  }

  @NonNull @Override public Completable removeMovieFromFavourites(int movieId) {
    return mMoviesStore.removeMovieFromFavourites(movieId);
  }
}
