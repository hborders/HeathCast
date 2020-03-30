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

    interface LeftLeftLeft<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class LeftLeftLeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements LeftLeftLeft<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
        private final Class<LeftLeftLeftType> selfClass;

        protected LeftLeftLeftImpl(Class<LeftLeftLeftType> selfClass) {
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface LeftLeft<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class LeftLeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements LeftLeft<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
        private final Class<LeftLeftType> selfClass;

        protected LeftLeftImpl(Class<LeftLeftType> selfClass) {
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface Left<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class LeftImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements Left<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface Right<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class RightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements Right<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface RightRight<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class RightRightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements RightRight<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
        private final Class<RightRightType> selfClass;

        protected RightRightImpl(Class<RightRightType> selfClass) {
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }

    interface RightRightRight<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > extends EmptyEither6<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
    }

    // Not final to allow reification
    abstract class RightRightRightImpl<
            LeftLeftLeftType extends LeftLeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftLeftType extends LeftLeft<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            LeftType extends Left<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightType extends Right<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightType extends RightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >,
            RightRightRightType extends RightRightRight<
                    LeftLeftLeftType,
                    LeftLeftType,
                    LeftType,
                    RightType,
                    RightRightType,
                    RightRightRightType
                    >
            > implements RightRightRight<
            LeftLeftLeftType,
            LeftLeftType,
            LeftType,
            RightType,
            RightRightType,
            RightRightRightType
            > {
        private final Class<RightRightRightType> selfClass;

        protected RightRightRightImpl(Class<RightRightRightType> selfClass) {
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
            return Objects.requireNonNull(selfClass.cast(this));
        }
    }
}
