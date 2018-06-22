package me.bloodybadboy.popularmovies.ui.movies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import me.bloodybadboy.popularmovies.Constants;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.storage.MovieGenreStore;
import me.bloodybadboy.popularmovies.ui.movies.model.DetailsActivityLaunchModel;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess") public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_ITEM = 0;
  public static final int VIEW_TYPE_LOADING = 1;

  private List<Movie> mMovieList;
  private Context mContext;
  private boolean isLoadingItemAdded = false;

  public MoviesAdapter(List<Movie> movies) {
    this.mMovieList = movies;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
    View view;
    if (viewType == VIEW_TYPE_ITEM) {
      view = layoutInflater.inflate(R.layout.listing_item_movie, parent, false);
      view.getLayoutParams().height = calculateMovieListItemHeight(parent);
      return new MovieListItemViewHolder(view);
    } else {
      view = layoutInflater.inflate(R.layout.listing_item_loading_row_layout, parent, false);
      return new LoadingItemViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      bindMovieListItemViewHolder(viewHolder, mMovieList.get(position));
    }
  }

  @Override public int getItemCount() {
    return mMovieList != null ? mMovieList.size() : 0;
  }

  @Override public int getItemViewType(int position) {
    return mMovieList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }

  public void swapMovieListItems(List<Movie> movies) {
    mMovieList.clear();
    mMovieList.addAll(movies);
    notifyDataSetChanged();
  }

  public void updateMovieListItems(List<Movie> movies) {
    final MovieListDiffCallback movieListDiffCallback =
        new MovieListDiffCallback(mMovieList, movies);
    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(movieListDiffCallback);

    mMovieList.clear();
    mMovieList.addAll(movies);

    diffResult.dispatchUpdatesTo(MoviesAdapter.this);
  }

  public void addLoadingItem() {
    if (!isLoadingItemAdded) {
      isLoadingItemAdded = true;
      mMovieList.add(null);
      notifyItemInserted(mMovieList.size() - 1);
    }
  }

  public void removeLoadingItem() {
    if (isLoadingItemAdded) {
      isLoadingItemAdded = false;
      mMovieList.remove(mMovieList.size() - 1);
      notifyItemRemoved(mMovieList.size());
    }
  }

  private int calculateMovieListItemHeight(ViewGroup parent) {
    int gridLayoutItemSpanCount = Utils.calculateNoOfColumns(mContext);
    float gridItemSpacing =
        mContext.getResources().getDimension(R.dimen.dimen_movie_list_grid_item_spacing);

    int itemWidth =
        (int) ((parent.getWidth() / gridLayoutItemSpanCount) - (3 * (gridItemSpacing / 2)));

    return (int) (itemWidth * Constants.POSTER_IMAGE_ASPECT_RATIO);
  }

  private void bindMovieListItemViewHolder(RecyclerView.ViewHolder viewHolder, Movie movie) {
    MovieListItemViewHolder movieListItemViewHolder = (MovieListItemViewHolder) viewHolder;
    if (movie != null) {
      movieListItemViewHolder.bind(mContext, movie, MovieGenreStore.getInstance().get());
    }
  }

  static class MovieListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_listing_item_movie_poster)
    ImageView mMoviePoster;

    @BindView(R.id.tv_listing_item_movie_name)
    TextView mMovieName;

    @BindView(R.id.tv_listing_item_movie_genres)
    TextView mMovieGenre;

    @BindView(R.id.tv_listing_item_movie_release_date)
    TextView mMovieReleaseDate;

    @BindView(R.id.rb_listing_item_movie_rating)
    RatingBar mMovieRatingBar;

    @BindView(R.id.tv_listing_item_movie_rating_value)
    TextView mMovieRating;

    @BindView(R.id.movie_info_background)
    View mMovieInfoBackground;

    private DecimalFormat mMovieRatingFormat = new DecimalFormat("0.0");

    MovieListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(v -> RxBus.getInstance()
          .send(new DetailsActivityLaunchModel((Movie) itemView.getTag(), getAdapterPosition(),
              new Pair<>(mMoviePoster,
                  itemView.getContext().getString(R.string.transition_movie_poster)),
              new Pair<>(mMovieName,
                  itemView.getContext().getString(R.string.transition_movie_title)),
              new Pair<>(mMovieGenre,
                  itemView.getContext().getString(R.string.transition_movie_genres)),
              new Pair<>(mMovieReleaseDate,
                  itemView.getContext().getString(R.string.transition_movie_release_date)),
              new Pair<>(mMovieRatingBar,
                  itemView.getContext().getString(R.string.transition_movie_ratebar))
              , new Pair<>(mMovieRating,
              itemView.getContext().getString(R.string.transition_movie_rating)))));
    }

    void bind(Context context, Movie movie, SparseArray<String> genres) {
      mMovieName.setText(movie.getTitle());
      mMovieName.setSelected(true);

      if (genres != null) {
        mMovieGenre.setText(
            Utils.getDisplayableGenreList(genres, movie.getGenreIds()));
        mMovieGenre.setSelected(true);
      } else {
        mMovieGenre.setText(R.string.unknown);
      }

      float movieRating = (float) (movie.getVoteAverage() / 2);

      mMovieRatingBar.setRating(movieRating);
      mMovieRating.setText(mMovieRatingFormat.format(movie.getVoteAverage()));

      if (movie.getReleaseDate() != null) {
        try {
          Date date =
              new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.getReleaseDate());
          mMovieReleaseDate.setText(Utils.getDisplayableReadableDate(date));
        } catch (ParseException e) {
          Timber.e(e);
        }
      }
      String posterUrl = Utils.getPosterUrl(movie.getPosterPath());

      RequestOptions options = new RequestOptions()
          .placeholder(R.drawable.poster_placeholder)
          .error(R.drawable.poster_placeholder);

      Glide.with(context).asBitmap().load(posterUrl)
          .thumbnail(0.5f)
          .transition(new BitmapTransitionOptions().crossFade())
          .apply(options)
          .into(new SimpleTarget<Bitmap>() {
            @Override public void onResourceReady(@NonNull Bitmap resource,
                @Nullable Transition<? super Bitmap> transition) {
              mMoviePoster.setImageBitmap(resource);
              Palette.from(resource)
                  .generate(p -> mMovieInfoBackground.setBackground(
                      createGradientDrawable(p)));
            }
          });

      itemView.setTag(movie);
    }

    private GradientDrawable createGradientDrawable(Palette palette) {
      int darkVibrantColor =
          ColorUtils.setAlphaComponent(palette.getDarkVibrantColor(Color.BLACK),
              (int) (0xFF * 0.87f)); // 87% alpha
      int darkMutedColor =
          ColorUtils.setAlphaComponent(palette.getDarkMutedColor(Color.BLACK),
              (int) (0xFF * 0.87f)); // 87% alpha
      return new GradientDrawable(GradientDrawable.Orientation.BR_TL,
          new int[] { darkMutedColor, darkVibrantColor });
    }
  }

  static class LoadingItemViewHolder extends RecyclerView.ViewHolder {
    LoadingItemViewHolder(View itemView) {
      super(itemView);
    }
  }
}
