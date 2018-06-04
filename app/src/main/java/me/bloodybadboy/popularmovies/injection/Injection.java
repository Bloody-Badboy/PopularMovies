package me.bloodybadboy.popularmovies.injection;

import android.content.Context;
import me.bloodybadboy.popularmovies.BuildConfig;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.PopularMoviesApplication;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.source.MoviesDataRepository;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.data.source.local.MoviesLocalDataSource;
import me.bloodybadboy.popularmovies.data.source.remote.MoviesRemoteDataSource;
import me.bloodybadboy.popularmovies.data.source.remote.TheMovieDbApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess") final public class Injection {
  private Injection() {
    throw new AssertionError();
  }

  public static Context provideApplicationContext() {
    return PopularMoviesApplication.getInstance().getApplicationContext();
  }

  public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    return new HttpLoggingInterceptor(s -> Timber.d(s)).setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
  }

  public static Retrofit provideRetrofit() {
    return new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(new OkHttpClient.Builder()
            .addNetworkInterceptor(Injection.provideHttpLoggingInterceptor()).build())
        .baseUrl(Constants.API_SERVICE_END_POINT)
        .build();
  }

  public static TheMovieDbApiService provideApiService(Retrofit retrofit) {
    return retrofit.create(TheMovieDbApiService.class);
  }

  public static MoviesDataSource provideLocalDataSource() {
    return MoviesLocalDataSource.getInstance();
  }

  public static MoviesDataSource provideRemoteDataSource() {
    return MoviesRemoteDataSource.getInstance(
        Injection.provideApiService(Injection.provideRetrofit()),
        Injection.provideApplicationContext().getString(R.string.the_movie_db_api_key));
  }

  public static MoviesDataSource providesDataRepo() {
    return MoviesDataRepository.getInstance(Injection.provideLocalDataSource(),
        Injection.provideRemoteDataSource());
  }
}
