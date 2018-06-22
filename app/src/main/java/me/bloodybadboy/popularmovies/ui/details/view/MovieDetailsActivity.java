package me.bloodybadboy.popularmovies.ui.details.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import io.reactivex.disposables.CompositeDisposable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.base.BaseActivity;
import me.bloodybadboy.popularmovies.data.model.Cast;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Review;
import me.bloodybadboy.popularmovies.data.model.Video;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.details.MovieDetailsContract;
import me.bloodybadboy.popularmovies.ui.details.presenter.MovieDetailsPresenter;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

import static me.bloodybadboy.popularmovies.Constants.ACTION_FAVOURITE_ITEM_REMOVE;
import static me.bloodybadboy.popularmovies.Constants.MOVIE_DATA_EXTRA;
import static me.bloodybadboy.popularmovies.Constants.MOVIE_LIST_ITEM_POSITION_EXTRA;
import static me.bloodybadboy.popularmovies.Constants.NO_POSITION;

public class MovieDetailsActivity
    extends BaseActivity<MovieDetailsContract.Presenter, MovieDetailsContract.View>
    implements MovieDetailsContract.View {

  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
      new AccelerateInterpolator();
  private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

  private boolean isDestroyedBySystem = false;

  @BindView(R.id.coordinator_layout)
  CoordinatorLayout mCoordinatorLayout;

  @BindView(R.id.tv_movie_details_title)
  TextView mMovieTitle;

  @BindView(R.id.tv_movie_details_genres)
  TextView mMovieGenres;

  @BindView(R.id.tv_movie_details_release_date)
  TextView mReleaseDate;

  @BindView(R.id.tv_movie_details_rating)
  TextView mMovieRating;

  @BindView(R.id.rb_movie_details_rate_bar)
  RatingBar mMovieRateBar;

  @BindView(R.id.tv_movie_details_plot_explanation)
  TextView mPlotExplanation;

  @BindView(R.id.iv_movie_details_poster)
  ImageView mMoviePoster;

  @BindView(R.id.iv_movie_details_backdrop)
  ImageView mMovieBackdrop;

  @BindView(R.id.fab_movie_details_add_to_favourites)
  FloatingActionButton mFabAddToFavourites;

  @BindView(R.id.pb_movie_details_loading)
  ProgressBar mLoadingProgress;

  @BindView(R.id.ll_movie_details_tabs_container)
  View mTabsContainer;

  @BindView(R.id.tl_movie_details_tabs)
  TabLayout mTabLayout;

  @BindView(R.id.vp_movie_details_pages)
  ViewPager mViewPager;

  private Movie mMovie;
  private int mPosition = NO_POSITION;

  private boolean isFavourite = false;
  private boolean wasInFavourite = false;

  CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(findViewById(R.id.toolbar));

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    Intent intent = getIntent();

    if (intent == null ||
        !intent.hasExtra(MOVIE_DATA_EXTRA) ||
        !intent.hasExtra(MOVIE_LIST_ITEM_POSITION_EXTRA)) {
      finishWithError();
      return;
    }
    mMovie = intent.getParcelableExtra(MOVIE_DATA_EXTRA);
    mPosition = intent.getIntExtra(MOVIE_LIST_ITEM_POSITION_EXTRA, NO_POSITION);
    if (mMovie == null) {
      finishWithError();
      return;
    }

    updateUI();
  }

  @Override protected void onResume() {
    super.onResume();
    isDestroyedBySystem = false;
    mPresenter.fetchMovieDetails(String.valueOf(mMovie.getMovieId()));
    mPresenter.checkMovieInFavourites(mMovie.getMovieId());
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    isDestroyedBySystem = true;
  }

  @Override protected void onDestroy() {
    compositeDisposable.dispose();
    if (!isDestroyedBySystem) {
      Intent intent = new Intent(ACTION_FAVOURITE_ITEM_REMOVE);
      intent.putExtra(MOVIE_LIST_ITEM_POSITION_EXTRA,
          (wasInFavourite && !isFavourite) ? mPosition : NO_POSITION);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      Timber.d("Send broadcast to parent activity.");
    }
    super.onDestroy();
  }

  @Override protected int getContentViewResId() {
    return R.layout.activity_movie_details;
  }

  @NonNull @Override protected Unbinder getViewsUnbinder() {
    return ButterKnife.bind(this);
  }

  @NonNull @Override protected MovieDetailsContract.Presenter initPresenter() {
    return new MovieDetailsPresenter(this, Injection.providesDataRepo());
  }

  @NonNull @Override protected MovieDetailsContract.View provideView() {
    return this;
  }

  private void finishWithError() {
    Toast.makeText(this, R.string.error_no_intent_data, Toast.LENGTH_LONG).show();
    finish();
  }

  private void updateUI() {

    String title = mMovie.getTitle();

    mMovieTitle.setText(title);

    mMovieTitle.setSelected(true);

    mMovieGenres.setText(
        Utils.getDisplayableGenreList(MovieGenreStore.getInstance().get(), mMovie.getGenreIds()));
    mMovieGenres.setSelected(true);

    if (mMovie.getReleaseDate() != null) {
      try {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(mMovie.getReleaseDate());
        mReleaseDate.setText(Utils.getDisplayableReadableDate(date));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    mMovieRateBar.setRating((float) (mMovie.getVoteAverage() / 2));
    mMovieRating.setText(Html.fromHtml(
        getString(R.string.about_title, new DecimalFormat("0.0").format(mMovie.getVoteAverage()))));

    mPlotExplanation.setText(mMovie.getOverview());

    String posterUrl = Utils.getPosterUrl(mMovie.getPosterPath());
    String backDropUrl = Utils.getBackdropUrl(mMovie.getBackdropPath());

    Timber.d("Poster URL:%s", posterUrl);

    Timber.d("Backdrop URL:%s", backDropUrl);

    RequestOptions options = new RequestOptions()
        .placeholder(R.drawable.poster_placeholder)
        .error(R.drawable.poster_placeholder);

    Glide.with(this).load(posterUrl)
        .transition(new DrawableTransitionOptions().crossFade())
        .apply(options)
        .into(mMoviePoster);

    options = new RequestOptions()
        .placeholder(R.drawable.backdrop_placeholder)
        .error(R.drawable.backdrop_placeholder);

    Glide.with(this).load(backDropUrl)
        .transition(new DrawableTransitionOptions().crossFade())
        .apply(options)
        .into(mMovieBackdrop);
  }

  @Override public void setIsMovieInFavourites(boolean isFavourite) {
    this.isFavourite = isFavourite;
    if (isFavourite) {
      mFabAddToFavourites.setImageResource(R.drawable.ic_like_active);
    } else {
      mFabAddToFavourites.setImageResource(R.drawable.ic_like_inactive);
    }
  }

  @Override public void onMovieDetailsFetchSuccess(ExtendedMovieDetails movieDetails) {
    if (movieDetails != null) {
      SectionsPagerAdapter mSectionsPagerAdapter =
          new SectionsPagerAdapter(getSupportFragmentManager(), movieDetails);
      mViewPager.setAdapter(mSectionsPagerAdapter);
    }
  }

  @Override public void onUnknownError() {
    showSnackBar(mCoordinatorLayout, getString(R.string.unknown_error),
        () -> mPresenter.fetchMovieDetails(String.valueOf(mMovie.getMovieId())));
  }

  @Override public void onNetworkError() {
    showSnackBar(mCoordinatorLayout, getString(R.string.something_went_wrong),
        () -> mPresenter.fetchMovieDetails(String.valueOf(mMovie.getMovieId())));
  }

  @Override public void onTimeout() {
    showSnackBar(mCoordinatorLayout, getString(R.string.timeout_error),
        () -> mPresenter.fetchMovieDetails(String.valueOf(mMovie.getMovieId())));
  }

  @Override public void onMovieInFavouritesCheckSuccess(boolean isFavourite) {
    this.isFavourite = isFavourite;
    if (isFavourite) {
      wasInFavourite = true;
      mFabAddToFavourites.setImageResource(R.drawable.ic_like_active);
    }
  }

  @Override public void onMovieInFavouritesCheckError(Throwable throwable) {
    Toast.makeText(this, "Query Failed: " + throwable.getMessage(), Toast.LENGTH_SHORT)
        .show();
  }

  @Override public void onAddMovieToFavouritesSuccess() {
    showSnackBar(mCoordinatorLayout, "Added to favourites!", null, Snackbar.LENGTH_SHORT);
  }

  @Override public void onAddMovieToFavouritesError(Throwable throwable) {
    Toast.makeText(this, "Insert Failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    isFavourite = !isFavourite;
    playLikeAnimation(isFavourite);
  }

  @Override public void onRemoveMovieFromFavouritesSuccess() {
    showSnackBar(mCoordinatorLayout, "Removed from favourites!", null, Snackbar.LENGTH_SHORT);
  }

  @Override public void onRemoveMovieFromFavouritesError(Throwable throwable) {
    Toast.makeText(this, "Delete Failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    isFavourite = !isFavourite;
    playLikeAnimation(isFavourite);
  }

  @Override public void launchVideoOnYoutube(String videoId) {
    if (videoId == null) {
      showSnackBar(mCoordinatorLayout, "Can't play the video.", null, Snackbar.LENGTH_SHORT);
    } else {
      Intent youtubeAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
      if (Utils.canPerformIntent(this, youtubeAppIntent)) {
        startActivity(youtubeAppIntent);
      } else {
        Intent browserIntent =
            new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        startActivity(browserIntent);
      }
    }
  }

  @OnClick(R.id.fab_movie_details_add_to_favourites) void onAddToFavouritesClick() {
    if (!isFavourite) {
      isFavourite = true;
      mPresenter.addMovieToFavourites(mMovie);
    } else {
      isFavourite = false;
      mPresenter.removeMovieFromFavourites(mMovie.getMovieId());
    }
    playLikeAnimation(isFavourite);
  }

  @Override public void showProgress() {
    mTabsContainer.setVisibility(View.INVISIBLE);
    mLoadingProgress.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    mTabsContainer.setVisibility(View.VISIBLE);
    mLoadingProgress.setVisibility(View.INVISIBLE);
  }

  void playLikeAnimation(final boolean isAddedToFavourites) {

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(mFabAddToFavourites, "rotation", 0f, 360f);
    rotationAnim.setDuration(300);
    rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(mFabAddToFavourites, "scaleX", 0.2f, 1f);
    bounceAnimX.setDuration(300);
    bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

    ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(mFabAddToFavourites, "scaleY", 0.2f, 1f);
    bounceAnimY.setDuration(300);
    bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);

    bounceAnimY.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        if (isAddedToFavourites) {
          mFabAddToFavourites.setImageResource(R.drawable.ic_like_active);
        } else {
          mFabAddToFavourites.setImageResource(R.drawable.ic_like_inactive);
        }
      }
    });
    animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
    animatorSet.start();
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ExtendedMovieDetails movieDetails;

    SectionsPagerAdapter(FragmentManager manager, ExtendedMovieDetails movieDetails) {
      super(manager);
      this.movieDetails = movieDetails;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return InfoFragment.newInstance(movieDetails);
        case 1:
          return CastsFragment.newInstance(
              (ArrayList<Cast>) movieDetails.getCredits().getCast());
        case 2:
          return VideosFragment.newInstance(
              (ArrayList<Video>) movieDetails.getMovieVideos().getResults());
        case 3:
          return ReviewsFragment.newInstance(
              (ArrayList<Review>) movieDetails.getMovieReviews().getReviews());
      }
      return null;
    }

    @Override
    public int getCount() {
      return 4;
    }
  }
}
