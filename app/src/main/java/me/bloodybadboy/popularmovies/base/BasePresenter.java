package me.bloodybadboy.popularmovies.base;

import androidx.annotation.NonNull;

public interface BasePresenter<V extends BaseView> {

  void attachView(@NonNull V view);

  void detachView();

  void subscribe();

  void unsubscribe();
}
