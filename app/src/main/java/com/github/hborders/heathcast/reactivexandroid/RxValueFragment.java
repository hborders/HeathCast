package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.github.hborders.heathcast.android.ViewUtil.setUserInteractionEnabled;

// Make this RxAsyncValue
// Make RxValue just have the Model+ViewHolder+Renderer
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
                >
        > extends RxFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    protected static abstract class State<UnparcelableValueType> {
        final UnparcelableValueType value;
        final boolean enabled;

        public State(
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
            State<?> state = (State<?>) o;
            return value.equals(state.value) &&
                    enabled == state.enabled;
        }

        @Override
        public final int hashCode() {
            return Objects.hash(value, enabled);
        }

        @Override
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends State> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "State$";
            } else {
                simpleName = clazz.getSimpleName();
            }
            return simpleName + "{" +
                    "value=" + value +
                    ", enabled=" + enabled +
                    '}';
        }
    }

    protected interface StateObservableProvider<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            StateType extends State<UnparcelableValueType>,
            UnparcelableValueType
            > {
        Observable<StateType> stateObservable(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface ViewHolder<ViewType extends View> {
        ViewType requireView();
    }

    protected interface ViewHolderFactory<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ViewHolderType extends ViewHolder<ViewType>,
            ViewType extends View
            > {
        ViewHolderType newViewHolder(
                FragmentType fragment,
                ListenerType listener,
                View view
        );
    }

    protected interface Unrenderer<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ViewHolderType
            > {
        void unrender(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewHolderType viewHolder
        );
    }

    protected interface Renderer<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            StateType extends State<UnparcelableValueType>,
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
                    UnparcelableValueType
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
                    UnparcelableValueType,
                    ViewHolderType
                    >,
            StateType extends State<UnparcelableValueType>,
            UnparcelableValueType,
            ViewHolderType extends ViewHolder<ViewType>,
            ViewType extends View
            > RxValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewHolderFactoryType viewHolderFactory,
            StateObservableProviderType stateObservableProvider,
            UnrendererType unrenderer,
            RendererType renderer
    ) {
        super(
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
                                final ViewHolderType viewHolder = viewHolderFactory.newViewHolder(
                                        getSelf(),
                                        listener,
                                        view
                                );
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

            abstract class RenderingChange {
                abstract void changeRendering(Prez prez);
            }
            final class RenderOnly extends RenderingChange {
                private final StateType state;

                RenderOnly(StateType state) {
                    this.state = state;
                }

                @Override
                void changeRendering(Prez prez) {
                    renderer.render(
                            getSelf(),
                            prez.listener,
                            prez.context,
                            prez.viewHolder,
                            state
                    );
                }
            }
            final class UnrenderThenRender extends RenderingChange {
                private final StateType state;

                UnrenderThenRender(StateType state) {
                    this.state = state;
                }

                @Override
                void changeRendering(Prez prez) {
                    unrenderer.unrender(
                            getSelf(),
                            prez.listener,
                            prez.context,
                            prez.viewHolder
                    );
                    renderer.render(
                            getSelf(),
                            prez.listener,
                            prez.context,
                            prez.viewHolder,
                            state
                    );
                }
            }
            final class UnrenderOnly extends RenderingChange {
                @Override
                void changeRendering(Prez prez) {
                    unrenderer.unrender(
                            getSelf(),
                            prez.listener,
                            prez.context,
                            prez.viewHolder
                    );
                }
            }

            final Observable<Prerender> prerenderObservable =
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


            return prerenderObservable.subscribe(
                    prerender -> {
                        unrenderer.unrender(
                                getSelf(),
                                prerender.prez.listener,
                                prerender.prez.context,
                                prerender.prez.viewHolder
                        );
                        setUserInteractionEnabled(
                                prerender.prez.view,
                                prerender.state.enabled
                        );
                        renderer.render(
                                getSelf(),
                                prerender.prez.listener,
                                prerender.prez.context,
                                prerender.prez.viewHolder,
                                prerender.state
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
