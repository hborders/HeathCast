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

    interface Left<
            LeftType extends Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > extends Either<
            LeftType,
            RightType,
            LeftValueType,
            RightValueType
            > {
    }

    // Not final to allow reification
    abstract class LeftImpl<
            LeftType extends Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > implements Left<
            LeftType,
            RightType,
            LeftValueType,
            RightValueType
            > {
        private final Class<LeftType> selfClass;
        public final LeftValueType value;

        protected LeftImpl(
                Class<LeftType> selfClass,
                LeftValueType value
        ) {
            this.selfClass = selfClass;
            this.value = value;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LeftImpl<?, ?, ?, ?> left = (LeftImpl<?, ?, ?, ?>) o;
            return value.equals(left.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends LeftImpl> clazz = getClass();
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface Right<
            LeftType extends Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > extends Either<
            LeftType,
            RightType,
            LeftValueType,
            RightValueType
            > {
    }

    // Not final to allow reification
    abstract class RightImpl<
            LeftType extends Left<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            RightType extends Right<
                    LeftType,
                    RightType,
                    LeftValueType,
                    RightValueType
                    >,
            LeftValueType,
            RightValueType
            > implements Right<
            LeftType,
            RightType,
            LeftValueType,
            RightValueType
            > {
        private final Class<RightType> selfClass;
        public final RightValueType value;

        protected RightImpl(
                Class<RightType> selfClass,
                RightValueType value
        ) {
            this.selfClass = selfClass;
            this.value = value;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RightImpl<?, ?, ?, ?> right = (RightImpl<?, ?, ?, ?>) o;
            return value.equals(right.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends RightImpl> clazz = getClass();
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }
}


