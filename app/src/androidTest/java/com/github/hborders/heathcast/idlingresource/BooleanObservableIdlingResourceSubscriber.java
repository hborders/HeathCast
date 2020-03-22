package com.github.hborders.heathcast.idlingresource;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.BUSY;
import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.IDLE;

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
                idle ->
                        idleMutableIdlingResource.setState(
                                idle ? IDLE : BUSY
                        )
        );
        return new BasicDisposableMutableIdlingResource(
                disposable,
                idleMutableIdlingResource
        );
    }
}
