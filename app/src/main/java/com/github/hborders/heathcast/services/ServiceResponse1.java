package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.core.Either31;

public interface ServiceResponse1<
        Loading1Type extends ServiceResponse1.Loading1<
                Loading1Type,
                Complete1Type,
                Failed1Type,
                ValueType
                >,
        Complete1Type extends ServiceResponse1.Complete1<
                Loading1Type,
                Complete1Type,
                Failed1Type,
                ValueType
                >,
        Failed1Type extends ServiceResponse1.Failed1<
                Loading1Type,
                Complete1Type,
                Failed1Type,
                ValueType
                >,
        ValueType
        > extends ServiceResponse<
        Loading1Type,
        Complete1Type,
        Failed1Type,
        ValueType,
        ValueType,
        ValueType
        > {
    interface Loading1<
            Loading1Type extends Loading1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Complete1Type extends Complete1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Failed1Type extends Failed1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            ValueType
            > extends Either31.Left<
            Loading1Type,
            Complete1Type,
            Failed1Type,
            ValueType
            >,
            ServiceResponse.Loading<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType,
                    ValueType,
                    ValueType
                    >, ServiceResponse1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    > {
    }

    interface Complete1<
            Loading1Type extends Loading1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Complete1Type extends Complete1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Failed1Type extends Failed1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            ValueType
            > extends Either31.Middle<
            Loading1Type,
            Complete1Type,
            Failed1Type,
            ValueType
            >,
            ServiceResponse.Complete<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType,
                    ValueType,
                    ValueType
                    >, ServiceResponse1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    > {
    }

    interface Failed1<
            Loading1Type extends Loading1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Complete1Type extends Complete1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            Failed1Type extends Failed1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    >,
            ValueType
            > extends Either31.Right<
            Loading1Type,
            Complete1Type,
            Failed1Type,
            ValueType
            >,
            ServiceResponse.Failed<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType,
                    ValueType,
                    ValueType
                    >, ServiceResponse1<
                    Loading1Type,
                    Complete1Type,
                    Failed1Type,
                    ValueType
                    > {
    }

    ValueType getValue();
}
