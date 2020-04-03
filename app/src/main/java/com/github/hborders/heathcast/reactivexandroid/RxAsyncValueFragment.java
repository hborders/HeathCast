package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource.Deceleration;

import java.util.Optional;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

// Make this RxAsyncValue
// Make RxValue just have the Model+ViewHolder+Renderer
public abstract class RxAsyncValueFragment<
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
        > extends RxValueFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    // even though AsyncState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface AsyncState<
            LoadingType extends AsyncState.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            CompleteType extends AsyncState.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            FailedType extends AsyncState.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            UnparcelableValueType
            > extends Either31<
            LoadingType,
            CompleteType,
            FailedType,
            UnparcelableValueType
            > {
        interface Loading<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                UnparcelableValueType
                > extends Either31.Left<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                >, AsyncState<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                > {
        }

        interface Complete<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                UnparcelableValueType
                > extends Either31.Middle<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                >, AsyncState<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                > {
        }

        interface Failed<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        UnparcelableValueType
                        >,
                UnparcelableValueType
                > extends Either31.Right<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                >, AsyncState<
                LoadingType,
                CompleteType,
                FailedType,
                UnparcelableValueType
                > {
        }

        // redefine reduce/act to get better parameter names

        <T> T reduce(
                Function<
                        ? super LoadingType,
                        ? extends T
                        > loadingReducer,
                Function<
                        ? super CompleteType,
                        ? extends T
                        > completeReducer,
                Function<
                        ? super FailedType,
                        ? extends T
                        > failedReducer
        );

        void act(
                VoidFunction<? super LoadingType> loadingAction,
                VoidFunction<? super CompleteType> completeAction,
                VoidFunction<? super FailedType> failedAction
        );
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
            ViewFacadeFactoryType extends ViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewFacadeType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType
                    >,
            ViewFacadeTransactionFactoryType extends ViewFacadeTransactionFactory<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            StateType extends State<AsyncStateType>,
            AsyncStateType extends AsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            LoadingType extends AsyncState.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            CompleteType extends AsyncState.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            FailedType extends AsyncState.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            UnparcelableValueType,
            ViewFacadeType extends ViewFacade<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            ViewFacadeTransactionType extends ViewFacadeTransaction<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >
            > RxAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider,
            ViewFacadeTransactionFactoryType viewFacadeTransactionFactory,
            RendererType renderer
    ) {
        this(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                viewFacadeFactory,
                stateObservableProvider,
                viewFacadeTransactionFactory,
                renderer,
                MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading"),
                MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed")
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
            ViewFacadeFactoryType extends ViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewFacadeType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType
                    >,
            ViewFacadeTransactionFactoryType extends ViewFacadeTransactionFactory<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            StateType extends State<AsyncStateType>,
            AsyncStateType extends AsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            LoadingType extends AsyncState.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            CompleteType extends AsyncState.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            FailedType extends AsyncState.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableValueType
                    >,
            UnparcelableValueType,
            ViewFacadeType extends ViewFacade<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            ViewFacadeTransactionType extends ViewFacadeTransaction<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >
            > RxAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider,
            ViewFacadeTransactionFactoryType viewFacadeTransactionFactory,
            RendererType renderer,
            MutableIdlingResource loadingMutableIdlingResource,
            MutableIdlingResource completeOrFailedMutableIdlingResource
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                viewFacadeFactory,
                stateObservableProvider,
                viewFacadeTransactionFactory,
                (
                        FragmentType fragmentType,
                        ListenerType listener,
                        Context context,
                        StateType state,
                        ViewFacadeTransactionType viewFacadeTransaction
                ) -> {
                    final Optional<Deceleration> decelerationOptional;
                    if (state.isEnabled()) {
                        final Deceleration deceleration =
                                state.getValue().reduce(
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

                    final Completable completable = renderer.render(
                            fragmentType,
                            listener,
                            context,
                            state,
                            viewFacadeTransaction
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
