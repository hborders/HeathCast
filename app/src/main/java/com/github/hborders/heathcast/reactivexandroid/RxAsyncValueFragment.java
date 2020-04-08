package com.github.hborders.heathcast.reactivexandroid;

import androidx.annotation.LayoutRes;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource.Deceleration;

import io.reactivex.Completable;

// Keep renderer types and ViewFacade types totally separate
// Don't have them keep extending each other. That way, each
// layer has a specific job.
public abstract class RxAsyncValueFragment<
        FragmentType extends RxAsyncValueFragment<
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
        > extends RxValueFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    // even though AsyncValueState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface AsyncValueState<
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueType
            > extends ValueState,
            Either31<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    > {
        interface AsyncValueLoading<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueType
                > extends Either31.Left<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
                > {
        }

        interface AsyncValueComplete<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueType
                > extends Either31.Middle<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
                > {
        }

        interface AsyncValueFailed<
                AsyncValueLoadingType extends AsyncValueLoading<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueCompleteType extends AsyncValueComplete<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueFailedType extends AsyncValueFailed<
                        AsyncValueLoadingType,
                        AsyncValueCompleteType,
                        AsyncValueFailedType,
                        AsyncValueType
                        >,
                AsyncValueType
                > extends Either31.Right<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
                >, AsyncValueState<
                AsyncValueLoadingType,
                AsyncValueCompleteType,
                AsyncValueFailedType,
                AsyncValueType
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

    private static final class ValueRendererImpl<
            ValueViewFacadeType extends ValueViewFacade,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueType,
            AsyncValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            AsyncValueRendererType extends ValueRenderer<
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >
            > implements ValueRenderer<
            ValueViewFacadeType,
            AsyncValueStateType,
            ValueRendererImpl.ValueViewFacadeTransactionImpl<
                    AsyncValueViewFacadeTransactionType
                    >
            > {
        private static final class ValueViewFacadeTransactionImpl<
                AsyncValueViewFacadeTransactionType extends ValueViewFacadeTransaction
                > implements ValueViewFacadeTransaction {
            private final Deceleration deceleration;
            private final AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction;

            private ValueViewFacadeTransactionImpl(
                    Deceleration deceleration,
                    AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction
            ) {
                this.deceleration = deceleration;
                this.asyncValueViewFacadeTransaction = asyncValueViewFacadeTransaction;
            }

            @Override
            public Completable complete() {
                final Completable completable = asyncValueViewFacadeTransaction.complete();
                return completable.doOnComplete(deceleration::maybeSetIdle);
            }
        }

        private final AsyncValueRendererType asyncValueRenderer;
        private final MutableIdlingResource loadingMutableIdlingResource;
        private final MutableIdlingResource completeOrFailedMutableIdlingResource;

        private ValueRendererImpl(
                AsyncValueRendererType asyncValueRenderer,
                MutableIdlingResource loadingMutableIdlingResource,
                MutableIdlingResource completeOrFailedMutableIdlingResource
        ) {
            this.asyncValueRenderer = asyncValueRenderer;
            this.loadingMutableIdlingResource = loadingMutableIdlingResource;
            this.completeOrFailedMutableIdlingResource = completeOrFailedMutableIdlingResource;
        }

        @Override
        public ValueRendererImpl.ValueViewFacadeTransactionImpl<
                AsyncValueViewFacadeTransactionType
                > render(
                ValueViewFacadeType valueViewFacade,
                AsyncValueStateType asyncValueState
        ) {
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

            final AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction =
                    asyncValueRenderer.render(
                            valueViewFacade,
                            asyncValueState
                    );

            return new ValueViewFacadeTransactionImpl<>(
                    deceleration,
                    asyncValueViewFacadeTransaction
            );
        }
    }

    private final IdlingResource loadingIdlingResource;
    private final IdlingResource completeOrFailedIdlingResource;

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
                    AsyncValueStateType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueType,
            AsyncValueRendererType extends ValueRenderer<
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >,
            AsyncValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            > RxAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
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
                MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading"),
                MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed"),
                asyncValueRenderer
        );
    }


    private <
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
                    AsyncValueStateType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            AsyncValueType,
            AsyncValueRendererType extends ValueRenderer<
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >,
            AsyncValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            > RxAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
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
                new ValueRendererImpl<>(
                        asyncValueRenderer,
                        loadingMutableIdlingResource,
                        completeOrFailedMutableIdlingResource
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
