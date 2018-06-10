package me.bloodybadboy.popularmovies.ui.homescreen.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Movies;
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

public class HomeActivityFragment extends Fragment implements HomeActivityContract.View {

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
  private int mMovieGridLayoutSpanCount;
  private MovieListAdapter mMovieListAdapter;
  GridLayoutManager mGridLayoutManager;
  private OnLoadMoreScrollListener mOnLoadMoreScrollListener;

  private boolean hasChangedSortOrder = false;
  private boolean hasMorePages = true;
  private boolean isProgressVisible;
  private int currentPage = 0;

  public static HomeActivityFragment newInstance() {
    return new HomeActivityFragment();
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);

    initToolbar();
    initRecycleView();

    setHasOptionsMenu(true);

    requestForMovieList();

    return rootView;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMovieListAdapter = new MovieListAdapter(new ArrayList<>(0));

    mMovieGridLayoutSpanCount = getResources().getInteger(R.integer.movie_list_grid_cols);
    mGridLayoutManager = new GridLayoutManager(getContext(), mMovieGridLayoutSpanCount);

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
  }

  @Override public void onStart() {
    super.onStart();
    mHomeActivityPresenter.onStart();
  }

  @Override public void onStop() {
    super.onStop();
    mHomeActivityPresenter.onStop();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mMovieListRecycleView.removeOnScrollListener(mOnLoadMoreScrollListener);
    mHomeActivityPresenter.clearCompositeSubscription();

    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    Timber.d((String) item.getTitle());
    switch (item.getItemId()) {
      case R.id.sort_by_popularity:
      case R.id.sort_by_rating:
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.sort_by_rating) {
          RxBus.getInstance().send(Constants.SortByOrder.TOP_RATED);
        } else if (item.getItemId() == R.id.sort_by_popularity) {
          RxBus.getInstance().send(Constants.SortByOrder.POPULARITY);
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onMovieListFetchSuccess(Movies movies) {
    hideProgress();

    mMovieListAdapter.removeLoadingItem();

    if (movies != null) {
      if (currentPage >= movies.getTotalPages()) {
        hasMorePages = false;
      }
      List<Movie> movieList = movies.getResults();
      if (movieList != null && movieList.size() > 0) {
        if (hasChangedSortOrder) {
          hasChangedSortOrder = false;
          mMovieList.clear();
          mMovieList.addAll(movieList);
          mMovieListAdapter.swapMovieListItems(mMovieList);
        } else {
          mMovieList.addAll(movieList);
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
        () -> mHomeActivityPresenter.fetchMovieListDataLayer(
            Utils.getQueryMapForMovieList(currentPage)));
    /*if (throwable instanceof HttpException) {
    } else if (throwable instanceof SocketTimeoutException) {
    } else if (throwable instanceof IOException) {
    } else {
    }*/
  }

  @Override public void onSortOrderChanged() {
    hasChangedSortOrder = true;
    showProgress();
    currentPage = 0;
    loadMoreProducts();
  }

  @Override public void launchDetailsActivity(DetailsActivityLaunchModel launchModel) {
    Intent intent = new Intent(getContext(), DetailsActivity.class);
    intent.putExtra(Constants.MOVIE_DATA_EXTRA, launchModel.getMovie());
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()),
            launchModel.getSharedElements())
            .toBundle();
    startActivity(intent, bundle);
  }

  @Override public void setPresenter(HomeActivityContract.Presenter presenter) {
    mHomeActivityPresenter = presenter;
  }

  private void initToolbar() {
    AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
    if (appCompatActivity == null) {
      return;
    }
    appCompatActivity.setSupportActionBar(mToolbar);
    setPresenter(new HomeActivityPresenter(this, Injection.providesDataRepo()));

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mStatusBarBackGround.getLayoutParams().height = Utils.getStatusBarHeight(appCompatActivity);
    } else {
      mStatusBarBackGround.setVisibility(View.GONE);
    }
  }

  private void initRecycleView() {
    mMovieListRecycleView.addItemDecoration(new GridSpacingItemDecoration(mMovieGridLayoutSpanCount,
        (int) getResources().getDimension(R.dimen.dimen_movie_list_grid_item_spacing)));
    mMovieListRecycleView.setLayoutManager(mGridLayoutManager);
    mMovieListRecycleView.setAdapter(mMovieListAdapter);
    mMovieListRecycleView.addOnScrollListener(mOnLoadMoreScrollListener);
    mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (mMovieListAdapter.getItemViewType(position) == MovieListAdapter.VIEW_TYPE_LOADING) {
          return mMovieGridLayoutSpanCount;
        }
        return 1;
      }
    });
  }

  private void requestForMovieList() {
    if (NetworkUtil.isOnline()) {
      showProgress();
      loadMoreProducts();
    } else {
      showSnackBar(getString(R.string.no_internet), this::requestForMovieList);
    }
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
    textView.setTextColor(ContextCompat.getColor(sbView.getContext(), android.R.color.white));
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
    if (NetworkUtil.isOnline()) {
      currentPage += 1;
      mHomeActivityPresenter.fetchMovieListDataLayer(Utils.getQueryMapForMovieList(currentPage));
    } else {
      showSnackBar(getString(R.string.no_internet), this::loadMoreProducts);
    }
  }
}
