package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// This doesn't work because we can't satisfy both Either and List semantics at the same time.
// List requires equals symmetry for all List interfaces, and satisfying Either equals semantics will break that.
public interface EitherNonEmptyList<
        EmptyType extends EitherNonEmptyList.Empty<
                EmptyType,
                NonEmptyType,
                ListType,
                ItemType
                >,
        NonEmptyType extends EitherNonEmptyList.NonEmpty<
                EmptyType,
                NonEmptyType,
                ListType,
                ItemType
                >,
        ListType extends List<ItemType>,
        ItemType
        > extends Either<
        EmptyType,
        NonEmptyType,
        Nothing,
        ListType
        >, List<ItemType> {
    interface Empty<
            EmptyType extends Empty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            NonEmptyType extends NonEmpty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            ListType extends List<ItemType>,
            ItemType
            > extends Either.Left<
            EmptyType,
            NonEmptyType,
            Nothing,
            ListType
            >, EitherNonEmptyList<
            EmptyType,
            NonEmptyType,
            ListType,
            ItemType
            > {
    }

    abstract class EmptyImpl<
            EmptyType extends Empty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            NonEmptyType extends NonEmpty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            ListType extends List<ItemType>,
            ItemType
            > extends Either.LeftImpl<
            EmptyType,
            NonEmptyType,
            Nothing,
            ListType
            > implements Empty<
            EmptyType,
            NonEmptyType,
            ListType,
            ItemType
            > {
        protected EmptyImpl(Class<EmptyType> selfClass) {
            super(

                    selfClass,
                    Nothing.INSTANCE
            );
        }

        // List

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @Override
        public Iterator<ItemType> iterator() {
            return new Iterator<ItemType>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public ItemType next() {
                    throw new NoSuchElementException();
                }
            };
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }

        @Override
        public boolean add(ItemType itemType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(@Nullable Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends ItemType> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    interface NonEmpty<
            EmptyType extends Empty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            NonEmptyType extends NonEmpty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            ListType extends List<ItemType>,
            ItemType
            > extends Either.Right<
            EmptyType,
            NonEmptyType,
            Nothing,
            ListType
            >, EitherNonEmptyList<
            EmptyType,
            NonEmptyType,
            ListType,
            ItemType
            > {
    }

    abstract class NonEmptyImpl<
            EmptyType extends Empty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            NonEmptyType extends NonEmpty<
                    EmptyType,
                    NonEmptyType,
                    ListType,
                    ItemType
                    >,
            ListType extends List<ItemType>,
            ItemType
            > extends Either.RightImpl<
            EmptyType,
            NonEmptyType,
            Nothing,
            ListType
            > implements NonEmpty<
            EmptyType,
            NonEmptyType,
            ListType,
            ItemType
            > {
        protected NonEmptyImpl(
                Class<NonEmptyType> selfClass,
                ListType value
        ) {
            super(
                    selfClass,
                    value
            );
        }
    }

    <T> T reduce(
            Function<
                    ? super EmptyType,
                    ? extends T
                    > emptyReducer,
            Function<
                    ? super NonEmptyType,
                    ? extends T
                    > nonEmptyReducer
    );

    void act(
            VoidFunction<? super EmptyType> emptyAction,
            VoidFunction<? super NonEmptyType> nonEmptyAction
    );
}
