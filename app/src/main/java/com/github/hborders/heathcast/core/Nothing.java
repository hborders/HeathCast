package com.github.hborders.heathcast.core;

public final class Nothing {
    public static final Nothing INSTANCE = new Nothing();

    public Nothing() {
    }

    @Override
    public String toString() {
        return "Nothing{}";
    }
}
