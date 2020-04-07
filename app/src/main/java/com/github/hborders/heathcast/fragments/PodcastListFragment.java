package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

// Next, I should consume it in the MainFragment as well
public abstract class PodcastListFragment<
        FragmentType extends PodcastListFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                PodcastListAsyncValueStateType,
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType
                >,
        ListenerType extends PodcastListFragment.PodcastListFragmentListener<
                FragmentType,
                ListenerType,
                AttachmentType,
                PodcastListAsyncValueStateType,
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType
                >,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType
                >,
        PodcastListAsyncValueStateType extends PodcastListFragment.PodcastListAsyncValueState<
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType
                >,
        PodcastListAsyncValueEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType
                >,
        PodcastListAsyncValueNonEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType
                >
        > extends RxListAsyncValueFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    public interface PodcastListFragmentListener<
            FragmentType extends PodcastListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            ListenerType extends PodcastListFragmentListener<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastListAsyncValueStateType extends PodcastListAsyncValueState<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >
            > {
        void onPodcastListFragmentAttached(FragmentType podcastListFragment);

        Observable<PodcastListAsyncValueStateType> podcastListAsyncValueStateObservable(
                FragmentType podcastListFragment
        );

        void onClickPodcastIdentified(
                FragmentType podcastListFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(FragmentType podcastListFragment);
    }

    public interface PodcastListAsyncValueState<
            PodcastListAsyncValueEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >
            > extends ListAsyncValueState<
            PodcastListAsyncValueEmptyType,
            PodcastListAsyncValueNonEmptyType,
            PodcastIdentifiedList,
            PodcastIdentified
            > {
        interface PodcastListAsyncValueEmpty<
                PodcastListAsyncValueEmptyType extends PodcastListAsyncValueEmpty<
                        PodcastListAsyncValueEmptyType,
                        PodcastListAsyncValueNonEmptyType
                        >,
                PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueNonEmpty<
                        PodcastListAsyncValueEmptyType,
                        PodcastListAsyncValueNonEmptyType
                        >
                > extends ListAsyncValueEmpty<
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType,
                PodcastIdentifiedList,
                PodcastIdentified
                > {
        }

        interface PodcastListAsyncValueNonEmpty<
                PodcastListAsyncValueEmptyType extends PodcastListAsyncValueEmpty<
                        PodcastListAsyncValueEmptyType,
                        PodcastListAsyncValueNonEmptyType
                        >,
                PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueNonEmpty<
                        PodcastListAsyncValueEmptyType,
                        PodcastListAsyncValueNonEmptyType
                        >
                > extends ListAsyncValueNonEmpty<
                PodcastListAsyncValueEmptyType,
                PodcastListAsyncValueNonEmptyType,
                PodcastIdentifiedList,
                PodcastIdentified
                > {
        }
    }

    protected static final class PodcastListAsyncValueViewFacadeTransaction implements
            ListAsyncValueViewFacadeTransaction,
            AsyncValueViewFacadeTransaction,
            ValueViewFacadeTransaction {

    }

    protected static final class PodcastListAsyncValueCompletable implements
            ListAsyncValueCompletable<
                    PodcastListAsyncValueCompletable,
                    PodcastListAsyncValueCompletable
                    >, AsyncValueCompletable<
            PodcastListAsyncValueCompletable
            >, ValueCompletable {
        private final Completable completable;

        private PodcastListAsyncValueCompletable(Completable completable) {
            this.completable = completable;
        }

        @Override
        public PodcastListAsyncValueCompletable toAsyncValueCompletable() {
            return this;
        }

        @Override
        public PodcastListAsyncValueCompletable toValueCompletable() {
            return this;
        }

        @Override
        public Completable toCompletable() {
            return completable;
        }
    }

    protected static abstract class PodcastListAsyncValueViewFacadeCompletableTransaction<
            PodcastListAsyncValueStateType extends PodcastListAsyncValueState<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >
            > implements ListAsyncValueViewFacadeCompletableTransaction<
            PodcastListAsyncValueViewFacadeTransaction,
            PodcastListAsyncValueStateType,
            PodcastListAsyncValueEmptyType,
            PodcastListAsyncValueNonEmptyType,
            PodcastIdentifiedList,
            PodcastIdentified,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable
            >, AsyncValueViewFacadeCompletableTransaction<
            PodcastListAsyncValueViewFacadeTransaction,
            AsyncValueStateType,
            AsyncValueLoadingType,
            AsyncValueCompleteType,
            AsyncValueFailedType,
            PodcastListAsyncValueStateType,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable
            >, ValueViewFacadeCompletableTransaction<
            PodcastListAsyncValueViewFacadeTransaction,
            AsyncValueStateType,
            PodcastListAsyncValueCompletable
            > {

        // ListAsyncValueViewFacadeCompletableTransaction

        @Override
        public PodcastListAsyncValueViewFacadeTransaction toListAsyncValueViewFacadeTransaction(
                PodcastListAsyncValueStateType listAsyncValueState
        ) {
            return null;
        }

        @Override
        public PodcastListAsyncValueCompletable completeListAsyncValue() {
            return null;
        }

        // AsyncValueViewFacadeCompletableTransaction

        @Override
        public PodcastListAsyncValueViewFacadeTransaction toAsyncValueViewFacadeTransaction(AsyncValueStateType asyncValueState) {
            return null;
        }

        @Override
        public PodcastListAsyncValueCompletable completeAsyncValue() {
            return null;
        }

        @Override
        public void addAsyncValueDisposable(Disposable disposable) {

        }

        // ValueViewFacadeCompletableTransaction

        @Override
        public PodcastListAsyncValueViewFacadeTransaction toValueViewFacadeTransaction(AsyncValueStateType valueState) {
            return null;
        }

        @Override
        public PodcastListAsyncValueCompletable completeValue() {
            return null;
        }
    }

    protected static abstract class PodcastListAsyncValueViewFacadeCompletableTransactionFactory<
            FragmentType extends PodcastListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            ListenerType extends PodcastListFragment.PodcastListFragmentListener<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastListAsyncValueStateType extends PodcastListAsyncValueState<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueViewFacadeCompletableTransactionType extends PodcastListAsyncValueViewFacadeCompletableTransaction<
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >
            > implements ListAsyncValueViewFacadeCompletableTransactionFactory<
            PodcastListAsyncValueViewFacadeTransaction,
            PodcastListAsyncValueStateType,
            PodcastListAsyncValueEmptyType,
            PodcastListAsyncValueNonEmptyType,
            PodcastIdentifiedList,
            PodcastIdentified,
            PodcastListAsyncValueViewFacadeCompletableTransactionType,
            PodcastListAsyncValueViewFacadeTransaction,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable
            >, AsyncValueViewFacadeCompletableTransactionFactory<
            PodcastListAsyncValueViewFacadeTransaction,
            PodcastListAsyncValueViewFacadeCompletableTransactionType,
            PodcastListAsyncValueViewFacadeTransaction,
            AsyncValueStateType,
            AsyncValueLoadingType,
            AsyncValueCompleteType,
            AsyncValueFailedType,
            PodcastListAsyncValueStateType,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable
            >, ValueViewFacadeCompletableTransactionFactory<
            FragmentType,
            ListenerType,
            AttachmentType,
            PodcastListAsyncValueViewFacade,
            PodcastListAsyncValueViewFacadeCompletableTransactionType,
            PodcastListAsyncValueViewFacadeTransaction,
            AsyncValueStateType,
            PodcastListAsyncValueCompletable
            > {
        @Override
        public final PodcastListAsyncValueViewFacadeCompletableTransactionType newListAsyncValueViewFacadeCompletableTransaction(
                PodcastListAsyncValueViewFacadeTransaction asyncValueViewFacadeTransaction
        ) {
            return null;
        }

        @Override
        public PodcastListAsyncValueViewFacadeCompletableTransactionType newAsyncValueViewFacadeCompletableTransaction(
                PodcastListAsyncValueViewFacadeTransaction valueViewFacadeTransaction
        ) {
            return null;
        }

        @Override
        public PodcastListAsyncValueViewFacadeCompletableTransactionType newValueViewFacadeCompletableTransaction(
                PodcastListAsyncValueViewFacade valueViewFacade
        ) {
            return null;
        }
    }

    protected static abstract class PodcastListAsyncValueRenderer<
            FragmentType extends PodcastListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            ListenerType extends PodcastListFragment.PodcastListFragmentListener<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastListAsyncValueStateType extends PodcastListFragment.PodcastListAsyncValueState<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >
            > implements ListAsyncValueRenderer<
            FragmentType,
            ListenerType,
            AttachmentType,
            PodcastListAsyncValueStateType,
            PodcastListAsyncValueEmptyType,
            PodcastListAsyncValueNonEmptyType,
            PodcastIdentifiedList,
            PodcastIdentified,
            PodcastListAsyncValueViewFacadeTransaction,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable,
            PodcastListAsyncValueCompletable
            > {
        @Override
        public final PodcastListAsyncValueCompletable render(
                FragmentType fragment,
                ListenerType listener,
                Context context,
                PodcastListAsyncValueStateType listAsyncValueState,
                PodcastListAsyncValueViewFacadeTransaction listAsyncValueViewFacadeTransaction
        ) {
            return null;
        }
    }

    protected static abstract class PodcastListAsyncValueViewFacadeFactory<
            FragmentType extends PodcastListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            ListenerType extends PodcastListFragment.PodcastListFragmentListener<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastListAsyncValueStateType extends PodcastListFragment.PodcastListAsyncValueState<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            PodcastListAsyncValueNonEmptyType extends PodcastListFragment.PodcastListAsyncValueState.PodcastListAsyncValueNonEmpty<
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >
            > implements ValueViewFacade.ValueViewFacadeFactory<
            FragmentType,
            ListenerType,
            AttachmentType,
            PodcastListAsyncValueViewFacade
            > {
        @Override
        public PodcastListAsyncValueViewFacade newViewFacade(
                FragmentType fragment,
                ListenerType listener,
                Context context,
                View view
        ) {
            final RecyclerView recyclerView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_podcasts_recycler_view
                    );
            final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter =
                    new PodcastRecyclerViewAdapter(
                            new PodcastIdentifiedList(),
                            podcastIdentified ->
                                    listener.onClickPodcastIdentified(
                                            fragment,
                                            podcastIdentified
                                    )
                    );
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(podcastRecyclerViewAdapter);
            final View emptyItemsLoadingView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_loading_group
                    );
            final View nonEmptyItemsLoadingView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_non_empty_loading_group
                    );
            final View emptyItemsCompleteView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_complete_text_view
                    );
            final View emptyItemsFailedView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_error_text_view
                    );
            final View nonEmptyItemsFailedView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_non_empty_error_text_view
                    );

            return new PodcastListAsyncValueViewFacade(
                    view,
                    recyclerView,
                    podcastRecyclerViewAdapter,
                    emptyItemsLoadingView,
                    nonEmptyItemsLoadingView,
                    emptyItemsCompleteView,
                    emptyItemsFailedView,
                    nonEmptyItemsFailedView
            );
        }
    }

    protected static final class PodcastListAsyncValueViewFacade implements
            ListAsyncValueViewFacade,
            AsyncValueViewFacade,
            ValueViewFacade {
        private final AtomicBoolean disposed = new AtomicBoolean();
        private final View view;
        private final RecyclerView recyclerView;
        private final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        private final View emptyItemsLoadingView;
        private final View nonEmptyItemsLoadingView;
        private final View emptyItemsCompleteView;
        private final View emptyItemsFailedView;
        private final View nonEmptyItemsFailedView;

        private PodcastListAsyncValueViewFacade(
                View view,
                RecyclerView recyclerView,
                PodcastRecyclerViewAdapter podcastRecyclerViewAdapter,
                View emptyItemsLoadingView,
                View nonEmptyItemsLoadingView,
                View emptyItemsCompleteView,
                View emptyItemsFailedView,
                View nonEmptyItemsFailedView
        ) {
            this.view = view;
            this.recyclerView = recyclerView;
            this.podcastRecyclerViewAdapter = podcastRecyclerViewAdapter;
            this.emptyItemsLoadingView = emptyItemsLoadingView;
            this.nonEmptyItemsLoadingView = nonEmptyItemsLoadingView;
            this.emptyItemsCompleteView = emptyItemsCompleteView;
            this.emptyItemsFailedView = emptyItemsFailedView;
            this.nonEmptyItemsFailedView = nonEmptyItemsFailedView;
        }

        // Disposable

        @Override
        public void dispose() {
            // theoretically, we could clean up after the recyclerView here
            // if it was shared or something
            disposed.set(true);
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    private static final String TAG = "PodcastList";

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
            PodcastListAsyncValueViewFacadeFactoryType extends PodcastListAsyncValueViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueStateType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListAsyncValueStateType
                    >,
            PodcastListAsyncValueViewFacadeCompletableTransactionFactoryType extends PodcastListAsyncValueViewFacadeCompletableTransactionFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType,
                    PodcastListAsyncValueViewFacadeCompletableTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType
                    >,
            PodcastListAsyncValueViewFacadeCompletableTransactionType extends PodcastListAsyncValueViewFacadeCompletableTransaction<
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType
                    >,
            PodcastListAsyncValueRendererType extends PodcastListAsyncValueRenderer<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueStateType,
                    PodcastListAsyncValueEmptyType,
                    PodcastListAsyncValueNonEmptyType
                    >
            > PodcastListFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            PodcastListAsyncValueViewFacadeFactoryType podcastListAsyncValueViewFacadeFactoryType,
            ValueStateObservableProviderType valueStateObservableProvider,
            PodcastListAsyncValueViewFacadeCompletableTransactionFactoryType podcastListAsyncValueViewFacadeCompletableTransactionFactory,
            PodcastListAsyncValueRendererType podcastListAsyncValueRenderer
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                R.layout.fragment_podcast_list,
                TAG,
                podcastListAsyncValueViewFacadeFactoryType,
                valueStateObservableProvider,
                podcastListAsyncValueViewFacadeCompletableTransactionFactory,
                podcastListAsyncValueViewFacadeCompletableTransactionFactory,
                podcastListAsyncValueViewFacadeCompletableTransactionFactory,
                podcastListAsyncValueRenderer
        );
    }
}
