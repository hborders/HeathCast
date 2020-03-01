package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.services.ServiceResponse1;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class ServiceResponse1Matchers {
    private ServiceResponse1Matchers() {
    }

    public static <T> Matcher<ServiceResponse1<T>> serviceResponse1Loading(
            Matcher<? super T> valueMatcher
    ) {
        return new ServiceResponse1LoadingMatcher<>(valueMatcher);
    }

    public static <T> Matcher<ServiceResponse1<T>> serviceResponse1Complete(
            Matcher<? super T> valueMatcher
    ) {
        return new ServiceResponse1CompleteMatcher<>(valueMatcher);
    }

    public static <T> Matcher<ServiceResponse1<T>> serviceResponse1Failed(
            Matcher<? super T> valueMatcher
    ) {
        return new ServiceResponse1FailedMatcher<>(valueMatcher);
    }

    private static final class ServiceResponse1LoadingMatcher<T> extends TypeSafeMatcher<ServiceResponse1<T>> {
        private final Matcher<? super T> valueMatcher;

        private ServiceResponse1LoadingMatcher(Matcher<? super T> valueMatcher) {
            super(ServiceResponse1.class);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponse1<T> item) {
            return item.reduce(
                    loading -> valueMatcher.matches(loading.value),
                    complete -> false,
                    failed -> false
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse1 is loading and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    private static final class ServiceResponse1CompleteMatcher<T> extends TypeSafeMatcher<ServiceResponse1<T>> {
        private final Matcher<? super T> valueMatcher;

        private ServiceResponse1CompleteMatcher(Matcher<? super T> valueMatcher) {
            super(ServiceResponse1.class);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponse1<T> item) {
            return item.reduce(
                    loading -> false,
                    complete -> valueMatcher.matches(complete.value),
                    failed -> false
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse1 is complete and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    private static final class ServiceResponse1FailedMatcher<T> extends TypeSafeMatcher<ServiceResponse1<T>> {
        private final Matcher<? super T> valueMatcher;

        private ServiceResponse1FailedMatcher(Matcher<? super T> valueMatcher) {
            super(ServiceResponse1.class);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponse1<T> item) {
            return item.reduce(
                    loading -> false,
                    complete -> false,
                    failed -> valueMatcher.matches(failed.value)
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse1 is failed and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }
}
