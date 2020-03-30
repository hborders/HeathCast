package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.fragments.PodcastListFragment;

public interface PodcastSearchPodcastIdentifiedListState extends PodcastListFragment.PodcastIdentifiedListState<
        PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
        PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
        PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
        PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
        > {
}
