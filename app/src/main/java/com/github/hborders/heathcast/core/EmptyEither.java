package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public interface EmptyEither<
        LeftType extends EmptyEither.Left<
                LeftType,
                RightType
                >,
        RightType extends EmptyEither.Right<
                LeftType,
                RightType
                >
        > {
    interface Left<
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > extends EmptyEither<
            LeftType,
            RightType
            > {
    }

    // Not final to allow reification
    abstract class LeftImpl<
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > implements Left<
            LeftType,
            RightType
            > {
        private final Class<LeftType> selfClass;

        protected LeftImpl(Class<LeftType> selfClass) {
            this.selfClass = selfClass;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public final int hashCode() {
            return Objects.hash(getClass());
        }

        @Override
        public final String toString() {
            final String simpleName = ClassUtil.getSpecificSimpleName(
                    LeftImpl.class,
                    getClass()
            );

            return simpleName + "{" +
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

        private LeftType getSelf() {
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface Right<
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > extends EmptyEither<
            LeftType,
            RightType
            > {
    }

    // Not final to allow reification
    abstract class RightImpl<
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > implements Right<
            LeftType,
            RightType
            > {
        private final Class<RightType> selfClass;

        protected RightImpl(Class<RightType> selfClass) {
            this.selfClass = selfClass;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public final int hashCode() {
            return Objects.hash(getClass());
        }

        @Override
        public final String toString() {
            final String simpleName = ClassUtil.getSpecificSimpleName(
                    RightImpl.class,
                    getClass()
            );

            return simpleName + "{" +
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

        private RightType getSelf() {
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

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
}


