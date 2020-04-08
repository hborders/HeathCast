package com.github.hborders.heathcast.core;

public interface Either31<
        LeftType extends Either31.Left<
                LeftType,
                MiddleType,
                RightType,
                ValueType
                >,
        MiddleType extends Either31.Middle<
                LeftType,
                MiddleType,
                RightType,
                ValueType
                >,
        RightType extends Either31.Right<
                LeftType,
                MiddleType,
                RightType,
                ValueType
                >,
        ValueType
        > {
    <T> T reduce(
            Function<
                    ? super LeftType,
                    ? extends T
                    > leftReducer,
            Function<
                    ? super MiddleType,
                    ? extends T
                    > middleReducer,
            Function<
                    ? super RightType,
                    ? extends T
                    > rightReducer
    );

    void act(
            VoidFunction<? super LeftType> leftAction,
            VoidFunction<? super MiddleType> middleAction,
            VoidFunction<? super RightType> rightAction
    );

    ValueType getValue();

    interface Left<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.Left<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > {
    }

    // Not final to allow reification
    abstract class LeftImpl<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.LeftImpl<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > implements Left<
            LeftType,
            MiddleType,
            RightType,
            ValueType
            > {
        protected LeftImpl(
                Class<LeftType> selfClass,
                ValueType value
        ) {
            super(
                    selfClass,
                    value
            );
        }
    }

    interface Middle<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.Middle<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > {
    }

    // Not final to allow reification
    abstract class MiddleImpl<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.MiddleImpl<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > implements Middle<
            LeftType,
            MiddleType,
            RightType,
            ValueType
            > {
        protected MiddleImpl(
                Class<MiddleType> selfClass,
                ValueType value
        ) {
            super(
                    selfClass,
                    value
            );
        }
    }

    interface Right<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.Right<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > {
    }

    // Not final to allow reification
    abstract class RightImpl<
            LeftType extends Either31.Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Either31.Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Either31.Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            ValueType
            > extends Either3.RightImpl<
            LeftType,
            MiddleType,
            RightType,
            ValueType,
            ValueType,
            ValueType
            > implements Right<
            LeftType,
            MiddleType,
            RightType,
            ValueType
            > {
        protected RightImpl(
                Class<RightType> selfClass,
                ValueType value
        ) {
            super(
                    selfClass,
                    value
            );
        }
    }
}
