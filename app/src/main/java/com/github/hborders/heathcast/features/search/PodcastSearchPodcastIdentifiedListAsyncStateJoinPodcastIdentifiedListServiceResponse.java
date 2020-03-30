package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

public interface PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse extends PodcastListFragment.PodcastIdentifiedListAsyncState<
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        >, PodcastIdentifiedListServiceResponse<
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        > {
}
