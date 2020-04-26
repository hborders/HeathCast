package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.models.Versioned;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public final class VersionedMatchers {
    public static final class VersionedValueMatcher<
            VersionedType extends Versioned<ValueType>,
            ValueType
            > extends TypeSafeMatcher<VersionedType> {
        private final Matcher<? super ValueType> valueMatcher;

        public VersionedValueMatcher(Matcher<? super ValueType> valueMatcher) {
            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(VersionedType item) {
            return valueMatcher.matches(item.getValue());
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Versioned value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    public static final class VersionedVersionMatcher<
            VersionedType extends Versioned<ValueType>,
            ValueType
            > extends TypeSafeMatcher<VersionedType> {
        private final Matcher<? super Long> versionMatcher;

        public VersionedVersionMatcher(Matcher<? super Long> versionMatcher) {
            this.versionMatcher = versionMatcher;
        }

        @Override
        protected boolean matchesSafely(VersionedType item) {
            return versionMatcher.matches(item.getVersion());
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Versioned version matches ")
                    .appendDescriptionOf(versionMatcher);
        }
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versionedValue(ValueType value) {
        return new VersionedValueMatcher<>(is(value));
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versionedValue(Matcher<? super ValueType> valueMatcher) {
        return new VersionedValueMatcher<>(valueMatcher);
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versionedVersion(long version) {
        return new VersionedVersionMatcher<>(is(version));
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versionedVersion(Matcher<? super Long> versionMatcher) {
        return new VersionedVersionMatcher<>(versionMatcher);
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versioned(
            Matcher<? super ValueType> valueMatcher,
            long version
    ) {
        return versioned(
                valueMatcher,
                is(version)
        );
    }

    public static <
            VersionedType extends Versioned<ValueType>,
            ValueType
            > Matcher<VersionedType> versioned(
                    Matcher<? super ValueType> valueMatcher,
                    Matcher<? super Long> versionMatcher
    ) {
        final Matcher<VersionedType> versionedValueMatcher = versionedValue(valueMatcher);
        final Matcher<VersionedType> versionedVersionMatcher = versionedVersion(versionMatcher);
        return allOf(
                versionedValueMatcher,
                versionedVersionMatcher
        );
    }

    private VersionedMatchers() {
    }
}
