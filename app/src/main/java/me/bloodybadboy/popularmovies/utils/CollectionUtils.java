package me.bloodybadboy.popularmovies.utils;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class CollectionUtils {

  private CollectionUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
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

  //public static <T> boolean filter(final Iterable<T> collection,
  //    final Predicate<? super T> predicate) {
  //  boolean result = false;
  //  if (collection != null && predicate != null) {
  //    for (final Iterator<T> it = collection.iterator(); it.hasNext(); ) {
  //      if (!predicate.evaluate(it.next())) {
  //        it.remove();
  //        result = true;
  //      }
  //    }
  //  }
  //  return result;
  //}
  //
  //public interface Predicate<T> {
  //  boolean evaluate(T t);
  //}
}
