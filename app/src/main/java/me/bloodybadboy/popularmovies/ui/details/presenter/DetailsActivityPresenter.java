package me.bloodybadboy.popularmovies.ui.details.presenter;

import me.bloodybadboy.popularmovies.ui.details.DetailsActivityContract;

public class DetailsActivityPresenter implements DetailsActivityContract.Presenter {

  private DetailsActivityContract.View mView;

  public DetailsActivityPresenter(DetailsActivityContract.View view) {
    this.mView = view;
  }

  @Override public void onCreate() {

  }

  @Override public void onDestroy() {

  }
}
