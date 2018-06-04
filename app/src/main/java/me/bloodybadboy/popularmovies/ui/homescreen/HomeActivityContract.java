package me.bloodybadboy.popularmovies.ui.homescreen;

import java.util.Map;
import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;

public interface HomeActivityContract {
  interface Presenter extends BasePresenter {
    void fetchMovieListFromServer(Map<String, String> options);
  }

  interface View extends BaseView<Presenter> {
    void onMovieListFetchSuccess(MovieList movieList);

    void onMovieListFetchError(Throwable throwable);

    void onSortOrderChanged();

    void launchDetailsActivity(DetailsActivityLaunchModel launchModel);
  }
}
