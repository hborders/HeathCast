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

    protected interface ViewFacade extends Disposable {
        void setEnabled(boolean enabled);
    }

    protected interface ViewFacadeFactory<
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
            ViewFacadeType
            > {
        ViewFacadeType newViewFacade(
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
            ViewFacadeType
            > {
        void unrender(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewFacadeType viewFacade
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
            ViewFacadeType
            > {
        void render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewFacadeType viewFacade,
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
                    UnparcelableValueType
                    >,
            UnrendererType extends Unrenderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ViewFacadeType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    UnparcelableValueType,
                    ViewFacadeType
                    >,
            StateType extends State<UnparcelableValueType>,
            UnparcelableValueType,
            ViewFacadeType extends ViewFacade
            > RxValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewFacadeFactoryType viewFacadeFactory,
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
                final ViewFacadeType viewFacade;

                Prez(
                        Context context,
                        ListenerType listener,
                        ViewCreation viewCreation,
                        View view,
                        ViewFacadeType viewFacade
                ) {
                    this.context = context;
                    this.listener = listener;
                    this.viewCreation = viewCreation;
                    this.view = view;
                    this.viewFacade = viewFacade;
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
                                final ViewFacadeType viewFacade = viewFacadeFactory.newViewFacade(
                                        getSelf(),
                                        listener,
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
                final StateType state;

                Prerender(
                        Prez prez,
                        StateType state
                ) {
                    this.prez = prez;
                    this.state = state;
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
                                prerender.prez.viewFacade
                        );
                        setUserInteractionEnabled(
                                prerender.prez.view,
                                prerender.state.enabled
                        );
                        renderer.render(
                                getSelf(),
                                prerender.prez.listener,
                                prerender.prez.context,
                                prerender.prez.viewFacade,
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
