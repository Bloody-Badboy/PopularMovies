package me.bloodybadboy.popularmovies.base;

public interface BaseView<T extends BasePresenter> {
  void setPresenter(T presenter);
}
