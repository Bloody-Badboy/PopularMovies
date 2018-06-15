package me.bloodybadboy.popularmovies.ui.homescreen.presenter;

import android.support.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.data.model.Genre;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.homescreen.HomeActivityContract;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.ui.homescreen.model.HomeActivityStore;
import me.bloodybadboy.popularmovies.utils.RxUtils;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.MoviesFilterType;

public class HomeActivityPresenter implements HomeActivityContract.Presenter {

  private HomeActivityContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;

  private Disposable mMovieListDisposable;

  private MoviesFilterType mMoviesFilterType = Constants.DEFAULT_SORT_BY_ORDER;

  private boolean isGenreListLoaded = false;

  private HomeActivityStore homeActivityStore;

  public HomeActivityPresenter(MoviesDataSource moviesDataSource) {
    mMoviesDataSource = moviesDataSource;
    mCompositeDisposable = new CompositeDisposable();
    homeActivityStore = new HomeActivityStore();
    Timber.d("<init>");
  }

  @Override public void attachView(@NonNull HomeActivityContract.View view) {
    mView = view;
    mView.setPresenter(this);
    Timber.d("attachView()");
  }

  @Override public void detachView() {
    mView = null;
    Timber.d("detachView()");
  }

  @Override public void subscribe() {
    Timber.d("subscribe()");
    mCompositeDisposable.add(RxBus.getInstance().toObservable().subscribe(o -> {
      if (o instanceof DetailsActivityLaunchModel) {
        DetailsActivityLaunchModel launchModel = (DetailsActivityLaunchModel) o;
        if (mView != null) {
          mView.launchDetailsActivity(launchModel);
        }
      } else if (o instanceof MoviesFilterType) {
        MoviesFilterType moviesFilterType = (MoviesFilterType) o;
        if (mMoviesFilterType != moviesFilterType) {
          mMoviesFilterType = moviesFilterType;
          if (mView != null) {
            mView.onSortOrderChanged(mMoviesFilterType);
          }
        }
      }
    }));
  }

  @Override public void unsubscribe() {
    Timber.d("unsubscribe()");
    RxUtils.clear(mCompositeDisposable);
  }

  @Override
  public void fetchMovieListFromDataLayer(Map<String, String> options) {
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
              }, throwable -> {
                Timber.e(throwable);
                if (mView != null) {
                  mView.onMovieListFetchError(throwable);
                }
              });
      RxUtils.addToCompositeSubscription(mCompositeDisposable, genreListDisposable);
    } else {
      RxUtils.dispose(mMovieListDisposable);

      mMovieListDisposable = mMoviesDataSource.getMovieList(mMoviesFilterType, options)
          .compose(RxUtils.applyIOScheduler())
          .subscribe(movieList -> {
                if (mView != null) {
                  mView.onMovieListFetchSuccess(movieList);
                }
              },
              throwable -> {
                if (mView != null) {
                  mView.onMovieListFetchError(throwable);
                }
              });
      RxUtils.addToCompositeSubscription(mCompositeDisposable, mMovieListDisposable);
    }
  }

  @Override public HomeActivityStore getStore() {
    return homeActivityStore;
  }
}
