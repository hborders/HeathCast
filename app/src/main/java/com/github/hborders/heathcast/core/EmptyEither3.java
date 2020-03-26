package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public interface EmptyEither3<
        LeftType extends EmptyEither3.Left<
                LeftType,
                MiddleType,
                RightType
                >,
        MiddleType extends EmptyEither3.Middle<
                LeftType,
                MiddleType,
                RightType
                >,
        RightType extends EmptyEither3.Right<
                LeftType,
                MiddleType,
                RightType
                >
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

    interface Left<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > extends EmptyEither3<
            LeftType,
            MiddleType,
            RightType
            > {
    }

    // Not final to allow reification
    class LeftImpl<
            LeftType extends LeftImpl<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends MiddleImpl<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends RightImpl<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > implements Left<
            LeftType,
            MiddleType,
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
            @SuppressWarnings("rawtypes") final Class<? extends LeftImpl> clazz = getClass();
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

        private LeftType getSelf() {
            return (LeftType) this;
        }
    }

    interface Middle<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > extends EmptyEither3<
            LeftType,
            MiddleType,
            RightType
            > {
    }

    // Not final to allow reification
    class MiddleImpl<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > implements Middle<
            LeftType,
            MiddleType,
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
            @SuppressWarnings("rawtypes") final Class<? extends MiddleImpl> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Middle$";
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

        private MiddleType getSelf() {
            return (MiddleType) this;
        }
    }

    interface Right<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > extends EmptyEither3<
            LeftType,
            MiddleType,
            RightType
            > {
    }

    // Not final to allow reification
    class RightImpl<
            LeftType extends Left<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            MiddleType extends Middle<
                    LeftType,
                    MiddleType,
                    RightType
                    >,
            RightType extends Right<
                    LeftType,
                    MiddleType,
                    RightType
                    >
            > implements Right<
            LeftType,
            MiddleType,
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
            @SuppressWarnings("rawtypes") final Class<? extends RightImpl> clazz = getClass();
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

        private RightType getSelf() {
            return (RightType) this;
        }
    }
}
