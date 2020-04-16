package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.ViewUtil;
import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.services.PodcastListServiceResponse;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.CompletableSubject;

// Next, I should consume it in the MainFragment as well
public final class PodcastListFragment extends RxListAsyncValueFragment<
        PodcastListFragment,
        PodcastListFragment.PodcastListFragmentListener,
        PodcastListAttachment
        > {
    public interface PodcastListFragmentListener {
        void onPodcastListFragmentAttached(
                PodcastListFragment fragment
        );

        Observable<PodcastListFragment.PodcastSearchPodcastListAsyncValueState> podcastSearchPodcastListAsyncValueStateObservable(
                PodcastListFragment fragment
        );

        void onPodcastListFragmentClickedPodcastIdentified(
                PodcastListFragment fragment,
                PodcastImpl.PodcastIdentifiedImpl podcastIdentified
        );

        void onPodcastListFragmentWillDetach(
                PodcastListFragment fragment
        );
    }

    public interface PodcastSearchPodcastListAsyncValueState extends AsyncValueState<
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastListServiceResponse<
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    public static final class PodcastSearchPodcastListAsyncValueStateLoading
            extends Either31.LeftImpl<
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseLoading<
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueLoading<
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastSearchPodcastListAsyncValueState {
        public PodcastSearchPodcastListAsyncValueStateLoading(PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value) {
            super(
                    PodcastSearchPodcastListAsyncValueStateLoading.class,
                    value
            );
        }
    }

    public static final class PodcastSearchPodcastListAsyncValueStateComplete
            extends Either31.MiddleImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseComplete<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueComplete<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastSearchPodcastListAsyncValueState {
        public PodcastSearchPodcastListAsyncValueStateComplete(PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value) {
            super(
                    PodcastSearchPodcastListAsyncValueStateComplete.class,
                    value
            );
        }
    }

    public static final class PodcastSearchPodcastListAsyncValueStateFailed
            extends Either31.RightImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponse.PodcastListServiceResponseFailed<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueFailed<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastSearchPodcastListAsyncValueState {
        public PodcastSearchPodcastListAsyncValueStateFailed(PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value) {
            super(
                    PodcastSearchPodcastListAsyncValueStateFailed.class,
                    value
            );
        }
    }

    private static final class PodcastListPodcastRecyclerViewAdapterListener
            implements PodcastRecyclerViewAdapter.PodcastRecyclerViewAdapterListener {
        private final PodcastListFragment fragment;
        private final PodcastListFragmentListener listener;

        PodcastListPodcastRecyclerViewAdapterListener(
                PodcastListFragment fragment,
                PodcastListFragmentListener listener
        ) {
            this.fragment = fragment;
            this.listener = listener;
        }

        @Override
        public void onClick(PodcastImpl.PodcastIdentifiedImpl podcastIdentified) {
            this.listener.onPodcastListFragmentClickedPodcastIdentified(
                    fragment,
                    podcastIdentified
            );
        }
    }

    private static final class PodcastListAsyncValueViewFacade implements ValueViewFacade {
        public static PodcastListAsyncValueViewFacade newPodcastListAsyncValueViewFacade(
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
                            new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl(),
                            new PodcastListPodcastRecyclerViewAdapterListener(
                                    fragment,
                                    listener
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

    private static final class PodcastListAsyncValueViewFacadeTransaction
            implements ValueViewFacadeTransaction {
        private final PodcastListAsyncValueViewFacade viewFacade;
        private final PodcastSearchPodcastListAsyncValueState state;
        private boolean complete;

        PodcastListAsyncValueViewFacadeTransaction(
                PodcastListAsyncValueViewFacade viewFacade,
                PodcastSearchPodcastListAsyncValueState state
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

            state.act(
                    loading -> {
                        final PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl podcasts = loading.getValue();
                        if (podcasts.isEmpty()) {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.VISIBLE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                            viewFacade.recyclerView.setVisibility(View.GONE);
                            viewFacade.podcastRecyclerViewAdapter.setItems(Collections.emptyList());
                            viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                        } else {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.VISIBLE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                            viewFacade.recyclerView.setVisibility(View.VISIBLE);
                            viewFacade.podcastRecyclerViewAdapter.setItems(podcasts);
                            viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                        }
                    },
                    complete -> {
                        final PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl podcasts = complete.getValue();
                        if (podcasts.isEmpty()) {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.VISIBLE);
                            viewFacade.recyclerView.setVisibility(View.GONE);
                            viewFacade.podcastRecyclerViewAdapter.setItems(Collections.emptyList());
                            viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                        } else {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                            viewFacade.recyclerView.setVisibility(View.VISIBLE);
                            viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                        }
                    },
                    failed -> {
                        final PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl podcasts = failed.getValue();
                        if (podcasts.isEmpty()) {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                            viewFacade.recyclerView.setVisibility(View.GONE);
                            viewFacade.podcastRecyclerViewAdapter.setItems(Collections.emptyList());
                            viewFacade.emptyItemsFailedView.setVisibility(View.VISIBLE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                        } else {
                            viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                            viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                            viewFacade.recyclerView.setVisibility(View.VISIBLE);
                            viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                            viewFacade.nonEmptyItemsFailedView.setVisibility(View.VISIBLE);
                        }
                    }
            );

            final CompletableSubject completableSubject = CompletableSubject.create();
            ViewUtil.doOnLayout(
                    viewFacade.view,
                    completableSubject::onComplete
            );

            return completableSubject;
        }
    }

    private static final String TAG = "PodcastList";

    public PodcastListFragment() {
        this(
                PodcastListFragmentListener::podcastSearchPodcastListAsyncValueStateObservable,
                PodcastListAsyncValueViewFacadeTransaction::new
        );
    }

    // private constructor to avoid a required downcast
    private PodcastListFragment(
            ValueStateObservableProvider<
                    PodcastListFragment,
                    PodcastListFragmentListener,
                    PodcastListAttachment,
                    PodcastSearchPodcastListAsyncValueState
                    > valueStateObservableProvider,
            ValueRenderer<
                    PodcastListAsyncValueViewFacade,
                    PodcastSearchPodcastListAsyncValueState,
                    PodcastListAsyncValueViewFacadeTransaction
                    > valueRenderer
    ) {
        super(
                PodcastListFragment.class,
                PodcastListFragmentListener.class,
                PodcastListAttachment::new,
                PodcastListFragmentListener::onPodcastListFragmentAttached,
                PodcastListFragmentListener::onPodcastListFragmentWillDetach,
                R.layout.fragment_podcast_list,
                TAG,
                PodcastListAsyncValueViewFacade::newPodcastListAsyncValueViewFacade,
                valueStateObservableProvider,
                valueRenderer
        );
    }
}

