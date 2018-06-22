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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Video;
import me.bloodybadboy.popularmovies.ui.details.adapters.ReviewsAdapter;
import me.bloodybadboy.popularmovies.ui.details.adapters.VideosAdapter;
import me.bloodybadboy.popularmovies.utils.CollectionUtils;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.VIDEO_LIST_EXTRA;

public class VideosFragment extends Fragment {
  @BindView(R.id.rv_reviews)
  RecyclerView mReviewListRecycleView;

  @BindView(R.id.ll_no_videos)
  View mNoVideosView;

  private Unbinder mUnbinder;
  private List<Video> mVideos;

  public VideosFragment() {
  }

  public static VideosFragment newInstance(ArrayList<Video> videos) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(VIDEO_LIST_EXTRA, videos);

    VideosFragment fragment = new VideosFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mVideos = getArguments().getParcelableArrayList(VIDEO_LIST_EXTRA);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_videos, container, false);
    mUnbinder = ButterKnife.bind(this, view);

    Context context = view.getContext();
    mReviewListRecycleView.setHasFixedSize(true);
    mReviewListRecycleView.setLayoutManager(new LinearLayoutManager(context));

    // https://www.themoviedb.org/talk/5451ec02c3a3680245005e3c
    mReviewListRecycleView.setAdapter(new VideosAdapter(mVideos));

    if (mVideos == null || mVideos.isEmpty()) {
      showNoVideos();
    }
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  private void showNoVideos() {
    mReviewListRecycleView.setVisibility(View.INVISIBLE);
    mNoVideosView.setVisibility(View.VISIBLE);
  }
}
