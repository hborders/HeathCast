package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.Either3;

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
        > extends Either3<
        LoadingType,
        CompleteType,
        FailedType,
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
            > extends LeftImpl<
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
            > extends MiddleImpl<
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
            > extends RightImpl<
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
}
