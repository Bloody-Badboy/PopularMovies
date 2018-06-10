package me.bloodybadboy.popularmovies.utils;

import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class ArrayUtils {
  private ArrayUtils() {
    throw new AssertionError();
  }
  public static int[] toPrimitiveIntArray(@Nullable List<Integer> integers) {
    if (integers == null) {
      return null;
    } else if (integers.size() == 0) {
      return new int[0];
    }
    final int[] result = new int[integers.size()];
    for (int i = 0; i < integers.size(); i++) {
      result[i] = integers.get(i);
    }
    return result;
  }

  public static List<Integer> toIntegerList(@Nullable int[] array) {
    if (array == null) {
      return null;
    } else if (array.length == 0) {
      return new ArrayList<>();
    }
    final List<Integer> result = new ArrayList<>(array.length);
    for (int i : array) {
      result.add(i);
    }
    return result;
  }
}
