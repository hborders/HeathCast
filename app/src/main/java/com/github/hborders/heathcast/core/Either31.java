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

    // Not final to allow reification
    class Left<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Right<
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
        public Left(ValueType value) {
            super(value);
        }
    }

    // Not final to allow reification
    class Middle<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Right<
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
        public Middle(ValueType value) {
            super(value);
        }
    }

    // Not final to allow reification
    class Right<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    ValueType
                    >,
            RightType extends Right<
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
        public Right(ValueType value) {
            super(value);
        }
    }
}
