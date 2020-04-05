package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Function;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public abstract class RxValueFragment<
        ValueFragmentType extends RxValueFragment<
                ValueFragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                ValueFragmentType,
                ListenerType,
                AttachmentType
                >
        > extends RxFragment<
        ValueFragmentType,
        ListenerType,
        AttachmentType
        > {
    // even though ValueState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ValueState<ValueType> {
        ValueType getValue();
    }

    protected interface ValueStateObservableProvider<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueStateType extends ValueState<ValueType>,
            ValueType
            > {
        Observable<ValueStateType> valueStateObservable(
                ListenerType listener,
                ValueFragmentType fragment
        );
    }

    protected interface ValueViewFacade extends Disposable {
        interface ValueViewFacadeFactory<
                // even though we don't use these bounds in our declarations,
                // javac gives better error messages with boundary enforcement
                ValueFragmentType extends RxValueFragment<
                        ValueFragmentType,
                        ListenerType,
                        AttachmentType
                        >,
                ListenerType,
                AttachmentType extends RxFragment.Attachment<
                        ValueFragmentType,
                        ListenerType,
                        AttachmentType
                        >,
                ValueViewFacadeType
                > {
            ValueViewFacadeType newViewFacade(
                    ValueFragmentType fragment,
                    ListenerType listener,
                    Context context,
                    View view
            );
        }
    }

    protected interface ValueViewFacadeTransaction {
    }

    protected interface ValueCompletable {
        Completable toCompletable();
    }

    protected interface ValueViewFacadeCompletableTransaction<
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueStateType extends ValueState<ValueType>,
            ValueType,
            ValueCompletableType extends ValueCompletable
            > {
        ValueViewFacadeTransactionType toValueViewFacadeTransaction(ValueStateType valueState);

        ValueCompletableType complete();
    }

    protected interface ValueViewFacadeCompletableTransactionFactory<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueStateType extends ValueState<ValueType>,
            ValueType,
            ValueCompletableType extends ValueCompletable
            > {
        ValueViewFacadeCompletableTransactionType newValueViewFacadeCompletableTransaction(
                ValueViewFacadeType valueViewFacade
        );
    }

    protected interface ValueRenderer<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueType,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueCompletableType extends ValueCompletable
            > {
        ValueCompletableType render(
                ValueFragmentType fragment,
                ListenerType listener,
                Context context,
                ValueType valueState,
                ValueViewFacadeTransactionType viewFacadeTransaction
        );
    }

    private final Function<Observable<AttachmentType>, Disposable> subscribeFunction;

    protected <
            ValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueOnAttachedType extends OnAttached<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueWillDetachType extends WillDetach<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType,
                    ValueType
                    >,
            ValueStateType extends ValueState<ValueType>,
            ValueType,
            ValueViewFacadeCompletableTransactionFactoryType extends ValueViewFacadeCompletableTransactionFactory<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType,
                    ValueViewFacadeCompletableTransactionType,
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueType,
                    ValueCompletableType
                    >,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueType,
                    ValueCompletableType
                    >,
            ValueCompletableType extends ValueCompletable,
            ValueRendererType extends ValueRenderer<
                    ValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueType,
                    ValueViewFacadeTransactionType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            >
    RxValueFragment(
            Class<ValueFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            ValueAttachmentFactoryType attachmentFactory,
            ValueOnAttachedType onAttached,
            ValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeCompletableTransactionFactoryType valueViewFacadeCompletableTransactionFactory,
            ValueRendererType valueRenderer
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
            final class Prez {
                final Context context;
                final ListenerType listener;
                final ViewCreation viewCreation;
                final View view;
                final ValueViewFacadeType valueViewFacade;

                Prez(
                        Context context,
                        ListenerType listener,
                        ViewCreation viewCreation,
                        View view,
                        ValueViewFacadeType valueViewFacade
                ) {
                    this.context = context;
                    this.listener = listener;
                    this.viewCreation = viewCreation;
                    this.view = view;
                    this.valueViewFacade = valueViewFacade;
                }
            }

            final Observable<Prez> prezObservable = attachmentObservable
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
                                final ValueViewFacadeType viewFacade = valueViewFacadeFactory.newViewFacade(
                                        getSelf(),
                                        listener,
                                        context,
                                        view
                                );
                                viewCreation.addDisposable(viewFacade);
                                return new Prez(
                                        context,
                                        listener,
                                        viewCreation,
                                        view,
                                        viewFacade
                                );
                            }
                    );

            final class Prerender {
                final Prez prez;
                final ValueStateType valueState;

                Prerender(
                        Prez prez,
                        ValueStateType valueState
                ) {
                    this.prez = prez;
                    this.valueState = valueState;
                }
            }

            final Observable<Prerender> prerenderObservable =
                    prezObservable.switchMap(
                            prez ->
                                    prez.viewCreation.switchMapToStart().switchMap(
                                            start ->
                                                    valueStateObservableProvider
                                                            .valueStateObservable(
                                                                    prez.listener,
                                                                    getSelf()
                                                            )
                                                            .map(
                                                                    state ->
                                                                            new Prerender(
                                                                                    prez,
                                                                                    state
                                                                            )
                                                            )
                                    )
                    );


            return prerenderObservable.subscribe(
                    prerender -> {
                        final ValueStateType valueState = prerender.valueState;
                        final ValueViewFacadeCompletableTransactionType valueViewFacadeCompletableTransaction =
                                valueViewFacadeCompletableTransactionFactory.newValueViewFacadeCompletableTransaction(
                                        prerender.prez.valueViewFacade
                                );
                        final ValueViewFacadeTransactionType valueViewFacadeTransaction =
                                valueViewFacadeCompletableTransaction.toValueViewFacadeTransaction(
                                        // TODO move valueState into completableTransactionFactory
                                        valueState
                                );

                        final ValueCompletableType ignored =
                                valueRenderer.render(
                                        getSelf(),
                                        prerender.prez.listener,
                                        prerender.prez.context,
                                        valueState.getValue(),
                                        valueViewFacadeTransaction
                                );
                    }
            );
        };
    }

    @Override
    protected final Disposable subscribe(Observable<AttachmentType> attachmentObservable) {
        return subscribeFunction.apply(attachmentObservable);
    }
}
