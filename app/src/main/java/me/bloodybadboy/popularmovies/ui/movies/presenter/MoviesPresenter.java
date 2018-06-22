package me.bloodybadboy.popularmovies.ui.movies.presenter;

import android.support.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.data.model.Genre;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.movies.MoviesContract;
import me.bloodybadboy.popularmovies.ui.movies.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.utils.RxUtils;
import me.bloodybadboy.popularmovies.utils.Utils;
import retrofit2.HttpException;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.MoviesFilterType;
import static me.bloodybadboy.popularmovies.Constants.NO_POSITION;

public class MoviesPresenter implements MoviesContract.Presenter {

  private MoviesContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;
  private Disposable mMovieListDisposable;

  private MoviesFilterType mCurrentMoviesFilterType = Constants.DEFAULT_SORT_BY_ORDER;

  private boolean isGenreListLoaded = false;

  private int currentPage = 0;

  private boolean hasMorePages = true;

  private boolean shouldSwap = false;

  private boolean isProgressVisible = false;

  private boolean isEmptyFavouritesVisible = false;

  private List<Movie> mMovieList = null;

  public MoviesPresenter(MoviesDataSource moviesDataSource) {
    Timber.d("<init>");
    mMoviesDataSource = moviesDataSource;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override public void attachView(@NonNull MoviesContract.View view) {
    Timber.d("attachView()");
    mView = Objects.requireNonNull(view);
    mView.setMovieFilterType(mCurrentMoviesFilterType);
    if (isEmptyFavouritesVisible) {
      mView.showEmptyFavourites();
    }
  }

  @Override public void detachView() {
    Timber.d("detachView()");
    mView = null;
  }

  @Override public void subscribe() {
    Timber.d("subscribe()");
    RxUtils.addToCompositeSubscription(mCompositeDisposable,
        RxBus.getInstance().toObservable().subscribe(o -> {
          if (o instanceof DetailsActivityLaunchModel) {
            DetailsActivityLaunchModel launchModel = (DetailsActivityLaunchModel) o;
            mView.launchDetailsActivity(launchModel);
          } else if (o instanceof MoviesFilterType) {
            MoviesFilterType moviesFilterType = (MoviesFilterType) o;
            if (mCurrentMoviesFilterType != moviesFilterType) {
              mCurrentMoviesFilterType = moviesFilterType;
              filterTypeChanged();
            }
          }
        }));
  }

  @Override public void unsubscribe() {
    Timber.d("unsubscribe()");
    RxUtils.clear(mCompositeDisposable);
  }

  @Override public void fetchMovieList() {
    Timber.d("isMoviesLoadedOnce:%s", (mMovieList != null));
    if (mMovieList == null) {
      showProgress();
      loadMoreMovies();
    } else {
      Timber.d("Update adapted with stored list");
      // it's a request due to configuration change get from stored list
      if (mMovieList.size() > 0) {
        mView.updateMoviesAdapterList(mMovieList, false);
      }
    }
  }

  @Override public void fetchPaginationMovieList() {
    Timber.d("fetchPaginationMovieList()");
    if (hasMorePages) {
      mView.showListLoadingItem();
      loadMoreMovies();
    } else {
      mView.notifyDataLoaded();
    }
  }

  @Override public void retryMovieListFetch() {
    Timber.d("retryMovieListFetch()");
    fetchMovieListFromDataLayer(Utils.getQueryMapForMovieList(currentPage));
  }

  @Override public void updateItemRemovedFromFavourites(int position) {
    if (position == NO_POSITION) {
      return;
    }
    if (mCurrentMoviesFilterType == MoviesFilterType.FAVOURITES) {
      try {
        mMovieList.remove(position);
        Timber.d("Removed item: %s", position);
        if (mMovieList.size() > 0) {
          mView.updateMoviesAdapterList(mMovieList, false);
        } else {
          showEmptyFavourites();
        }
      } catch (Throwable throwable) {
        Timber.e(throwable);
      }
    }
  }

  private void fetchMovieListFromDataLayer(Map<String, String> options) {
    Timber.d("fetchMovieListFromDataLayer() options:%s", options);
    if (!isGenreListLoaded) {
      Disposable genreListDisposable = mMoviesDataSource.getMovieGenreList()
          .compose(RxUtils.applyIOScheduler())
          .subscribe(
              genreList -> {
                isGenreListLoaded = true;
                if (genreList != null) {
                  List<Genre> genres = genreList.getGenres();
                  if (genres != null && genres.size() > 0) {
                    MovieGenreStore.getInstance().put(genreList.getGenres());
                  }
                }
                fetchMovieListFromDataLayer(options);
              }, this::showError);
      RxUtils.addToCompositeSubscription(mCompositeDisposable, genreListDisposable);
    } else {
      RxUtils.dispose(mMovieListDisposable);

      mMovieListDisposable = mMoviesDataSource.getMovieList(mCurrentMoviesFilterType, options)
          .compose(RxUtils.applyIOScheduler())
          .subscribe(movies -> {

                hideProgress();

                mView.hideListLoadingItem();

                if (movies != null) {
                  if (currentPage >= movies.getTotalPages()) {
                    hasMorePages = false;
                  }
                  Timber.d("Data received total pages:%d, current page:%d",
                      movies.getTotalPages(), movies.getPage());
                  List<Movie> results = movies.getResults();
                  if (results != null) {
                    if (results.size() > 0) {
                      if (mMovieList == null) {
                        mMovieList = new ArrayList<>();
                      }
                      if (shouldSwap) {
                        shouldSwap = false;
                        mMovieList.clear();
                        mMovieList.addAll(results);
                        mView.updateMoviesAdapterList(mMovieList, true);
                      } else {
                        mMovieList.addAll(results);
                        mView.updateMoviesAdapterList(mMovieList, false);
                      }
                    } else if (mCurrentMoviesFilterType == MoviesFilterType.FAVOURITES) {
                      showEmptyFavourites();
                    }
                  }
                }

                mView.notifyDataLoaded();
              },
              this::showError);
      RxUtils.addToCompositeSubscription(mCompositeDisposable, mMovieListDisposable);
    }
  }

  private void loadMoreMovies() {
    currentPage += 1;
    fetchMovieListFromDataLayer(Utils.getQueryMapForMovieList(currentPage));
    Timber.d("loadMoreMovies() current page:%s", currentPage);
  }

  private void filterTypeChanged() {
    currentPage = 0;
    hasMorePages = true;
    shouldSwap = true;

    if (mCurrentMoviesFilterType != MoviesFilterType.FAVOURITES) {
      hideEmptyFavourites();
    }

    showProgress();
    loadMoreMovies();
  }

  private void showProgress() {
    if (!isProgressVisible) {
      isProgressVisible = true;
      mView.showProgress();
    }
  }

  private void hideProgress() {
    if (isProgressVisible) {
      isProgressVisible = false;
      mView.hideProgress();
    }
  }

  private void showEmptyFavourites() {
    if (!isEmptyFavouritesVisible) {
      isEmptyFavouritesVisible = true;

      mView.showEmptyFavourites();
    }
  }

  private void hideEmptyFavourites() {
    if (isEmptyFavouritesVisible) {
      isEmptyFavouritesVisible = false;

      mView.hideEmptyFavourites();
    }
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
