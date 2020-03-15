package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

public class SubscriptionIdentifiedOpt extends Opt<SubscriptionIdentified> {
    public static final SubscriptionIdentifiedOpt EMPTY = new SubscriptionIdentifiedOpt();
    public static final SubscriptionIdentifiedOpt.Factory FACTORY = new SubscriptionIdentifiedOpt.Factory();

    public static final class Factory implements Opt.Factory<SubscriptionIdentifiedOpt, SubscriptionIdentified> {
        private Factory() {
        }

        @Override
        public SubscriptionIdentifiedOpt empty() {
            return EMPTY;
        }

        @Override
        public SubscriptionIdentifiedOpt of(SubscriptionIdentified value) {
            return new SubscriptionIdentifiedOpt(value);
        }
    }

    private SubscriptionIdentifiedOpt() {
    }

    public SubscriptionIdentifiedOpt(SubscriptionIdentified value) {
        super(value);
    }
}
