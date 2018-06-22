package me.bloodybadboy.popularmovies.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.popularmovies.data.model.Movie;
import me.bloodybadboy.popularmovies.data.model.Movies;
import me.bloodybadboy.popularmovies.injection.Injection;

import static me.bloodybadboy.popularmovies.data.source.local.MovieContract.BASE_CONTENT_URI;
import static me.bloodybadboy.popularmovies.data.source.local.MovieContract.CONTENT_AUTHORITY;
import static me.bloodybadboy.popularmovies.utils.CursorUtils.getDouble;
import static me.bloodybadboy.popularmovies.utils.CursorUtils.getInt;
import static me.bloodybadboy.popularmovies.utils.CursorUtils.getString;

@SuppressWarnings({ "unused", "WeakerAccess" }) public final class FavouriteMovieStore {

  public static final String PATH_FAVOURITE_MOVIE = "favourite_movie";
  public static final Uri CONTENT_URI;
  public static final String CONTENT_TYPE;
  private final static JsonAdapter<List<Integer>> sIntegerListAdapter;
  private static volatile FavouriteMovieStore sInstance = null;

  static {
    CONTENT_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_FAVOURITE_MOVIE)
        .build();
    CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIE;

    sIntegerListAdapter =
        Injection.provideMoshi().adapter(Types.newParameterizedType(List.class, Integer.class));
  }

  private FavouriteMovieStore() {
    if (sInstance != null) {
      throw new AssertionError("Another instance of " + FavouriteMovieStore.class.getName()
          + " class already exists, Can't create a new instance.");
    }
  }

  public static FavouriteMovieStore getInstance() {
    if (sInstance == null) {
      synchronized (MenuView.class) {
        if (sInstance == null) {
          sInstance = new FavouriteMovieStore();
        }
      }
    }
    return sInstance;
  }

  void onCreate(final SQLiteDatabase db) {
    final String SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS "
            + Columns.TABLE_NAME
            + " ( "
            + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Columns.MOVIE_ID + " INTEGER NOT NULL , "
            + Columns.MOVIE_TITLE + " TEXT NOT NULL , "
            + Columns.MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL , "
            + Columns.MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL , "
            + Columns.MOVIE_POSTER_PATH + " TEXT NOT NULL , "
            + Columns.MOVIE_BACKDROP_PATH + " TEXT NOT NULL , "
            + Columns.MOVIE_OVERVIEW + " TEXT NOT NULL , "
            + Columns.MOVIE_ADULT + " TEXT NOT NULL , "
            + Columns.MOVIE_VIDEO + " TEXT NOT NULL , "
            + Columns.MOVIE_POPULARITY + " REAL NOT NULL , "
            + Columns.MOVIE_VIEW_COUNT + " INT NOT NULL , "
            + Columns.MOVIE_VOTE_AVERAGE + " REAL NOT NULL , "
            + Columns.MOVIE_GENRE_IDS + " TEXT NOT NULL , "
            + Columns.MOVIE_RELEASE_DATE + " DATE NOT NULL" +
            ")";
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + Columns.TABLE_NAME);
  }

  void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + Columns.TABLE_NAME);
  }

  public Completable addMovieToFavourites(@NonNull final Movie movie) {
    return Completable.create(emitter -> {
      ContentValues contentValues = new ContentValues();

      contentValues.put(Columns.MOVIE_ID, movie.getMovieId());
      contentValues.put(Columns.MOVIE_TITLE, movie.getTitle());
      contentValues.put(Columns.MOVIE_ORIGINAL_TITLE,
          movie.getOriginalTitle());
      contentValues.put(Columns.MOVIE_ORIGINAL_LANGUAGE,
          movie.getOriginalLanguage());
      contentValues.put(Columns.MOVIE_GENRE_IDS, sIntegerListAdapter.toJson(movie.getGenreIds()));
      contentValues.put(Columns.MOVIE_POSTER_PATH,
          movie.getPosterPath() == null ? "" : movie.getPosterPath());
      contentValues.put(Columns.MOVIE_BACKDROP_PATH,
          movie.getBackdropPath() == null ? "" : movie.getBackdropPath());
      contentValues.put(Columns.MOVIE_OVERVIEW, movie.getOverview());
      contentValues.put(Columns.MOVIE_ADULT,
          String.valueOf(movie.isAdult()));
      contentValues.put(Columns.MOVIE_VIDEO,
          String.valueOf(movie.isVideo()));
      contentValues.put(Columns.MOVIE_POPULARITY,
          movie.getPopularity());
      contentValues.put(Columns.MOVIE_VIEW_COUNT, movie.getVoteCount());
      contentValues.put(Columns.MOVIE_VOTE_AVERAGE,
          movie.getVoteAverage());
      contentValues.put(Columns.MOVIE_RELEASE_DATE,
          movie.getReleaseDate());

      Uri uri =
          Injection.provideApplicationContext()
              .getContentResolver()
              .insert(CONTENT_URI, contentValues);
      if (uri != null) {
        emitter.onComplete();
      }
    });
  }

  public Completable removeMovieFromFavourites(final int movieId) {
    return Completable.create(emitter -> {

      Uri deleteUri = CONTENT_URI.buildUpon()
          .appendPath(String.valueOf(movieId))
          .build();
      int rowsDeleted = Injection.provideApplicationContext()
          .getContentResolver()
          .delete(deleteUri, null, null);
      if (rowsDeleted != 0) {
        emitter.onComplete();
      }
    });
  }

  public Single<Boolean> isMovieInFavourites(final int movieId) {
    return Single.create(emitter -> {
      Uri queryUri = CONTENT_URI.buildUpon()
          .appendPath(String.valueOf(movieId))
          .build();

      try (Cursor movieCursor = Injection.provideApplicationContext()
          .getContentResolver()
          .query(queryUri, new String[] { Columns.MOVIE_ID }, null, null, null)) {
        if (movieCursor != null && movieCursor.moveToFirst()) {
          emitter.onSuccess(true);
        } else {
          emitter.onSuccess(false);
        }
      }
    });
  }

  Single<Movies> getFavouriteMovieList() {
    return Single.create(emitter -> {
      List<Movie> movies = new ArrayList<>();
      try (Cursor cursor = Injection.provideApplicationContext()
          .getContentResolver()
          .query(FavouriteMovieStore.CONTENT_URI, null, null, null, null)) {
        if (cursor != null && cursor.moveToFirst()) {
          do {
            Movie movie = new Movie.Builder()
                .withId(getInt(cursor, Columns.MOVIE_ID))
                .withTitle(getString(cursor, Columns.MOVIE_TITLE))
                .withOriginalTitle(getString(cursor, Columns.MOVIE_ORIGINAL_TITLE))
                .withGenreIds(
                    sIntegerListAdapter.fromJson(getString(cursor, Columns.MOVIE_GENRE_IDS)))
                .withPosterPath(getString(cursor, Columns.MOVIE_POSTER_PATH))
                .withBackdropPath(getString(cursor, Columns.MOVIE_BACKDROP_PATH))
                .withOverview(getString(cursor, Columns.MOVIE_OVERVIEW))
                .withAdult(Boolean.valueOf(getString(cursor, Columns.MOVIE_ADULT)))
                .withVideo(Boolean.valueOf(getString(cursor, Columns.MOVIE_VIDEO)))
                .withPopularity(getDouble(cursor, Columns.MOVIE_POPULARITY))
                .withVoteCount(getInt(cursor, Columns.MOVIE_VIEW_COUNT))
                .withVoteAverage(getDouble(cursor, Columns.MOVIE_VOTE_AVERAGE))
                .withReleaseDate(getString(cursor, Columns.MOVIE_RELEASE_DATE))
                .build();
            movies.add(movie);
          } while (cursor.moveToNext());
        }
      }
      emitter.onSuccess(new Movies.Builder().withPage(1)
          .withTotalPages(1)
          .withTotalResults(movies.size())
          .withMovies(movies)
          .build());
    });
  }

  public interface Columns extends BaseColumns {
    /* Table name */
    String TABLE_NAME = "favourite_movie";

    /* All columns */
    String MOVIE_ID = "movie_id";
    String MOVIE_TITLE = "title";
    String MOVIE_ORIGINAL_TITLE = "original_title";
    String MOVIE_ORIGINAL_LANGUAGE = "original_language";
    String MOVIE_GENRE_IDS = "genre_ids";
    String MOVIE_POSTER_PATH = "poster_path";
    String MOVIE_BACKDROP_PATH = "backdrop_path";
    String MOVIE_OVERVIEW = "overview";
    String MOVIE_ADULT = "adult";
    String MOVIE_VIDEO = "video";
    String MOVIE_POPULARITY = "popularity";
    String MOVIE_VIEW_COUNT = "vote_count";
    String MOVIE_VOTE_AVERAGE = "vote_average";
    String MOVIE_RELEASE_DATE = "release_date";
  }
}
