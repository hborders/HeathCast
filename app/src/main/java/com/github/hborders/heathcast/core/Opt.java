package com.github.hborders.heathcast.core;

import androidx.annotation.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public interface Opt<ValueType> {
    interface OptEmptyFactory<
            OptType extends Opt<ValueType>,
            ValueType
            > {
        OptType newOpt();
    }

    interface OptNonEmptyFactory<
            OptType extends Opt<ValueType>,
            ValueType
            > {
        OptType newOpt(ValueType value);
    }

    default void act(VoidFunction<? super ValueType> action) {
        @Nullable final ValueType value = getValue();
        if (value != null) {
            action.apply(value);
        }
    }

    @Nullable
    ValueType getValue();

    default boolean isPresent() {
        return getValue() != null;
    }

    @Nullable
    default ValueType orElseNull() {
        return getValue();
    }

    default ValueType orElse(ValueType other) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            return other;
        } else {
            return value;
        }
    }

    default ValueType orElseGet(Supplier<? extends ValueType> otherSupplier) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            final ValueType other = otherSupplier.get();
            return other;
        } else {
            return value;
        }
    }

    default <T extends Throwable> ValueType orElseThrow(Supplier<? extends T> exceptionSupplier) throws T {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            final T throwable = exceptionSupplier.get();
            throw throwable;
        } else {
            return value;
        }
    }

    default <
            OtherOptType extends Opt<OtherType>,
            OtherType
            > OtherOptType map(
            OptEmptyFactory<
                                OtherOptType,
                                OtherType
                                > emptyOtherOptFactory,
            OptNonEmptyFactory<
                                OtherOptType,
                                OtherType
                                > nonEmptyOtherOptFactory,
            Function<
                    ? super ValueType,
                    OtherType
                    > mapper
    ) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            return emptyOtherOptFactory.newOpt();
        } else {
            @Nullable final OtherType other = mapper.apply(value);
            return nonEmptyOtherOptFactory.newOpt(other);
        }
    }

    default <
            OtherOptType extends Opt<OtherType>,
            OtherType
            > OtherOptType flatMap(
            OptEmptyFactory<
                                OtherOptType,
                                OtherType
                                > emptyOtherOptFactory,
            Function<
                    ? super ValueType,
                    OtherOptType
                    > mapper
    ) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            return emptyOtherOptFactory.newOpt();
        } else {
            @Nullable final OtherOptType otherOpt = mapper.apply(value);
            return otherOpt;
        }
    }

    default Optional<ValueType> toOptional() {
        return Optional.ofNullable(getValue());
    }
}
