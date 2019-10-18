package me.bloodybadboy.popularmovies.ui.details.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.data.model.ExtendedMovieDetails;
import me.bloodybadboy.popularmovies.data.model.ProductionCompany;
import me.bloodybadboy.popularmovies.data.model.ProductionCountry;
import me.bloodybadboy.popularmovies.data.model.SpokenLanguage;

import static me.bloodybadboy.popularmovies.Constants.MOVIE_INFO_EXTRA;

public class InfoFragment extends Fragment {

  @BindView(R.id.tv_info_overview)
  TextView mOverView;

  @BindView(R.id.tv_info_status)
  TextView mStatus;

  @BindView(R.id.tv_info_adult)
  TextView mAdult;

  @BindView(R.id.tv_info_tagline)
  TextView mTagline;

  @BindView(R.id.tv_info_original_title)
  TextView mOriginalTitle;

  @BindView(R.id.tv_info_original_language)
  TextView mOriginalLanguage;

  @BindView(R.id.tv_info_duration)
  TextView mDuration;

  @BindView(R.id.tv_info_spoken_lang)
  TextView mSpokenLangs;

  @BindView(R.id.tv_info_budget)
  TextView mBudget;

  @BindView(R.id.tv_info_revenue)
  TextView mRevenue;

  @BindView(R.id.tv_info_prod_countries)
  TextView mProdCountries;

  @BindView(R.id.tv_info_prod_companies)
  TextView mProdCompanies;

  @BindView(R.id.tv_info_website)
  TextView mWebsite;

  private ExtendedMovieDetails mMovieDetails;
  private Unbinder mUnbinder;

  public InfoFragment() {
  }

  public static InfoFragment newInstance(ExtendedMovieDetails movieDetails) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(MOVIE_INFO_EXTRA, movieDetails);

    InfoFragment fragment = new InfoFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mMovieDetails = getArguments().getParcelable(MOVIE_INFO_EXTRA);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_info, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);

    updateUI();
    return rootView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  private void updateUI() {

    mStatus.setText(unknownIfNullOrEmpty(mMovieDetails.getStatus()));

    mAdult.setText(mMovieDetails.isAdult() ? getString(R.string.yes) : getString(R.string.no));

    mOverView.setText(unknownIfNullOrEmpty(mMovieDetails.getOverview()));

    mTagline.setText(unknownIfNullOrEmpty(mMovieDetails.getTagLine()));

    mOriginalTitle.setText(unknownIfNullOrEmpty(mMovieDetails.getOriginalTitle()));

    mOriginalLanguage.setText(unknownIfNullOrEmpty(mMovieDetails.getOriginalLanguage()));

    int runtime = (int) mMovieDetails.getRuntime();
    int hours = runtime / 60;
    int minutes = runtime % 60;

    mDuration.setText(
        String.format(Locale.getDefault(), getString(R.string.movie_info_duration_format), hours,
            minutes));

    List<String> spokenLangs = new ArrayList<>();
    for (SpokenLanguage language : mMovieDetails.getSpokenLanguages()) {
      spokenLangs.add(language.getName());
    }

    mSpokenLangs.setText(unknownIfNullOrEmpty(TextUtils.join(", ", spokenLangs)));

    float budget = mMovieDetails.getBudget();
    float revenue = mMovieDetails.getRevenue();

    if (budget > 0) {
      mBudget.setText(
          String.format(Locale.getDefault(), getString(R.string.movie_info_currency_format),
              (float) budget));
    } else {
      mBudget.setText(R.string.unknown);
    }

    if (revenue > 0) {
      mRevenue.setText(
          String.format(Locale.getDefault(), getString(R.string.movie_info_currency_format),
              (float) revenue));
    } else {
      mRevenue.setText(R.string.unknown);
    }

    List<String> prodCompanies = new ArrayList<>();
    for (ProductionCompany company : mMovieDetails.getProductionCompanies()) {
      prodCompanies.add(company.getName());
    }

    mProdCompanies.setText(unknownIfNullOrEmpty(TextUtils.join(", ", prodCompanies)));

    List<String> prodCountries = new ArrayList<>();
    for (ProductionCountry country : mMovieDetails.getProductionCountries()) {
      prodCountries.add(country.getName());
    }

    mProdCountries.setText(unknownIfNullOrEmpty(TextUtils.join(", ", prodCountries)));
    mWebsite.setText(unknownIfNullOrEmpty(mMovieDetails.getHomepage()));
  }

  public String unknownIfNullOrEmpty(String text) {
    if (TextUtils.isEmpty(text)) {
      return "Unknown";
    }
    return text;
  }

  private void showNoInfo() {

  }
}
