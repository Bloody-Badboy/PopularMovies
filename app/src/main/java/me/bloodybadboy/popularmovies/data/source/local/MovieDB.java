package me.bloodybadboy.popularmovies.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.view.menu.MenuView;
import me.bloodybadboy.popularmovies.injection.Injection;

public class MovieDB extends SQLiteOpenHelper {

  private static volatile MovieDB sInstance = null;
  public static final String DATABASE_NAME = "moviedb.db";
  private static final int DATABASE_VERSION = 4;

  private MovieDB(final Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    if (sInstance != null) {
      throw new AssertionError("Another instance of " + MovieDB.class.getName()
          + " class already exists, Can't create a new instance.");
    }
  }

  public static MovieDB getInstance() {
    if (sInstance == null) {
      synchronized (MenuView.class) {
        if (sInstance == null) {
          sInstance = new MovieDB(Injection.provideApplicationContext());
        }
      }
    }
    return sInstance;
  }
  @Override public void onCreate(SQLiteDatabase db) {
MovieStore.getInstance().onCreate(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    MovieStore.getInstance().onUpgrade(db, oldVersion, newVersion);
  }

  @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    MovieStore.getInstance().onDowngrade(db, oldVersion, newVersion);
  }
}
