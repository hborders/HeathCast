package com.github.hborders.heathcast.mvp;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Observable;

public interface ModelObservableProvider<
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
        ModelType
        > {
    Observable<ModelType> modelObservable(
            ListenerType listener,
            FragmentType fragment
    );
}
