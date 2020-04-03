package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Function;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

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
    // even though State could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface State<UnparcelableValueType> {
        boolean isEnabled();

        UnparcelableValueType getValue();
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

    protected interface ViewFacade<
            ViewFacadeType extends ViewFacade<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >
            > extends Disposable {
        interface ViewFacadeTransaction<
                ViewFacadeType extends ViewFacade<
                        ViewFacadeType,
                        ViewFacadeTransactionType,
                        ViewFacadeEmptyActionType
                        >,
                ViewFacadeTransactionType extends ViewFacadeTransaction<
                        ViewFacadeType,
                        ViewFacadeTransactionType,
                        ViewFacadeEmptyActionType
                        >,
                ViewFacadeEmptyActionType extends ViewFacadeTransaction.ViewFacadeEmptyAction<
                        ViewFacadeType,
                        ViewFacadeTransactionType,
                        ViewFacadeEmptyActionType
                        >
                > {

            // Reifying ViewFacadeEmptyActionType might not work
            // not sure how it will compile when I actually use it in
            // RxListAsyncValueFragment
            interface ViewFacadeEmptyAction<
                    ViewFacadeType extends ViewFacade<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >,
                    ViewFacadeTransactionType extends ViewFacadeTransaction<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >,
                    ViewFacadeEmptyActionType extends ViewFacadeTransaction.ViewFacadeEmptyAction<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >
                    > {
                void act(ViewFacadeType viewFacade);
            }

            interface ViewFacadeAction<
                    ViewFacadeType extends ViewFacade<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >,
                    ViewFacadeTransactionType extends ViewFacadeTransaction<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >,
                    ViewFacadeEmptyActionType extends ViewFacadeTransaction.ViewFacadeEmptyAction<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType
                            >,
                    ArgType
                    > {
                void act(
                        ViewFacadeType viewFacade,
                        ArgType arg
                );
            }

            // Need to figure out how to make this work
            // Might be able to unreify ViewFacadeEmptyActionType
            // but get it working reified first
            // we can always make a new interface if necessary
//            ViewFacadeTransactionType act(ViewFacadeEmptyActionType emptyAction);

            <
                    ViewFacadeActionType extends ViewFacadeAction<
                            ViewFacadeType,
                            ViewFacadeTransactionType,
                            ViewFacadeEmptyActionType,
                            ArgType
                            >,
                    ArgType
                    > ViewFacadeTransactionType act(
                    ViewFacadeActionType action,
                    ArgType arg
            );

            Completable complete();
        }

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
                Context context,
                View view
        );
    }

    protected interface ViewFacadeTransactionFactory<
            ViewFacadeType,
            ViewFacadeTransactionType
            > {
        ViewFacadeTransactionType newViewFacadeTransaction(ViewFacadeType viewFacade);
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
            ViewFacadeType extends ViewFacade<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >
            > {
        @CheckResult
        Completable render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                StateType state,
                ViewFacadeTransactionType viewFacadeTransaction
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
            ViewFacadeTransactionFactoryType extends ViewFacadeTransactionFactory<
                    ViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            RendererType extends Renderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    UnparcelableValueType,
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            StateType extends State<UnparcelableValueType>,
            UnparcelableValueType,
            ViewFacadeType extends ViewFacade<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >
            >
    RxValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider,
            ViewFacadeTransactionFactoryType viewFacadeTransactionFactory,
            RendererType renderer
    ) {
        super(
                selfClass,
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
                                        context,
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
                        ViewFacadeTransactionType viewFacadeTransaction =
                                viewFacadeTransactionFactory.newViewFacadeTransaction(
                                        prerender.prez.viewFacade
                                );
                        prerender.prez.viewFacade.setEnabled(
                                prerender.state.isEnabled()
                        );

                        final Completable ignored =
                                renderer.render(
                                        getSelf(),
                                        prerender.prez.listener,
                                        prerender.prez.context,
                                        prerender.state,
                                        viewFacadeTransaction
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
