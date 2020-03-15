package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.Either3;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

public interface ServiceResponse<
        LoadingType extends ServiceResponse.Loading<
                LoadingType,
                CompleteType,
                FailedType,
                LoadingValueType,
                CompleteValueType,
                FailedValueType
                >,
        CompleteType extends ServiceResponse.Complete<
                LoadingType,
                CompleteType,
                FailedType,
                LoadingValueType,
                CompleteValueType,
                FailedValueType
                >,
        FailedType extends ServiceResponse.Failed<
                LoadingType,
                CompleteType,
                FailedType,
                LoadingValueType,
                CompleteValueType,
                FailedValueType
                >,
        LoadingValueType,
        CompleteValueType,
        FailedValueType
        > {
    abstract class Loading<
            LoadingType extends Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends Either3.Left<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > implements ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
        public Loading(LoadingValueType value) {
            super(value);
        }
    }

    abstract class Complete<
            LoadingType extends Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends Either3.Middle<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > implements ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
        public Complete(CompleteValueType value) {
            super(value);
        }
    }

    abstract class Failed<
            LoadingType extends Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends Either3.Right<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > implements ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
        public Failed(FailedValueType value) {
            super(value);
        }
    }

    <T> T reduce(
            Function<? super LoadingType, ? extends T> leftReducer,
            Function<? super CompleteType, ? extends T> middleReducer,
            Function<? super FailedType, ? extends T> rightReducer
    );

    void act(
            VoidFunction<? super LoadingType> leftAction,
            VoidFunction<? super CompleteType> middleAction,
            VoidFunction<? super FailedType> rightAction
    );
}
