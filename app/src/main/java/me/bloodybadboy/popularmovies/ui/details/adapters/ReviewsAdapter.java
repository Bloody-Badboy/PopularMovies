package me.bloodybadboy.popularmovies.ui.details.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

  private final List<Review> mReviews;

  public ReviewsAdapter(List<Review> reviews) {
    mReviews = reviews;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.listing_item_review_row_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, mReviews.get(position));
  }

  @Override
  public int getItemCount() {
    return mReviews == null ? 0 : mReviews.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Review review) {
    ViewHolder reviewListItemViewHolder = (ViewHolder) viewHolder;
    if (review != null) {
      reviewListItemViewHolder.bind(review);
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_listing_item_review_by)
    TextView mReviewBy;

    @BindView(R.id.tv_listing_item_review_content)
    TextView mReview;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
    }

    void bind(Review review) {
      mReviewBy.setText(review.getAuthor());
      mReview.setText(review.getContent());

      itemView.setTag(review);
    }
  }
}
