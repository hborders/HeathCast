package com.github.hborders.heathcast.idlingresource;

import io.reactivex.Observable;

public interface DisposableIdlingResourceObserableSubscriber<T> {
    DisposableIdlingResource subscribe(
            String name,
            Observable<T> observable
    );
}
