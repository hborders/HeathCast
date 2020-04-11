package com.github.hborders.heathcast.core;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public interface Opt2<ValueType> {
    interface EmptyOptFactory<
            OptType extends Opt2<ValueType>,
            ValueType
            > {
        OptType newOpt();
    }

    interface NonEmptyOptFactory<
            OptType extends Opt2<ValueType>,
            ValueType
            > {
        OptType newOpt(ValueType value);
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
            OtherOptType extends Opt2<OtherType>,
            OtherType
            > OtherOptType map(
            EmptyOptFactory<
                    OtherOptType,
                    OtherType
                    > emptyOtherOptFactory,
            NonEmptyOptFactory<
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
            OtherOptType extends Opt2<OtherType>,
            OtherType
            > OtherOptType flatMap(
            EmptyOptFactory<
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
