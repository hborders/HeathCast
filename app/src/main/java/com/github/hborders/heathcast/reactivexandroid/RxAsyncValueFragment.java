package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource.Deceleration;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

// Keep renderer types and ViewFacade types totally separate
// Don't have them keep extending each other. That way, each
// layer has a specific job.
public abstract class RxAsyncValueFragment<
        AsyncValueFragmentType extends RxAsyncValueFragment<
                AsyncValueFragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                AsyncValueFragmentType,
                ListenerType,
                AttachmentType
                >
        > extends RxValueFragment<
        AsyncValueFragmentType,
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
            > extends Either31<
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

    protected interface AsyncValueViewFacade {
    }

    protected interface AsyncValueViewFacadeTransaction {
    }

    protected interface AsyncValueCompletable<
            ValueCompletableType extends ValueCompletable
            > {
        Completable toCompletable();
        ValueCompletableType toValueCompletable();
    }

    protected interface AsyncValueViewFacadeCompletableTransaction<
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
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
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        AsyncValueViewFacadeTransactionType toAsyncValueViewFacadeTransaction(
                AsyncValueStateType asyncValueState
        );

        AsyncValueCompletableType complete();

        void addDisposable(Disposable disposable);
    }

    protected interface AsyncValueViewFacadeCompletableTransactionFactory<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            AsyncValueViewFacadeCompletableTransactionType extends AsyncValueViewFacadeCompletableTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
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
                    AsyncValueType>,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType>,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType>,
            AsyncValueType,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        AsyncValueViewFacadeCompletableTransactionType newAsyncValueViewFacadeCompletableTransaction(
                ValueViewFacadeTransactionType valueViewFacadeTransaction
        );
    }

    protected interface AsyncValueRenderer<
            AsyncValueFragmentType extends RxAsyncValueFragment<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueType,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        AsyncValueCompletableType render(
                AsyncValueFragmentType fragment,
                ListenerType listener,
                Context context,
                AsyncValueType asyncValue,
                AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction
        );
    }

    private static final class ValueRendererImpl<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            AsyncValueFragmentType extends RxAsyncValueFragment<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
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
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            AsyncValueViewFacadeCompletableTransactionFactoryType extends AsyncValueViewFacadeCompletableTransactionFactory<
                    ValueViewFacadeTransactionType,
                    AsyncValueViewFacadeCompletableTransactionType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeCompletableTransactionType extends AsyncValueViewFacadeCompletableTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > implements ValueRenderer<
            AsyncValueFragmentType,
            ListenerType,
            AttachmentType,
            AsyncValueStateType,
            ValueViewFacadeTransactionType,
            ValueCompletableType
            > {
        private final AsyncValueViewFacadeCompletableTransactionFactoryType asyncValueViewFacadeCompletableTransactionFactory;
        private final MutableIdlingResource loadingMutableIdlingResource;
        private final MutableIdlingResource completeOrFailedMutableIdlingResource;
        private final AsyncValueRendererType asyncValueRenderer;

        private ValueRendererImpl(
                AsyncValueViewFacadeCompletableTransactionFactoryType asyncValueViewFacadeCompletableTransactionFactory,
                MutableIdlingResource loadingMutableIdlingResource,
                MutableIdlingResource completeOrFailedMutableIdlingResource,
                AsyncValueRendererType asyncValueRenderer
        ) {
            this.asyncValueViewFacadeCompletableTransactionFactory = asyncValueViewFacadeCompletableTransactionFactory;
            this.loadingMutableIdlingResource = loadingMutableIdlingResource;
            this.completeOrFailedMutableIdlingResource = completeOrFailedMutableIdlingResource;
            this.asyncValueRenderer = asyncValueRenderer;
        }

        @Override
        public ValueCompletableType render(
                AsyncValueFragmentType fragment,
                ListenerType listener,
                Context context,
                AsyncValueStateType asyncValueState,
                ValueViewFacadeTransactionType valueViewFacadeTransaction
        ) {
            final AsyncValueType asyncValue = asyncValueState.getValue();
            final AsyncValueViewFacadeCompletableTransactionType asyncValueViewFacadeCompletableTransaction =
                    asyncValueViewFacadeCompletableTransactionFactory.newAsyncValueViewFacadeCompletableTransaction(
                            valueViewFacadeTransaction
                    );
            final AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction =
                    asyncValueViewFacadeCompletableTransaction.toAsyncValueViewFacadeTransaction(
                            // TODO move asyncValueState into completableTransactionFactory
                            asyncValueState
                    );

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

            final AsyncValueCompletableType asyncValueCompletable =
                    asyncValueRenderer.render(
                            fragment,
                            listener,
                            context,
                            asyncValue,
                            asyncValueViewFacadeTransaction
                    );
            final Completable completable = asyncValueCompletable.toCompletable();

            final Disposable disposable =
                    completable.subscribe(deceleration::maybeSetIdle);
            asyncValueViewFacadeCompletableTransaction.addDisposable(disposable);

            return asyncValueCompletable.toValueCompletable();
        }
    }

    private final IdlingResource loadingIdlingResource;
    private final IdlingResource completeOrFailedIdlingResource;

    protected <
            AsyncValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueOnAttachedType extends OnAttached<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueWillDetachType extends WillDetach<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType,
                    AsyncValueStateType
                    >,
            ValueStateType extends ValueState<AsyncValueStateType>,
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
            ValueViewFacadeCompletableTransactionFactoryType extends ValueViewFacadeCompletableTransactionFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType,
                    ValueViewFacadeCompletableTransactionType,
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            AsyncValueViewFacadeCompletableTransactionFactoryType extends AsyncValueViewFacadeCompletableTransactionFactory<
                    ValueViewFacadeTransactionType,
                    AsyncValueViewFacadeCompletableTransactionType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeCompletableTransactionType extends AsyncValueViewFacadeCompletableTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<
                    ValueCompletableType
                    >,
            ValueCompletableType extends ValueCompletable
            > RxAsyncValueFragment(
            Class<AsyncValueFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AsyncValueAttachmentFactoryType attachmentFactory,
            AsyncValueOnAttachedType onAttached,
            AsyncValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeCompletableTransactionFactoryType valueViewFacadeCompletableTransactionFactory,
            AsyncValueViewFacadeCompletableTransactionFactoryType asyncValueViewFacadeCompletableTransactionFactory,
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
                valueViewFacadeCompletableTransactionFactory,
                asyncValueViewFacadeCompletableTransactionFactory,
                MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading"),
                MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed"),
                asyncValueRenderer
        );
    }


    private <
            AsyncValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueOnAttachedType extends OnAttached<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            AsyncValueWillDetachType extends WillDetach<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType,
                    AsyncValueStateType
                    >,
            ValueStateType extends ValueState<AsyncValueStateType>,
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
            ValueViewFacadeCompletableTransactionFactoryType extends ValueViewFacadeCompletableTransactionFactory<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType,
                    ValueViewFacadeCompletableTransactionType,
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            AsyncValueViewFacadeCompletableTransactionFactoryType extends AsyncValueViewFacadeCompletableTransactionFactory<
                    ValueViewFacadeTransactionType,
                    AsyncValueViewFacadeCompletableTransactionType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeCompletableTransactionType extends AsyncValueViewFacadeCompletableTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    AsyncValueType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            AsyncValueRendererType extends AsyncValueRenderer<
                    AsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<
                    ValueCompletableType
                    >,
            ValueCompletableType extends ValueCompletable
            > RxAsyncValueFragment(
            Class<AsyncValueFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AsyncValueAttachmentFactoryType attachmentFactory,
            AsyncValueOnAttachedType onAttached,
            AsyncValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeCompletableTransactionFactoryType valueViewFacadeCompletableTransactionFactory,
            AsyncValueViewFacadeCompletableTransactionFactoryType asyncValueViewFacadeCompletableTransactionFactory,
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
                valueViewFacadeCompletableTransactionFactory,
                new ValueRendererImpl<>(
                        asyncValueViewFacadeCompletableTransactionFactory,
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
