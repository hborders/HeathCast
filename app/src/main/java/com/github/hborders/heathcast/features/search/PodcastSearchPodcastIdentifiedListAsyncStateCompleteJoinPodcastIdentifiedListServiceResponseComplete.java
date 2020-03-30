package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

public interface PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete extends
        PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete<
                PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                >,
        PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
                PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
                PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
                >,
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse {
}
