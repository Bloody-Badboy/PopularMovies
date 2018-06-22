package me.bloodybadboy.popularmovies.ui.details;

import android.support.annotation.NonNull;
import me.bloodybadboy.popularmovies.base.BasePresenter;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;

public interface MovieDetailsContract {
  interface Presenter extends BasePresenter<View> {

    void fetchMovieDetails(String movieId);

    void checkMovieInFavourites(int movieId);

    void addMovieToFavourites(@NonNull Movie movie);

    void removeMovieFromFavourites(int movieId);
  }

  interface View extends BaseView<Presenter> {

    void showProgress();

    void hideProgress();

    void setIsMovieInFavourites(boolean isFavourite);

    void onMovieDetailsFetchSuccess(ExtendedMovieDetails movieDetails);

    void onUnknownError();

    void onNetworkError();

    void onTimeout();

    void onMovieInFavouritesCheckSuccess(boolean isFavourite);

    void onMovieInFavouritesCheckError(Throwable throwable);

    void onAddMovieToFavouritesSuccess();

    void onAddMovieToFavouritesError(Throwable throwable);

    void onRemoveMovieFromFavouritesSuccess();

    void onRemoveMovieFromFavouritesError(Throwable throwable);

    void launchVideoOnYoutube(String videoId);
  }
}
