package com.github.hborders.heathcast.mvp;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public interface Presenter<
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
    Disposable subscribe(
            Observable<
                    Presentation<
                                                FragmentType,
                                                ListenerType,
                                                AttachmentType,
                                                VistaType
                                                >
                    > previstaObservable
    );
}
