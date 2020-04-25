package com.github.hborders.heathcast.reactivex;

import com.github.hborders.heathcast.core.Function;

import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public class RxObservableUtil {
    public static <T> Maybe<T> maybeFromOptional(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> optional
    ) {
        return optional.map(Maybe::just).orElse(Maybe.empty());
    }

//    public static <
//            T
//            > Observable<T> ifNonEmptyConcat(
//            Observable<T> observable,
//            T lastValue
//    ) {
//        final Observable<T> foo = Observable.combineLatest(
//                observable,
//                Observable.just(
//                        true,
//                        false
//                ),
//                (value, first) -> {
//
//                }
//        );
//
//        final Observable<Unknown<T>> nonEmptyUnknownObservable =
//                observable.map(
//                        Unknown.NonEmpty::new
//                );
//        final Observable<Unknown<T>> emptyOrNonEmptyUnknownObservable =
//                nonEmptyUnknownObservable.defaultIfEmpty(
//                        new Unknown.Empty<>()
//                );
//        final Observable<Known<T>> knownObservable = emptyOrNonEmptyUnknownObservable.map(
//                unknown ->
//                        unknown.reduce(
//                                empty -> new Known.Empty<>(),
//                                nonEmpty -> new Known.NonEmpty<>(nonEmpty.value),
//                                end -> { throw new AssertionError(); }
//                        )
//        );
//
//        observable.redu
//
//        final Observable<Unknown<T>> unknownObservable =
//                emptyOrNonEmptyUnknownObservable.concatWith(
//                        Single.just(new Unknown.End<>())
//                );
//        final Observable<Known<T>> knownObservable2 = unknownObservable.scan(
//                new Known.Empty<>(),
//                (known, unknown) ->
//                        unknown.reduce(
//                                empty -> new Known.Empty<>(),
//                                nonEmpty -> new Known.NonEmpty<>(nonEmpty.value),
//                                end -> known.reduce(
//                                        empty -> new Known.Empty<>(),
//                                        nonEmpty -> new Known.NonEmptyEnd<>(),
//                                        nonEmptyEnd -> {
//                                            throw new AssertionError();
//                                        }
//                                )
//                        )
//        );
//
//    }

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
//
//    private interface Unknown<ValueType> extends Either3<
//            Unknown.Empty<ValueType>,
//            Unknown.NonEmpty<ValueType>,
//            Unknown.End<ValueType>,
//            Nothing,
//            ValueType,
//            Nothing
//            > {
//        final class Empty<ValueType> extends Either3.LeftImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                End<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Unknown<ValueType> {
//            public Empty() {
//                super(Nothing.INSTANCE);
//            }
//        }
//
//        final class NonEmpty<ValueType> extends Either3.MiddleImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                End<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Unknown<ValueType> {
//            public NonEmpty(ValueType value) {
//                super(value);
//            }
//        }
//
//        final class End<ValueType> extends Either3.RightImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                End<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Unknown<ValueType> {
//            public End() {
//                super(Nothing.INSTANCE);
//            }
//        }
//
//        <T> T reduce(
//                Function<
//                        ? super Empty<ValueType>,
//                        ? extends T
//                        > emptyReducer,
//                Function<
//                        ? super NonEmpty<ValueType>,
//                        ? extends T
//                        > nonEmptyReducer,
//                Function<
//                        ? super End<ValueType>,
//                        ? extends T
//                        > endReducer
//        );
//
//        void act(
//                VoidFunction<? super Empty<ValueType>> emptyAction,
//                VoidFunction<? super NonEmpty<ValueType>> nonEmptyAction,
//                VoidFunction<? super End<ValueType>> endAction
//        );
//    }
//
//    private interface Known<ValueType> extends Either3<
//            Known.Empty<ValueType>,
//            Known.NonEmpty<ValueType>,
//            Known.NonEmptyEnd<ValueType>,
//            Nothing,
//            ValueType,
//            Nothing
//            > {
//        final class Empty<ValueType> extends Either3.LeftImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                NonEmptyEnd<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Known<ValueType> {
//            public Empty() {
//                super(Nothing.INSTANCE);
//            }
//        }
//
//        final class NonEmpty<ValueType> extends Either3.MiddleImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                NonEmptyEnd<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Known<ValueType> {
//            public NonEmpty(ValueType value) {
//                super(value);
//            }
//        }
//
//        final class NonEmptyEnd<ValueType> extends Either3.RightImpl<
//                Empty<ValueType>,
//                NonEmpty<ValueType>,
//                NonEmptyEnd<ValueType>,
//                Nothing,
//                ValueType,
//                Nothing
//                > implements Known<ValueType> {
//            public NonEmptyEnd() {
//                super(Nothing.INSTANCE);
//            }
//        }
//
//        <T> T reduce(
//                Function<
//                        ? super Empty<ValueType>,
//                        ? extends T
//                        > emptyReducer,
//                Function<
//                        ? super NonEmpty<ValueType>,
//                        ? extends T
//                        > nonEmptyReducer,
//                Function<
//                        ? super NonEmptyEnd<ValueType>,
//                        ? extends T
//                        > endReducer
//        );
//
//        void act(
//                VoidFunction<? super Empty<ValueType>> emptyAction,
//                VoidFunction<? super NonEmpty<ValueType>> nonEmptyAction,
//                VoidFunction<? super NonEmptyEnd<ValueType>> endAction
//        );
//    }
}
