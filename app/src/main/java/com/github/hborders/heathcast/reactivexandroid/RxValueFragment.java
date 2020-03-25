package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.github.hborders.heathcast.android.ViewUtil.setUserInteractionEnabled;

public abstract class RxValueFragment<
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
                >,
        ModelType extends RxValueFragment.Model<UnparcelableValueType>,
        UnparcelableValueType
        > extends RxFragment<
        FragmentType,
        ListenerType,
        AttachmentType
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
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
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

    protected interface ViewHolderProvider<
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
                    >,
            ViewHolderType
            > {
        ViewHolderType viewHolder(View view);
    }

    protected interface Renderer<
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
            ViewHolderType
            > {
        void render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewHolderType viewHolder,
                StateType state
        );
    }

    private final MutableIdlingResource loadingMutableIdlingResource;
    private final MutableIdlingResource completeOrFailedMutableIdlingResource;

    protected <
            AttachmentFactoryType extends Attachment.Factory<
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
            ViewHolderProviderType extends ViewHolderProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewHolderType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType,
                    ViewHolderType
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
            ViewHolderType
            > RxValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            ViewHolderProviderType viewHolderProvider,
            StateObservableProviderType stateObservableProvider,
            RendererType renderer
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );


        loadingMutableIdlingResource =
                MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading");
        completeOrFailedMutableIdlingResource =
                MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed");

        final class Prez {
            final Context context;
            final ListenerType listener;
            final ViewCreation viewCreation;
            final View view;
            final ViewHolderType viewHolder;

            Prez(
                    Context context,
                    ListenerType listener,
                    ViewCreation viewCreation,
                    View view,
                    ViewHolderType viewHolder
            ) {
                this.context = context;
                this.listener = listener;
                this.viewCreation = viewCreation;
                this.view = view;
                this.viewHolder = viewHolder;
            }
        }

        final Observable<Prez> prezObservable = beginRxGraph().switchMap(
                attachmentTypeObservable -> attachmentTypeObservable
        ).switchMap(
                Attachment::switchMapToViewCreation
        ).map(
                attachmentFragmentCreationViewCreationTriple -> {
                    final Context context =
                            attachmentFragmentCreationViewCreationTriple.first.context;
                    final ListenerType listener =
                            attachmentFragmentCreationViewCreationTriple.first.listener;
                    final ViewCreation viewCreation =
                            attachmentFragmentCreationViewCreationTriple.third;
                    final View view = viewCreation.view;
                    final ViewHolderType viewHolder = viewHolderProvider.viewHolder(view);
                    return new Prez(
                            context,
                            listener,
                            viewCreation,
                            view,
                            viewHolder
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

                    setUserInteractionEnabled(
                            prerender.prez.view,
                            prerender.state.getValue().enabled
                    );

                    renderer.render(
                            getSelf(),
                            prerender.prez.listener,
                            prerender.prez.context,
                            prerender.prez.viewHolder,
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
