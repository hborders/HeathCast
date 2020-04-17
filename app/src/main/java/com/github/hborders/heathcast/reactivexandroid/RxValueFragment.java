package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Function;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class RxValueFragment<
        FragmentType extends RxValueFragment<
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
    // even though ValueState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ValueState {
    }

    protected interface ValueStateObservableProvider<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
            FragmentType extends RxValueFragment<
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
            ValueStateType extends ValueState
            > {
        Observable<ValueStateType> valueStateObservable(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface ValueViewFacade extends Disposable {
        interface ValueViewFacadeFactory<
                // even though we don't use these bounds in our declarations,
                // javac gives better error messages with boundary enforcement
                FragmentType extends RxValueFragment<
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
                ValueViewFacadeType
                > {
            ValueViewFacadeType newViewFacade(
                    FragmentType fragment,
                    ListenerType listener,
                    Context context,
                    View view
            );
        }
    }

    protected interface ValueViewFacadeTransaction {
        Completable complete();
    }

    protected interface ValueRenderer<
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateType extends ValueState,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            > {
        ValueViewFacadeTransactionType render(
                ValueViewFacadeType valueViewFacade,
                ValueStateType valueState
        );
    }

    private static final class TombestoneDisposable implements Disposable {
        // don't use Disposables.disposed() because maybe a Completable would use
        // Disposables.disposed(), and then our tombstone wouldn't work.
        static final TombestoneDisposable INSTANCE = new TombestoneDisposable();

        private TombestoneDisposable() {
        }

        @Override
        public void dispose() {
        }

        @Override
        public boolean isDisposed() {
            return true;
        }
    }

    private final Function<Observable<AttachmentType>, Disposable> subscribeFunction;

    protected <
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
                    ValueStateType
                    >,
            ValueStateType extends ValueState,
            ValueRendererType extends ValueRenderer<
                    ValueViewFacadeType,
                    ValueStateType,
                    ValueViewFacadeTransactionType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            >
    RxValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            Attachment.AttachmentFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > attachmentFactory,
            OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > onAttached,
            WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > willDetach,
            @LayoutRes int layoutResource,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueRendererType valueRenderer
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
            final class Prez implements Disposable {
                final Context context;
                final ListenerType listener;
                final ViewCreation viewCreation;
                final View view;
                final ValueViewFacadeType valueViewFacade;

                private final AtomicBoolean disposed = new AtomicBoolean();
                private final CompositeDisposable compositeDisposable = new CompositeDisposable();

                Prez(
                        Context context,
                        ListenerType listener,
                        ViewCreation viewCreation,
                        View view,
                        ValueViewFacadeType valueViewFacade
                ) {
                    this.context = context;
                    this.listener = listener;
                    this.viewCreation = viewCreation;
                    this.view = view;
                    this.valueViewFacade = valueViewFacade;
                }

                // Disposable

                @Override
                public void dispose() {
                    if (disposed.compareAndSet(false, true)) {
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            disposeOnce();
                        } else {
                            new Handler(Looper.getMainLooper()).post(
                                    // we must dispose on the main thread
                                    // because disposables isn't thread-safe,
                                    // and it assumes access only on the main thread
                                    this::disposeOnce
                            );
                        }
                    }
                }

                @Override
                public boolean isDisposed() {
                    return disposed.get();
                }

                // Public API

                void addCompletable(Completable completable) {
                    // disposablePointer starts out null
                    // if completable is already disposed, subscribe will immediately be called
                    // and we won't be able to set disposablePointer. We will set disposablePointer
                    // to tombstoneDisposable to signal this case to addCompletable.
                    // Thus, if we finish the completable.subscribe call, and
                    // disposablePointer is tombstoneDisposable, then we already disposed,
                    // and we don't need to track disposable, possibly introducing a memory leak.
                    final Disposable[] disposablePointer = new Disposable[1];
                    final Disposable tombstoneDisposable = TombestoneDisposable.INSTANCE;
                    final Disposable disposable = completable.subscribe(
                            () -> {
                                if (Looper.getMainLooper() != Looper.myLooper()) {
                                    throw new IllegalStateException("ValueViewFacadeTransaction Completable completed on a non-main thread");
                                }
                                @Nullable final Disposable disposable_ = disposablePointer[0];
                                if (disposable_ == null) {
                                    // we're being called from within the addCompletable stack
                                    // so we haven't assigned disposablePointer yet
                                    disposablePointer[0] = tombstoneDisposable;
                                } else {
                                    compositeDisposable.remove(disposable_);
                                }
                            }
                    );
                    if (disposablePointer[0] == tombstoneDisposable) {
                        // the completable was already disposed
                    } else {
                        disposablePointer[0] = disposable;
                        compositeDisposable.add(disposable);
                    }
                }

                // Private API

                void disposeOnce() {
                    compositeDisposable.dispose();
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
                                final ValueViewFacadeType viewFacade =
                                        valueViewFacadeFactory.newViewFacade(
                                                getSelf(),
                                                listener,
                                                context,
                                                view
                                        );
                                viewCreation.addDisposable(viewFacade);
                                final Prez prez = new Prez(
                                        context,
                                        listener,
                                        viewCreation,
                                        view,
                                        viewFacade
                                );
                                viewCreation.addDisposable(prez);
                                return prez;
                            }
                    );

            final class Prerender {
                final Prez prez;
                final ValueStateType valueState;

                Prerender(
                        Prez prez,
                        ValueStateType valueState
                ) {
                    this.prez = prez;
                    this.valueState = valueState;
                }
            }

            final Observable<Prerender> prerenderObservable =
                    prezObservable.switchMap(
                            prez ->
                                    prez.viewCreation.switchMapToStart().switchMap(
                                            start ->
                                                    valueStateObservableProvider
                                                            .valueStateObservable(
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
                        final ValueStateType valueState = prerender.valueState;
                        final ValueViewFacadeTransactionType valueViewFacadeTransaction =
                                valueRenderer.render(
                                        prerender.prez.valueViewFacade,
                                        valueState
                                );
                        final Completable ignored = valueViewFacadeTransaction.complete();
                    }
            );
        };
    }

    @Override
    protected final Disposable subscribe(Observable<AttachmentType> attachmentObservable) {
        return subscribeFunction.apply(attachmentObservable);
    }
}
