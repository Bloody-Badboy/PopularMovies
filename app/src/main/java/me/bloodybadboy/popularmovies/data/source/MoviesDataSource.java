package me.bloodybadboy.popularmovies.data.source;

import android.support.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Movies;

import static me.bloodybadboy.popularmovies.Constants.MoviesFilterType;

public interface MoviesDataSource {
  @NonNull Single<Genres> getMovieGenreList();

  @NonNull Single<Movies> getMovieList(@NonNull MoviesFilterType moviesFilterType,
      @NonNull Map<String, String> options);

  @NonNull Single<ExtendedMovieDetails> getExtendedMovieDetails(@NonNull String movieId);

  @NonNull Single<Boolean> isMovieInFavourites(int movieId);

  @NonNull Completable addMovieToFavourites(@NonNull Movie movie);

  @NonNull Completable removeMovieFromFavourites(int movieId);
}
