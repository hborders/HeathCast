package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.services.ServiceResponse;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class ServiceResponseMatchers {
    public static <
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > Matcher<ServiceResponseType> serviceResponseLoading(
            Class<ServiceResponseType> serviceResponseClass,
            Matcher<? super LoadingValueType> valueMatcher
    ) {
        return new ServiceResponseLoadingMatcher<>(
                serviceResponseClass,
                valueMatcher
        );
    }

    public static <
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > Matcher<ServiceResponseType> serviceResponseComplete(
            Class<ServiceResponseType> serviceResponseClass,
            Matcher<? super CompleteValueType> valueMatcher
    ) {
        return new ServiceResponseCompleteMatcher<>(
                serviceResponseClass,
                valueMatcher
        );
    }

    public static <
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > Matcher<ServiceResponseType> serviceResponseFailed(
            Class<ServiceResponseType> serviceResponseClass,
            Matcher<? super FailedValueType> valueMatcher
    ) {
        return new ServiceResponseFailedMatcher<>(
                serviceResponseClass,
                valueMatcher
        );
    }

    private static final class ServiceResponseLoadingMatcher<
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends TypeSafeMatcher<ServiceResponseType> {
        private final Matcher<? super LoadingValueType> valueMatcher;

        private ServiceResponseLoadingMatcher(
                Class<ServiceResponseType> serviceResponseClass,
                Matcher<? super LoadingValueType> valueMatcher
        ) {
            super(serviceResponseClass);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponseType item) {
            return item.reduce(
                    loading -> valueMatcher.matches(loading.getValue()),
                    complete -> false,
                    failed -> false
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse is loading and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    private static final class ServiceResponseCompleteMatcher<
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends TypeSafeMatcher<ServiceResponseType> {
        private final Matcher<? super CompleteValueType> valueMatcher;

        private ServiceResponseCompleteMatcher(
                Class<ServiceResponseType> serviceResponseClass,
                Matcher<? super CompleteValueType> valueMatcher
        ) {
            super(serviceResponseClass);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponseType item) {
            return item.reduce(
                    loading -> false,
                    complete -> valueMatcher.matches(complete.getValue()),
                    failed -> false
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse is complete and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    private static final class ServiceResponseFailedMatcher<
            ServiceResponseType extends ServiceResponse<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingType extends ServiceResponse.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            CompleteType extends ServiceResponse.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            FailedType extends ServiceResponse.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    LoadingValueType,
                    CompleteValueType,
                    FailedValueType
                    >,
            LoadingValueType,
            CompleteValueType,
            FailedValueType
            > extends TypeSafeMatcher<ServiceResponseType> {
        private final Matcher<? super FailedValueType> valueMatcher;

        private ServiceResponseFailedMatcher(
                Class<ServiceResponseType> serviceResponseClass,
                Matcher<? super FailedValueType> valueMatcher
        ) {
            super(serviceResponseClass);

            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(ServiceResponseType item) {
            return item.reduce(
                    loading -> false,
                    complete -> false,
                    failed -> valueMatcher.matches(failed.getValue())
            );
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("ServiceResponse is failed and value matches ")
                    .appendDescriptionOf(valueMatcher);
        }
    }

    private ServiceResponseMatchers() {
    }
}
