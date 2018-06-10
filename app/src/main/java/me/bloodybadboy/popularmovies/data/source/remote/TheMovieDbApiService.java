package me.bloodybadboy.popularmovies.data.source.remote;

import io.reactivex.Single;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Genres;
import me.bloodybadboy.popularmovies.data.model.Movies;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TheMovieDbApiService {

  @GET("genre/movie/list")
  Single<Genres> getMovieGenreList(@Query("api_key") String apiKey);

  @GET("movie/{sort_by}")
  Single<Movies> getMovieList(@Path("sort_by") String sortBy,
      @Query("api_key") String apiKey, @QueryMap Map<String, String> options);

  //https://api.themoviedb.org/3/movie/157336?api_key=f7be0ecc27217f0b7fd3eea0d1988b37&append_to_response=videos,images,reviews
  @GET("movie/{movie_id}?append_to_response=videos,images,reviews")
  Single<ExtendedMovieDetails> getExtendedMovieDetails(@Path("movie_id") String movieId,
      @Query("api_key") String apiKey);
}
