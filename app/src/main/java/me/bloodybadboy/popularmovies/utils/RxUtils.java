package me.bloodybadboy.popularmovies.utils;

import androidx.annotation.Nullable;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class RxUtils {

  private RxUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static void dispose(@Nullable CompositeDisposable compositeDisposable) {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.dispose();
    }
  }

  public static void dispose(@Nullable Disposable disposable) {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  public static void clear(@Nullable CompositeDisposable compositeDisposable) {
    if (compositeDisposable != null) {
      compositeDisposable.clear();
    }
  }

  public static void addToCompositeSubscription(@Nullable CompositeDisposable compositeDisposable,
      @Nullable Disposable disposable) {
    if (compositeDisposable != null && disposable != null) {
      compositeDisposable.add(disposable);
    }
  }

  public static <T> SingleTransformer<T, T> applyIOScheduler() {
    return upstream -> upstream.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

}