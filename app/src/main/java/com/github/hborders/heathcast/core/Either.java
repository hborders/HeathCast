package com.github.hborders.heathcast.core;

import java.util.Objects;

public abstract class Either<L, R> {
    public static <L, R> Either<L, R> left(
            Class<L> reifiedLeftClass,
            L leftValue
    ) {
        return new Left<>(
                reifiedLeftClass,
                leftValue
        );
    }
    public static <L, R> Either<L, R> liftLeft(
            Class<L> reifiedLeftClass,
            Class<R> reifiedRightClass, // unused, helps with type inference
            L leftValue
    ) {
        return new Left<>(
                reifiedLeftClass,
                leftValue
        );
    }

    public static <L, R> Either<L, R> right(
            Class<R> reifiedRightClass,
            R rightValue
    ) {
        return new Right<>(
                reifiedRightClass,
                rightValue
        );
    }

    public static <L, R> Either<L, R> liftRight(
            Class<L> reifiedLeftClass, // unused, helps with type inference
            Class<R> reifiedRightClass,
            R rightValue
    ) {
        return new Right<>(
                reifiedRightClass,
                rightValue
        );
    }

    private Either() {
    }

    public abstract <T> T reduce(
            Function<L, T> leftFunction,
            Function<R, T> rightFunction
    );

    public static final class Left<L, R> extends Either<L, R> {
        public final Class<L> reifiedClass;
        public final L value;

        private Left(
                Class<L> reifiedClass,
                L value
        ) {
            this.reifiedClass = reifiedClass;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Left<?, ?> left = (Left<?, ?>) o;
            return reifiedClass.equals(left.reifiedClass) &&
                    value.equals(left.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reifiedClass, value);
        }

        @Override
        public String toString() {
            return "Left{" +
                    "reifiedClass=" + reifiedClass +
                    ", value=" + value +
                    '}';
        }

        @Override
        public <T> T reduce(
                Function<L, T> leftFunction,
                Function<R, T> rightFunction
        ) {
            return leftFunction.apply(value);
        }
    }

    public static final class Right<L, R> extends Either<L, R> {
        public final Class<R> reifiedClass;
        public final R value;

        private Right(
                Class<R> reifiedClass,
                R value
        ) {
            this.reifiedClass = reifiedClass;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Right<?, ?> right = (Right<?, ?>) o;
            return reifiedClass.equals(right.reifiedClass) &&
                    value.equals(right.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reifiedClass, value);
        }

        @Override
        public String toString() {
            return "Right{" +
                    "reifiedClass=" + reifiedClass +
                    ", value=" + value +
                    '}';
        }

        @Override
        public <T> T reduce(
                Function<L, T> leftFunction,
                Function<R, T> rightFunction
        ) {
            return rightFunction.apply(value);
        }
    }
}
