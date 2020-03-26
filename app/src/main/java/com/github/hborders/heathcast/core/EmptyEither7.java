package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public interface EmptyEither7<
        LeftLeftLeftType extends EmptyEither7.LeftLeftLeft<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        LeftLeftType extends EmptyEither7.LeftLeft<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        LeftType extends EmptyEither7.Left<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        MiddleType extends EmptyEither7.Middle<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightType extends EmptyEither7.Right<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightRightType extends EmptyEither7.RightRight<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightRightRightType extends EmptyEither7.RightRightRight<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                MiddleType,
                RightType,
                RightRightType,
                RightRightRightType
                >
        > {
    <T> T reduce(
            Function<
                    ? super LeftLeftLeftType,
                    ? extends T
                    > leftLeftLeftReducer,
            Function<
                    ? super LeftLeftType,
                    ? extends T
                    > leftLeftReducer,
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
                    > rightReducer,
            Function<
                    ? super RightRightType,
                    ? extends T
                    > rightRightReducer,
            Function<
                    ? super RightRightRightType,
                    ? extends T
                    > rightRightRightReducer
    );

    void act(
            VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
            VoidFunction<? super LeftLeftType> leftLeftAction,
            VoidFunction<? super LeftType> leftAction,
            VoidFunction<? super MiddleType> middleAction,
            VoidFunction<? super RightType> rightAction,
            VoidFunction<? super RightRightType> rightRightAction,
            VoidFunction<? super RightRightRightType> rightRightRightAction
    );

    interface LeftLeftLeft<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class LeftLeftLeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements LeftLeftLeft<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
            @SuppressWarnings("rawtypes") final Class<? extends LeftLeftLeftImpl> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "LeftLeftLeft$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    '}';
        }

        @Override
        public final <T> T reduce(
                Function<
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return leftLeftLeftReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            leftLeftLeftAction.apply(getSelf());
        }

        private LeftLeftLeftType getSelf() {
            return (LeftLeftLeftType) this;
        }
    }

    interface LeftLeft<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class LeftLeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements LeftLeft<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
            @SuppressWarnings("rawtypes") final Class<? extends LeftLeftImpl> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "LeftLeft$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    '}';
        }

        @Override
        public final <T> T reduce(
                Function<
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return leftLeftReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            leftLeftAction.apply(getSelf());
        }

        private LeftLeftType getSelf() {
            return (LeftLeftType) this;
        }
    }

    interface Left<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class LeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements Left<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return leftReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            leftAction.apply(getSelf());
        }


        private LeftType getSelf() {
            return (LeftType) this;
        }
    }

    interface Middle<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class MiddleImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements Middle<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return middleReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            middleAction.apply(getSelf());
        }

        private MiddleType getSelf() {
            return (MiddleType) this;
        }
    }

    interface Right<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class RightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements Right<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return rightReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            rightAction.apply(getSelf());
        }

        private RightType getSelf() {
            return (RightType) this;
        }
    }

    interface RightRight<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class RightRightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements RightRight<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
            @SuppressWarnings("rawtypes") final Class<? extends RightRightImpl> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "RightRight$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    '}';
        }

        @Override
        public final <T> T reduce(
                Function<
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return rightRightReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            rightRightAction.apply(getSelf());
        }

        private RightRightType getSelf() {
            return (RightRightType) this;
        }
    }

    interface RightRightRight<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither7<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    class RightRightRightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            MiddleType extends Middle<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    MiddleType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements RightRightRight<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            MiddleType,
            RightType,
            RightRightType,
            RightRightRightType
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
            @SuppressWarnings("rawtypes") final Class<? extends RightRightRightImpl> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "RightRightRight$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    '}';
        }

        @Override
        public final <T> T reduce(
                Function<
                        ? super LeftLeftLeftType,
                        ? extends T
                        > leftLeftLeftReducer,
                Function<
                        ? super LeftLeftType,
                        ? extends T
                        > leftLeftReducer,
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
                        > rightReducer,
                Function<
                        ? super RightRightType,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super RightRightRightType,
                        ? extends T
                        > rightRightRightReducer
        ) {
            return rightRightRightReducer.apply(getSelf());
        }

        @Override
        public final void act(
                VoidFunction<? super LeftLeftLeftType> leftLeftLeftAction,
                VoidFunction<? super LeftLeftType> leftLeftAction,
                VoidFunction<? super LeftType> leftAction,
                VoidFunction<? super MiddleType> middleAction,
                VoidFunction<? super RightType> rightAction,
                VoidFunction<? super RightRightType> rightRightAction,
                VoidFunction<? super RightRightRightType> rightRightRightAction
        ) {
            rightRightRightAction.apply(getSelf());
        }

        private RightRightRightType getSelf() {
            return (RightRightRightType) this;
        }
    }
}
