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
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > implements EmptyEither<
            LeftType,
            RightType
            > {
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
            @SuppressWarnings("rawtypes") final Class<? extends Left> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Left$";
            } else {
                simpleName = clazz.getSimpleName();
            }

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
            return (LeftType) this;
        }
    }

    // Not final to allow reification
    class Right<
            LeftType extends EmptyEither.Left<
                    LeftType,
                    RightType
                    >,
            RightType extends EmptyEither.Right<
                    LeftType,
                    RightType
                    >
            > implements EmptyEither<
            LeftType,
            RightType
            > {
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
            @SuppressWarnings("rawtypes") final Class<? extends Right> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Right$";
            } else {
                simpleName = clazz.getSimpleName();
            }

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
            return (RightType) this;
        }
    }
}


