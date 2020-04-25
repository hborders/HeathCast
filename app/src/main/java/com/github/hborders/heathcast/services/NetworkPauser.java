package com.github.hborders.heathcast.services;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public final class NetworkPauser {
    private final Object monitor = new Object();
    private boolean paused = true;
    private final CompletableSubject completableSubject = CompletableSubject.create();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <T> Single<T> pauseSingle(Single<T> single) {
        return completableSubject.andThen(single);
    }

    public void resume() throws InterruptedException {
        final boolean unpaused;
        synchronized (monitor) {
            unpaused = paused;
            paused = false;
        }

        if (unpaused) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                completableSubject.onComplete();
            } else {
                // We need to wait until the main thread

                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final boolean postedOnComplete = handler.post(completableSubject::onComplete);
                if (!postedOnComplete) {
                    throw new IllegalStateException("Failed to post onComplete");
                }
                // give the main thread a chance to react to the onComplete
                final boolean postedCountDown = handler.post(countDownLatch::countDown);
                if (!postedCountDown) {
                    throw new IllegalStateException("Failed to post countDown");
                }
                final boolean countedDown = countDownLatch.await(5, TimeUnit.SECONDS);
                if (!countedDown) {
                    throw new IllegalStateException("Looping the main thread never counted down");
                }
            }
        }
    }
}
