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
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.Cast;
import me.bloodybadboy.popularmovies.utils.Utils;
import timber.log.Timber;

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.ViewHolder> {

  private final List<Cast> mCasts;
  private Context mContext;

  public CastsAdapter(List<Cast> casts) {
    mCasts = casts;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.listing_item_casts_row_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, mCasts.get(position));
  }

  @Override
  public int getItemCount() {
    return mCasts == null ? 0 : mCasts.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Cast cast) {
    ViewHolder videoListItemViewHolder = (ViewHolder) viewHolder;
    if (cast != null) {
      videoListItemViewHolder.bind(mContext, cast);
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_listing_item_casts_profile_picture)
    ImageView mProfilePicture;

    @BindView(R.id.tv_listing_item_casts_name)
    TextView mName;

    @BindView(R.id.tv_listing_item_casts_character)
    TextView mCharacterName;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
    }

    void bind(Context context, Cast cast) {
      String profilePicUrl = Utils.getProfilePictureUrl(cast.getProfilePath());

      Timber.d(profilePicUrl);

      RequestOptions options;

      // Gender ID `0` or `1` for female, `2` for male
      if (cast.getGender() == 0 || cast.getGender() == 1) {
        options = new RequestOptions()
            .placeholder(R.drawable.female_placeholder)
            .error(R.drawable.female_placeholder);
      } else {
        options = new RequestOptions()
            .placeholder(R.drawable.male_placeholder)
            .error(R.drawable.male_placeholder);
      }

      Glide.with(context)
          .asDrawable()
          .load(profilePicUrl)
          .transition(DrawableTransitionOptions.withCrossFade())
          .apply(options)
          .into(mProfilePicture);

      mName.setText(cast.getName());
      mCharacterName.setText(cast.getCharacter());

      itemView.setTag(cast);
    }
  }
}
