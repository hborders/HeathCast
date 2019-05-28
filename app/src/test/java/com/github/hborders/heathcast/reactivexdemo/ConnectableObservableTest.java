package com.github.hborders.heathcast.reactivexdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class ConnectableObservableTest {
    @Test
    public void testConnectableObservable() {
        final List<ObservableEmitter<String>> observableEmitters =
                Collections.synchronizedList(new ArrayList<>());
        final ConnectableObservable<String> testObject =
                Observable.<String>create(e -> {
                    observableEmitters.add(e);
                    e.setCancellable(() -> observableEmitters.remove(e));
                }).replay(1);

        final TestObserver<String> testObserver1 = new TestObserver<>();
        final TestObserver<String> testObserver2 = new TestObserver<>();

        testObject.subscribe(testObserver1);
        testObject.subscribe(testObserver2);

        testObserver1.assertNoValues();
        testObserver2.assertNoValues();

        assertEquals(0, observableEmitters.size());

        final Disposable disposable1 = testObject.connect();

        assertEquals(1, observableEmitters.size());

        observableEmitters.get(0).onNext("1");

        testObserver1.assertValues("1");
        testObserver2.assertValues("1");

        disposable1.dispose();

        assertEquals(0, observableEmitters.size());

        final TestObserver<String> testObserver3 = new TestObserver<>();
        final TestObserver<String> testObserver4 = new TestObserver<>();

        testObject.subscribe(testObserver3);
        testObject.subscribe(testObserver4);

        testObserver3.assertValues("1");
        testObserver4.assertValues("1");

        assertEquals(0, observableEmitters.size());

        // after every "connect", all state of the "replay" resets

        final Disposable disposable2 = testObject.connect();

        assertEquals(1, observableEmitters.size());

        final TestObserver<String> testObserver5 = new TestObserver<>();
        testObject.subscribe(testObserver5);

        testObserver5.assertNoValues();

        observableEmitters.get(0).onNext("2");

        testObserver5.assertValues("2");

        final TestObserver<String> testObserver6 = new TestObserver<>();
        testObject.subscribe(testObserver6);

        testObserver6.assertValues("2");

        testObserver1.assertValues("1");
        testObserver2.assertValues("1");

        testObserver3.assertValues("1");
        testObserver4.assertValues("1");

        disposable2.dispose();
    }
}
