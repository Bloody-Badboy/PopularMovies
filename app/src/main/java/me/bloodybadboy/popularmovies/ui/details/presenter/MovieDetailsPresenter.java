package me.bloodybadboy.popularmovies.ui.details.presenter;

import android.support.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.ui.details.MovieDetailsContract;
import me.bloodybadboy.popularmovies.ui.details.model.YouTubeVideo;
import me.bloodybadboy.popularmovies.utils.RxUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

  private MovieDetailsContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;

  private Disposable mAddToFavouritesDisposable;
  private Disposable mRemoveFromFavouritesDisposable;

  private ExtendedMovieDetails mExtendedMovieDetails;
  private Boolean isFavourite = null;

  public MovieDetailsPresenter(MovieDetailsContract.View view,
      MoviesDataSource moviesDataSource) {
    Timber.d("<init>");
    this.mView = view;
    this.mMoviesDataSource = moviesDataSource;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override public void attachView(@NonNull MovieDetailsContract.View view) {
    Timber.d("attachView()");
    mView = Objects.requireNonNull(view);
    if (isFavourite == null) {
      mView.setIsMovieInFavourites(false);
    } else {
      mView.setIsMovieInFavourites(isFavourite);
    }
  }

  @Override public void detachView() {
    Timber.d("detachView()");
  }

  @Override public void subscribe() {
    RxUtils.addToCompositeSubscription(mCompositeDisposable,
        RxBus.getInstance().toObservable().subscribe(o -> {
          if (o instanceof YouTubeVideo) {
            YouTubeVideo youTubeVideo = (YouTubeVideo) o;
            mView.launchVideoOnYoutube(youTubeVideo.getVideoId());
          }
        }));
  }

  @Override public void unsubscribe() {
    RxUtils.clear(mCompositeDisposable);
  }

  @Override public void fetchMovieDetails(String movieId) {
    if (mExtendedMovieDetails == null) {
      mView.showProgress();
      RxUtils.addToCompositeSubscription(mCompositeDisposable,
          mMoviesDataSource.getExtendedMovieDetails(movieId)
              .compose(RxUtils.applyIOScheduler())
              .subscribe(
                  extendedMovieDetails -> {
                    mView.hideProgress();
                    mExtendedMovieDetails = extendedMovieDetails;
                    mView.onMovieDetailsFetchSuccess(extendedMovieDetails);
                  }, throwable -> {
                    mView.hideProgress();
                    showError(throwable);
                  }));
    } else {
      Timber.d("Update UI with stored movie details.");
      mView.onMovieDetailsFetchSuccess(mExtendedMovieDetails);
    }
  }

  @Override public void checkMovieInFavourites(int movieId) {
    if (isFavourite != null) {
      Timber.d("Update isFavourite with stored value.");
      mView.onMovieInFavouritesCheckSuccess(isFavourite);
      return;
    }
    if (movieId > 0) {
      RxUtils.addToCompositeSubscription(mCompositeDisposable,
          mMoviesDataSource.isMovieInFavourites(movieId)
              .compose(RxUtils.applyIOScheduler())
              .subscribe(
                  favourite -> {
                    isFavourite = favourite;
                    mView.onMovieInFavouritesCheckSuccess(favourite);
                  }, throwable -> mView.onMovieInFavouritesCheckError(throwable)));
    }
  }

  @Override public void addMovieToFavourites(@NonNull Movie movie) {
    RxUtils.dispose(mAddToFavouritesDisposable);
    mAddToFavouritesDisposable = mMoviesDataSource.addMovieToFavourites(movie)
        .compose(upstream -> upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()))
        .subscribe(
            () -> mView.onAddMovieToFavouritesSuccess(),
            throwable -> mView.onAddMovieToFavouritesError(throwable));
    RxUtils.addToCompositeSubscription(mCompositeDisposable, mAddToFavouritesDisposable);
  }

  @Override public void removeMovieFromFavourites(int movieId) {
    RxUtils.dispose(mRemoveFromFavouritesDisposable);
    mRemoveFromFavouritesDisposable = mMoviesDataSource.removeMovieFromFavourites(movieId)
        .compose(upstream -> upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()))
        .subscribe(
            () -> mView.onRemoveMovieFromFavouritesSuccess(),
            throwable -> mView.onRemoveMovieFromFavouritesError(throwable));
    RxUtils.addToCompositeSubscription(mCompositeDisposable, mRemoveFromFavouritesDisposable);
  }

  private void showError(Throwable throwable) {
    if (throwable instanceof HttpException) {
      mView.onNetworkError();
    } else if (throwable instanceof SocketTimeoutException) {
      mView.onTimeout();
    } else if (throwable instanceof IOException) {
      mView.onNetworkError();
    } else {
      mView.onUnknownError();
    }
  }
}
