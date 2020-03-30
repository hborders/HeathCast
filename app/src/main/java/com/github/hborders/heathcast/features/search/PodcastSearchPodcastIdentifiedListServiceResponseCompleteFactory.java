package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

public interface PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory
        extends PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete.PodcastIdentifiedListServiceResponseCompleteFactory<
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        > {
}
