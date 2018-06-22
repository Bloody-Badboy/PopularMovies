package me.bloodybadboy.popularmovies.ui.details.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Review;
import me.bloodybadboy.popularmovies.ui.details.adapters.ReviewsAdapter;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.REVIEW_LIST_EXTRA;

public class ReviewsFragment extends Fragment {
  @BindView(R.id.rv_reviews)
  RecyclerView mReviewsRecycleView;

  @BindView(R.id.ll_no_reviews)
  View mNoReviewsView;

  private Unbinder mUnbinder;
  private List<Review> mReviews;

  public ReviewsFragment() {
  }

  public static ReviewsFragment newInstance(ArrayList<Review> reviews) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(REVIEW_LIST_EXTRA, reviews);

    ReviewsFragment fragment = new ReviewsFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mReviews = getArguments().getParcelableArrayList(REVIEW_LIST_EXTRA);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reviews, container, false);
    mUnbinder = ButterKnife.bind(this, view);

    Context context = view.getContext();
    mReviewsRecycleView.setHasFixedSize(true);
    mReviewsRecycleView.setLayoutManager(new LinearLayoutManager(context));
    mReviewsRecycleView.setAdapter(new ReviewsAdapter(mReviews));

    Timber.d(mReviews.toString());

    if (mReviews == null || mReviews.isEmpty()) {
      Timber.d("no reviews");
      showNoReviews();
    }
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  private void showNoReviews() {
    mReviewsRecycleView.setVisibility(View.INVISIBLE);
    mNoReviewsView.setVisibility(View.VISIBLE);
  }
}
