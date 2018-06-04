package me.bloodybadboy.popularmovies.data.source.remote;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.MovieGenreList;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TheMovieDbApiService {

  @GET("genre/movie/list")
  Single<MovieGenreList> getMovieGenreList(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Single<MovieList> getPopularMovieList(@Query("api_key") String apiKey,
      @QueryMap Map<String, String> options);

  @GET("movie/top_rated")
  Single<MovieList> getTopRatedMovieList(@Query("api_key") String apiKey, @QueryMap
      Map<String, String> options);
}
