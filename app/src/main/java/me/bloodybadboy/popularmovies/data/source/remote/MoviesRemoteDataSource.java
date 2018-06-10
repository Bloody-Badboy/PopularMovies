package me.bloodybadboy.popularmovies.data.source.remote;

import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;

import static me.bloodybadboy.popularmovies.Constants.SortByOrder;

public class MoviesRemoteDataSource implements MoviesDataSource {

  private static volatile MoviesRemoteDataSource sInstance;

  private TheMovieDbApiService mTheMovieDbApiService;
  private String mApiKey;

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

  @Override public Single<Genres> getMovieGenreList() {
    return mTheMovieDbApiService.getMovieGenreList(mApiKey);
  }

  @Override
  public Single<Movies> getMovieList(SortByOrder sortByOrder, Map<String, String> options) {
    return mTheMovieDbApiService.getMovieList(sortByOrder.toString(), mApiKey, options);
  }

  @Override public Single<ExtendedMovieDetails> getExtendedMovieDetails(String movieId) {
    return mTheMovieDbApiService.getExtendedMovieDetails(movieId, mApiKey);
  }

  @Override public Completable addMovieToFavourites(Movie movie) {
    throw new UnsupportedOperationException("Not supported");
  }
}
