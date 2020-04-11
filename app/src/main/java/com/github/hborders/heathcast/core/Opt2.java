package com.github.hborders.heathcast.core;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public interface Opt2<ValueType> {
    interface OptFactoryEmpty<
            OptType extends Opt<ValueType>,
            ValueType
            > {
        OptType newOpt();
    }

    interface OptFactoryNonEmpty<
            OptType extends Opt<ValueType>,
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
            OtherOptFactoryEmptyType extends OptFactoryEmpty<
                    OtherOptType,
                    OtherType
                    >,
            OtherOptFactoryNonEmptyType extends OptFactoryNonEmpty<
                    OtherOptType,
                    OtherType
                    >,
            OtherOptType extends Opt<OtherType>,
            OtherType
            > OtherOptType map(
            OtherOptFactoryEmptyType otherOptFactoryEmpty,
            OtherOptFactoryNonEmptyType otherOptFactoryNonEmpty,
            Function<
                    ? super ValueType,
                    OtherType
                    > mapper
    ) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            return otherOptFactoryEmpty.newOpt();
        } else {
            @Nullable final OtherType other = mapper.apply(value);
            return otherOptFactoryNonEmpty.newOpt(other);
        }
    }

    default <
            OtherOptFactoryEmptyType extends OptFactoryEmpty<
                    OtherOptType,
                    OtherType
                    >,
            OtherOptFactoryNonEmptyType extends OptFactoryNonEmpty<
                    OtherOptType,
                    OtherType
                    >,
            OtherOptType extends Opt<OtherType>,
            OtherType
            > OtherOptType flatMap(
            OtherOptFactoryEmptyType otherOptFactoryEmpty,
            OtherOptFactoryNonEmptyType otherOptFactoryNonEmpty,
            Function<
                    ? super ValueType,
                    OtherOptType
                    > mapper
    ) {
        @Nullable final ValueType value = getValue();
        if (value == null) {
            return otherOptFactoryEmpty.newOpt();
        } else {
            @Nullable final OtherOptType otherOpt = mapper.apply(value);
            return otherOpt;
        }
    }

    default Optional<ValueType> toOptional() {
        return Optional.ofNullable(getValue());
    }
}
