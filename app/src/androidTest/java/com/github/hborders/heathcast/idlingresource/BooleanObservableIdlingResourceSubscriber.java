package com.github.hborders.heathcast.idlingresource;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

// can't just observe an optional. Need to actually observe whether a search is active
// thus, we have to actually expose the loading state, not just the effects
public final class BooleanObservableIdlingResourceSubscriber {
    public static DisposableIdlingResource subscribe(
            String name,
            Observable<Boolean> idleObservable
    ) {
        final MutableIdlingResource idleMutableIdlingResource = BasicMutableIdlingResource.idle(
                name
        );
        final Disposable disposable = idleObservable.subscribe(
                idleMutableIdlingResource::setIdleOrBusy
        );
        return new BasicDisposableMutableIdlingResource(
                disposable,
                idleMutableIdlingResource
        );
    }
}
