package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

public interface PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory
        extends PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading.PodcastIdentifiedListServiceResponseLoadingFactory<
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        > {
}
