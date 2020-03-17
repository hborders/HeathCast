package com.github.hborders.heathcast.services;

public interface ServiceResponse1<
        LoadingType extends ServiceResponse1.Loading<
                LoadingType,
                CompleteType,
                FailedType,
                ValueType
                >,
        CompleteType extends ServiceResponse1.Complete<
                LoadingType,
                CompleteType,
                FailedType,
                ValueType
                >,
        FailedType extends ServiceResponse1.Failed<
                LoadingType,
                CompleteType,
                FailedType,
                ValueType
                >,
        ValueType
        > extends ServiceResponse<
        LoadingType,
        CompleteType,
        FailedType,
        ValueType,
        ValueType,
        ValueType
        > {
    abstract class Loading<
            LoadingType extends ServiceResponse1.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            CompleteType extends ServiceResponse1.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            FailedType extends ServiceResponse1.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            ValueType
            > extends ServiceResponse.Loading<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType,
            ValueType,
            ValueType
            > implements ServiceResponse1<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType
            > {
        public Loading(ValueType value) {
            super(value);
        }
    }

    abstract class Complete<
            LoadingType extends ServiceResponse1.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            CompleteType extends ServiceResponse1.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            FailedType extends ServiceResponse1.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            ValueType
            > extends ServiceResponse.Complete<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType,
            ValueType,
            ValueType
            > implements ServiceResponse1<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType
            > {
        public Complete(ValueType value) {
            super(value);
        }
    }

    abstract class Failed<
            LoadingType extends ServiceResponse1.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            CompleteType extends ServiceResponse1.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            FailedType extends ServiceResponse1.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ValueType
                    >,
            ValueType
            > extends ServiceResponse.Failed<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType,
            ValueType,
            ValueType
            > implements ServiceResponse1<
            LoadingType,
            CompleteType,
            FailedType,
            ValueType
            > {
        public Failed(ValueType value) {
            super(value);
        }
    }

    ValueType getValue();
}
