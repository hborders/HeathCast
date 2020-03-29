package com.github.hborders.heathcast.features.search;

import android.content.Context;

import com.github.hborders.heathcast.fragments.PodcastListFragment2;
import com.github.hborders.heathcast.fragments.PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete;
import com.github.hborders.heathcast.fragments.PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed;
import com.github.hborders.heathcast.fragments.PodcastListFragment2.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListAttachment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListState;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class PodcastSearchPodcastListFragment extends PodcastListFragment2<
        PodcastSearchPodcastListFragment,
        PodcastSearchPodcastListFragmentListener,
        PodcastSearchPodcastListAttachment,
        PodcastSearchPodcastIdentifiedListState,
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        > {
    public interface PodcastSearchPodcastListFragmentListener extends PodcastListFragmentListener<
            PodcastSearchPodcastListFragment,
            PodcastSearchPodcastListFragmentListener,
            PodcastSearchPodcastListAttachment,
            PodcastSearchPodcastIdentifiedListState,
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
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

    public interface PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse extends PodcastIdentifiedListAsyncState<
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
            PodcastIdentifiedListAsyncStateFailed<
                    PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                    PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                    PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                    >,
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
    }

    public PodcastSearchPodcastListFragment() {
        super(
                PodcastSearchPodcastListFragmentListener.class,
                PodcastSearchPodcastListAttachment::new,
                PodcastSearchPodcastListFragmentListener::onPodcastListFragmentAttached,
                PodcastSearchPodcastListFragmentListener::onPodcastListFragmentWillDetach,
                PodcastSearchPodcastListFragmentListener::podcastIdentifiedListStateObservable
        );
    }
}
