package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.fragments.PodcastListFragmentListener;

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
