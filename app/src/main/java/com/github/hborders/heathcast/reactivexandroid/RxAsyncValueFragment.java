package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.ObjectTransaction;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource.Deceleration;

import java.util.Optional;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

// Keep renderer types and ViewFacade types totally separate
// Don't have them keep extending each other. That way, each
// layer has a specific job.
public abstract class RxAsyncValueFragment<
        AsyncValueFragmentType extends RxAsyncValueFragment<
                AsyncValueFragmentType,
                AsyncValueListenerType,
                AsyncValueAttachmentType
                >,
        AsyncValueListenerType,
        AsyncValueAttachmentType extends RxFragment.Attachment<
                AsyncValueFragmentType,
                AsyncValueListenerType,
                AsyncValueAttachmentType
                >
        > extends RxValueFragment<
        AsyncValueFragmentType,
        AsyncValueListenerType,
        AsyncValueAttachmentType
        > {
    // even though AsyncState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface AsyncValueState<
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueUnparcelableType
            > extends Either31<
            AsyncValueLoadingType,
            AsyncValueCompleteType,
            AsyncValueFailedType,
            AsyncValueUnparcelableType
            > {
        interface AsyncValueLoading<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueUnparcelableType
                > extends Either31.Left<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                > {
        }

        interface AsyncValueComplete<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueUnparcelableType
                > extends Either31.Middle<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                > {
        }

        interface AsyncValueFailed<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueUnparcelableType
                        >,
                AsyncValueUnparcelableType
                > extends Either31.Right<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueUnparcelableType
                > {
        }

        // redefine reduce/act to get better parameter names

        <T> T reduce(
                Function<
                        ? super AsyncValueLoadingType,
                        ? extends T
                        > loadingReducer,
                Function<
                        ? super AsyncValueCompleteType,
                        ? extends T
                        > completeReducer,
                Function<
                        ? super AsyncValueFailedType,
                        ? extends T
                        > failedReducer
        );

        void act(
                VoidFunction<? super AsyncValueLoadingType> loadingAction,
                VoidFunction<? super AsyncValueCompleteType> completeAction,
                VoidFunction<? super AsyncValueFailedType> failedAction
        );
    }

    protected interface AsyncValueViewFacade {
        void setLoadingViewVisible(boolean visible);

        void setCompleteViewVisible(boolean visible);

        void setFailedViewVisible(boolean visible);
    }

    // TODO make real objects for transactions instead of generic ones
    // generic objects don't tell us what methods are required, so they provide no guidance
    // it's cool that we were able to make that though.
    protected interface AsyncValueViewFacadeTransactionFactory<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<ValueUnparcelableType>,
            ValueUnparcelableType,
            AsyncValueViewFacadeTransactionType extends ObjectTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeType extends AsyncValueViewFacade
            > {
        AsyncValueViewFacadeTransactionType newAsyncValueViewFacadeTransaction(
                ValueViewFacadeTransactionType valueViewFacadeTransaction
        );
    }

    protected interface AsyncValueRenderer<
            AsyncValueFragmentType,
            AsyncValueListenerType,
            AsyncValueStateType,
            AsyncValueViewFacadeTransactionType
            > {
        @CheckResult
        Completable render(
                AsyncValueFragmentType fragment,
                AsyncValueListenerType listener,
                Context context,
                AsyncValueStateType asyncValueState,
                AsyncValueViewFacadeTransactionType asyncViewFacadeTransaction
        );
    }

    private static final class ValueRendererImplAsync<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            AsyncValueFragmentType extends RxAsyncValueFragment<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            AsyncValueListenerType,
            AsyncValueAttachmentType extends RxFragment.Attachment<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            ValueStateType extends ValueState<AsyncValueStateType>,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<AsyncValueStateType>,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueUnparcelableType,
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >,
            AsyncValueViewFacadeType extends RxAsyncValueFragment.AsyncValueViewFacade,
            AsyncValueViewFacadeTransactionType extends ObjectTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeTransactionFactoryType extends AsyncValueViewFacadeTransactionFactory<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >
            > implements ValueRenderer<
            AsyncValueFragmentType,
            AsyncValueListenerType,
            AsyncValueAttachmentType,
            ValueStateType,
            AsyncValueStateType,
            ValueViewFacadeTransactionType,
            ValueViewFacadeType
            > {
        private final AsyncValueViewFacadeTransactionFactoryType asyncValueViewFacadeTransactionFactory;
        private final MutableIdlingResource loadingMutableIdlingResource;
        private final MutableIdlingResource completeOrFailedMutableIdlingResource;
        private final AsyncValueRendererType asyncValueRenderer;

        private ValueRendererImplAsync(
                AsyncValueViewFacadeTransactionFactoryType asyncValueViewFacadeTransactionFactory,
                MutableIdlingResource loadingMutableIdlingResource,
                MutableIdlingResource completeOrFailedMutableIdlingResource,
                AsyncValueRendererType asyncValueRenderer
        ) {
            this.asyncValueViewFacadeTransactionFactory = asyncValueViewFacadeTransactionFactory;
            this.loadingMutableIdlingResource = loadingMutableIdlingResource;
            this.completeOrFailedMutableIdlingResource = completeOrFailedMutableIdlingResource;
            this.asyncValueRenderer = asyncValueRenderer;
        }

        @Override
        public Completable render(
                AsyncValueFragmentType fragment,
                AsyncValueListenerType listener,
                Context context,
                ValueStateType valueState,
                ValueViewFacadeTransactionType valueViewFacadeTransaction
        ) {
            final AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction =
                    asyncValueViewFacadeTransactionFactory.newAsyncValueViewFacadeTransaction(
                            valueViewFacadeTransaction
                    );

            final AsyncValueStateType asyncValueState = valueState.getValue();

            asyncValueState.act(
                    loading -> {
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setLoadingViewVisible,
                                true
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setCompleteViewVisible,
                                false
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setFailedViewVisible,
                                false
                        );
                    },
                    complete -> {
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setLoadingViewVisible,
                                false
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setCompleteViewVisible,
                                true
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setFailedViewVisible,
                                false
                        );
                    },
                    failed -> {
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setLoadingViewVisible,
                                false
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setCompleteViewVisible,
                                false
                        );
                        asyncValueViewFacadeTransaction.act(
                                AsyncValueViewFacadeType::setFailedViewVisible,
                                true
                        );
                    }
            );

            final Optional<Deceleration> decelerationOptional;
            if (valueState.isEnabled()) {
                final Deceleration deceleration =
                        asyncValueState.reduce(
                                loading -> {
                                    completeOrFailedMutableIdlingResource.setBusy();
                                    return loadingMutableIdlingResource.decelerate();
                                },
                                complete -> {
                                    loadingMutableIdlingResource.setBusy();
                                    return completeOrFailedMutableIdlingResource.decelerate();
                                },
                                failed -> {
                                    loadingMutableIdlingResource.setBusy();
                                    return completeOrFailedMutableIdlingResource.decelerate();
                                }
                        );
                decelerationOptional = Optional.of(deceleration);
            } else {
                loadingMutableIdlingResource.setBusy();
                completeOrFailedMutableIdlingResource.setBusy();
                decelerationOptional = Optional.empty();
            }

            final Completable completable =
                    asyncValueRenderer.render(
                            fragment,
                            listener,
                            context,
                            asyncValueState,
                            asyncValueViewFacadeTransaction
                    );

            final Disposable ignored =
                    decelerationOptional.map(
                            deceleration ->
                                    completable.subscribe(
                                            deceleration::maybeSetIdle
                                    )
                    ).orElse(Disposables.disposed());

            return completable;
        }
    }

    private final IdlingResource loadingIdlingResource;
    private final IdlingResource completeOrFailedIdlingResource;

    protected <
            AsyncValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            AsyncValueOnAttachedType extends OnAttached<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            AsyncValueWillDetachType extends WillDetach<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<AsyncValueStateType>,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    ValueStateType,
                    AsyncValueStateType
                    >,
            ValueStateType extends ValueState<AsyncValueStateType>,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueUnparcelableType,
            ValueViewFacadeTransactionFactoryType extends ValueViewFacadeTransactionFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    ValueViewFacadeTransactionType
                    >,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            AsyncValueViewFacadeTransactionFactoryType extends AsyncValueViewFacadeTransactionFactory<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeTransactionType extends ObjectTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeType extends AsyncValueViewFacade,
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >
            > RxAsyncValueFragment(
            Class<AsyncValueFragmentType> selfClass,
            Class<AsyncValueListenerType> listenerClass,
            AsyncValueAttachmentFactoryType attachmentFactory,
            AsyncValueOnAttachedType onAttached,
            AsyncValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeTransactionFactoryType valueViewFacadeTransactionFactory,
            AsyncValueViewFacadeTransactionFactoryType asyncValueViewFacadeTransactionFactory,
            AsyncValueRendererType asyncValueRenderer
    ) {
        this(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                valueViewFacadeFactory,
                valueStateObservableProvider,
                valueViewFacadeTransactionFactory,
                asyncValueViewFacadeTransactionFactory,
                MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading"),
                MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed"),
                asyncValueRenderer
        );
    }


    private <
            AsyncValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            AsyncValueOnAttachedType extends OnAttached<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            AsyncValueWillDetachType extends WillDetach<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade<AsyncValueStateType>,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    AsyncValueValueStateType,
                    AsyncValueStateType
                    >,
            AsyncValueValueStateType extends ValueState<AsyncValueStateType>,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueUnparcelableType
                    >,
            AsyncValueUnparcelableType,
            ValueViewFacadeTransactionFactoryType extends ValueViewFacadeTransactionFactory<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueAttachmentType,
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    ValueViewFacadeTransactionType
                    >,
            ValueViewFacadeTransactionType extends ObjectTransaction<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType
                    >,
            AsyncValueViewFacadeTransactionFactoryType extends AsyncValueViewFacadeTransactionFactory<
                    ValueViewFacadeTransactionType,
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeTransactionType extends ObjectTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueViewFacadeType
                    >,
            AsyncValueViewFacadeType extends AsyncValueViewFacade,
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    AsyncValueListenerType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >
            > RxAsyncValueFragment(
            Class<AsyncValueFragmentType> selfClass,
            Class<AsyncValueListenerType> listenerClass,
            AsyncValueAttachmentFactoryType attachmentFactory,
            AsyncValueOnAttachedType onAttached,
            AsyncValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeTransactionFactoryType valueViewFacadeTransactionFactory,
            AsyncValueViewFacadeTransactionFactoryType asyncValueViewFacadeTransactionFactory,
            MutableIdlingResource loadingMutableIdlingResource,
            MutableIdlingResource completeOrFailedMutableIdlingResource,
            AsyncValueRendererType asyncValueRenderer
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                valueViewFacadeFactory,
                valueStateObservableProvider,
                valueViewFacadeTransactionFactory,
                new ValueRendererImplAsync<>(
                        asyncValueViewFacadeTransactionFactory,
                        loadingMutableIdlingResource,
                        completeOrFailedMutableIdlingResource,
                        asyncValueRenderer
                )
        );
        this.loadingIdlingResource = loadingMutableIdlingResource;
        this.completeOrFailedIdlingResource = completeOrFailedMutableIdlingResource;
    }

    public final IdlingResource getLoadingIdlingResource() {
        return loadingIdlingResource;
    }

    public final IdlingResource getCompleteOrFailedIdlingResource() {
        return completeOrFailedIdlingResource;
    }
}
