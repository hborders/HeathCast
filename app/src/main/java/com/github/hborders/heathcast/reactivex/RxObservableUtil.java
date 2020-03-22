package com.github.hborders.heathcast.reactivex;

import com.github.hborders.heathcast.core.Function;

import java.util.Optional;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public class RxObservableUtil {
    public static <T> Maybe<T> maybeFromOptional(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> optional
    ) {
        return optional.map(Maybe::just).orElse(Maybe.empty());
    }

    public static <T, R> Observable<Optional<R>> switchMapOptional(
            Observable<Optional<T>> optionalObservable,
            Function<? super T, Observable<R>> mapper
    ) {
        return optionalObservable.switchMap(
                optional ->
                        optional
                                .map(
                                        value ->
                                                mapper
                                                        .apply(value)
                                                        .map(Optional::of)
                                )
                                .orElse(
                                        Observable.just(Optional.empty())
                                )
        );
    }

    public static <T, R> Observable<Optional<R>> switchMapOptionalFlatMap(
            Observable<Optional<T>> optionalObservable,
            Function<? super T, Observable<Optional<R>>> mapper
    ) {
        return optionalObservable.switchMap(
                optional ->
                        optional
                                .map(
                                        mapper::apply
                                )
                                .orElse(
                                        Observable.just(Optional.empty())
                                )
        );
    }

    public static <T, R> Observable<R> mapAndFilter(
            Observable<T> observable,
            Function<T, Optional<R>> mapper
    ) {
        return observable.switchMapMaybe(
                t ->
                        maybeFromOptional(
                                mapper.apply(t)
                        )
        );
    }
}
