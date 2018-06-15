package me.bloodybadboy.popularmovies.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public final class ActivityUtils {
  private ActivityUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, int container) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(container, fragment);
    transaction.commit();
  }
}
