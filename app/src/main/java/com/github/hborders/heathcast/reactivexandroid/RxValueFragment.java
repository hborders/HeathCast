package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.ObjectTransaction;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public abstract class RxValueFragment<
        ValueFragmentType extends RxValueFragment<
                ValueFragmentType,
                ValueListenerType,
                ValueAttachmentType
                >,
        ValueListenerType,
        ValueAttachmentType extends RxFragment.Attachment<
                ValueFragmentType,
                ValueListenerType,
                ValueAttachmentType
                >
        > extends RxFragment<
        ValueFragmentType,
        ValueListenerType,
        ValueAttachmentType
        > {
    // even though ValueState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ValueState<ValueUnparcelableType> {
        boolean isEnabled();

        ValueUnparcelableType getValue();
    }

    protected interface ValueStateObservableProvider<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueListenerType,
            ValueAttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueStateType extends ValueState<ValueUnparcelableType>,
            ValueUnparcelableType
            > {
        Observable<ValueStateType> valueStateObservable(
                ValueListenerType listener,
                ValueFragmentType fragment
        );
    }

    protected interface ValueViewFacade<ValueUnparcelableType> extends Disposable {
        interface ValueViewFacadeFactory<
                // even though we don't use these bounds in our declarations,
                // javac gives better error messages with boundary enforcement
                ValueFragmentType extends RxValueFragment<
                        ValueFragmentType,
                        ValueListenerType,
                        ValueAttachmentType
                        >,
                ValueListenerType,
                ValueAttachmentType extends RxFragment.Attachment<
                        ValueFragmentType,
                        ValueListenerType,
                        ValueAttachmentType
                        >,
                ValueViewFacadeType
                > {
            ValueViewFacadeType newViewFacade(
                    ValueFragmentType fragment,
                    ValueListenerType listener,
                    Context context,
                    View view
            );
        }

        void setEnabled(boolean enabled);

        void setValue(ValueUnparcelableType value);
    }

    protected interface ValueViewFacadeTransactionFactory<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueListenerType,
            ValueAttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueViewFacadeType extends ValueViewFacade<ValueUnparcelableType>,
            ValueUnparcelableType,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >
            > {
        ValueViewFacadeTransactionType newValueViewFacadeTransaction(ValueViewFacadeType valueViewFacade);
    }

    protected interface ValueRenderer<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueFragmentType extends RxValueFragment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueListenerType,
            ValueAttachmentType extends RxFragment.Attachment<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueStateType extends ValueState<ValueUnparcelableType>,
            ValueUnparcelableType,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<ValueUnparcelableType>
            > {
        @CheckResult
        Completable render(
                ValueFragmentType fragment,
                ValueListenerType listener,
                Context context,
                ValueStateType valueState,
                ValueViewFacadeTransactionType viewFacadeTransaction
        );
    }

    private final Function<Observable<ValueAttachmentType>, Disposable> subscribeFunction;

    protected <
            ValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueOnAttachedType extends OnAttached<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueWillDetachType extends WillDetach<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<ValueUnparcelableType>,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType,
                    ValueStateType,
                    ValueUnparcelableType
                    >,
            ValueStateType extends ValueState<ValueUnparcelableType>,
            ValueUnparcelableType,
            ValueViewFacadeTransactionFactoryType extends ValueViewFacadeTransactionFactory<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType,
                    ValueViewFacadeType,
                    ValueUnparcelableType,
                    ValueViewFacadeTransactionType
                    >,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            ValueRendererType extends ValueRenderer<
                    ValueFragmentType,
                    ValueListenerType,
                    ValueAttachmentType,
                    ValueStateType,
                    ValueUnparcelableType,
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >
            >
    RxValueFragment(
            Class<ValueFragmentType> selfClass,
            Class<ValueListenerType> listenerClass,
            ValueAttachmentFactoryType attachmentFactory,
            ValueOnAttachedType onAttached,
            ValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeTransactionFactoryType valueViewFacadeTransactionFactory,
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
                final ValueListenerType listener;
                final ViewCreation viewCreation;
                final View view;
                final ValueViewFacadeType valueViewFacade;

                Prez(
                        Context context,
                        ValueListenerType listener,
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
                                final ValueListenerType listener =
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
                        final ValueViewFacadeTransactionType valueViewFacadeTransaction =
                                valueViewFacadeTransactionFactory.newValueViewFacadeTransaction(
                                        prerender.prez.valueViewFacade
                                );

                        valueViewFacadeTransaction.act(
                                ValueViewFacadeType::setEnabled,
                                prerender.valueState.isEnabled()
                        );
                        valueViewFacadeTransaction.act(
                                ValueViewFacadeType::setValue,
                                prerender.valueState.getValue()
                        );

                        final Completable ignored =
                                valueRenderer.render(
                                        getSelf(),
                                        prerender.prez.listener,
                                        prerender.prez.context,
                                        prerender.valueState,
                                        valueViewFacadeTransaction
                                );
                    }
            );
        };
    }

    @Override
    protected final Disposable subscribe(Observable<ValueAttachmentType> attachmentObservable) {
        return subscribeFunction.apply(attachmentObservable);
    }
}
