package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public interface Either<
        LeftType extends Either.Left<
                LeftType,
                RightType,
                LeftValueType,
                RightValueType
                >,
        RightType extends Either.Right<
                LeftType,
                RightType,
                LeftValueType,
                RightValueType
                >,
        LeftValueType,
        RightValueType
        > {
    <T> T reduce(
            Function<
                    ? super LeftType,
                    ? extends T
                    > leftReducer,
            Function<
                    ? super RightType,
                    ? extends T
                    > rightReducer
    );

    void act(
            VoidFunction<? super LeftType> leftAction,
            VoidFunction<? super RightType> rightAction
    );

    // Not final to allow reification
    class Left<
            LeftType extends Either.Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Either.Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > implements Either<
            LeftType,
            RightType,
            LeftValueType,
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
            Left<?, ?, ?, ?> left = (Left<?, ?, ?, ?>) o;
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
                        ? super RightType,
                        ? extends T
                        > rightReducer
        ) {
            return leftReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftType> leftAction,
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
    class Right<
            LeftType extends Either.Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Either.Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > implements Either<
            LeftType,
            RightType,
            LeftValueType,
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
            Right<?, ?, ?, ?> right = (Right<?, ?, ?, ?>) o;
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
                        ? super RightType,
                        ? extends T
                        > rightReducer
        ) {
            return rightReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftType> leftAction,
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


