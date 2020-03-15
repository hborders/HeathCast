package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.PodcastIdentifiedList;

public interface PodcastIdentifiedListServiceResponse extends ServiceResponse<
        PodcastIdentifiedListServiceResponse.Loading,
        PodcastIdentifiedListServiceResponse.Complete,
        PodcastIdentifiedListServiceResponse.Failed,
        PodcastIdentifiedList,
        PodcastIdentifiedList,
        PodcastIdentifiedList
        > {
    final class Loading extends ServiceResponse.Loading<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList,
            PodcastIdentifiedList,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Loading(PodcastIdentifiedList value) {
            super(value);
        }
    }

    final class Complete extends ServiceResponse.Complete<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList,
            PodcastIdentifiedList,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Complete(PodcastIdentifiedList value) {
            super(value);
        }
    }

    final class Failed extends ServiceResponse.Failed<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList,
            PodcastIdentifiedList,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Failed(PodcastIdentifiedList value) {
            super(value);
        }
    }
}
