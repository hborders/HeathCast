package com.github.hborders.heathcast.views.recyclerviews;

import com.github.hborders.heathcast.core.Ranged;

import java.util.Objects;

import javax.annotation.Nullable;

public final class ItemRange {
    public static ItemRange visible(
            int itemCount,
            int visibleStart,
            int visibleEnd
    ) {

        if (itemCount < 0) {
            throw new IllegalArgumentException(
                    "Item ranges should have a non-negative item count: " +
                            debugString(
                                    itemCount,
                                    visibleStart,
                                    visibleEnd
                            )
            );
        }

        if (visibleStart < 0) {
            throw new IllegalArgumentException(
                    "The start of the visible range start should be non-negative: " +
                            debugString(
                                    itemCount,
                                    visibleStart,
                                    visibleEnd
                            )
            );
        }
        if (visibleEnd < visibleStart) {
            throw new IllegalArgumentException(
                    "The end of the visible range should not be before the start: " +
                            debugString(
                                    itemCount,
                                    visibleStart,
                                    visibleEnd
                            )
            );
        }
        if (itemCount - 1 < visibleEnd) {
            throw new IllegalArgumentException(
                    "The end of the visible range should not be after the last item: " +
                            debugString(
                                    itemCount,
                                    visibleStart,
                                    visibleEnd
                            )
            );
        }

        return new ItemRange(
                new Ranged.Closed<>(
                        0,
                        itemCount
                ),
                new Ranged.Closed<>(
                        visibleStart,
                        visibleEnd
                )
        );
    }

    private static String debugString(
            int itemCount,
            int visibleStart,
            int visibleEnd
    ) {
        return "item count: "
                + itemCount
                + " visibleStart: "
                + visibleStart
                + " visibleEnd: "
                + visibleEnd;
    }

    public static ItemRange invisible(int itemCount) {
        if (itemCount < 0) {
            throw new IllegalArgumentException(
                    "Item ranges should have a non-negative item count: " + itemCount
            );
        }
        return new ItemRange(
                new Ranged.Closed<>(
                        0,
                        itemCount
                ),
                null
        );
    }

    public final Ranged.Closed<Integer> itemRange;
    @Nullable
    public final Ranged.Closed<Integer> visibleClosedRange;

    private ItemRange(
            Ranged.Closed<Integer> itemRange,
            @Nullable Ranged.Closed<Integer> visibleClosedRange
    ) {
        this.itemRange = itemRange;
        this.visibleClosedRange = visibleClosedRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRange itemRange1 = (ItemRange) o;
        return itemRange.equals(itemRange1.itemRange) &&
                Objects.equals(visibleClosedRange, itemRange1.visibleClosedRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemRange, visibleClosedRange);
    }

    @Override
    public String toString() {
        return "ItemRange{" +
                "itemRange=" + itemRange +
                ", visibleClosedRange=" + visibleClosedRange +
                '}';
    }
}
