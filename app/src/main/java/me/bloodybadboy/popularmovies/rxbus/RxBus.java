package me.bloodybadboy.popularmovies.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
  private static volatile RxBus sInstance = null;
  private Subject<Object> mBusSubject = PublishSubject.create();

  private RxBus() {
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RxBus.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RxBus getInstance() {
    if (sInstance == null) {
      synchronized (RxBus.class) {
        if (sInstance == null) {
          sInstance = new RxBus();
        }
      }
    }
    return sInstance;
  }

  public void send(Object o) {
    mBusSubject.onNext(o);
  }

  public Observable<Object> toObservable() {
    return mBusSubject;
  }
}
