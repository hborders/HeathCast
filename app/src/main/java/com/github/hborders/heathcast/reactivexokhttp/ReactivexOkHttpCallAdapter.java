package com.github.hborders.heathcast.reactivexokhttp;

import androidx.annotation.Nullable;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import okhttp3.Call;
import okhttp3.Response;

// Slightly modified version of
// https://github.com/square/retrofit/blob/4faf9e217be8ed4cdc36af50fb15166ed8faecc5/retrofit-adapters/rxjava2/src/main/java/retrofit2/adapter/rxjava2/RxJava2CallAdapter.java
public final class ReactivexOkHttpCallAdapter {
    private final boolean isAsync;
    @Nullable
    private final Scheduler scheduler;

    public static ReactivexOkHttpCallAdapter create() {
        return new ReactivexOkHttpCallAdapter(false, null);
    }

    public static ReactivexOkHttpCallAdapter createAsync() {
        return new ReactivexOkHttpCallAdapter(true, null);
    }

    public static ReactivexOkHttpCallAdapter createWithScheduler(Scheduler scheduler) {
        return new ReactivexOkHttpCallAdapter(false, scheduler);
    }

    private ReactivexOkHttpCallAdapter(boolean isAsync, @Nullable Scheduler scheduler) {
        this.isAsync = isAsync;
        this.scheduler = scheduler;
    }

    public Completable completable(Call call) {
        Observable<Response> observable = unassembledObservable(call);
        return observable.ignoreElements();
    }

    public Flowable<Response> flowable(Call call) {
        Observable<Response> observable = unassembledObservable(call);
        return observable.toFlowable(BackpressureStrategy.LATEST);
    }

    public Maybe<Response> maybe(Call call) {
        Observable<Response> observable = unassembledObservable(call);
        return observable.singleElement();
    }

    public Observable<Response> observable(Call call) {
        Observable<Response> observable = unassembledObservable(call);
        return RxJavaPlugins.onAssembly(observable);
    }

    public Single<Response> single(Call call) {
        Observable<Response> observable = unassembledObservable(call);
        return observable.singleOrError();
    }

    private Observable<Response> unassembledObservable(Call call) {
        final Observable<Response> originalObservable;
        if (isAsync) {
            originalObservable = new CallExecuteObservable(call);
        } else {
            originalObservable = new CallEnqueueObservable(call);
        }

        final Observable<Response> observable;
        if (scheduler == null) {
            observable = originalObservable;
        } else {
            observable = originalObservable.subscribeOn(scheduler);
        }

        return observable;
    }
}
