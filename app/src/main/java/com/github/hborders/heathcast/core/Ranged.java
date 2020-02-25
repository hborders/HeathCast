package com.github.hborders.heathcast.core;

import java.util.Objects;

import javax.annotation.Nullable;

public abstract class Ranged<E extends Comparable<E>> {
    private Ranged() {
    }

    public abstract <R> R reduceRanged(
            Function<Open<E>, R> openFunction,
            Function<OpenStart<E>, R> openStartFunction,
            Function<OpenEnd<E>, R> openEndFunction,
            Function<Closed<E>, R> closedFunction
    );

    public static <E extends Comparable<E>> Open<E> open() {
        return new Open<E>();
    }

    public static final class Open<E extends Comparable<E>> extends Ranged<E> {
        public Open() {
        }

        @Override
        public <R> R reduceRanged(
                Function<Open<E>, R> openFunction,
                Function<OpenStart<E>, R> openStartFunction,
                Function<OpenEnd<E>, R> openEndFunction,
                Function<Closed<E>, R> closedFunction
        ) {
            return openFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            return o != null && getClass() == o.getClass();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getClass());
        }

        @Override
        public String toString() {
            return "Open{}";
        }
    }

    public static final class OpenStart<E extends Comparable<E>> extends Ranged<E> {
        public final E end;

        public OpenStart(E end) {
            this.end = end;
        }

        @Override
        public <R> R reduceRanged(
                Function<Open<E>, R> openFunction,
                Function<OpenStart<E>, R> openStartFunction,
                Function<OpenEnd<E>, R> openEndFunction,
                Function<Closed<E>, R> closedFunction
        ) {
            return openStartFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OpenStart<?> openStart = (OpenStart<?>) o;
            return end.equals(openStart.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(end);
        }

        @Override
        public String toString() {
            return "OpenStart{" +
                    "end=" + end +
                    '}';
        }
    }

    public static final class OpenEnd<E extends Comparable<E>> extends Ranged<E> {
        public final E start;

        public OpenEnd(E start) {
            this.start = start;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OpenEnd<?> openEnd = (OpenEnd<?>) o;
            return start.equals(openEnd.start);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start);
        }

        @Override
        public String toString() {
            return "OpenEnd{" +
                    "start=" + start +
                    '}';
        }

        @Override
        public <R> R reduceRanged(
                Function<Open<E>, R> openFunction,
                Function<OpenStart<E>, R> openStartFunction,
                Function<OpenEnd<E>, R> openEndFunction,
                Function<Closed<E>, R> closedFunction
        ) {
            return openEndFunction.apply(this);
        }
    }

    public static final class Closed<E extends Comparable<E>> extends Ranged<E> {
        public final E start;
        public final E end;

        public Closed(E start, E end) {
            if (start.compareTo(end) > 0) {
                throw new IllegalArgumentException(
                        "start must be less than or equal to end: "
                                + " start: " + start
                                + " end: " + end
                );
            }

            this.start = start;
            this.end = end;
        }

        @Override
        public <R> R reduceRanged(
                Function<Open<E>, R> openFunction,
                Function<OpenStart<E>, R> openStartFunction,
                Function<OpenEnd<E>, R> openEndFunction,
                Function<Closed<E>, R> closedFunction
        ) {
            return closedFunction.apply(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Closed<?> closed = (Closed<?>) o;
            return start.equals(closed.start) &&
                    end.equals(closed.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "Closed{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
