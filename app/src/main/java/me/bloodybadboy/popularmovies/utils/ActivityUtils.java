package me.bloodybadboy.popularmovies.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
