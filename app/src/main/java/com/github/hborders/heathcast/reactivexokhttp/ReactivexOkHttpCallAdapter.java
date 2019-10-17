package com.github.hborders.heathcast.reactivexokhttp;

import androidx.annotation.Nullable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Call;
import okhttp3.Response;

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
