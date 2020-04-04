package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.ViewUtil;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.reactivexandroid.RxValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.CompletableSubject;

// Next, I should consume it in the MainFragment as well
public abstract class PodcastListFragment<
        PodcastListFragmentType extends PodcastListFragment<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                PodcastListAttachmentType,
                PodcastIdentifiedListStateType,
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastListFragmentListenerType extends PodcastListFragment.PodcastListFragmentListener<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                PodcastListAttachmentType,
                PodcastIdentifiedListStateType,
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastListAttachmentType extends RxFragment.Attachment<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                PodcastListAttachmentType
                >,
        PodcastIdentifiedListStateType extends PodcastListFragment.PodcastIdentifiedListState<
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastIdentifiedListAsyncStateType extends PodcastListFragment.PodcastIdentifiedListAsyncState<
                LoadingType,
                CompleteType,
                FailedType
                >,
        LoadingType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                LoadingType,
                CompleteType,
                FailedType
                >,
        CompleteType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                LoadingType,
                CompleteType,
                FailedType
                >,
        FailedType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                LoadingType,
                CompleteType,
                FailedType
                >
        > extends RxListAsyncValueFragment<
        PodcastListFragmentType,
        PodcastListFragmentListenerType,
        PodcastListAttachmentType
        > {
    public interface PodcastListFragmentListener<
            PodcastListFragmentType extends PodcastListFragment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListFragmentListenerType extends PodcastListFragmentListener<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListAttachmentType extends RxFragment.Attachment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType
                    >,
            PodcastIdentifiedListStateType extends PodcastIdentifiedListState<
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastIdentifiedListAsyncStateType extends PodcastIdentifiedListAsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            LoadingType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            CompleteType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            FailedType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >
            > {
        void onPodcastListFragmentAttached(PodcastListFragmentType podcastListFragment);

        Observable<PodcastIdentifiedListStateType> podcastIdentifiedListStateObservable(
                PodcastListFragmentType podcastListFragment
        );

        void onClickPodcastIdentified(
                PodcastListFragmentType podcastListFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListFragmentType podcastListFragment);
    }

    public interface PodcastIdentifiedListState<
            PodcastIdentifiedListAsyncStateType extends PodcastIdentifiedListAsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            LoadingType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            CompleteType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            FailedType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >
            > extends State<
            PodcastIdentifiedListAsyncStateType
            > {
    }

    public interface PodcastIdentifiedListAsyncState<
            LoadingType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            CompleteType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            FailedType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >
            > extends AsyncState<
            LoadingType,
            CompleteType,
            FailedType,
            PodcastIdentifiedList
            > {
        interface PodcastIdentifiedListAsyncStateLoading<
                LoadingType extends PodcastIdentifiedListAsyncStateLoading<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                CompleteType extends PodcastIdentifiedListAsyncStateComplete<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                FailedType extends PodcastIdentifiedListAsyncStateFailed<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >
                > extends AsyncState.Loading<
                LoadingType,
                CompleteType,
                FailedType,
                PodcastIdentifiedList
                > {
        }

        interface PodcastIdentifiedListAsyncStateComplete<
                LoadingType extends PodcastIdentifiedListAsyncStateLoading<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                CompleteType extends PodcastIdentifiedListAsyncStateComplete<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                FailedType extends PodcastIdentifiedListAsyncStateFailed<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >
                > extends AsyncState.Complete<
                LoadingType,
                CompleteType,
                FailedType,
                PodcastIdentifiedList
                > {
        }

        interface PodcastIdentifiedListAsyncStateFailed<
                LoadingType extends PodcastIdentifiedListAsyncStateLoading<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                CompleteType extends PodcastIdentifiedListAsyncStateComplete<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
                FailedType extends PodcastIdentifiedListAsyncStateFailed<
                        LoadingType,
                        CompleteType,
                        FailedType
                        >
                > extends AsyncState.Failed<
                LoadingType,
                CompleteType,
                FailedType,
                PodcastIdentifiedList
                > {
        }
    }

    private static final class PodcastListViewFacade implements ListAsyncValueViewFacade<
            PodcastListViewFacade,
            PodcastListViewFacadeTransaction,
            PodcastIdentifiedList,
            PodcastIdentified
            > {
        public static PodcastListViewFacadeTransaction newPodcastListViewFacadeTransaction(
                PodcastListViewFacade podcastListViewFacade
        ) {
            return new PodcastListViewFacadeTransaction(
                    podcastListViewFacade,
                    podcastListViewFacade.view
            );
        }

        private final AtomicBoolean disposed = new AtomicBoolean();
        private final View view;
        private final RecyclerView recyclerView;
        private final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        private final View emptyItemsLoadingView;
        private final View nonEmptyItemsLoadingView;
        private final View emptyItemsCompleteView;
        private final View emptyItemsFailedView;
        private final View nonEmptyItemsFailedView;

        private PodcastListViewFacade(
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

        // ViewFacade

        @Override
        public void setEnabled(boolean enabled) {
            ViewUtil.setUserInteractionEnabled(
                    view,
                    enabled
            );
        }

        // AsyncValueViewFacade

        @Override
        public void setValue(PodcastIdentifiedList value) {
            podcastRecyclerViewAdapter.setItems(value);
        }

        @Override
        public void setLoadingViewVisible(boolean visible) {

        }

        @Override
        public void setEmptyItemsLoadingViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    emptyItemsLoadingView,
                    visible
            );
        }

        @Override
        public void setNonEmptyItemsLoadingViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    nonEmptyItemsLoadingView,
                    visible
            );
        }

        @Override
        public void setEmptyItemsCompleteViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    emptyItemsCompleteView,
                    visible
            );
        }

        @Override
        public void setListViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    recyclerView,
                    visible
            );
        }

        @Override
        public void setEmptyItemsFailedViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    emptyItemsFailedView,
                    visible
            );
        }

        @Override
        public void setNonEmptyItemsFailedViewVisible(boolean visible) {
            ViewUtil.setVisible(
                    nonEmptyItemsFailedView,
                    visible
            );
        }
    }

    private static final class PodcastListViewFacadeTransaction extends ViewFacadeTransaction<
            PodcastListViewFacade,
            PodcastListViewFacadeTransaction
            > {
        private final View view;

        PodcastListViewFacadeTransaction(
                PodcastListViewFacade podcastListViewFacade,
                View view
        ) {
            super(
                    PodcastListViewFacadeTransaction.class,
                    podcastListViewFacade
            );
            this.view = view;
        }

        @Override
        public Completable complete() {
            final CompletableSubject completableSubject = transact();
            ViewUtil.doOnLayout(
                    view,
                    completableSubject::onComplete
            );
            return completableSubject;
        }
    }

    private static final class ViewFacadeFactory<
            PodcastListFragmentType extends PodcastListFragment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListFragmentListenerType extends PodcastListFragmentListener<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListAttachmentType extends RxFragment.Attachment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType
                    >,
            PodcastIdentifiedListStateType extends PodcastIdentifiedListState<
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastIdentifiedListAsyncStateType extends PodcastIdentifiedListAsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            LoadingType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            CompleteType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            FailedType extends PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                    LoadingType,
                    CompleteType,
                    FailedType
                    >
            > implements RxValueFragment.ViewFacadeFactory<
            PodcastListFragmentType,
            PodcastListFragmentListenerType,
            PodcastListAttachmentType,
            PodcastListViewFacade
            > {
        @Override
        public PodcastListViewFacade newViewFacade(
                PodcastListFragmentType fragment,
                PodcastListFragmentListenerType listener,
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

            return new PodcastListViewFacade(
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

    private static final String TAG = "PodcastList";

    protected <
            AttachmentFactoryType extends Attachment.AttachmentFactory<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType
                    >,
            OnAttachedType extends OnAttached<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType
                    >,
            WillDetachType extends WillDetach<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    PodcastListAttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType
                    >
            > PodcastListFragment(
            Class<PodcastListFragmentType> selfClass,
            Class<PodcastListFragmentListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            StateObservableProviderType stateObservableProvider
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                R.layout.fragment_podcast_list,
                TAG,
                new ViewFacadeFactory<>(),
                stateObservableProvider,
                PodcastListViewFacade::newPodcastListViewFacadeTransaction
        );
    }
}
