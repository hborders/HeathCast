package com.github.hborders.heathcast.mvp;

import android.content.Context;
import android.view.View;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.rxjava3.disposables.Disposable;

public interface Vista<
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
                >
        > extends Disposable {
    interface VistaFactory<
            VistaType extends Vista<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
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
                    >
            > {
        VistaType newVista(
                MvpFragmentType fragment,
                ListenerType listener,
                Context context,
                View view
        );
    }
}
