package me.bloodybadboy.popularmovies.injection;

import android.content.Context;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.moshi.Moshi;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.PopularMoviesApplication;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.source.MoviesDataRepository;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.data.source.local.MoviesLocalDataSource;
import me.bloodybadboy.popularmovies.data.source.remote.MoviesRemoteDataSource;
import me.bloodybadboy.popularmovies.data.source.remote.TheMovieDbApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@SuppressWarnings("WeakerAccess") final public class Injection {
  private Injection() {
    throw new AssertionError();
  }

  private static volatile OkHttpClient sOkHttpClientInstance = null;
  private static volatile Retrofit sRetrofitInstance = null;
  private static volatile Moshi sMoshiInstance = null;

  public static Context provideApplicationContext() {
    return PopularMoviesApplication.getInstance().getApplicationContext();
  }

  public static OkHttpClient provideOkHttpClient() {
    if (sOkHttpClientInstance == null) {
      synchronized (Injection.class) {
        if (sOkHttpClientInstance == null) {
          sOkHttpClientInstance = new OkHttpClient.Builder()
              .addNetworkInterceptor(new StethoInterceptor())
              .build();
        }
      }
    }
    return sOkHttpClientInstance;
  }

  public static Retrofit provideRetrofit() {
    if (sRetrofitInstance == null) {
      synchronized (Injection.class) {
        if (sRetrofitInstance == null) {
          sRetrofitInstance = new Retrofit.Builder()
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(MoshiConverterFactory.create())
              .client(provideOkHttpClient())
              .baseUrl(Constants.API_SERVICE_END_POINT)
              .build();
        }
      }
    }
    return sRetrofitInstance;
  }

  public static Moshi provideMoshi() {
    if (sMoshiInstance == null) {
      synchronized (Injection.class) {
        if (sMoshiInstance == null) {
          sMoshiInstance = new Moshi.Builder().build();
        }
      }
    }
    return sMoshiInstance;
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
