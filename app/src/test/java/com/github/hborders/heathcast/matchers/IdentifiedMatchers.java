package com.github.hborders.heathcast.matchers;

import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class IdentifiedMatchers {
    public static final class IdentifiedIdentifierMatcher<
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > extends TypeSafeMatcher<IdentifiedType> {
        private final Matcher<IdentifierType> identifierMatcher;

        public IdentifiedIdentifierMatcher(Matcher<IdentifierType> identifierMatcher) {
            this.identifierMatcher = identifierMatcher;
        }

        @Override
        protected boolean matchesSafely(IdentifiedType item) {
            return identifierMatcher.matches(item.getIdentifier());
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Identified identifier matches ")
                    .appendDescriptionOf(identifierMatcher);
        }
    }

    public static final class IdentifiedModelMatcher<
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > extends TypeSafeMatcher<IdentifiedType> {
        private final Matcher<ModelType> modelMatcher;

        public IdentifiedModelMatcher(Matcher<ModelType> modelMatcher) {
            this.modelMatcher = modelMatcher;
        }

        @Override
        protected boolean matchesSafely(IdentifiedType item) {
            return modelMatcher.matches(item.getModel());
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("Identified model matches ")
                    .appendDescriptionOf(modelMatcher);
        }
    }

    public static <
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > Matcher<IdentifiedType> identifiedIdentifier(Matcher<IdentifierType> identifierMatcher) {
        return new IdentifiedIdentifierMatcher<>(identifierMatcher);
    }

    public static <
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > Matcher<IdentifiedType> identifiedIdentifier(IdentifierType identifier) {
        return identifiedIdentifier(equalTo(identifier));
    }

    public static <
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > Matcher<IdentifiedType> identifiedModel(Matcher<ModelType> modelMatcher) {
        return new IdentifiedModelMatcher<>(modelMatcher);
    }

    public static <
            IdentifiedType extends Identified<
                                IdentifierType,
                                ModelType
                                >,
            IdentifierType extends Identifier,
            ModelType
            > Matcher<IdentifiedType> identifiedModel(ModelType model) {
        return identifiedModel(equalTo(model));
    }

    private IdentifiedMatchers() {
    }
}
