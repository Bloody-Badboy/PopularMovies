package me.bloodybadboy.popularmovies.ui.movies.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.base.BaseActivity;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.ui.details.view.MovieDetailsActivity;
import me.bloodybadboy.popularmovies.ui.helper.OnLoadMoreScrollListener;
import me.bloodybadboy.popularmovies.ui.movies.MoviesContract;
import me.bloodybadboy.popularmovies.ui.movies.adapters.MoviesAdapter;
import me.bloodybadboy.popularmovies.ui.movies.decorators.GridSpacingItemDecoration;
import me.bloodybadboy.popularmovies.ui.movies.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.ui.movies.presenter.MoviesPresenter;
import me.bloodybadboy.popularmovies.utils.NetworkUtil;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.DEFAULT_SORT_BY_ORDER;
import static me.bloodybadboy.popularmovies.Constants.MOVIE_DATA_EXTRA;
import static me.bloodybadboy.popularmovies.Constants.MOVIE_LIST_ITEM_POSITION_EXTRA;
import static me.bloodybadboy.popularmovies.Constants.MoviesFilterType;
import static me.bloodybadboy.popularmovies.Constants.NO_POSITION;

public class MoviesActivity extends BaseActivity<MoviesContract.Presenter, MoviesContract.View>
    implements MoviesContract.View {

  @BindView(R.id.coordinator_layout)
  CoordinatorLayout mCoordinatorLayout;

  @BindView(R.id.status_bar)
  View mStatusBarBackGround;

  @BindView(R.id.rv_movies)
  RecyclerView mMoviesRecycleView;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.layout_loading)
  View mLoadingLayout;

  @BindView(R.id.tv_movies_empty_favourites)
  TextView mEmptyFavourites;

  private GridLayoutManager mGridLayoutManager;
  private int mMovieGridLayoutSpanCount;
  private MoviesAdapter mMoviesAdapter;
  private OnLoadMoreScrollListener mOnLoadMoreScrollListener;
  private MoviesFilterType mCurrentMoviesFilterType = DEFAULT_SORT_BY_ORDER;

  private BroadcastReceiver mRemovedFromFavouritesReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Timber.d("Received intent from launched activity.");

      int position = intent.getIntExtra(Constants.MOVIE_LIST_ITEM_POSITION_EXTRA, NO_POSITION);


      mPresenter.updateItemRemovedFromFavourites(position);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(mToolbar);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mStatusBarBackGround.getLayoutParams().height = Utils.getStatusBarHeight(this);
    } else {
      mStatusBarBackGround.setVisibility(View.GONE);
    }

    mMovieGridLayoutSpanCount = Utils.calculateNoOfColumns(this);

    mGridLayoutManager = new GridLayoutManager(this, mMovieGridLayoutSpanCount);

    mMoviesAdapter = new MoviesAdapter(new ArrayList<>(0));

    initRecycleView();

    LocalBroadcastManager.getInstance(this)
        .registerReceiver(mRemovedFromFavouritesReceiver,
            new IntentFilter(Constants.ACTION_FAVOURITE_ITEM_REMOVE));
  }

  @Override protected void onResume() {
    super.onResume();
    loadMovies();
  }

  private void initRecycleView() {
    mOnLoadMoreScrollListener = new OnLoadMoreScrollListener(mGridLayoutManager) {
      @Override public void onLoadMore() {
        if (NetworkUtil.isOnline()) {
          mPresenter.fetchPaginationMovieList();
        } else {
          showSnackBar(mCoordinatorLayout, getString(R.string.no_internet),
              () -> mPresenter.fetchPaginationMovieList());
        }
      }
    };

    mMoviesRecycleView.addItemDecoration(new GridSpacingItemDecoration(mMovieGridLayoutSpanCount,
        (int) getResources().getDimension(R.dimen.dimen_movie_list_grid_item_spacing)));
    mMoviesRecycleView.setLayoutManager(mGridLayoutManager);
    mMoviesRecycleView.setAdapter(mMoviesAdapter);
    mMoviesRecycleView.addOnScrollListener(mOnLoadMoreScrollListener);
    mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (mMoviesAdapter.getItemViewType(position) == MoviesAdapter.VIEW_TYPE_LOADING) {
          return mMovieGridLayoutSpanCount;
        }
        return 1;
      }
    });
  }

  @NonNull @Override protected MoviesContract.Presenter initPresenter() {
    return new MoviesPresenter(Injection.providesDataRepo());
  }

  @NonNull @Override protected MoviesContract.View provideView() {
    return this;
  }

  @Override protected void onDestroy() {
    mMoviesRecycleView.removeOnScrollListener(mOnLoadMoreScrollListener);
    LocalBroadcastManager.getInstance(this)
        .unregisterReceiver(mRemovedFromFavouritesReceiver);
    super.onDestroy();
  }

  @Override protected int getContentViewResId() {
    return R.layout.activity_movies;
  }

  @NonNull @Override protected Unbinder getViewsUnbinder() {
    return ButterKnife.bind(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);

    MenuItem menuItem = null;
    switch (mCurrentMoviesFilterType) {
      case POPULARITY:
        menuItem = menu.findItem(R.id.sort_by_popularity);
        break;
      case TOP_RATED:
        menuItem = menu.findItem(R.id.sort_by_rating);
        break;
      case FAVOURITES:
        menuItem = menu.findItem(R.id.sort_by_favourites);
        break;
    }
    if (menuItem != null) {
      menuItem.setChecked(true);
    }

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sort_by_popularity:
      case R.id.sort_by_rating:
      case R.id.sort_by_favourites:
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.sort_by_rating) {
          RxBus.getInstance().send(MoviesFilterType.TOP_RATED);
        } else if (item.getItemId() == R.id.sort_by_popularity) {
          RxBus.getInstance().send(MoviesFilterType.POPULARITY);
        } else if (item.getItemId() == R.id.sort_by_favourites) {
          RxBus.getInstance().send(MoviesFilterType.FAVOURITES);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void showProgress() {
    Timber.d("showProgress()");
    mLoadingLayout.setVisibility(View.VISIBLE);
    mMoviesRecycleView.setVisibility(View.INVISIBLE);
  }

  @Override public void hideProgress() {
    Timber.d("hideProgress()");
    mLoadingLayout.setVisibility(View.INVISIBLE);
    mMoviesRecycleView.setVisibility(View.VISIBLE);
  }

  @Override public void notifyDataLoaded() {
    Timber.d("notifyDataLoaded()");
    if (mOnLoadMoreScrollListener.isLoading()) {
      mOnLoadMoreScrollListener.setDataLoaded();
    }
  }

  @Override public void showListLoadingItem() {
    Timber.d("showListLoadingItem()");
    mMoviesAdapter.addLoadingItem();
  }

  @Override public void hideListLoadingItem() {
    Timber.d("hideListLoadingItem()");
    mMoviesAdapter.removeLoadingItem();
  }

  @Override public void updateMoviesAdapterList(List<Movie> movies, boolean shouldSwap) {
    Timber.d("should swap: " + shouldSwap + " Movie object count:" + movies.size());
    if (shouldSwap) {
      mMoviesAdapter.swapMovieListItems(movies);
    } else {
      mMoviesAdapter.updateMovieListItems(movies);
    }
  }

  @Override public void onUnknownError() {
    showSnackBar(mCoordinatorLayout, getString(R.string.unknown_error),
        () -> mPresenter.retryMovieListFetch());
  }

  @Override public void onNetworkError() {
    showSnackBar(mCoordinatorLayout, getString(R.string.something_went_wrong),
        () -> mPresenter.retryMovieListFetch());
  }

  @Override public void onTimeout() {
    showSnackBar(mCoordinatorLayout, getString(R.string.timeout_error),
        () -> mPresenter.retryMovieListFetch());
  }

  @Override public void showEmptyFavourites() {
    mMoviesRecycleView.setVisibility(View.INVISIBLE);
    mEmptyFavourites.setVisibility(View.VISIBLE);
  }

  @Override public void hideEmptyFavourites() {
    mEmptyFavourites.setVisibility(View.INVISIBLE);
    mMoviesRecycleView.setVisibility(View.VISIBLE);
  }

  @Override public void setMovieFilterType(MoviesFilterType movieFilterType) {
    Timber.d("setMovieFilterType()");
    mCurrentMoviesFilterType = movieFilterType;
  }

  @Override public void launchDetailsActivity(DetailsActivityLaunchModel launchModel) {
    Intent intent = new Intent(this, MovieDetailsActivity.class);
    intent.putExtra(MOVIE_DATA_EXTRA, launchModel.getMovie());
    intent.putExtra(MOVIE_LIST_ITEM_POSITION_EXTRA, launchModel.getPosition());
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(this, launchModel.getSharedElements())
            .toBundle();
    startActivity(intent, bundle);
  }

  private void loadMovies() {
    if (NetworkUtil.isOnline()) {
      mPresenter.fetchMovieList();
    } else {
      showSnackBar(mCoordinatorLayout, getString(R.string.no_internet), this::loadMovies);
    }
  }
}
