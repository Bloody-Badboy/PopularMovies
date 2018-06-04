package me.bloodybadboy.popularmovies.ui.homescreen.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.MovieList;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.ui.details.view.DetailsActivity;
import me.bloodybadboy.popularmovies.ui.helper.OnLoadMoreScrollListener;
import me.bloodybadboy.popularmovies.ui.homescreen.HomeActivityContract;
import me.bloodybadboy.popularmovies.ui.homescreen.adapters.MovieListAdapter;
import me.bloodybadboy.popularmovies.ui.homescreen.decorators.GridSpacingItemDecoration;
import me.bloodybadboy.popularmovies.ui.homescreen.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.ui.homescreen.presenter.HomeActivityPresenter;
import me.bloodybadboy.popularmovies.utils.NetworkUtil;
import me.bloodybadboy.popularmovies.utils.Utils;
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
  private MovieListAdapter mMovieListAdapter;
  private OnLoadMoreScrollListener mOnLoadMoreScrollListener;

  private boolean sortOrderChanged = false;
  private boolean hasMorePages = true;
  private boolean isProgressVisible;
  private int currentPage = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    mUnbinder = ButterKnife.bind(this);
    setSupportActionBar(mToolbar);

    setPresenter(new HomeActivityPresenter(this, Injection.providesDataRepo()));
    mHomeActivityPresenter.onCreate();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mStatusBarBackGround.getLayoutParams().height = getStatusBarHeight();
    } else {
      mStatusBarBackGround.setVisibility(View.GONE);
    }

    final int movieGridLayoutSpanCount = getResources().getInteger(R.integer.movie_list_grid_cols);

    GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, movieGridLayoutSpanCount);

    mMovieListAdapter = new MovieListAdapter(new ArrayList<>());

    mOnLoadMoreScrollListener = new OnLoadMoreScrollListener(mGridLayoutManager) {
      @Override public void onLoadMore() {
        if (hasMorePages) {
          mMovieListAdapter.addLoadingItem();
          loadMoreProducts();
        } else {
          mOnLoadMoreScrollListener.setDataLoaded();
        }
      }
    };

    mMovieListRecycleView.addItemDecoration(new GridSpacingItemDecoration(movieGridLayoutSpanCount,
        (int) getResources().getDimension(R.dimen.dimen_movie_list_grid_item_spacing)));
    mMovieListRecycleView.setLayoutManager(mGridLayoutManager);
    mMovieListRecycleView.setAdapter(mMovieListAdapter);
    mMovieListRecycleView.addOnScrollListener(mOnLoadMoreScrollListener);
    mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (mMovieListAdapter.getItemViewType(position) == MovieListAdapter.VIEW_TYPE_LOADING) {
          return movieGridLayoutSpanCount;
        }
        return 1;
      }
    });
    showProgress();
    if (NetworkUtil.isOnline()) {
      loadMoreProducts();
    } else {
      showSnackBar(getString(R.string.no_internet), this::loadMoreProducts);
    }
  }

  @Override protected void onDestroy() {
    mHomeActivityPresenter.onDestroy();
    mMovieListRecycleView.removeOnScrollListener(mOnLoadMoreScrollListener);
    mUnbinder.unbind();
    super.onDestroy();
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
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.sort_by_rating) {
          RxBus.getInstance().send(Constants.SortOrder.TOP_RATED);
        } else if (item.getItemId() == R.id.sort_by_popularity) {
          RxBus.getInstance().send(Constants.SortOrder.POPULARITY);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onMovieListFetchSuccess(MovieList movieList) {

    hideProgress();

    mMovieListAdapter.removeLoadingItem();

    if (movieList != null) {
      if (currentPage >= movieList.getTotalPages()) {
        hasMorePages = false;
      }
      List<Movie> movies = movieList.getResults();
      if (movies != null && movies.size() > 0) {
        if (sortOrderChanged) {
          sortOrderChanged = false;
          mMovieList.clear();
          mMovieList.addAll(movies);
          mMovieListAdapter.swapMovieListItems(mMovieList);
        } else {
          mMovieList.addAll(movies);
          mMovieListAdapter.updateMovieListItems(mMovieList);
        }
      }
    }

    if (mOnLoadMoreScrollListener.isLoading()) {
      mOnLoadMoreScrollListener.setDataLoaded();
    }
  }

  @Override public void onMovieListFetchError(Throwable throwable) {
    showSnackBar(getString(R.string.something_went_wrong),
        () -> mHomeActivityPresenter.fetchMovieListFromServer(
            Utils.getQueryMapForMovieList(currentPage)));
    /*if (throwable instanceof HttpException) {
    } else if (throwable instanceof SocketTimeoutException) {
    } else if (throwable instanceof IOException) {
    } else {
    }*/
  }

  @Override public void onSortOrderChanged() {
    sortOrderChanged = true;
    showProgress();
    currentPage = 0;
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

  @Override public void setPresenter(HomeActivityContract.Presenter presenter) {
    mHomeActivityPresenter = presenter;
  }

  protected void showSnackBar(String message, @Nullable Runnable runnable) {
    Snackbar snackBar =
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
    snackBar.setAction(R.string.retry, v -> {
      if (runnable != null) {
        runnable.run();
      }
    });

    View sbView = snackBar.getView();
    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackBar.show();
  }

  public int getStatusBarHeight() {
    int result = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = getResources().getDimensionPixelSize(resourceId);
    }
    return result;
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
    if (NetworkUtil.isOnline()) {
      currentPage += 1;
      mHomeActivityPresenter.fetchMovieListFromServer(Utils.getQueryMapForMovieList(currentPage));
    } else {
      showSnackBar(getString(R.string.no_internet), this::loadMoreProducts);
    }
  }
}
