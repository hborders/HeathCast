package com.github.hborders.heathcast.features.search;

import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastSearch;

import io.reactivex.Observable;

public interface PodcastSearchFragmentListener<
        PodcastSearchFragmentType extends PodcastSearchFragment<
                PodcastSearchFragmentType,
                PodcastSearchFragmentListenerType
                >,
        PodcastSearchFragmentListenerType extends PodcastSearchFragmentListener<
                PodcastSearchFragmentType,
                PodcastSearchFragmentListenerType
                >
        > {
    void onPodcastSearchFragmentAttached(PodcastSearchFragmentType podcastSearchFragment);

    Observable<PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse> searchForPodcasts2(
            PodcastSearchFragmentType podcastSearchFragment,
            PodcastSearch podcastSearch,
            PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory failedFactory
    );

    void onClickPodcastIdentified(
            PodcastSearchFragmentType podcastSearchFragment,
            PodcastIdentified podcastIdentified
    );

    void onPodcastSearchFragmentWillDetach(PodcastSearchFragmentType podcastSearchFragment);
}
