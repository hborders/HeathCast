package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Objects;

public interface EmptyEither6<
        LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        LeftLeftType extends EmptyEither6.LeftLeft<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        LeftType extends EmptyEither6.Left<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightType extends EmptyEither6.Right<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightRightType extends EmptyEither6.RightRight<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
                RightType,
                RightRightType,
                RightRightRightType
                >,
        RightRightRightType extends EmptyEither6.RightRightRight<
                LeftLeftLeftType,
                LeftLeftType,
                LeftType,
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
            VoidFunction<? super RightType> rightAction,
            VoidFunction<? super RightRightType> rightRightAction,
            VoidFunction<? super RightRightRightType> rightRightRightAction
    );

    // Not final to allow reification
    class LeftLeftLeft<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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
            @SuppressWarnings("rawtypes") final Class<? extends LeftLeftLeft> clazz = getClass();
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

    // Not final to allow reification
    class LeftLeft<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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
            @SuppressWarnings("rawtypes") final Class<? extends LeftLeft> clazz = getClass();
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

    // Not final to allow reification
    class Left<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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

    // Not final to allow reification
    class Right<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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

    // Not final to allow reification
    class RightRight<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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
            @SuppressWarnings("rawtypes") final Class<? extends RightRight> clazz = getClass();
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

    // Not final to allow reification
    class RightRightRight<
            LeftLeftLeftType extends EmptyEither6.LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends EmptyEither6.LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends EmptyEither6.Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends EmptyEither6.Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends EmptyEither6.RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends EmptyEither6.RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
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
            @SuppressWarnings("rawtypes") final Class<? extends RightRightRight> clazz = getClass();
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
