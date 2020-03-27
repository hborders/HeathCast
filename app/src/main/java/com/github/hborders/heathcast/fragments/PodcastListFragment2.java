package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.reactivexandroid.RxAsyncValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

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

        Observable<PodcastIdentifiedListAsyncState> podcastIdentifiedListStateObservable(
                PodcastListFragment2 podcastListValueFragment
        );

        void onClickPodcastIdentified(
                PodcastListFragment2 podcastListValueFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListFragment2 podcastListValueFragment);
    }

    public static final class PodcastIdentifiedListModel
            extends RxAsyncValueFragment.Model<PodcastIdentifiedList> {
        public PodcastIdentifiedListModel(
                PodcastIdentifiedList value,
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
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                > {
        interface PodcastIdentifiedListLoading extends AsyncState.Loading<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListAsyncState {
        }

        interface PodcastIdentifiedListComplete extends AsyncState.Complete<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListAsyncState {
        }

        interface PodcastIdentifiedListFailed extends AsyncState.Failed<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListAsyncState {
        }
    }

    private static final class ViewHolder implements RxListViewHolder<
            PodcastRecyclerViewAdapter,
            PodcastIdentified,
            PodcastIdentifiedList,
            PodcastRecyclerViewAdapter.PodcastViewHolder
            > {
        private final RecyclerView recyclerView;
        private final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        private final View emptyItemsLoadingView;
        private final View nonEmptyItemsLoadingView;
        private final View emptyItemsCompleteView;
        private final View emptyItemsFailedView;
        private final View nonEmptyItemsFailedView;

        private ViewHolder(
                RecyclerView recyclerView,
                PodcastRecyclerViewAdapter podcastRecyclerViewAdapter,
                View emptyItemsLoadingView,
                View nonEmptyItemsLoadingView,
                View emptyItemsCompleteView,
                View emptyItemsFailedView,
                View nonEmptyItemsFailedView
        ) {
            this.recyclerView = recyclerView;
            this.podcastRecyclerViewAdapter = podcastRecyclerViewAdapter;
            this.emptyItemsLoadingView = emptyItemsLoadingView;
            this.nonEmptyItemsLoadingView = nonEmptyItemsLoadingView;
            this.emptyItemsCompleteView = emptyItemsCompleteView;
            this.emptyItemsFailedView = emptyItemsFailedView;
            this.nonEmptyItemsFailedView = nonEmptyItemsFailedView;
        }

        @Override
        public RecyclerView requireRecyclerView() {
            return recyclerView;
        }

        @Override
        public PodcastRecyclerViewAdapter requireListRecyclerViewAdapter() {
            return podcastRecyclerViewAdapter;
        }

        @Override
        public View findEmptyItemsLoadingView() {
            return emptyItemsLoadingView;
        }

        @Override
        public View findNonEmptyItemsLoadingView() {
            return nonEmptyItemsLoadingView;
        }

        @Override
        public View findEmptyItemsCompleteView() {
            return emptyItemsCompleteView;
        }

        @Override
        public View findEmptyItemsFailedView() {
            return emptyItemsFailedView;
        }

        @Override
        public View findNonEmptyItemsFailedView() {
            return nonEmptyItemsFailedView;
        }
    }

    private static final class ViewHolderFactory implements RxAsyncValueFragment.ViewHolderFactory<
            PodcastListFragment2,
            PodcastListValueFragmentListener,
            PodcastListAttachment,
            ViewHolder
            > {
        @Override
        public ViewHolder newViewHolder(
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

            return new ViewHolder(
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
            PodcastIdentifiedListAsyncState,
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListLoading,
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListComplete,
            PodcastIdentifiedListAsyncState.PodcastIdentifiedListFailed,
            PodcastIdentifiedListModel,
            PodcastIdentifiedList
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
                new ViewHolderFactory(),
                stateObservableProvider
        );
    }
}
