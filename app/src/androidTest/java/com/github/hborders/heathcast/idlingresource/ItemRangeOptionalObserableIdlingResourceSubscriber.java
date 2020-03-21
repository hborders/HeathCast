package com.github.hborders.heathcast.idlingresource;

import com.github.hborders.heathcast.views.recyclerviews.ItemRange;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ItemRangeOptionalObserableIdlingResourceSubscriber {
    public static DisposableIdlingResource subscribe(
            String name,
            Observable<Optional<ItemRange>> itemRangeObservable
    ) {
        final MutableIdlingResource idleMutableIdlingResource = BasicMutableIdlingResource.idle(
                name
        );
        final Disposable disposable = itemRangeObservable.subscribe(
                itemRangeOptional -> {
                    final boolean idle =
                            itemRangeOptional.map(
                                    itemRange ->
                                            itemRange.visibleClosedRange != null
                            ).orElse(false);
                    idleMutableIdlingResource.setIdleOrBusy(idle);
                }
        );
        return new BasicDisposableMutableIdlingResource(
                disposable,
                idleMutableIdlingResource
        );
    }
}
