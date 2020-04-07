package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Function;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public abstract class RxValueFragment<
        FragmentType extends RxValueFragment<
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
        > extends RxFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    // even though ValueState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ValueState {
    }

    protected interface ValueStateObservableProvider<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            FragmentType extends RxValueFragment<
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
            ValueStateType extends ValueState
            > {
        Observable<ValueStateType> valueStateObservable(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface ValueViewFacade extends Disposable {
        interface ValueViewFacadeFactory<
                // even though we don't use these bounds in our declarations,
                // javac gives better error messages with boundary enforcement
                FragmentType extends RxValueFragment<
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
                ValueViewFacadeType
                > {
            ValueViewFacadeType newViewFacade(
                    FragmentType fragment,
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
            ValueStateType extends ValueState,
            ValueCompletableType extends ValueCompletable
            > {
        ValueViewFacadeTransactionType toValueViewFacadeTransaction(ValueStateType valueState);

        ValueCompletableType completeValue();
    }

    protected interface ValueViewFacadeCompletableTransactionFactory<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            FragmentType extends RxValueFragment<
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
            ValueViewFacadeType extends ValueViewFacade,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueStateType extends ValueState,
            ValueCompletableType extends ValueCompletable
            > {
        ValueViewFacadeCompletableTransactionType newValueViewFacadeCompletableTransaction(
                ValueViewFacadeType valueViewFacade
        );
    }

    protected interface ValueRenderer<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            Type extends RxValueFragment<
                    Type,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    Type,
                    ListenerType,
                    AttachmentType
                    >,
            ValueStateType extends ValueState,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueCompletableType extends ValueCompletable
            > {
        ValueCompletableType render(
                Type fragment,
                ListenerType listener,
                Context context,
                ValueStateType valueState,
                ValueViewFacadeTransactionType viewFacadeTransaction
        );
    }

    private final Function<Observable<AttachmentType>, Disposable> subscribeFunction;

    protected <
            AttachmentFactoryType extends Attachment.AttachmentFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            OnAttachedType extends OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            WillDetachType extends WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType
                    >,
            ValueStateType extends ValueState,
            ValueViewFacadeCompletableTransactionFactoryType extends ValueViewFacadeCompletableTransactionFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType,
                    ValueViewFacadeCompletableTransactionType,
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    ValueCompletableType
                    >,
            ValueCompletableType extends ValueCompletable,
            ValueRendererType extends ValueRenderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType,
                    ValueViewFacadeTransactionType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            >
    RxValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
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
                                        valueState,
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
