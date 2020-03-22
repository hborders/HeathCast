package com.github.hborders.heathcast.idlingresource;

import com.github.hborders.heathcast.views.recyclerviews.ItemRange;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.BUSY;
import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.IDLE;

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
                    idleMutableIdlingResource.setState(
                            itemRangeOptional
                                    .map(
                                            itemRange ->
                                                    itemRange.visibleClosedRange != null ? IDLE : BUSY
                                    )
                                    .orElse(BUSY)
                    );
                }
        );
        return new BasicDisposableMutableIdlingResource(
                disposable,
                idleMutableIdlingResource
        );
    }
}
