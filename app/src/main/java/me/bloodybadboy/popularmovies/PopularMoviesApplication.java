package me.bloodybadboy.popularmovies;

import android.support.annotation.NonNull;
import timber.log.Timber;

public class PopularMoviesApplication extends android.app.Application {

  private static PopularMoviesApplication sInstance;

  public static PopularMoviesApplication getInstance() {
    return sInstance;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    sInstance = this;

    if (BuildConfig.DEBUG) {
      plantTimberDebug();
    }
  }

  private void plantTimberDebug() {
    Timber.plant(new Timber.DebugTree() {
      @Override protected String createStackElementTag(@NonNull StackTraceElement element) {
        return ""
            + super.createStackElementTag(element)
            + ":"
            + element.getLineNumber();
      }
    });
  }
}
