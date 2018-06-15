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
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Review;
import me.bloodybadboy.popularmovies.data.source.local.FavouriteMovieStore;
import me.bloodybadboy.popularmovies.injection.Injection;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.details.DetailsActivityContract;
import me.bloodybadboy.popularmovies.ui.details.presenter.DetailsActivityPresenter;
import me.bloodybadboy.popularmovies.utils.RxUtils;
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
  private Movie mMovie;

  private boolean isFavourite = false;
  CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details_v2);
    mUnbinder = ButterKnife.bind(this);

    if (mDetailsActivityPresenter == null) {
      mDetailsActivityPresenter = new DetailsActivityPresenter(this, Injection.providesDataRepo());
    }

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    if (getIntent().hasExtra(Constants.MOVIE_DATA_EXTRA)) {
      Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_DATA_EXTRA);
      if (movie != null) {
        this.mMovie = movie;

        updateUI();
        showProgress();

        mDetailsActivityPresenter.fetchMovieDetailsFromServer(String.valueOf(movie.getMovieId()));
      }
    }
  }

  @Override protected void onStart() {
    super.onStart();
    mDetailsActivityPresenter.subscribe();
  }

  @Override protected void onStop() {
    super.onStop();
    mDetailsActivityPresenter.unsubscribe();
  }

  @Override protected void onDestroy() {
    mUnbinder.unbind();
    compositeDisposable.dispose();
    super.onDestroy();
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return mDetailsActivityPresenter;
  }

  private void updateUI() {
    if (mMovie == null) {
      return;
    }
    String originalTitle = mMovie.getOriginalTitle();
    String title = mMovie.getTitle();

    if (originalTitle.equalsIgnoreCase(title)) {
      mMovieTitle.setText(title);
    } else {
      mMovieTitle.setText(
          getString(R.string.details_activity_movie_title_format, title, originalTitle));
    }
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

    compositeDisposable.add(FavouriteMovieStore.getInstance()
        .isMovieInFavourites(mMovie.getMovieId())
        .compose(RxUtils.applyIOScheduler())
        .subscribe(
            favourite -> {
              this.isFavourite = favourite;
              if (favourite) {
                mFabAddToFavourites.setImageResource(R.drawable.ic_like_active);
              }
            }, throwable -> {
              Toast.makeText(this, "Query Failed: " + throwable.getMessage(), Toast.LENGTH_SHORT)
                  .show();
            }));
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
  }

  @Override public void onMovieDetailsFetchSuccess(ExtendedMovieDetails movieDetails) {
    hideProgress();
    if (movieDetails != null) {
      Timber.d(movieDetails.toString());
      SectionsPagerAdapter mSectionsPagerAdapter =
          new SectionsPagerAdapter(getSupportFragmentManager(), movieDetails);
      mViewPager.setAdapter(mSectionsPagerAdapter);
    }
  }

  @Override public void onMovieDetailsFetchError(Throwable throwable) {

  }

  @OnClick(R.id.fab_movie_details_add_to_favourites) void onAddToFavouritesClick() {
    if (!isFavourite) {
      isFavourite = true;
      compositeDisposable.add(
          FavouriteMovieStore.getInstance().addMovieToFavourites(mMovie).compose(
              upstream -> upstream.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())).subscribe(() -> {
            Toast.makeText(this, "Added to favourites!", Toast.LENGTH_SHORT).show();
          }, throwable -> {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            isFavourite = !isFavourite;
            playLikeAnimation(isFavourite);
          }));
    } else {
      isFavourite = false;
      compositeDisposable.add(
          FavouriteMovieStore.getInstance().removeMovieFromFavourites(mMovie.getMovieId()).compose(
              upstream -> upstream.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())).subscribe(() -> {
            Toast.makeText(this, "Removed from favourites!", Toast.LENGTH_SHORT).show();
          }, throwable -> {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            isFavourite = !isFavourite;
            playLikeAnimation(isFavourite);
          }));
    }
    playLikeAnimation(isFavourite);
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
          mFabAddToFavourites.setImageResource(R.drawable.ic_like_inactive);
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

    private ExtendedMovieDetails movieDetails;

    SectionsPagerAdapter(FragmentManager manager, ExtendedMovieDetails movieDetails) {
      super(manager);
      this.movieDetails = movieDetails;
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 2) {
        return ReviewsFragment.newInstance(
            (ArrayList<Review>) movieDetails.getMovieReviews().getReviews());
      }
      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }
  }
}
