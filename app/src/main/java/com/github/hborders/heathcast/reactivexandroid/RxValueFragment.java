package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class RxValueFragment<
        FragmentType extends RxFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        AttachmentFactoryType extends RxFragment.Attachment.Factory<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        ModelType extends RxValueFragment.Model<UnparcelableValueType>,
        UnparcelableValueType,
        ViewType extends View
        > extends RxFragment<
        FragmentType,
        ListenerType,
        AttachmentType,
        AttachmentFactoryType
        > {
    public static abstract class Model<UnparcelableValueType> {
        final UnparcelableValueType value;
        final boolean enabled;

        public Model(
                UnparcelableValueType value,
                boolean enabled
        ) {
            this.value = value;
            this.enabled = enabled;
        }

        @Override
        public final boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model<?> model = (Model<?>) o;
            return value.equals(model.value) &&
                    enabled == model.enabled;
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value, enabled);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends Model> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Model$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    "value=" + value +
                    ", enabled=" + enabled +
                    '}';
        }
    }

    protected interface State<
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            ModelType extends Model<UnparcelableValueType>,
            UnparcelableValueType
            > extends Either31<
            LoadingType,
            CompleteType,
            FailedType,
            ModelType
            > {
        abstract class Loading<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                ModelType extends Model<UnparcelableValueType>,
                UnparcelableValueType
                > extends Either31.Left<
                LoadingType,
                CompleteType,
                FailedType,
                ModelType
                > {
            public Loading(ModelType value) {
                super(value);
            }
        }

        abstract class Complete<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                ModelType extends Model<UnparcelableValueType>,
                UnparcelableValueType
                > extends Either31.Middle<
                LoadingType,
                CompleteType,
                FailedType,
                ModelType
                > {
            public Complete(ModelType value) {
                super(value);
            }
        }

        abstract class Failed<
                LoadingType extends Loading<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                CompleteType extends Complete<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                FailedType extends Failed<
                        LoadingType,
                        CompleteType,
                        FailedType,
                        ModelType,
                        UnparcelableValueType
                        >,
                ModelType extends Model<UnparcelableValueType>,
                UnparcelableValueType
                > extends Either31.Right<
                LoadingType,
                CompleteType,
                FailedType,
                ModelType
                > {
            public Failed(ModelType value) {
                super(value);
            }
        }
    }

    protected interface StateObservableProvider<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            ModelType extends RxValueFragment.Model<UnparcelableValueType>,
            UnparcelableValueType
            > {
        Observable<StateType> stateObservable(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface ViewProvider<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ViewType extends View
            > {
        ViewType requireView(View fragmentView);
    }

    protected interface Renderer<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            ModelType extends RxValueFragment.Model<UnparcelableValueType>,
            UnparcelableValueType,
            ViewType extends View
            > {
        void render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewType view,
                StateType state
        );
    }

    private final MutableIdlingResource loadingMutableIdlingResource;
    private final MutableIdlingResource completeOrFailedMutableIdlingResource;

    protected <
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >
            > RxValueFragment(
            Class<FragmentType> fragmentClass,
            Class<ListenerType> listenerClass,
            Class<AttachmentType> attachmentClass,
            AttachmentFactoryType attachmentFactory,
            OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    > onAttached,
            WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    > willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            ViewProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    ViewType
                    > viewProvider,
            StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    StateType,
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    > stateObservableProvider,
            Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    StateType,
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType,
                    ViewType
                    > renderer
    ) {
        super(
                fragmentClass,
                listenerClass,
                attachmentClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );


        loadingMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading");

        completeOrFailedMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed");

        final class Prez {
            final Context context;
            final ListenerType listener;
            final ViewCreation viewCreation;
            final ViewType view;

            Prez(
                    Context context,
                    ListenerType listener,
                    ViewCreation viewCreation,
                    ViewType view
            ) {
                this.context = context;
                this.listener = listener;
                this.viewCreation = viewCreation;
                this.view = view;
            }
        }

        final Observable<Prez> prezObservable = beginRxGraph().switchMap(
                Attachment::switchMapToViewCreation
        ).map(
                attachmentFragmentCreationViewCreationTriple -> {
                    final Context context =
                            attachmentFragmentCreationViewCreationTriple.first.context;
                    final ListenerType listener =
                            attachmentFragmentCreationViewCreationTriple.first.listener;
                    final ViewCreation viewCreation =
                            attachmentFragmentCreationViewCreationTriple.third;
                    final View fragmentView = viewCreation.view;
                    final ViewType view = viewProvider.requireView(fragmentView);
                    return new Prez(
                            context,
                            listener,
                            viewCreation,
                            view
                    );
                }
        );

        final class Prerender {
            final Prez prez;
            final StateType state;

            Prerender(
                    Prez prez,
                    StateType state
            ) {
                this.prez = prez;
                this.state = state;
            }
        }

        final Observable<Prerender> renderingAttemptObservable =
                prezObservable.switchMap(
                        prez ->
                                prez.viewCreation.switchMapToStart().switchMap(
                                        start ->
                                                stateObservableProvider
                                                        .stateObservable(
                                                                prez.listener,
                                                                getSelf()
                                                        )
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .map(
                                                                state ->
                                                                        new Prerender(
                                                                                prez,
                                                                                state
                                                                        )
                                                        )
                                )
                );
        renderingAttemptObservable.subscribe(
                prerender -> {
                    markIdlingResourcesAsBusyBeforeRender();

                    renderer.render(
                            getSelf(),
                            prerender.prez.listener,
                            prerender.prez.context,
                            prerender.prez.view,
                            prerender.state
                    );

                    updateIdlingResourcesPostRender(prerender.state);
                }
        ).isDisposed();
    }

    private void markIdlingResourcesAsBusyBeforeRender() {
        loadingMutableIdlingResource.setBusy();
        completeOrFailedMutableIdlingResource.setBusy();
    }

    private <
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >
            > void updateIdlingResourcesPostRender(StateType state) {
        if (
                state
                        .getValue()
                        .enabled
        ) {
            state.act(
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
            loadingMutableIdlingResource.setBusy();
            completeOrFailedMutableIdlingResource.setBusy();
        }
    }
}
