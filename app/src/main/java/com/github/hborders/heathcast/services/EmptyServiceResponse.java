package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.EmptyEither3;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

public interface EmptyServiceResponse {
    EmptyServiceResponse LOADING = new EmptyServiceResponse.Loading();
    EmptyServiceResponse COMPLETE = new EmptyServiceResponse.Complete();
    EmptyServiceResponse FAILED = new EmptyServiceResponse.Failed();

    final class Loading extends EmptyEither3.Left<
            Loading,
            Complete,
            Failed
            > implements EmptyServiceResponse {
        private Loading() {
        }
    }

    final class Complete extends EmptyEither3.Middle<
            Loading,
            Complete,
            Failed
            > implements EmptyServiceResponse {
        private Complete() {
        }
    }

    final class Failed extends EmptyEither3.Right<
            Loading,
            Complete,
            Failed
            > implements EmptyServiceResponse {
        private Failed() {
        }
    }

    <T> T reduce(
            Function<
                    ? super Loading,
                    ? extends T
                    > leftReducer,
            Function<
                    ? super Complete,
                    ? extends T
                    > middleReducer,
            Function<
                    ? super Failed,
                    ? extends T
                    > rightReducer
    );

    void act(
            VoidFunction<? super Loading> leftAction,
            VoidFunction<? super Complete> middleAction,
            VoidFunction<? super Failed> rightAction
    );
}
