package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Nothing;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public final class PodcastListFragment extends RxListAsyncValueFragment<
        PodcastListFragment,
        PodcastListFragment.PodcastListFragmentListener,
        PodcastListFragment.PodcastListFragmentAttachment
        > {
    public interface PodcastListFragmentListener {
        void onPodcastListFragmentAttached(PodcastListFragment podcastListFragment);

        Observable<PodcastIdentifiedListValueStateAsyncValueState> podcastIdentifiedListValueStateAsyncValueStateObservable(
                PodcastListFragment podcastListFragment
        );

        void onClickPodcastIdentified(
                PodcastListFragment podcastListFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListFragment podcastListFragment);
    }

    public static final class PodcastListFragmentAttachment extends RxFragment.Attachment<
            PodcastListFragment,
            PodcastListFragmentListener,
            PodcastListFragmentAttachment
            > {
        PodcastListFragmentAttachment(
                PodcastListFragment fragment,
                Context context,
                PodcastListFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    PodcastListFragmentAttachment.class,
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

    public interface PodcastIdentifiedListValueStateAsyncValueState extends AsyncValueState<
            PodcastIdentifiedListValueStateAsyncValueState.PodcastIdentifiedListValueStateAsyncValueLoading,
            PodcastIdentifiedListValueStateAsyncValueState.PodcastIdentifiedListValueStateAsyncValueComplete,
            PodcastIdentifiedListValueStateAsyncValueState.PodcastIdentifiedListValueStateAsyncValueFailed,
            PodcastIdentifiedListValueState
            > {
        final class PodcastIdentifiedListValueStateAsyncValueLoading extends Either31.LeftImpl<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                > implements AsyncValueState.AsyncValueLoading<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                >, PodcastIdentifiedListValueStateAsyncValueState {
            public PodcastIdentifiedListValueStateAsyncValueLoading(PodcastIdentifiedListValueState value) {
                super(
                        PodcastIdentifiedListValueStateAsyncValueLoading.class,
                        value
                );
            }
        }

        final class PodcastIdentifiedListValueStateAsyncValueComplete extends Either31.MiddleImpl<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                > implements AsyncValueState.AsyncValueComplete<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                >, PodcastIdentifiedListValueStateAsyncValueState {

            public PodcastIdentifiedListValueStateAsyncValueComplete(PodcastIdentifiedListValueState value) {
                super(
                        PodcastIdentifiedListValueStateAsyncValueComplete.class,
                        value
                );
            }
        }

        final class PodcastIdentifiedListValueStateAsyncValueFailed extends Either31.RightImpl<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                > implements AsyncValueState.AsyncValueFailed<
                PodcastIdentifiedListValueStateAsyncValueLoading,
                PodcastIdentifiedListValueStateAsyncValueComplete,
                PodcastIdentifiedListValueStateAsyncValueFailed,
                PodcastIdentifiedListValueState
                >, PodcastIdentifiedListValueStateAsyncValueState {

            public PodcastIdentifiedListValueStateAsyncValueFailed(PodcastIdentifiedListValueState value) {
                super(
                        PodcastIdentifiedListValueStateAsyncValueFailed.class,
                        value
                );
            }
        }
    }

    public interface PodcastIdentifiedListValueState extends ListValueState<
            PodcastIdentifiedListValueState.PodcastIdentifiedListValueEmpty,
            PodcastIdentifiedListValueState.PodcastIdentifiedListValueNonEmpty,
            PodcastIdentifiedList,
            PodcastIdentified
            > {
        final class PodcastIdentifiedListValueEmpty extends Either.LeftImpl<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                Nothing,
                PodcastIdentifiedList
                > implements ListValueState.ListValueEmpty<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                PodcastIdentifiedList,
                PodcastIdentified
                >, PodcastIdentifiedListValueState {
            public PodcastIdentifiedListValueEmpty() {
                super(
                        PodcastIdentifiedListValueEmpty.class,
                        Nothing.INSTANCE
                );
            }
        }

        final class PodcastIdentifiedListValueNonEmpty extends Either.RightImpl<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                Nothing,
                PodcastIdentifiedList
                > implements ListValueState.ListValueNonEmpty<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                PodcastIdentifiedList,
                PodcastIdentified
                >, PodcastIdentifiedListValueState {
            public PodcastIdentifiedListValueNonEmpty(PodcastIdentifiedList value) {
                super(
                        PodcastIdentifiedListValueNonEmpty.class,
                        value
                );
            }
        }
    }

    private static final class PodcastListAsyncValueViewFacade implements ValueViewFacade {
        final AtomicBoolean disposed = new AtomicBoolean();
        final View view;
        final RecyclerView recyclerView;
        final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        final View emptyItemsLoadingView;
        final View nonEmptyItemsLoadingView;
        final View emptyItemsCompleteView;
        final View emptyItemsFailedView;
        final View nonEmptyItemsFailedView;

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

    private static final class PodcastListAsyncValueViewFacadeTransaction implements ValueViewFacadeTransaction {
        private final PodcastListAsyncValueViewFacade viewFacade;
        private final PodcastIdentifiedListValueStateAsyncValueState state;
        private boolean complete;

        private PodcastListAsyncValueViewFacadeTransaction(
                PodcastListAsyncValueViewFacade viewFacade,
                PodcastIdentifiedListValueStateAsyncValueState state
        ) {
            this.viewFacade = viewFacade;
            this.state = state;
        }

        @Override
        public Completable complete() {
            if (complete) {
                throw new IllegalStateException("Already complete");
            }
            complete = true;

            // TODO implement!
            return null;
        }
    }

    protected static final class PodcastListAsyncValueViewFacadeFactory implements
            ValueViewFacade.ValueViewFacadeFactory<
                    PodcastListFragment,
                    PodcastListFragmentListener,
                    PodcastListFragmentAttachment,
                    PodcastListAsyncValueViewFacade
                    > {
        @Override
        public PodcastListAsyncValueViewFacade newViewFacade(
                PodcastListFragment fragment,
                PodcastListFragmentListener listener,
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

    private static final String TAG = "PodcastList";

    public PodcastListFragment() {
        this(PodcastListAsyncValueViewFacadeTransaction::new);
    }

    // need this constructor to avoid a cast for [asyncValueRenderer]
    private PodcastListFragment(
            ValueRenderer<
                    PodcastListAsyncValueViewFacade,
                    PodcastIdentifiedListValueStateAsyncValueState,
                    PodcastListAsyncValueViewFacadeTransaction
                    > asyncValueRenderer
    ) {
        super(
                PodcastListFragment.class,
                PodcastListFragmentListener.class,
                PodcastListFragmentAttachment::new,
                PodcastListFragmentListener::onPodcastListFragmentAttached,
                PodcastListFragmentListener::onPodcastListFragmentWillDetach,
                R.layout.fragment_podcast_list,
                TAG,
                new PodcastListAsyncValueViewFacadeFactory(),
                PodcastListFragmentListener::podcastIdentifiedListValueStateAsyncValueStateObservable,
                asyncValueRenderer
        );
    }
}
