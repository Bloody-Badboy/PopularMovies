package me.bloodybadboy.popularmovies.ui.homescreen.presenter;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import java.util.Map;
import me.bloodybadboy.popularmovies.data.model.Genre;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import me.bloodybadboy.popularmovies.data.source.MoviesDataSource;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.homescreen.HomeActivityContract;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.utils.RxUtils;
import me.bloodybadboy.popularmovies.utils.Utils;

import static me.bloodybadboy.popularmovies.Constants.SortOrder;

public class HomeActivityPresenter implements HomeActivityContract.Presenter {

  private HomeActivityContract.View mView;
  private MoviesDataSource mMoviesDataSource;
  private CompositeDisposable mCompositeDisposable;

  private Disposable mMovieListDisposable;

  private SortOrder mSortOrder = SortOrder.POPULARITY;

  private boolean isGenreListLoaded = false;

  public HomeActivityPresenter(HomeActivityContract.View view, MoviesDataSource moviesDataSource) {
    mView = view;
    mMoviesDataSource = moviesDataSource;
  }

  @Override public void onCreate() {
    mCompositeDisposable = new CompositeDisposable();
    mCompositeDisposable.add(RxBus.getInstance().toObservable().subscribe(o -> {
      if (o instanceof DetailsActivityLaunchModel) {
        DetailsActivityLaunchModel launchModel = (DetailsActivityLaunchModel) o;
        mView.launchDetailsActivity(launchModel);
      } else if (o instanceof SortOrder) {
        SortOrder sortOrder = (SortOrder) o;
        if (mSortOrder != sortOrder) {
          mSortOrder = sortOrder;
          mView.onSortOrderChanged();
        }
      }
    }));
  }

  @Override public void onDestroy() {
    RxUtils.dispose(mCompositeDisposable);
  }

  @Override
  public void fetchMovieListFromServer(Map<String, String> options) {
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
                fetchMovieListFromServer(options);
              }, throwable -> mView.onMovieListFetchError(throwable));
      RxUtils.addToCompositeSubscription(mCompositeDisposable, genreListDisposable);
    } else {
      RxUtils.dispose(mMovieListDisposable);

      Single<MovieList> movieListSingle;
      if (mSortOrder == SortOrder.POPULARITY) {
        movieListSingle = mMoviesDataSource.getPopularMovieList(options);
      } else {
        movieListSingle = mMoviesDataSource.getTopRatedMovieList(options);
      }

      mMovieListDisposable = movieListSingle
          .compose(RxUtils.applyIOScheduler())
          .subscribe(movieList -> mView.onMovieListFetchSuccess(movieList),
              throwable -> mView.onMovieListFetchError(throwable));
      RxUtils.addToCompositeSubscription(mCompositeDisposable, mMovieListDisposable);
    }
  }
}
