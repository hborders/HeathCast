package com.github.hborders.heathcast.features.search;

import android.content.Context;

import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListAttachment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener;
import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading;

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

        void onPodcastSearchPodcastListFragmentClickedPodcastIdentified(
                PodcastIdentified podcastIdentified
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

    public interface PodcastSearchPodcastIdentifiedListState extends PodcastIdentifiedListState<
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse extends PodcastListAsyncValueState<
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            >, PodcastIdentifiedListServiceResponse<
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading extends
            PodcastIdentifiedListServiceResponseLoading<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            PodcastIdentifiedListAsyncStateLoading<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
    }

    public interface PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete extends
            PodcastIdentifiedListServiceResponseComplete<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            PodcastIdentifiedListAsyncStateComplete<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
    }

    public interface PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed extends
            PodcastIdentifiedListServiceResponseFailed<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            AsyncValueState.AsyncValueFailed<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed,
                    PodcastIdentified
                    >,
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
    }

    public PodcastSearchPodcastListFragment() {
        super(
                PodcastSearchPodcastListFragment.class,
                PodcastSearchPodcastListFragmentListener.class,
                PodcastSearchPodcastListAttachment::new,
                PodcastSearchPodcastListFragmentListener::onPodcastListFragmentAttached,
                PodcastSearchPodcastListFragmentListener::onPodcastListFragmentWillDetach,
                PodcastSearchPodcastListFragmentListener::podcastIdentifiedListStateObservable
        );
    }
}
