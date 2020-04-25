package com.github.hborders.heathcast.mvp;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class MvpFragment<
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
        > extends RxFragment<
        MvpFragmentType,
        ListenerType,
        AttachmentType
        > {
    private final Function<Observable<AttachmentType>, Disposable> subscribeFunction;

    protected <
            MvpAttachmentFactoryType extends Attachment.AttachmentFactory<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            MvpOnAttachedType extends OnAttached<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            MvpWillDetachType extends WillDetach<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            VistaFactoryType extends Vista.VistaFactory<
                    VistaType,
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            VistaType extends Vista<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PresenterType extends Presenter<
                    MvpFragmentType,
                    ListenerType,
                    AttachmentType,
                    VistaType
                    >
            >
    MvpFragment(
            Class<MvpFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            MvpAttachmentFactoryType attachmentFactory,
            MvpOnAttachedType onAttached,
            MvpWillDetachType willDetach,
            @LayoutRes int layoutResource,
            VistaFactoryType vistaFactory,
            PresenterType presenter
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );
        subscribeFunction = attachmentObservable -> {
            final Observable<
                    Presentation<
                            MvpFragmentType,
                            ListenerType,
                            AttachmentType,
                            VistaType
                            >
                    > previstaObservable = attachmentObservable
                    .switchMap(
                            Attachment::switchMapToViewCreation
                    )
                    .map(
                            attachmentFragmentCreationViewCreationTriple -> {
                                final Context context =
                                        attachmentFragmentCreationViewCreationTriple.first.context;
                                final ListenerType listener =
                                        attachmentFragmentCreationViewCreationTriple.first.listener;
                                final ViewCreation viewCreation =
                                        attachmentFragmentCreationViewCreationTriple.third;
                                final View view = viewCreation.view;
                                final VistaType vista = vistaFactory.newVista(
                                        getSelf(),
                                        listener,
                                        context,
                                        view
                                );
                                viewCreation.addDisposable(vista);
                                return new Presentation<>(
                                        context,
                                        getSelf(),
                                        listener,
                                        viewCreation,
                                        view,
                                        vista
                                );
                            }
                    );

            return presenter.subscribe(previstaObservable);
        };
    }

    @Override
    protected final Disposable subscribe(Observable<AttachmentType> attachmentObservable) {
        return subscribeFunction.apply(attachmentObservable);
    }
}
