package me.bloodybadboy.popularmovies.ui.details.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import jp.wasabeef.blurry.Blurry;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.details.DetailsActivityContract;
import me.bloodybadboy.popularmovies.ui.details.presenter.DetailsActivityPresenter;
import me.bloodybadboy.popularmovies.utils.Utils;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityContract.View {

  private static final int BACKDROP_BLUR_RADIUS = 5;

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

  private DetailsActivityContract.Presenter mDetailsActivityPresenter;
  private Unbinder mUnbinder;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    mUnbinder = ButterKnife.bind(this);

    setPresenter(new DetailsActivityPresenter(this));
    mDetailsActivityPresenter.onCreate();

    if (getIntent().hasExtra(Constants.MOVIE_DATA_EXTRA)) {
      Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_DATA_EXTRA);
      if (movie != null) {
        updateUI(movie);
      }
    }
  }

  @Override protected void onDestroy() {
    mDetailsActivityPresenter.onDestroy();
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
          String.format(getString(R.string.details_activity_movie_title_format), title,
              originalTitle));
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
    mMovieRating.setText(new DecimalFormat("0.0").format(movie.getVoteAverage()));

    mPlotExplanation.setText(movie.getOverview());

    String posterUrl = Utils.getPosterUrl(movie.getPosterPath());
    String backDropUrl = Utils.getBackdropUrl(movie.getBackdropPath());

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
            Palette.from(resource)
                .generate(p -> findViewById(android.R.id.content).setBackground(
                    createGradientDrawable(p)));
          }
        });

    Glide.with(this).asBitmap().load(backDropUrl)
        .into(new SimpleTarget<Bitmap>() {
          @Override public void onResourceReady(@NonNull Bitmap resource,
              @Nullable Transition<? super Bitmap> transition) {
            Blurry.with(DetailsActivity.this)
                .radius(BACKDROP_BLUR_RADIUS)
                .from(resource)
                .into(mMovieBackdrop);
          }
        });
  }

  private GradientDrawable createGradientDrawable(Palette palette) {
    int darkVibrantColor =
        ColorUtils.setAlphaComponent(palette.getDarkVibrantColor(Color.BLACK),
            (int) (0xFF * 1f)); // 87% alpha
    int darkMutedColor =
        ColorUtils.setAlphaComponent(palette.getDarkMutedColor(Color.BLACK),
            (int) (0xFF * 1f)); // 87% alpha
    return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[] { darkMutedColor, darkVibrantColor });
  }

  @Override public void setPresenter(DetailsActivityContract.Presenter presenter) {
    mDetailsActivityPresenter = presenter;
  }
}
