package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.services.ServiceResponse;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

public final class ServiceResponseMatchers {
    private ServiceResponseMatchers() {
    }

    public static <T> Matcher<ServiceResponse<?>> serviceResponse(
            Matcher<? super T> valueMatcher,
            Matcher<? super ServiceResponse.RemoteStatus> remoteStatusMatcher
    ) {
        return new ServiceResponseMatcher<T>(
                valueMatcher,
                remoteStatusMatcher
        );
    }

    public static <T> Matcher<ServiceResponse<?>> serviceResponse(
            T value,
            Matcher<? super ServiceResponse.RemoteStatus> remoteStatusMatcher
    ) {
        return serviceResponse(
                is(value),
                remoteStatusMatcher
        );
    }

    public static <T> Matcher<ServiceResponse<?>> serviceResponse(
            Matcher<? super T> valueMatcher,
            ServiceResponse.RemoteStatus remoteStatus
    ) {
        return serviceResponse(
                valueMatcher,
                is(remoteStatus)
        );
    }

    public static <T> Matcher<ServiceResponse<?>> serviceResponse(
            T value,
            ServiceResponse.RemoteStatus remoteStatus
    ) {
        return serviceResponse(
                is(value),
                is(remoteStatus)
        );
    }

    private static final class ServiceResponseMatcher<T> extends TypeSafeMatcher<ServiceResponse<?>> {

        private final Matcher<? super T> valueMatcher;
        private final Matcher<? super ServiceResponse.RemoteStatus> remoteStatusMatcher;

        private ServiceResponseMatcher(
                Matcher<? super T> valueMatcher,
                Matcher<? super ServiceResponse.RemoteStatus> remoteStatusMatcher
        ) {
            super(ServiceResponse.class);

            this.valueMatcher = valueMatcher;
            this.remoteStatusMatcher = remoteStatusMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponse<?> item) {
            return valueMatcher.matches(item.value) &&
                    remoteStatusMatcher.matches(item.remoteStatus);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse.value matches ")
                    .appendDescriptionOf(valueMatcher)
                    .appendText(" and ServiceResponse.remoteStatus matches ")
                    .appendDescriptionOf(remoteStatusMatcher);
        }
    }
}
