package me.bloodybadboy.popularmovies.ui.homescreen;

import java.util.Map;
import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;

public interface HomeActivityContract {
  interface Presenter extends BasePresenter {
    void fetchMovieListDataLayer(Map<String, String> options);

    void clearCompositeSubscription();
  }

  interface View extends BaseView<Presenter> {
    void onMovieListFetchSuccess(Movies movies);

    void onMovieListFetchError(Throwable throwable);

    void onSortOrderChanged();

    void launchDetailsActivity(DetailsActivityLaunchModel launchModel);
  }
}
