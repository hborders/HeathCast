package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public abstract class Either3<
        LeftType extends Either3.Left<
                LeftType,
                MiddleType,
                RightType,
                LeftValueType,
                MiddleValueType,
                RightValueType
                >,
        MiddleType extends Either3.Middle<
                LeftType,
                MiddleType,
                RightType,
                LeftValueType,
                MiddleValueType,
                RightValueType
                >,
        RightType extends Either3.Right<
                LeftType,
                MiddleType,
                RightType,
                LeftValueType,
                MiddleValueType,
                RightValueType
                >,
        LeftValueType,
        MiddleValueType,
        RightValueType
        > {
    private Either3() {
    }

    public abstract <T> T reduce(
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

    public abstract void act(
            VoidFunction<? super LeftType> leftAction,
            VoidFunction<? super MiddleType> middleAction,
            VoidFunction<? super RightType> rightAction
    );

    // Not final to allow reification
    public static class Left<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > extends Either3<
            LeftType,
            MiddleType,
            RightType,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > {
        public final LeftValueType value;

        public Left(LeftValueType value) {
            this.value = value;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Left<?, ?, ?, ?, ?, ?> left = (Left<?, ?, ?, ?, ?, ?>) o;
            return value.equals(left.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends Left> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Left$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public final <T> T reduce(
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
        ) {
            return leftReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction
        ) {
            leftAction.apply(getSelf());
        }

        public LeftValueType getValue() {
            return value;
        }

        private LeftType getSelf() {
            return (LeftType) this;
        }
    }

    // Not final to allow reification
    public static class Middle<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > extends Either3<
            LeftType,
            MiddleType,
            RightType,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > {
        public final MiddleValueType value;

        public Middle(MiddleValueType value) {
            this.value = value;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Middle<?, ?, ?, ?, ?, ?> middle = (Middle<?, ?, ?, ?, ?, ?>) o;
            return value.equals(middle.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends Middle> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Middle$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public final <T> T reduce(
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
        ) {
            return middleReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction
        ) {
            middleAction.apply(getSelf());
        }

        public MiddleValueType getValue() {
            return value;
        }

        private MiddleType getSelf() {
            return (MiddleType) this;
        }
    }

    // Not final to allow reification
    public static class Right<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType,
                    LeftValueType,
                    MiddleValueType,
                    RightValueType
                    >,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > extends Either3<
            LeftType,
            MiddleType,
            RightType,
            LeftValueType,
            MiddleValueType,
            RightValueType
            > {
        public final RightValueType value;

        public Right(RightValueType value) {
            this.value = value;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Right<?, ?, ?, ?, ?, ?> right = (Right<?, ?, ?, ?, ?, ?>) o;
            return value.equals(right.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends Right> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Right$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public final <T> T reduce(
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
        ) {
            return rightReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction
        ) {
            rightAction.apply(getSelf());
        }

        public RightValueType getValue() {
            return value;
        }

        private RightType getSelf() {
            return (RightType) this;
        }
    }
}
