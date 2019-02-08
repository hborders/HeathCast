package com.github.hborders.heathcast.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * Extends {@code ParametersAreNonnullByDefault} to also apply to Method results and fields.
 *
 * @see javax.annotation.ParametersAreNonnullByDefault
 */
@Documented
@Nonnull
@TypeQualifierDefault({
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EverythingIsNonNullByDefault {
}
