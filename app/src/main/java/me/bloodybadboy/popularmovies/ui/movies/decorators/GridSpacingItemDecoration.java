package me.bloodybadboy.popularmovies.ui.movies.decorators;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

  private int spanCount;
  private int spacing;

  public GridSpacingItemDecoration(int spanCount, int spacing) {
    this.spanCount = spanCount;
    this.spacing = spacing;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    int position = parent.getChildAdapterPosition(view);

    int column = position % spanCount;

    outRect.left = spacing - column * spacing / spanCount;
    outRect.right = (column + 1) * spacing / spanCount;

    if (position < spanCount) {
      outRect.top = spacing;
    }
    outRect.bottom = spacing;
  }
}

