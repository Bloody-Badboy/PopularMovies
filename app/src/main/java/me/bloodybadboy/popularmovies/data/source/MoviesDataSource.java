package me.bloodybadboy.popularmovies.data.source;

import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movies;

import static me.bloodybadboy.popularmovies.Constants.SortByOrder;

public interface MoviesDataSource {
  Single<Genres> getMovieGenreList();

  Single<Movies> getMovieList(SortByOrder sortByOrder, Map<String, String> options);

  Single<ExtendedMovieDetails> getExtendedMovieDetails(String movieId);

  Completable addMovieToFavourites(Movie movie);
}
