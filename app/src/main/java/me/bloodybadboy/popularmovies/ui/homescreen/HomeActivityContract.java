package me.bloodybadboy.popularmovies.ui.homescreen;

import java.util.Map;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.ui.homescreen.model.HomeActivityStore;

public interface HomeActivityContract {

  interface Presenter extends BasePresenter<View> {
    void fetchMovieListFromDataLayer(Map<String, String> options);

    HomeActivityStore getStore();
  }

  interface View extends BaseView<Presenter> {
    void onMovieListFetchSuccess(Movies movies);

    void onMovieListFetchError(Throwable throwable);

    void onSortOrderChanged(Constants.MoviesFilterType moviesFilterType);

    void launchDetailsActivity(DetailsActivityLaunchModel launchModel);
  }
}
