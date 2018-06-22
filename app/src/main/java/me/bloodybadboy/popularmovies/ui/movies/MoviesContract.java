package me.bloodybadboy.popularmovies.ui.movies;

import java.util.List;
import me.bloodybadboy.popularmovies.Constants.MoviesFilterType;
import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.ui.movies.model.DetailsActivityLaunchModel;

public interface MoviesContract {

  interface Presenter extends BasePresenter<View> {

    void fetchMovieList();

    void fetchPaginationMovieList();

    void retryMovieListFetch();

    void updateItemRemovedFromFavourites(int position);
  }

  interface View extends BaseView<Presenter> {

    void showProgress();

    void hideProgress();

    void setMovieFilterType(MoviesFilterType movieFilterType);

    void notifyDataLoaded();

    void showListLoadingItem();

    void hideListLoadingItem();

    void updateMoviesAdapterList(List<Movie> movies, boolean shouldSwap);

    void onUnknownError();

    void onNetworkError();

    void onTimeout();

    void showEmptyFavourites();

    void hideEmptyFavourites();

    void launchDetailsActivity(DetailsActivityLaunchModel launchModel);
  }
}
