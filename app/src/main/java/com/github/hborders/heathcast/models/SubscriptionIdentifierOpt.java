package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public class SubscriptionIdentifierOpt extends Opt<SubscriptionIdentifier> {
    public static final SubscriptionIdentifierOpt EMPTY = new SubscriptionIdentifierOpt();
    public static final SubscriptionIdentifierOpt.Factory FACTORY = new SubscriptionIdentifierOpt.Factory();

    public static final class Factory implements Opt.Factory<SubscriptionIdentifierOpt, SubscriptionIdentifier> {
        private Factory() {
        }

        @Override
        public SubscriptionIdentifierOpt empty() {
            return EMPTY;
        }

        @Override
        public SubscriptionIdentifierOpt of(SubscriptionIdentifier value) {
            return new SubscriptionIdentifierOpt(value);
        }
    }

    private SubscriptionIdentifierOpt() {
    }

    public SubscriptionIdentifierOpt(SubscriptionIdentifier value) {
        super(value);
    }
}
