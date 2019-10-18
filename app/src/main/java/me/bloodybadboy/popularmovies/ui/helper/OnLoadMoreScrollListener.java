package me.bloodybadboy.popularmovies.ui.helper;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

  private int mVisibleThreshold = 2;
  private boolean isLoading = false;
  private GridLayoutManager mGridLayoutManager;

  public OnLoadMoreScrollListener(GridLayoutManager layoutManager) {
    this.mGridLayoutManager = layoutManager;
    mVisibleThreshold *= layoutManager.getSpanCount();
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void setDataLoaded() {
    this.isLoading = false;
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
  }

  @Override
  public void onScrolled(RecyclerView view, int dx, int dy) {
    int mLastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
    int mTotalItemCount = mGridLayoutManager.getItemCount();

    if (!isLoading && (mLastVisibleItemPosition + mVisibleThreshold) > mTotalItemCount) {
      isLoading = true;
      onLoadMore();
    }
  }

  public abstract void onLoadMore();
}