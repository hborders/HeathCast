package com.github.hborders.heathcast.core;

public interface Result {
    Result SUCCESS = new Result.Success();
    Result FAILURE = new Result.Failure();

    final class Success extends EmptyEither.LeftImpl<
            Success,
            Failure
            > implements Result {
        public Success() {
            super(Success.class);
        }
    }

    final class Failure extends EmptyEither.RightImpl<
            Success,
            Failure
            > implements Result {
        public Failure() {
            super(Failure.class);
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
