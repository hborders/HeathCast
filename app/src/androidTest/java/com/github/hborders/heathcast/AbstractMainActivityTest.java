package com.github.hborders.heathcast;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.hborders.heathcast.activities.MainActivity;
import com.github.hborders.heathcast.idlingresource.DisposableIdlingResource;
import com.github.hborders.heathcast.idlingresource.DisposableIdlingResourceObserableSubscriber;
import com.github.hborders.heathcast.services.NetworkPauser;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AbstractMainActivityTest {
    @Rule public final TestName testName = new TestName();

    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private final Set<DisposableIdlingResource> disposableIdlingResources =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    interface ObservableProvider<T> {
        Observable<T> provideObservable(MainActivity mainActivity);
    }

    public <T> IdlingResource subscribeIdlingResource(
            String name,
            ObservableProvider<T> observableProvider,
            DisposableIdlingResourceObserableSubscriber<T> subscriber
    ) {
        final AtomicReference<DisposableIdlingResource> disposableIdlingResourceAtomicReference =
                new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(
                mainActivity ->
                        disposableIdlingResourceAtomicReference.set(
                                subscriber.subscribe(
                                        testName.getMethodName() + "-" + name,
                                        observableProvider.provideObservable(mainActivity)
                                )
                        )
        );
        @Nullable final DisposableIdlingResource disposableIdlingResource = disposableIdlingResourceAtomicReference.get();
        if (disposableIdlingResource == null) {
            throw new AssertionError();
        }
        disposableIdlingResources.add(disposableIdlingResource);
        return disposableIdlingResource;
    }

    public interface SetNetworkPausers {
        void setNetworkPausers(
                MainActivity mainActivity,
                NetworkPauser... networkPausers
        );
    }

    public void setNetworkPausers(
            SetNetworkPausers setNetworkPausers,
            NetworkPauser... networkPausers
    ) {
        activityScenarioRule.getScenario().onActivity(
                mainActivity ->
                        setNetworkPausers.setNetworkPausers(
                                mainActivity,
                                networkPausers
                        )
        );
    }

    @After
    public void tearDisposableIdlingResource() {
        for (final DisposableIdlingResource disposableIdlingResource : disposableIdlingResources) {
            IdlingRegistry.getInstance().unregister(disposableIdlingResource);
            disposableIdlingResource.dispose();
        }
    }

    @After
    public final void removeNetworkPausers() {
        activityScenarioRule.getScenario().onActivity(MainActivity::clearNetworkPausers);
    }
}
