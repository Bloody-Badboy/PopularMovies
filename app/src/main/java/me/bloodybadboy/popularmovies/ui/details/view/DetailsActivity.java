package me.bloodybadboy.popularmovies.ui.details.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.details.DetailsActivityContract;
import me.bloodybadboy.popularmovies.ui.details.presenter.DetailsActivityPresenter;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityContract.View {

  private static final DecelerateInterpolator DECELERATE_INTERPOLATOR =
      new DecelerateInterpolator();
  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
      new AccelerateInterpolator();
  private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

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

  private DetailsActivityContract.Presenter mDetailsActivityPresenter;
  private Unbinder mUnbinder;
  private boolean added = true;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details_v2);
    mUnbinder = ButterKnife.bind(this);

    mDetailsActivityPresenter = new DetailsActivityPresenter(this, Injection.providesDataRepo());

    SectionsPagerAdapter mSectionsPagerAdapter =
        new SectionsPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mSectionsPagerAdapter);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    if (getIntent().hasExtra(Constants.MOVIE_DATA_EXTRA)) {
      Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_DATA_EXTRA);
      if (movie != null) {
        updateUI(movie);
        showProgress();
        mDetailsActivityPresenter.fetchMovieDetailsFromServer(String.valueOf(movie.getMovieId()));
      }
    }
  }

  @Override protected void onStart() {
    super.onStart();
    mDetailsActivityPresenter.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
    mDetailsActivityPresenter.onStop();
  }

  @Override protected void onDestroy() {
    mUnbinder.unbind();
    super.onDestroy();
  }

  private void updateUI(Movie movie) {
    String originalTitle = movie.getOriginalTitle();
    String title = movie.getTitle();

    if (originalTitle.equalsIgnoreCase(title)) {
      mMovieTitle.setText(title);
    } else {
      mMovieTitle.setText(
          getString(R.string.details_activity_movie_title_format, title, originalTitle));
    }
    mMovieTitle.setSelected(true);

    Map<String, String> genresMap = MovieGenreStore.getInstance().getGenresMap();
    if (genresMap != null) {
      mMovieGenres.setText(
          Utils.getDisplayableGenreList(genresMap, movie.getGenreIds()));
      mMovieGenres.setSelected(true);
    }

    if (movie.getReleaseDate() != null) {
      try {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.getReleaseDate());
        mReleaseDate.setText(Utils.getDisplayableReadableDate(date));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    mMovieRateBar.setRating((float) (movie.getVoteAverage() / 2));
    mMovieRating.setText(Html.fromHtml(
        getString(R.string.about_title, new DecimalFormat("0.0").format(movie.getVoteAverage()))));

    mPlotExplanation.setText(movie.getOverview());

    String posterUrl = Utils.getPosterUrl(movie.getPosterPath());
    String backDropUrl = Utils.getBackdropUrl(movie.getBackdropPath());

    Timber.d(posterUrl);
    Timber.d(backDropUrl);

    RequestOptions options = new RequestOptions()
        .placeholder(R.drawable.placeholder);

    Glide.with(this).asBitmap().load(posterUrl)
        .thumbnail(0.5f)
        .transition(new BitmapTransitionOptions().crossFade())
        .apply(options)
        .into(new SimpleTarget<Bitmap>() {
          @Override public void onResourceReady(@NonNull Bitmap resource,
              @Nullable Transition<? super Bitmap> transition) {
            mMoviePoster.setImageBitmap(resource);
           /* Palette.from(resource)
                .generate(p -> findViewById(android.R.id.content).setBackground(
                    createGradientDrawable(p)));*/
          }
        });

    Glide.with(this)
        .load(backDropUrl)
        .into(mMovieBackdrop);
  }

  private GradientDrawable createGradientDrawable(Palette palette) {
    int darkVibrantColor =
        ColorUtils.setAlphaComponent(palette.getLightVibrantColor(Color.BLACK),
            (int) (0xFF * 1f)); // 87% alpha
    int darkMutedColor =
        ColorUtils.setAlphaComponent(palette.getLightMutedColor(Color.BLACK),
            (int) (0xFF * 1f)); // 87% alpha
    return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[] { darkMutedColor, darkVibrantColor });
  }

  @Override public void setPresenter(DetailsActivityContract.Presenter presenter) {
    mDetailsActivityPresenter = presenter;
  }

  @Override public void onMovieDetailsFetchSuccess(ExtendedMovieDetails movieDetails) {
    hideProgress();
    Timber.d(movieDetails.toString());
  }

  @Override public void onMovieDetailsFetchError(Throwable throwable) {

  }

  @OnClick(R.id.fab_movie_details_add_to_favourites) void onAddToFavouritesClick() {
    added = !added;
    playLikeAnimation(added);
  }

  void showProgress() {
    mTabsContainer.setVisibility(View.INVISIBLE);
    mLoadingProgress.setVisibility(View.VISIBLE);
  }

  void hideProgress() {
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
          mFabAddToFavourites.setImageResource(R.drawable.ic_like_non_active);
        }
      }
    });
    animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
    animatorSet.start();
  }

  public static class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      TextView textView = (TextView) rootView.findViewById(R.id.section_label);
      textView.setText("Section Number: " + getArguments().getInt(ARG_SECTION_NUMBER));
      return rootView;
    }
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }
  }
}
