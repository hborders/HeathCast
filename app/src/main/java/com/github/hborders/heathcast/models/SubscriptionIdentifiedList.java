package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SubscriptionIdentifiedList extends ArrayList<SubscriptionIdentified> {
    public SubscriptionIdentifiedList() {
    }

    public SubscriptionIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public SubscriptionIdentifiedList(Collection<? extends SubscriptionIdentified> c) {
        super(c);
    }

    public SubscriptionIdentifiedList(SubscriptionIdentified... subscriptionIdentifieds) {
        super(Arrays.asList(subscriptionIdentifieds));
    }
}
