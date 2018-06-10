package me.bloodybadboy.popularmovies.ui.homescreen.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.Genre;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.homescreen.HomeActivityContract;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.utils.RxUtils;
import me.bloodybadboy.popularmovies.utils.Utils;

import static me.bloodybadboy.popularmovies.Constants.SortByOrder;

public class HomeActivityPresenter implements HomeActivityContract.Presenter {

  private HomeActivityContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;
  private Disposable mRxBusDisposable;

  private Disposable mMovieListDisposable;

  private SortByOrder mSortByOrder = SortByOrder.POPULARITY;

  private boolean isGenreListLoaded = false;

  public HomeActivityPresenter(HomeActivityContract.View view, MoviesDataSource moviesDataSource) {
    mView = view;
    mMoviesDataSource = moviesDataSource;
  }

  @Override public void onStart() {
    mCompositeDisposable = new CompositeDisposable();
    mRxBusDisposable = new CompositeDisposable();

    mRxBusDisposable = RxBus.getInstance().toObservable().subscribe(o -> {
      if (o instanceof DetailsActivityLaunchModel) {
        DetailsActivityLaunchModel launchModel = (DetailsActivityLaunchModel) o;
        mView.launchDetailsActivity(launchModel);
      } else if (o instanceof SortByOrder) {
        SortByOrder sortByOrder = (SortByOrder) o;
        if (mSortByOrder != sortByOrder) {
          mSortByOrder = sortByOrder;
          mView.onSortOrderChanged();
        }
      }
    });
  }

  @Override public void onStop() {
    RxUtils.dispose(mRxBusDisposable);
  }

  @Override
  public void fetchMovieListDataLayer(Map<String, String> options) {
    if (!isGenreListLoaded) {
      Disposable genreListDisposable = mMoviesDataSource.getMovieGenreList()
          .compose(RxUtils.applyIOScheduler())
          .subscribe(
              genreList -> {
                isGenreListLoaded = true;
                if (genreList != null) {
                  List<Genre> genres = genreList.getGenres();
                  if (genres != null && genres.size() > 0) {
                    MovieGenreStore.getInstance().store(Utils.getGenresMap(genreList.getGenres()));
                  }
                }
                fetchMovieListDataLayer(options);
              }, throwable -> mView.onMovieListFetchError(throwable));
      RxUtils.addToCompositeSubscription(mCompositeDisposable, genreListDisposable);
    } else {
      RxUtils.dispose(mMovieListDisposable);

      mMovieListDisposable = mMoviesDataSource.getMovieList(mSortByOrder, options)
          .compose(RxUtils.applyIOScheduler())
          .subscribe(movieList -> mView.onMovieListFetchSuccess(movieList),
              throwable -> mView.onMovieListFetchError(throwable));
      RxUtils.addToCompositeSubscription(mCompositeDisposable, mMovieListDisposable);
    }
  }

  @Override public void clearCompositeSubscription() {
    RxUtils.clear(mCompositeDisposable);
  }
}
