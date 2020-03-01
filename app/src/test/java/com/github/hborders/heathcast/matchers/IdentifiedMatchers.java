package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class IdentifiedMatchers {
    private IdentifiedMatchers() {
    }

    public static <M, I extends Identified<M>> Matcher<I> identifiedIdentifier(Matcher<Identifier<M>> identifierMatcher) {
        return new IdentifiedIdentifierMatcher<M, I>(identifierMatcher);
    }

    public static <M, I extends Identified<M>> Matcher<I> identifiedIdentifier(Identifier<M> identifier) {
        return identifiedIdentifier(equalTo(identifier));
    }

    public static <M, I extends Identified<M>> Matcher<I> identifiedModel(Matcher<M> modelMatcher) {
        return new IdentifiedModelMatcher<M, I>(modelMatcher);
    }

    public static <M, I extends Identified<M>> Matcher<I> identifiedModel(M model) {
        return identifiedModel(equalTo(model));
    }

    public static final class IdentifiedIdentifierMatcher<M, I extends Identified<M>> extends TypeSafeMatcher<I> {
        private final Matcher<Identifier<M>> identifierMatcher;

        public IdentifiedIdentifierMatcher(Matcher<Identifier<M>> identifierMatcher) {
            this.identifierMatcher = identifierMatcher;
        }

        @Override
        protected boolean matchesSafely(I item) {
            return identifierMatcher.matches(item.identifier);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Identified identifier matches ")
                    .appendDescriptionOf(identifierMatcher);
        }
    }

    public static final class IdentifiedModelMatcher<M, I extends Identified<M>> extends TypeSafeMatcher<I> {
        private final Matcher<M> modelMatcher;

        public IdentifiedModelMatcher(Matcher<M> modelMatcher) {
            this.modelMatcher = modelMatcher;
        }

        @Override
        protected boolean matchesSafely(I item) {
            return modelMatcher.matches(item.model);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Identified model matches ")
                    .appendDescriptionOf(modelMatcher);
        }
    }
}
