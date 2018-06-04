package me.bloodybadboy.popularmovies.utils;

import android.support.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class RxUtils {

  private RxUtils() {
    throw new AssertionError();
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

  public static void addToCompositeSubscription(CompositeDisposable compositeDisposable,
      Disposable disposable) {
    if (compositeDisposable != null && disposable != null) {
      compositeDisposable.add(disposable);
    }
  }

  public static <T> SingleTransformer<T, T> applyIOScheduler() {
    return tObservable -> tObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

}