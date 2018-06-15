package me.bloodybadboy.popularmovies.ui.details;

import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;

public interface DetailsActivityContract {
  interface Presenter extends BasePresenter<View> {
    void fetchMovieDetailsFromServer(String movieId);
  }

  interface View extends BaseView<Presenter> {
    void onMovieDetailsFetchSuccess(ExtendedMovieDetails movieDetails);

    void onMovieDetailsFetchError(Throwable throwable);
  }
}
