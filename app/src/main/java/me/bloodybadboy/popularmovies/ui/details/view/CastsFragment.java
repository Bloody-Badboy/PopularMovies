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
import me.bloodybadboy.popularmovies.data.model.Cast;
import me.bloodybadboy.popularmovies.ui.details.adapters.CastsAdapter;

import static me.bloodybadboy.popularmovies.Constants.CAST_LIST_EXTRA;

public class CastsFragment extends Fragment {

  @BindView(R.id.rv_casts)
  RecyclerView mCastsRecycleView;

  @BindView(R.id.ll_no_casts)
  View mNoCastsView;

  private Unbinder mUnbinder;
  private List<Cast> mCasts;

  public CastsFragment() {
  }

  public static CastsFragment newInstance(ArrayList<Cast> casts) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(CAST_LIST_EXTRA, casts);

    CastsFragment fragment = new CastsFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mCasts = getArguments().getParcelableArrayList(CAST_LIST_EXTRA);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_casts, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);

    Context context = rootView.getContext();
    mCastsRecycleView.setHasFixedSize(true);
    mCastsRecycleView.setLayoutManager(new LinearLayoutManager(context));
    mCastsRecycleView.setAdapter(new CastsAdapter(mCasts));

    if (mCasts == null || mCasts.isEmpty()) {
      showNoCasts();
    }
    return rootView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  private void showNoCasts() {
    mCastsRecycleView.setVisibility(View.INVISIBLE);
    mNoCastsView.setVisibility(View.VISIBLE);
  }
}
