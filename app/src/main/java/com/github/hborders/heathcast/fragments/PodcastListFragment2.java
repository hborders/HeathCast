package com.github.hborders.heathcast.fragments;

import android.content.Context;
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

import io.reactivex.Completable;
import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public final class PodcastListFragment2 extends RxListAsyncValueFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListValueFragmentListener,
        PodcastListFragment2.PodcastListAttachment
        > {
    public static final class PodcastListAttachment extends RxFragment.Attachment<
            PodcastListFragment2,
            PodcastListValueFragmentListener,
            PodcastListAttachment> {
        private PodcastListAttachment(
                PodcastListFragment2 fragment,
                Context context,
                PodcastListValueFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

    public interface PodcastListValueFragmentListener {
        void onPodcastListFragmentAttached(PodcastListFragment2 podcastListValueFragment);

        Observable<PodcastIdentifiedListState> podcastIdentifiedListStateObservable(
                PodcastListFragment2 podcastListValueFragment
        );

        void onClickPodcastIdentified(
                PodcastListFragment2 podcastListValueFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListFragment2 podcastListValueFragment);
    }

    public static final class PodcastIdentifiedListState extends State<
            PodcastIdentifiedListAsyncState
            > {
        public PodcastIdentifiedListState(
                PodcastIdentifiedListAsyncState value,
                boolean enabled
        ) {
            super(
                    value,
                    enabled
            );
        }
    }

    public interface PodcastIdentifiedListAsyncState extends AsyncState<
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListLoading,
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListComplete,
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListFailed,
            PodcastIdentifiedList
            > {
        interface PodcastIdentifiedListLoading extends AsyncState.Loading<
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListLoading,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListComplete,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListFailed,
                PodcastIdentifiedList
                > {
        }

        interface PodcastIdentifiedListComplete extends AsyncState.Complete<
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListLoading,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListComplete,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListFailed,
                PodcastIdentifiedList
                > {
        }

        interface PodcastIdentifiedListFailed extends AsyncState.Failed<
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListLoading,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListComplete,
                PodcastIdentifiedListAsyncState.PodcastIdentifiedListFailed,
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

    private static final class ViewFacadeFactory implements RxValueFragment.ViewFacadeFactory<
            PodcastListFragment2,
            PodcastListValueFragmentListener,
            PodcastListAttachment,
            ViewFacade
            > {
        @Override
        public ViewFacade newViewFacade(
                PodcastListFragment2 fragment,
                PodcastListValueFragmentListener listener,
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

    private interface PodcastIdentifiedListStateObservableProvider
            extends StateObservableProvider<
            PodcastListFragment2,
            PodcastListFragment2.PodcastListValueFragmentListener,
            PodcastListAttachment,
            PodcastIdentifiedListState,
            PodcastIdentifiedListAsyncState
            > {
    }

    private static final String TAG = "PodcastList";

    public PodcastListFragment2() {
        this(PodcastListValueFragmentListener::podcastIdentifiedListStateObservable);
    }

    private PodcastListFragment2(
            // Without this explicit type, we have to cast the method reference,
            // and I find that distasteful.
            PodcastIdentifiedListStateObservableProvider stateObservableProvider
    ) {
        super(
                PodcastListValueFragmentListener.class,
                PodcastListAttachment::new,
                PodcastListValueFragmentListener::onPodcastListFragmentAttached,
                PodcastListValueFragmentListener::onPodcastListFragmentWillDetach,
                R.layout.fragment_podcast_list_2,
                TAG,
                new ViewFacadeFactory(),
                stateObservableProvider
        );
    }
}
