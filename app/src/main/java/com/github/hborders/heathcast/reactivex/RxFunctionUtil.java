package com.github.hborders.heathcast.reactivex;

import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;

public final class RxFunctionUtil {
    public static <T1, T2, R> Consumer<T2> capture1(BiFunction<T1, T2, R> biFunction, T1 t1) {
        return t2 -> biFunction.apply(t1, t2);
    }

    public static <T1, T2, R> Consumer<T1> capture2(BiFunction<T1, T2, R> biFunction, T2 t2) {
        return t1 -> biFunction.apply(t1, t2);
    }

    private RxFunctionUtil() {
    }
}
