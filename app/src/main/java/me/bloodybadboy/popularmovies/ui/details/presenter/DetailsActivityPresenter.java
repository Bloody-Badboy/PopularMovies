package me.bloodybadboy.popularmovies.ui.details.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.bloodybadboy.popularmovies.base.BaseView;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.ui.details.DetailsActivityContract;
import me.bloodybadboy.popularmovies.utils.RxUtils;

public class DetailsActivityPresenter implements DetailsActivityContract.Presenter {

  private DetailsActivityContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;

  private Disposable mMovieDetailsDisposable;

  public DetailsActivityPresenter(DetailsActivityContract.View view,
      MoviesDataSource moviesDataSource) {
    this.mView = view;
    this.mMoviesDataSource = moviesDataSource;

    mView.setPresenter(this);
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override public void attachView(DetailsActivityContract.View view) {
    mView = view;
  }

  @Override public void detachView() {

  }

  @Override public void subscribe() {

  }

  @Override public void unsubscribe() {
    RxUtils.clear(mCompositeDisposable);
  }

  @Override public void fetchMovieDetailsFromServer(String movieId) {
    RxUtils.dispose(mMovieDetailsDisposable);
    mMovieDetailsDisposable = mMoviesDataSource.getExtendedMovieDetails(movieId)
        .compose(RxUtils.applyIOScheduler())
        .subscribe(
            extendedMovieDetails -> {
              mView.onMovieDetailsFetchSuccess(extendedMovieDetails);
            }, throwable -> {
              mView.onMovieDetailsFetchError(throwable);
            });
    RxUtils.addToCompositeSubscription(mCompositeDisposable, mMovieDetailsDisposable);
  }
}
