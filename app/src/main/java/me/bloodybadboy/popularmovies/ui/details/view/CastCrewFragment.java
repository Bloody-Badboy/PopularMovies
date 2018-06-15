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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Review;
import me.bloodybadboy.popularmovies.ui.details.adapters.ReviewsAdapter;

import static me.bloodybadboy.popularmovies.Constants.REVIEW_LIST_EXTRA;

public class CastCrewFragment extends Fragment {
  @BindView(R.id.rv_reviews)
  RecyclerView mReviewListRecycleView;

  @BindView(R.id.tv_no_reviews)
  TextView mTvNoReviews;

  private Unbinder mUnbinder;
  private List<Review> mReviews;

  public CastCrewFragment() {
  }

  public static CastCrewFragment newInstance(ArrayList<Review> reviews) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(REVIEW_LIST_EXTRA, reviews);

    CastCrewFragment fragment = new CastCrewFragment();
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
    mReviewListRecycleView.setLayoutManager(new LinearLayoutManager(context));
    mReviewListRecycleView.setAdapter(new ReviewsAdapter(mReviews));

    if (mReviews == null || mReviews.isEmpty()) {
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
    mReviewListRecycleView.setVisibility(View.INVISIBLE);
    mTvNoReviews.setVisibility(View.VISIBLE);
  }
}
