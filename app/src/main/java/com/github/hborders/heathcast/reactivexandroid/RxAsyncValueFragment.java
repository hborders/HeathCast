package com.github.hborders.heathcast.reactivexandroid;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;

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

    protected interface AsyncState<
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
            ViewHolderFactoryType extends ViewHolderFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewHolderType,
                    ViewType
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
                    ViewHolderType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    ViewHolderType
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
            ViewHolderType extends ViewHolder<ViewType>,
            ViewType extends View,
            UnparcelableValueType
            > RxAsyncValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ViewHolderFactoryType viewHolderFactory,
            StateObservableProviderType stateObservableProvider,
            UnrendererType unrenderer,
            RendererType renderer
    ) {
        this(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                viewHolderFactory,
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
            ViewHolderFactoryType extends ViewHolderFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewHolderType,
                    ViewType
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
                    ViewHolderType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType,
                    ViewHolderType
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
            ViewHolderType extends ViewHolder<ViewType>,
            ViewType extends View,
            UnparcelableValueType
            > RxAsyncValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewHolderFactoryType viewHolderFactory,
            StateObservableProviderType stateObservableProvider,
            UnrendererType unrenderer,
            RendererType renderer,
            MutableIdlingResource loadingMutableIdlingResource,
            MutableIdlingResource completeOrFailedMutableIdlingResource
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                viewHolderFactory,
                stateObservableProvider,
                (fragmentType, listener, context, viewHolder) -> {
                    loadingMutableIdlingResource.setBusy();
                    completeOrFailedMutableIdlingResource.setBusy();

                    unrenderer.unrender(fragmentType, listener, context, viewHolder);
                },
                (fragmentType, listener, context, viewHolder, state) -> {
                    renderer.render(fragmentType, listener, context, viewHolder, state);

                    if (state.enabled) {
                        state.value.act(
                                loading -> {
                                    loadingMutableIdlingResource.setIdle();
                                    completeOrFailedMutableIdlingResource.setBusy();
                                },
                                complete -> {
                                    loadingMutableIdlingResource.setBusy();
                                    completeOrFailedMutableIdlingResource.setIdle();
                                },
                                failed -> {
                                    loadingMutableIdlingResource.setBusy();
                                    completeOrFailedMutableIdlingResource.setIdle();
                                }
                        );
                    } else {
                        // leave idling resources busy
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
