package com.github.hborders.heathcast.idlingresource;

import com.github.hborders.heathcast.reactivexandroid.RxListFragment;

import java.util.Optional;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.github.hborders.heathcast.core.SetUtil.nonEmptySetOf;
import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.BUSY;
import static com.github.hborders.heathcast.idlingresource.MutableIdlingResource.State.IDLE;

public final class RxListFragmentModeOptionalIdlingResourceSubscriber
        implements DisposableIdlingResourceObserableSubscriber<Optional<RxListFragment.Mode>> {
    public static DisposableIdlingResource subscribe(
            String name,
            Observable<Optional<RxListFragment.Mode>> modeOptionalObservable,
            RxListFragment.Mode requiredIdleMode,
            RxListFragment.Mode... idleModes
    ) {
        return new RxListFragmentModeOptionalIdlingResourceSubscriber(
                requiredIdleMode,
                idleModes
        )
                .subscribe(
                        name,
                        modeOptionalObservable
                );
    }

    private final Set<RxListFragment.Mode> idleModes;

    public RxListFragmentModeOptionalIdlingResourceSubscriber(
            RxListFragment.Mode requiredIdleMode,
            RxListFragment.Mode... idleModes
    ) {
        this.idleModes = nonEmptySetOf(
                requiredIdleMode,
                idleModes
        );
    }

    public DisposableIdlingResource subscribe(
            String name,
            Observable<Optional<RxListFragment.Mode>> modeOptionalObservable
    ) {
        final MutableIdlingResource mutableIdlingResource = BasicMutableIdlingResource.busy(name);
        IdlingResourceLog.initial(mutableIdlingResource);
        final Disposable disposable = modeOptionalObservable.subscribe(
                modeOptional -> {
                    IdlingResourceLog.v(
                            mutableIdlingResource,
                            () ->
                                    "before setState with " + modeOptional.orElse(null)
                    );
                    mutableIdlingResource.setState(
                            modeOptional
                                    .map(
                                            mode ->
                                                    idleModes.contains(mode) ? IDLE : BUSY
                                    )
                                    .orElse(
                                            IDLE
                                    )
                    );
                    IdlingResourceLog.v(
                            mutableIdlingResource,
                            () ->
                                    "after setState with " + modeOptional.orElse(null)
                    );
                }
        );
        return new BasicDisposableMutableIdlingResource(
                disposable,
                mutableIdlingResource
        );
    }
}
