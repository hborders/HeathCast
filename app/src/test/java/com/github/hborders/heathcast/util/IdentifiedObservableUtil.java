package com.github.hborders.heathcast.util;

import com.github.hborders.heathcast.models.Identified;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public final class IdentifiedObservableUtil {
    private IdentifiedObservableUtil() {
    }

    public static <M> Observable<List<M>> modelsObservable(Observable<List<Identified<M>>> identifiedsObservable) {
        return identifiedsObservable.map(
                identifieds -> identifieds
                        .stream()
                        .map(identified -> identified.model)
                        .collect(Collectors.toList())
        );
    }
}
