package com.github.hborders.heathcast.features.search;

import android.content.Context;
import android.view.ViewGroup;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListAttachment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener;
import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.services.PodcastListServiceResponse;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseComplete;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseFailed;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseLoading;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class PodcastSearchPodcastListFragment extends PodcastListFragment<
        PodcastSearchPodcastListFragment,
        PodcastSearchPodcastListFragmentListener,
        PodcastSearchPodcastListAttachment
        > {
    public interface PodcastSearchPodcastListFragmentListener {
        void onPodcastSearchPodcastListFragmentAttached(
                PodcastSearchPodcastListFragment fragment
        );

        Observable<PodcastSearchPodcastListAsyncValueState> podcastSearchPodcastListAsyncValueStateObservable(
                PodcastSearchPodcastListFragment fragment
        );

        void onPodcastSearchPodcastListFragmentClickedPodcastIdentified(
                PodcastSearchPodcastListFragment fragment,
                PodcastImpl.PodcastIdentifiedImpl podcastIdentified
        );

        void onPodcastSearchPodcastListFragmentWillDetach(
                PodcastSearchPodcastListFragment fragment
        );
    }

    public static final class PodcastSearchPodcastListAttachment extends Attachment<
            PodcastSearchPodcastListFragment,
            PodcastSearchPodcastListFragmentListener,
            PodcastSearchPodcastListAttachment
            > {
        PodcastSearchPodcastListAttachment(
                PodcastSearchPodcastListFragment fragment,
                Context context,
                PodcastSearchPodcastListFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    PodcastSearchPodcastListAttachment.class,
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

    public interface PodcastSearchPodcastListAsyncValueState extends AsyncValueState<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastListServiceResponse<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    public static final class PodcastSearchPodcastListAsyncValueStateLoading
            extends Either31.LeftImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponseLoading<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueLoading<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
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
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponseComplete<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueComplete<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
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
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            > implements PodcastListServiceResponseFailed<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            >, AsyncValueState.AsyncValueFailed<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
            >, PodcastSearchPodcastListAsyncValueState {
        public PodcastSearchPodcastListAsyncValueStateFailed(PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl value) {
            super(
                    PodcastSearchPodcastListAsyncValueStateFailed.class,
                    value
            );
        }
    }

    private static final class PodcastSearchPodcastRecyclerViewAdapter
            extends PodcastRecyclerViewAdapter<
            PodcastSearchPodcastRecyclerViewAdapter,
            PodcastSearchPodcastRecyclerViewAdapter.PodcastSearchPodcastViewHolder,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastSearchPodcastRecyclerViewAdapter.PodcastSearchPodcastRecyclerViewAdapterListener
            > {
        static final class PodcastSearchPodcastViewHolder extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                PodcastSearchPodcastRecyclerViewAdapter,
                PodcastSearchPodcastRecyclerViewAdapter.PodcastSearchPodcastViewHolder,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                PodcastImpl.PodcastIdentifiedImpl,
                PodcastSearchPodcastRecyclerViewAdapterListener
                > {

            PodcastSearchPodcastViewHolder(
                    ViewGroup parent,
                    int viewType
            ) {
                super(
                        parent,
                        viewType
                );
            }
        }

        static final class PodcastSearchPodcastRecyclerViewAdapterListener {
            private final PodcastSearchPodcastListFragment fragment;
            private final PodcastSearchPodcastListFragmentListener listener;

            PodcastSearchPodcastRecyclerViewAdapterListener(
                    PodcastSearchPodcastListFragment fragment,
                    PodcastSearchPodcastListFragmentListener listener
            ) {
                this.fragment = fragment;
                this.listener = listener;
            }

            void onClick(PodcastImpl.PodcastIdentifiedImpl podcastIdentified) {
                this.listener.onPodcastSearchPodcastListFragmentClickedPodcastIdentified(
                        fragment,
                        podcastIdentified
                );
            }
        }

        PodcastSearchPodcastRecyclerViewAdapter(
                PodcastSearchPodcastListFragment fragment,
                PodcastSearchPodcastListFragmentListener listener
        ) {
            super(
                    PodcastSearchPodcastRecyclerViewAdapter.class,
                    PodcastSearchPodcastViewHolder::new,
                    new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl(),
                    new PodcastSearchPodcastRecyclerViewAdapterListener(
                            fragment,
                            listener
                    ),
                    PodcastSearchPodcastRecyclerViewAdapterListener::onClick
            );
        }
    }

    public PodcastSearchPodcastListFragment() {
        this(PodcastSearchPodcastListFragmentListener::podcastSearchPodcastListAsyncValueStateObservable);
    }

    // Chain constructors to avoid a method reference downcast
    private PodcastSearchPodcastListFragment(ValueStateObservableProvider<
            PodcastSearchPodcastListFragment,
            PodcastSearchPodcastListFragmentListener,
            PodcastSearchPodcastListAttachment,
            PodcastSearchPodcastListAsyncValueState
            > podcastSearchPodcastListAsyncValueStateProvider) {
        super(
                PodcastSearchPodcastListFragment.class,
                PodcastSearchPodcastListFragmentListener.class,
                PodcastSearchPodcastListAttachment::new,
                PodcastSearchPodcastListFragmentListener::onPodcastSearchPodcastListFragmentAttached,
                PodcastSearchPodcastListFragmentListener::onPodcastSearchPodcastListFragmentWillDetach,
                PodcastSearchPodcastRecyclerViewAdapter::new,
                podcastSearchPodcastListAsyncValueStateProvider
        );
    }
}
