package com.github.hborders.heathcast.core;

public interface Result {
    Result SUCCESS = new Result.Success();
    Result FAILURE = new Result.Failure();

    final class Success extends EmptyEither.Left<
            Success,
            Failure
            > implements Result {
        private Success() {
        }
    }

    final class Failure extends EmptyEither.Right<
            Success,
            Failure
            > implements Result {
        private Failure() {
        }
    }

    <T> T reduce(
            Function<
                    ? super Success,
                    ? extends T
                    > leftReducer,
            Function<
                    ? super Failure,
                    ? extends T
                    > rightReducer
    );

    void act(
            VoidFunction<? super Success> leftAction,
            VoidFunction<? super Failure> rightAction
    );
}
