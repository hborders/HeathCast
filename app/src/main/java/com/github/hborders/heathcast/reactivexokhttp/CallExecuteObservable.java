package com.github.hborders.heathcast.reactivexokhttp;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Call;
import okhttp3.Response;

// Copied from
// https://github.com/square/retrofit/blob/4faf9e217be8ed4cdc36af50fb15166ed8faecc5/retrofit-adapters/rxjava2/src/main/java/retrofit2/adapter/rxjava2/CallExecuteObservable.java
final class CallExecuteObservable extends Observable<Response> {
    private final Call originalCall;

    CallExecuteObservable(Call originalCall) {
        this.originalCall = originalCall;
    }

    @Override protected void subscribeActual(Observer<? super Response> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call call = originalCall.clone();
        CallDisposable disposable = new CallDisposable(call);
        observer.onSubscribe(disposable);
        if (disposable.isDisposed()) {
            return;
        }

        boolean terminated = false;
        try {
            Response response = call.execute();
            if (!disposable.isDisposed()) {
                observer.onNext(response);
            }
            if (!disposable.isDisposed()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!disposable.isDisposed()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private static final class CallDisposable implements Disposable {
        private final Call call;
        private volatile boolean disposed;

        CallDisposable(Call call) {
            this.call = call;
        }

        @Override public void dispose() {
            disposed = true;
            call.cancel();
        }

        @Override public boolean isDisposed() {
            return disposed;
        }
    }
}
