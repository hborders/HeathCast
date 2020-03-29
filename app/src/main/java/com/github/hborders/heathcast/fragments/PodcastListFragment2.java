package com.github.hborders.heathcast.fragments;

import android.view.View;

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

import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public abstract class PodcastListFragment2<
        PodcastListFragmentType extends PodcastListFragment2<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                AttachmentType,
                PodcastIdentifiedListStateType,
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastListFragmentListenerType extends PodcastListFragment2.PodcastListFragmentListener<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                AttachmentType,
                PodcastIdentifiedListStateType,
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        AttachmentType extends RxFragment.Attachment<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                AttachmentType
                >,
        PodcastIdentifiedListStateType extends PodcastListFragment2.PodcastIdentifiedListState<
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastIdentifiedListAsyncStateType extends PodcastListFragment2.PodcastIdentifiedListAsyncState<
                LoadingType,
                CompleteType,
                FailedType
                >,
        LoadingType extends PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                LoadingType,
                CompleteType,
                FailedType
                >,
        CompleteType extends PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                LoadingType,
                CompleteType,
                FailedType
                >,
        FailedType extends PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                LoadingType,
                CompleteType,
                FailedType
                >
        > extends RxListAsyncValueFragment<
        PodcastListFragmentType,
        PodcastListFragmentListenerType,
        AttachmentType
        > {
    public interface PodcastListFragmentListener<
            PodcastListFragmentType extends PodcastListFragment2<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListFragmentListenerType extends PodcastListFragmentListener<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType
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

    private static final class ViewFacade implements ListViewFacade<
            PodcastIdentifiedList,
            PodcastIdentified
            > {
        private final AtomicBoolean disposed = new AtomicBoolean();
        private final View view;
        private final RecyclerView recyclerView;
        private final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        private final View emptyItemsLoadingView;
        private final View nonEmptyItemsLoadingView;
        private final View emptyItemsCompleteView;
        private final View emptyItemsFailedView;
        private final View nonEmptyItemsFailedView;

        private ViewFacade(
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

        @Override
        public void setEnabled(boolean enabled) {
            ViewUtil.setUserInteractionEnabled(
                    view,
                    enabled
            );
        }

        @Override
        public void setListValue(PodcastIdentifiedList listValue) {
            podcastRecyclerViewAdapter.setItems(listValue);
        }

        @Override
        public void setEmptyItemsLoadingViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    emptyItemsLoadingView,
                    visible
            );
        }

        @Override
        public void setNonEmptyItemsLoadingViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    nonEmptyItemsLoadingView,
                    visible
            );
        }

        @Override
        public void setEmptyItemsCompleteViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    emptyItemsCompleteView,
                    visible
            );
        }

        @Override
        public void setListViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    recyclerView,
                    visible
            );
        }

        @Override
        public void setEmptyItemsFailedViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    emptyItemsFailedView,
                    visible
            );
        }

        @Override
        public void setNonEmptyItemsFailedViewVisible(boolean visible) {
            ViewUtil.setVisibility(
                    nonEmptyItemsFailedView,
                    visible
            );
        }
    }

    private static final class ViewFacadeFactory<
            PodcastListFragmentType extends PodcastListFragment2<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            PodcastListFragmentListenerType extends PodcastListFragmentListener<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType,
                    LoadingType,
                    CompleteType,
                    FailedType
                    >,
            AttachmentType extends RxFragment.Attachment<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType
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
            AttachmentType,
            ViewFacade
            > {
        @Override
        public ViewFacade newViewFacade(
                PodcastListFragmentType fragment,
                PodcastListFragmentListenerType listener,
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

            return new ViewFacade(
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
                    AttachmentType
                    >,
            OnAttachedType extends OnAttached<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType
                    >,
            WillDetachType extends WillDetach<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    PodcastListFragmentType,
                    PodcastListFragmentListenerType,
                    AttachmentType,
                    PodcastIdentifiedListStateType,
                    PodcastIdentifiedListAsyncStateType
                    >
            > PodcastListFragment2(
            Class<PodcastListFragmentListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            StateObservableProviderType stateObservableProvider
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                R.layout.fragment_podcast_list_2,
                TAG,
                new ViewFacadeFactory<>(),
                stateObservableProvider
        );
    }
}
