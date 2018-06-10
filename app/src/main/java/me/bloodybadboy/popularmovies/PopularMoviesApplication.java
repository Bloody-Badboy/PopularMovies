package me.bloodybadboy.popularmovies;

import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
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

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);

    Stetho.initializeWithDefaults(this);
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
