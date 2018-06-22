package me.bloodybadboy.popularmovies.utils;

import android.database.Cursor;

public final class CursorUtils {
  private CursorUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static byte[] getBlob(Cursor cursor, String columnName) {
    return cursor.getBlob(cursor.getColumnIndexOrThrow(columnName));
  }

  public static double getDouble(Cursor cursor, String columnName) {
    return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
  }

  public static float getFloat(Cursor cursor, String columnName) {
    return cursor.getFloat(cursor.getColumnIndexOrThrow(columnName));
  }

  public static int getInt(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
  }

  public static long getLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
  }

  public static short getShort(Cursor cursor, String columnName) {
    return cursor.getShort(cursor.getColumnIndexOrThrow(columnName));
  }

  public static String getString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
  }
}
