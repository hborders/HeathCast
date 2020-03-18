package com.github.hborders.heathcast.services;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;

public final class NetworkPauser {
    private final Object monitor = new Object();
    private boolean paused = true;
    private final HashSet<CompletableEmitter> completableEmitters = new HashSet<>();
    private final Completable completable;

    public NetworkPauser() {
        completable = Completable.create(emitter -> {
            final HashSet<CompletableEmitter> completingCompletableEmitters = new HashSet<>();
            synchronized (monitor) {
                if (paused) {
                    completableEmitters.add(emitter);
                } else {
                    completingCompletableEmitters.add(emitter);
                    completingCompletableEmitters.addAll(completableEmitters);
                    completableEmitters.clear();
                }
            }

            completingCompletableEmitters.forEach(CompletableEmitter::onComplete);
        });
    }

    public <T> Single<T> pauseSingle(Single<T> single) {
        return completable.andThen(single);
    }

    public void resume() {
        final Set<CompletableEmitter> completingCompletableEmitters;
        synchronized (monitor) {
            if (paused) {
                paused = false;
                completingCompletableEmitters = new HashSet<>(completableEmitters);
                completableEmitters.clear();
            } else {
                throw new IllegalStateException("Should only resume once");
            }
        }

        completingCompletableEmitters.forEach(CompletableEmitter::onComplete);
    }
}
