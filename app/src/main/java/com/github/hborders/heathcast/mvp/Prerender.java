package com.github.hborders.heathcast.mvp;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import java.util.Objects;

public final class Prerender<
        PrevistaType extends Presentation<
                        MvpFragmentType,
                        ListenerType,
                        AttachmentType,
                        VistaType
                        >,
        MvpFragmentType extends MvpFragment<
                MvpFragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                MvpFragmentType,
                ListenerType,
                AttachmentType
                >,
        VistaType extends Vista<
                MvpFragmentType,
                ListenerType,
                AttachmentType
                >,
        ModelType
        > {
    private final PrevistaType prevista;
    private final ModelType model;


    public Prerender(
            PrevistaType prevista,
            ModelType model
    ) {
        this.prevista = prevista;
        this.model = model;
    }

    // Object

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prerender<?, ?, ?, ?, ?, ?> prerender = (Prerender<?, ?, ?, ?, ?, ?>) o;
        return prevista.equals(prerender.prevista) &&
                model.equals(prerender.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                prevista,
                model
        );
    }

    @Override
    public String toString() {
        return "Prerender{" +
                "prevista=" + prevista +
                ", model=" + model +
                '}';
    }
}
