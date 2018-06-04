package me.bloodybadboy.popularmovies.data.source.remote;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.MovieGenreList;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;

public class MoviesRemoteDataSource implements MoviesDataSource {

  public static MoviesRemoteDataSource INSTANCE;

  private TheMovieDbApiService mTheMovieDbApiService;
  private String mApiKey;

  private MoviesRemoteDataSource(TheMovieDbApiService theMovieDbApiService, String apiKey) {
    mTheMovieDbApiService = theMovieDbApiService;
    mApiKey = apiKey;
  }

  public static MoviesDataSource getInstance(TheMovieDbApiService theMovieDbApiService,
      String apiKey) {
    if (INSTANCE == null) {
      synchronized (MoviesRemoteDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new MoviesRemoteDataSource(theMovieDbApiService, apiKey);
        }
      }
    }
    return INSTANCE;
  }

  @Override public Single<MovieGenreList> getMovieGenreList() {
    return mTheMovieDbApiService.getMovieGenreList(mApiKey);
  }

  @Override public Single<MovieList> getPopularMovieList(Map<String, String> options) {
    return mTheMovieDbApiService.getPopularMovieList(mApiKey, options);
  }

  @Override public Single<MovieList> getTopRatedMovieList(Map<String, String> options) {
    return mTheMovieDbApiService.getTopRatedMovieList(mApiKey, options);
  }
}
