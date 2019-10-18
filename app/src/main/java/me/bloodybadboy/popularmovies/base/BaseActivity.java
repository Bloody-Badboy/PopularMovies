package me.bloodybadboy.popularmovies.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import java.util.Objects;
import me.bloodybadboy.popularmovies.R;

public abstract class BaseActivity<T extends BasePresenter, V extends BaseView>
    extends AppCompatActivity {
  protected T mPresenter;
  private Unbinder mUnbinder;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentViewResId());
    mUnbinder = Objects.requireNonNull(getViewsUnbinder());
    attachPresenter();
  }

  @SuppressWarnings("unchecked") private void attachPresenter() {
    mPresenter = (T) getLastCustomNonConfigurationInstance();
    if (mPresenter == null) {
      mPresenter = initPresenter();
    }
    mPresenter.attachView(provideView());
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return mPresenter;
  }

  @Override protected void onResume() {
    super.onResume();
    mPresenter.subscribe();
  }

  @Override protected void onPause() {
    super.onPause();
    mPresenter.unsubscribe();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mPresenter.detachView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
  }

  @LayoutRes protected abstract int getContentViewResId();

  @NonNull protected abstract Unbinder getViewsUnbinder();

  @NonNull protected abstract T initPresenter();

  @NonNull protected abstract V provideView();

  protected void showSnackBar(@Nullable View view, @NonNull String message,
      @Nullable Runnable runnable, int duration) {
    if (view == null) {
      view = findViewById(android.R.id.content);
    }
    Snackbar snackBar = Snackbar.make(view, message, duration);
    if (runnable != null) {
      snackBar.setAction(R.string.retry, __ -> runnable.run());
    }

    View sbView = snackBar.getView();
    TextView textView = sbView.findViewById(R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackBar.show();
  }

  protected void showSnackBar(@Nullable View view, @NonNull String message,
      @Nullable Runnable runnable) {
    showSnackBar(view, message, runnable, Snackbar.LENGTH_INDEFINITE);
  }
}
