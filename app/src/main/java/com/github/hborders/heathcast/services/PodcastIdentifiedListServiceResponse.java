package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.PodcastIdentifiedList;

public interface PodcastIdentifiedListServiceResponse extends ServiceResponse1<
        PodcastIdentifiedListServiceResponse.Loading,
        PodcastIdentifiedListServiceResponse.Complete,
        PodcastIdentifiedListServiceResponse.Failed,
        PodcastIdentifiedList
        > {
    final class Loading extends ServiceResponse1.Loading<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Loading(PodcastIdentifiedList value) {
            super(value);
        }
    }

    final class Complete extends ServiceResponse1.Complete<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Complete(PodcastIdentifiedList value) {
            super(value);
        }
    }

    final class Failed extends ServiceResponse1.Failed<
            Loading,
            Complete,
            Failed,
            PodcastIdentifiedList
            > implements PodcastIdentifiedListServiceResponse {
        public Failed(PodcastIdentifiedList value) {
            super(value);
        }
    }
}
