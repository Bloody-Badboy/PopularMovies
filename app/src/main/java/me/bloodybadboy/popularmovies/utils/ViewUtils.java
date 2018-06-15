package me.bloodybadboy.popularmovies.utils;

import android.content.Context;
import android.util.TypedValue;

public final class ViewUtils {
  private ViewUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static float dpToPx(Context context, int dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics());
  }
}
