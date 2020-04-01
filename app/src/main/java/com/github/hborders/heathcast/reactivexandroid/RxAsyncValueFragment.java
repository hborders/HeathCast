package com.github.hborders.heathcast.reactivexandroid;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.android.ViewUtil.ViewAction;
import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource.Deceleration;

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

    protected interface AsyncValueViewFacade extends ViewFacade {
        void doOnLayout(ViewAction viewAction);
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
                    AsyncValueViewFacadeType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType
                    >,
            UnrendererType extends Unrenderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueViewFacadeType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    AsyncValueViewFacadeType
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
            AsyncValueViewFacadeType extends AsyncValueViewFacade,
            UnparcelableValueType
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
            @Nullable UnrendererType unrenderer,
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
                unrenderer,
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
                    AsyncValueViewFacadeType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType
                    >,
            UnrendererType extends Unrenderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueViewFacadeType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    AsyncValueViewFacadeType
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
            AsyncValueViewFacadeType extends AsyncValueViewFacade,
            UnparcelableValueType
            > RxAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider,
            @Nullable UnrendererType unrenderer,
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
                (fragmentType, listener, context, viewFacade) -> {
//                    loadingMutableIdlingResource.setBusy();
//                    completeOrFailedMutableIdlingResource.setBusy();

                    if (unrenderer != null) {
                        unrenderer.unrender(fragmentType, listener, context, viewFacade);
                    }
                },
                (fragmentType, listener, context, viewFacade, state) -> {
                    renderer.render(fragmentType, listener, context, viewFacade, state);

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
                        viewFacade.doOnLayout(
                                view -> deceleration.maybeSetIdle()
                        );
                    } else {
                        loadingMutableIdlingResource.setBusy();
                        completeOrFailedMutableIdlingResource.setBusy();
                    }
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
