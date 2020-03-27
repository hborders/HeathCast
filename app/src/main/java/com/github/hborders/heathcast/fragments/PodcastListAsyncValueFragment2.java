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
public final class PodcastListAsyncValueFragment2 extends RxListAsyncValueFragment<
        PodcastListAsyncValueFragment2,
        PodcastListAsyncValueFragment2.PodcastListValueFragmentListener,
        PodcastListAsyncValueFragment2.Attachment
        > {
    public static final class Attachment extends RxFragment.Attachment<
            PodcastListAsyncValueFragment2,
            PodcastListValueFragmentListener,
            Attachment> {
        public interface Factory extends RxListAsyncValueFragment.Attachment.Factory<
                PodcastListAsyncValueFragment2,
                PodcastListValueFragmentListener,
                Attachment
                > {
        }

        private Attachment(
                PodcastListAsyncValueFragment2 fragment,
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
        void onPodcastListFragmentAttached(PodcastListAsyncValueFragment2 podcastListValueFragment);

        Observable<PodcastIdentifiedListState> podcastIdentifiedListStateObservable(
                PodcastListAsyncValueFragment2 podcastListValueFragment
        );

        void onClickPodcastIdentified(
                PodcastListAsyncValueFragment2 podcastListValueFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListAsyncValueFragment2 podcastListValueFragment);
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

    public interface PodcastIdentifiedListState extends State<
            PodcastIdentifiedListState.PodcastIdentifiedListLoading,
            PodcastIdentifiedListState.PodcastIdentifiedListComplete,
            PodcastIdentifiedListState.PodcastIdentifiedListFailed,
            PodcastIdentifiedListModel,
            PodcastIdentifiedList
            > {
        interface PodcastIdentifiedListLoading extends State.Loading<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListState {
        }

        interface PodcastIdentifiedListComplete extends State.Complete<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListState {
        }

        interface PodcastIdentifiedListFailed extends State.Failed<
                PodcastIdentifiedListLoading,
                PodcastIdentifiedListComplete,
                PodcastIdentifiedListFailed,
                PodcastIdentifiedListModel,
                PodcastIdentifiedList
                >, PodcastIdentifiedListState {
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
            PodcastListAsyncValueFragment2,
            PodcastListValueFragmentListener,
            Attachment,
            ViewHolder
            > {
        @Override
        public ViewHolder newViewHolder(
                PodcastListAsyncValueFragment2 fragment,
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
            PodcastListAsyncValueFragment2,
            PodcastListAsyncValueFragment2.PodcastListValueFragmentListener,
            PodcastListAsyncValueFragment2.Attachment,
            PodcastIdentifiedListState,
            PodcastIdentifiedListState.PodcastIdentifiedListLoading,
            PodcastIdentifiedListState.PodcastIdentifiedListComplete,
            PodcastIdentifiedListState.PodcastIdentifiedListFailed,
            PodcastIdentifiedListModel,
            PodcastIdentifiedList
            > {
    }

    private static final String TAG = "PodcastList";

    public PodcastListAsyncValueFragment2() {
        this(PodcastListValueFragmentListener::podcastIdentifiedListStateObservable);
    }

    private PodcastListAsyncValueFragment2(
            // Without this explicit type, we have to cast the method reference,
            // and I find that distasteful.
            PodcastIdentifiedListStateObservableProvider stateObservableProvider
    ) {
        super(
                PodcastListValueFragmentListener.class,
                Attachment::new,
                PodcastListValueFragmentListener::onPodcastListFragmentAttached,
                PodcastListValueFragmentListener::onPodcastListFragmentWillDetach,
                R.layout.fragment_podcast_list_2,
                TAG,
                new ViewHolderFactory(),
                stateObservableProvider
        );
    }
}
