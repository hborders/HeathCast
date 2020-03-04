package com.github.hborders.heathcast.models;

public class SubscriptionIdentified extends Identified<SubscriptionIdentifier, Subscription> {
    public SubscriptionIdentified(SubscriptionIdentifier identifier, Subscription model) {
        super(identifier, model);
    }
}
