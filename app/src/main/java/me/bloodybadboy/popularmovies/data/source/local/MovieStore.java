package me.bloodybadboy.popularmovies.data.source.local;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.view.menu.MenuView;

public class MovieStore {

  private static volatile MovieStore sInstance = null;

  private MovieStore() {
    if (sInstance != null) {
      throw new AssertionError("Another instance of " + MovieStore.class.getName()
          + " class already exists, Can't create a new instance.");
    }
  }

  public static MovieStore getInstance() {
    if (sInstance == null) {
      synchronized (MenuView.class) {
        if (sInstance == null) {
          sInstance = new MovieStore();
        }
      }
    }
    return sInstance;
  }

  public void onCreate(final SQLiteDatabase db) {
    final String SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS "
            + FavouriteMovieStoreColumns.TABLE_NAME
            + " ( "
            + FavouriteMovieStoreColumns._ID
            + " INTEGER PRIMARY KEY , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_ID
            + " INTEGER NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_TITLE
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_ORIGINAL_TITLE
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_ORIGINAL_ORIGINAL_LANGUAGE
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_POSTER_PATH
            + "TEXT NOT NULL , +"
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_BACKDROP_PATH
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_OVERVIEW
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_ADULT
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_VIDEO
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_POPULARITY
            + " REAL NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_VIEW_COUNT
            + " INT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_VOTE_AVERAGE
            + " REAL NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_GENRE_IDS
            + " TEXT NOT NULL , "
            + FavouriteMovieStoreColumns.COLUMN_MOVIE_RELEASE_DATE
            + " DATE NOT NULL" + ")";
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieStoreColumns.TABLE_NAME);
  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieStoreColumns.TABLE_NAME);
  }

  public void addMovie() {
    final SQLiteDatabase database = MovieDB.getInstance().getWritableDatabase();
    database.beginTransaction();
  }

  public interface FavouriteMovieStoreColumns extends BaseColumns {
    String TABLE_NAME = "favourite-movie";

    String COLUMN_MOVIE_ID = "movie_id";
    String COLUMN_MOVIE_TITLE = "title";
    String COLUMN_MOVIE_ORIGINAL_TITLE = "original_title";
    String COLUMN_MOVIE_ORIGINAL_ORIGINAL_LANGUAGE = "original_language";
    String COLUMN_MOVIE_POSTER_PATH = "poster_path";
    String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
    String COLUMN_MOVIE_OVERVIEW = "overview";
    String COLUMN_MOVIE_ADULT = "adult";
    String COLUMN_MOVIE_VIDEO = "video";
    String COLUMN_MOVIE_POPULARITY = "popularity";
    String COLUMN_MOVIE_VIEW_COUNT = "vote_count";
    String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
    String COLUMN_MOVIE_GENRE_IDS = "genre_ids";
    String COLUMN_MOVIE_RELEASE_DATE = "release_date";

    String[] MOVIE_COLUMNS = {
        COLUMN_MOVIE_ID,
        COLUMN_MOVIE_TITLE,
        COLUMN_MOVIE_ORIGINAL_TITLE,
        COLUMN_MOVIE_ORIGINAL_ORIGINAL_LANGUAGE,
        COLUMN_MOVIE_POSTER_PATH,
        COLUMN_MOVIE_BACKDROP_PATH,
        COLUMN_MOVIE_OVERVIEW,
        COLUMN_MOVIE_ADULT,
        COLUMN_MOVIE_VIDEO,
        COLUMN_MOVIE_POPULARITY,
        COLUMN_MOVIE_VIEW_COUNT,
        COLUMN_MOVIE_VOTE_AVERAGE,
        COLUMN_MOVIE_GENRE_IDS,
        COLUMN_MOVIE_RELEASE_DATE,
    };
  }
}
