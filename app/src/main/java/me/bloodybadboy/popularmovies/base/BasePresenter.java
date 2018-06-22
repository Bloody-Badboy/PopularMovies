package me.bloodybadboy.popularmovies.base;

import android.support.annotation.NonNull;

public interface BasePresenter<V extends BaseView> {

  void attachView(@NonNull V view);

  void detachView();

  void subscribe();

  void unsubscribe();
}
