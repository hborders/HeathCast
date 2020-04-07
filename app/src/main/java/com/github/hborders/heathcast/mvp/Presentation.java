package com.github.hborders.heathcast.mvp;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import java.util.Objects;

public final class Presentation<
        FragmentType extends RxFragment<
                FragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType
                >,
        VistaType extends Vista<
                FragmentType,
                ListenerType,
                AttachmentType
                >
        > {
    public final Context context;
    public final FragmentType fragment;
    public final ListenerType listener;
    public final RxFragment.ViewCreation viewCreation;
    public final View view;
    public final VistaType vista;

    public Presentation(
            Context context,
            FragmentType fragment,
            ListenerType listener,
            RxFragment.ViewCreation viewCreation,
            View view,
            VistaType vista
    ) {
        this.context = context;
        this.fragment = fragment;
        this.listener = listener;
        this.viewCreation = viewCreation;
        this.view = view;
        this.vista = vista;
    }

    // Object

    @Override
    public final boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Presentation<?, ?, ?, ?> that = (Presentation<?, ?, ?, ?>) o;
        return context.equals(that.context) &&
                fragment.equals(that.fragment) &&
                listener.equals(that.listener) &&
                viewCreation.equals(that.viewCreation) &&
                view.equals(that.view) &&
                vista.equals(that.vista);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(
                context,
                fragment,
                listener,
                viewCreation,
                view,
                vista
        );
    }

    @Override
    public final String toString() {
        return "Presentation{" +
                "context=" + context +
                ", fragment=" + fragment +
                ", listener=" + listener +
                ", viewCreation=" + viewCreation +
                ", view=" + view +
                ", vista=" + vista +
                '}';
    }
}
