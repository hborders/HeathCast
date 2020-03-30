package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

public interface PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading extends
        PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading<
                PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                >,
        PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                >,
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
}
