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
    interface Loading<
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
            > extends Left<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            >, ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
    }

    interface Complete<
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
            > extends Middle<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            >, ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
    }

    interface Failed<
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
            > extends Right<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            >, ServiceResponse<
            LoadingType,
            CompleteType,
            FailedType,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > {
    }
}
