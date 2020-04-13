package com.github.hborders.heathcast.core;

public final class ClassUtil {
    public static <
            AbstractType,
            SpecificType extends AbstractType
            > String getSpecificSimpleName(
            Class<AbstractType> abstractClass,
            Class<SpecificType> specificClass
    ) {
        final String simpleName;
        if (specificClass.isAnonymousClass()) {
            if (abstractClass.isAnonymousClass()) {
                simpleName = "$$";
            } else {
                simpleName = abstractClass.getSimpleName() + "$";
            }
        } else {
            simpleName = specificClass.getSimpleName();
        }

        return simpleName;
    }

    private ClassUtil() {
    }
}
