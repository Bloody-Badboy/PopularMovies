package me.bloodybadboy.popularmovies.ui.homescreen.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.ui.details.view.DetailsActivity;
import me.bloodybadboy.popularmovies.ui.helper.OnLoadMoreScrollListener;
import me.bloodybadboy.popularmovies.ui.homescreen.HomeActivityContract;
import me.bloodybadboy.popularmovies.ui.homescreen.adapters.MoviesAdapter;
import me.bloodybadboy.popularmovies.ui.homescreen.decorators.GridSpacingItemDecoration;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.ui.homescreen.presenter.HomeActivityPresenter;
import me.bloodybadboy.popularmovies.utils.NetworkUtil;
import me.bloodybadboy.popularmovies.utils.Utils;
import retrofit2.HttpException;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements HomeActivityContract.View {

  @BindView(R.id.coordinator_layout)
  CoordinatorLayout mCoordinatorLayout;

  @BindView(R.id.status_bar)
  View mStatusBarBackGround;

  @BindView(R.id.rv_movie_list)
  RecyclerView mMovieListRecycleView;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.layout_loading)
  View mLoadingLayout;

  private HomeActivityContract.Presenter mHomeActivityPresenter;
  private Unbinder mUnbinder;

  private List<Movie> mMovieList = new ArrayList<>();
  private GridLayoutManager mGridLayoutManager;
  private int mMovieGridLayoutSpanCount;
  private MoviesAdapter mMoviesAdapter;
  private OnLoadMoreScrollListener mOnLoadMoreScrollListener;
  private Constants.MoviesFilterType mMoviesFilterType = Constants.DEFAULT_SORT_BY_ORDER;

  private boolean shouldSwapMovieList = false;
  private boolean hasMorePages = true;
  private boolean isProgressVisible = true;
  private int currentPage = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    mUnbinder = ButterKnife.bind(this);
    setSupportActionBar(mToolbar);

    attachPresenter();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mStatusBarBackGround.getLayoutParams().height = Utils.getStatusBarHeight(this);
    } else {
      mStatusBarBackGround.setVisibility(View.GONE);
    }

    mMovieGridLayoutSpanCount = Utils.calculateNoOfColumns(this);

    mGridLayoutManager = new GridLayoutManager(this, mMovieGridLayoutSpanCount);

    mMoviesAdapter = new MoviesAdapter(new ArrayList<>());

    initRecycleView();

    if (savedInstanceState != null) {
      hideProgress();
      List<Movie> movies = savedInstanceState.getParcelableArrayList("movieList");
      if (movies != null) {
        mMovieList.addAll(movies);
      }
      mMoviesAdapter.swapMovieListItems(mMovieList);
      mMovieListRecycleView.smoothScrollToPosition(savedInstanceState.getInt("scrollPosition"));
      mMoviesFilterType = (Constants.MoviesFilterType) savedInstanceState.getSerializable("sortByOrder");
    } else {
      requestForMovieList();
    }
  }

  private void attachPresenter() {
    mHomeActivityPresenter =
        (HomeActivityContract.Presenter) getLastCustomNonConfigurationInstance();
    if (mHomeActivityPresenter == null) {
      mHomeActivityPresenter = new HomeActivityPresenter(Injection.providesDataRepo());
    }
    mHomeActivityPresenter.attachView(this);
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return mHomeActivityPresenter;
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    mHomeActivityPresenter.getStore().mMovieList.addAll(mMovieList);
    if (!mMovieList.isEmpty()) {
      outState.putParcelableArrayList("movieList", (ArrayList<Movie>) mMovieList);
    }
    outState.putInt("scrollPosition",
        ((GridLayoutManager) mMovieListRecycleView.getLayoutManager()).findFirstVisibleItemPosition());
    outState.putSerializable("sortByOrder", mMoviesFilterType);
    super.onSaveInstanceState(outState);
  }

  @Override protected void onResume() {
    super.onResume();
    mHomeActivityPresenter.subscribe();
  }

  @Override protected void onPause() {
    super.onPause();
    mHomeActivityPresenter.unsubscribe();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mMovieListRecycleView.removeOnScrollListener(mOnLoadMoreScrollListener);
    mUnbinder.unbind();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    Timber.d((String) item.getTitle());
    switch (item.getItemId()) {
      case R.id.sort_by_popularity:
      case R.id.sort_by_rating:
      case R.id.sort_by_favourites:
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.sort_by_rating) {
          RxBus.getInstance().send(Constants.MoviesFilterType.TOP_RATED);
        } else if (item.getItemId() == R.id.sort_by_popularity) {
          RxBus.getInstance().send(Constants.MoviesFilterType.POPULARITY);
        } else if (item.getItemId() == R.id.sort_by_favourites) {
          RxBus.getInstance().send(Constants.MoviesFilterType.FAVOURITES);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  @Override
  public void onMovieListFetchSuccess(Movies movieList) {
    Timber.d(movieList.toString());
    hideProgress();

    mMoviesAdapter.removeLoadingItem();

    if (movieList != null) {
      if (currentPage >= movieList.getTotalPages()) {
        hasMorePages = false;
      }
      List<Movie> movies = movieList.getResults();
      if (movies != null && movies.size() > 0) {
        if (shouldSwapMovieList) {
          shouldSwapMovieList = false;
          mMovieList.clear();
          mMovieList.addAll(movies);
          mMoviesAdapter.swapMovieListItems(mMovieList);
        } else {
          mMovieList.addAll(movies);
          mMoviesAdapter.updateMovieListItems(mMovieList);
        }
      }
    }

    if (mOnLoadMoreScrollListener.isLoading()) {
      mOnLoadMoreScrollListener.setDataLoaded();
    }
  }

  @Override public void onMovieListFetchError(Throwable throwable) {
    Timber.e(throwable);
    showSnackBar(getString(R.string.something_went_wrong),
        () -> mHomeActivityPresenter.fetchMovieListFromDataLayer(
            Utils.getQueryMapForMovieList(currentPage)));
    if (throwable instanceof HttpException) {
    } else if (throwable instanceof SocketTimeoutException) {
    } else if (throwable instanceof IOException) {
    } else {
    }
  }

  @Override public void onSortOrderChanged(Constants.MoviesFilterType moviesFilterType) {
    mMoviesFilterType = moviesFilterType;
    currentPage = 0;
    hasMorePages = true;
    shouldSwapMovieList = true;
    showProgress();
    loadMoreProducts();
  }

  @Override public void launchDetailsActivity(DetailsActivityLaunchModel launchModel) {
    Intent intent = new Intent(this, DetailsActivity.class);
    intent.putExtra(Constants.MOVIE_DATA_EXTRA, launchModel.getMovie());
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(this, launchModel.getSharedElements())
            .toBundle();
    startActivity(intent, bundle);
  }

  private void initRecycleView() {
    mOnLoadMoreScrollListener = new OnLoadMoreScrollListener(mGridLayoutManager) {
      @Override public void onLoadMore() {
        if (hasMorePages) {
          mMoviesAdapter.addLoadingItem();
          loadMoreProducts();
        } else {
          mOnLoadMoreScrollListener.setDataLoaded();
        }
      }
    };

    mMovieListRecycleView.addItemDecoration(new GridSpacingItemDecoration(mMovieGridLayoutSpanCount,
        (int) getResources().getDimension(R.dimen.dimen_movie_list_grid_item_spacing)));
    mMovieListRecycleView.setLayoutManager(mGridLayoutManager);
    mMovieListRecycleView.setAdapter(mMoviesAdapter);
    mMovieListRecycleView.addOnScrollListener(mOnLoadMoreScrollListener);
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

  private void requestForMovieList() {
    if (NetworkUtil.isOnline()) {
      Timber.d("requestForMovieList()");
      showProgress();
      loadMoreProducts();
    } else {
      showSnackBar(getString(R.string.no_internet), this::requestForMovieList);
    }
  }

  protected void showSnackBar(@NonNull String message, @Nullable Runnable runnable) {
    Snackbar snackBar =
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
    snackBar.setAction(R.string.retry, __ -> {
      if (runnable != null) {
        runnable.run();
      }
    });

    View sbView = snackBar.getView();
    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackBar.show();
  }

  private void showProgress() {
    if (!isProgressVisible) {
      isProgressVisible = true;
      mLoadingLayout.setVisibility(View.VISIBLE);
      mMovieListRecycleView.setVisibility(View.INVISIBLE);
    }
  }

  private void hideProgress() {
    if (isProgressVisible) {
      isProgressVisible = false;
      mLoadingLayout.setVisibility(View.INVISIBLE);
      mMovieListRecycleView.setVisibility(View.VISIBLE);
    }
  }

  private void loadMoreProducts() {
    Timber.d("loadMoreProducts()");
    if (NetworkUtil.isOnline()) {
      currentPage += 1;
      mHomeActivityPresenter.fetchMovieListFromDataLayer(
          Utils.getQueryMapForMovieList(currentPage));
    } else {
      showSnackBar(getString(R.string.no_internet), this::loadMoreProducts);
    }
  }

  @Override public void setPresenter(HomeActivityContract.Presenter presenter) {
    // used only in fragment
  }
}
