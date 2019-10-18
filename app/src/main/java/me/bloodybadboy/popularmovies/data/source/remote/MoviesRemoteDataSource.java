package me.bloodybadboy.popularmovies.data.source.remote;

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

public class MoviesRemoteDataSource implements MoviesDataSource {

  private static volatile MoviesRemoteDataSource sInstance;

  private final TheMovieDbApiService mTheMovieDbApiService;
  private final String mApiKey;

  private MoviesRemoteDataSource(TheMovieDbApiService theMovieDbApiService, String apiKey) {
    mTheMovieDbApiService = theMovieDbApiService;
    mApiKey = apiKey;
  }

  public static MoviesDataSource getInstance(TheMovieDbApiService theMovieDbApiService,
      String apiKey) {
    if (sInstance == null) {
      synchronized (MoviesRemoteDataSource.class) {
        if (sInstance == null) {
          sInstance = new MoviesRemoteDataSource(theMovieDbApiService, apiKey);
        }
      }
    }
    return sInstance;
  }

  @NonNull @Override public Single<Genres> getMovieGenreList() {
    return mTheMovieDbApiService.getMovieGenreList(mApiKey);
  }

  @NonNull @Override
  public Single<Movies> getMovieList(@NonNull MoviesFilterType moviesFilterType, @NonNull
      Map<String, String> options) {
    return mTheMovieDbApiService.getMovieList(moviesFilterType.toString(), mApiKey, options);
  }

  @NonNull @Override
  public Single<ExtendedMovieDetails> getExtendedMovieDetails(@NonNull String movieId) {
    return mTheMovieDbApiService.getExtendedMovieDetails(movieId, mApiKey);
  }

  @NonNull @Override public Single<Boolean> isMovieInFavourites(int movieId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @NonNull @Override public Completable addMovieToFavourites(@NonNull Movie movie) {
    throw new UnsupportedOperationException("Not supported");
  }

  @NonNull @Override public Completable removeMovieFromFavourites(int movieId) {
    throw new UnsupportedOperationException("Not supported");
  }
}
