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

    public static <I extends Identifier<M>, J extends Identified<I, M>, M> Matcher<J> identifiedIdentifier(Matcher<I> identifierMatcher) {
        return new IdentifiedIdentifierMatcher<>(identifierMatcher);
    }

    public static <I extends Identifier<M>, J extends Identified<I, M>, M> Matcher<J> identifiedIdentifier(I identifier) {
        return identifiedIdentifier(equalTo(identifier));
    }

    public static <I extends Identifier<M>, J extends Identified<I, M>, M> Matcher<J> identifiedModel(Matcher<M> modelMatcher) {
        return new IdentifiedModelMatcher<>(modelMatcher);
    }

    public static <I extends Identifier<M>, J extends Identified<I, M>, M> Matcher<J> identifiedModel(M model) {
        return identifiedModel(equalTo(model));
    }

    public static final class IdentifiedIdentifierMatcher<I extends Identifier<M>, J extends Identified<I, M>, M> extends TypeSafeMatcher<J> {
        private final Matcher<I> identifierMatcher;

        public IdentifiedIdentifierMatcher(Matcher<I> identifierMatcher) {
            this.identifierMatcher = identifierMatcher;
        }

        @Override
        protected boolean matchesSafely(J item) {
            return identifierMatcher.matches(item.identifier);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Identified identifier matches ")
                    .appendDescriptionOf(identifierMatcher);
        }
    }

    public static final class IdentifiedModelMatcher<I extends Identifier<M>, J extends Identified<I, M>, M> extends TypeSafeMatcher<J> {
        private final Matcher<M> modelMatcher;

        public IdentifiedModelMatcher(Matcher<M> modelMatcher) {
            this.modelMatcher = modelMatcher;
        }

        @Override
        protected boolean matchesSafely(J item) {
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
