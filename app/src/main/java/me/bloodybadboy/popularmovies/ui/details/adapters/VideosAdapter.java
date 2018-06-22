package me.bloodybadboy.popularmovies.ui.details.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import java.util.List;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Video;
import me.bloodybadboy.popularmovies.rxbus.RxBus;
import me.bloodybadboy.popularmovies.ui.details.model.YouTubeVideo;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

  private final List<Video> mVideos;
  private Context mContext;

  public VideosAdapter(List<Video> videos) {
    mVideos = videos;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.listing_item_video_row_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, mVideos.get(position));
  }

  @Override
  public int getItemCount() {
    return mVideos == null ? 0 : mVideos.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Video video) {
    ViewHolder videoListItemViewHolder = (ViewHolder) viewHolder;
    if (video != null) {
      videoListItemViewHolder.bind(mContext, video);
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_listing_item_casts_profile_picture)
    ImageView mThumbnail;

    @BindView(R.id.tv_listing_item_casts_name)
    TextView mTitle;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(
          v -> RxBus.getInstance().send(new YouTubeVideo(((Video) itemView.getTag()).getKey())));
    }

    void bind(Context context, Video video) {
      String ytThumbnailUrl = Utils.getYouTubeThumbnailUrl(video.getKey());

      Timber.d(ytThumbnailUrl);

      Glide.with(context)
          .asDrawable()
          .load(ytThumbnailUrl)
          .thumbnail(0.5f)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(mThumbnail);

      mTitle.setText(video.getName());

      itemView.setTag(video);
    }
  }
}
