package com.github.hborders.heathcast.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class SubscriptionIdentifiedList extends ArrayList<SubscriptionIdentified> {
    public SubscriptionIdentifiedList() {
    }

    public SubscriptionIdentifiedList(int initialCapacity) {
        super(initialCapacity);
    }

    public SubscriptionIdentifiedList(Collection<? extends SubscriptionIdentified> items) {
        super(items);
    }

    public SubscriptionIdentifiedList(SubscriptionIdentified... items) {
        super(Arrays.asList(items));
    }
}
