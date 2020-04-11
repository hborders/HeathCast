package com.github.hborders.heathcast.services;

import java.util.List;

public interface PodcastListServiceResponse<
        PodcastListServiceResponseLoadingType extends PodcastListServiceResponse.PodcastListServiceResponseLoading<
                PodcastListServiceResponseLoadingType,
                PodcastListServiceResponseCompleteType,
                PodcastListServiceResponseFailedType,
                PodcastListType,
                PodcastItemType
                >,
        PodcastListServiceResponseCompleteType extends PodcastListServiceResponse.PodcastListServiceResponseComplete<
                PodcastListServiceResponseLoadingType,
                PodcastListServiceResponseCompleteType,
                PodcastListServiceResponseFailedType,
                PodcastListType,
                PodcastItemType
                >,
        PodcastListServiceResponseFailedType extends PodcastListServiceResponse.PodcastListServiceResponseFailed<
                PodcastListServiceResponseLoadingType,
                PodcastListServiceResponseCompleteType,
                PodcastListServiceResponseFailedType,
                PodcastListType,
                PodcastItemType
                >,
        PodcastListType extends List<PodcastItemType>,
        PodcastItemType
        > extends ServiceResponse1<
        PodcastListServiceResponseLoadingType,
        PodcastListServiceResponseCompleteType,
        PodcastListServiceResponseFailedType,
        PodcastListType
        > {
    interface PodcastListServiceResponseLoading<
            PodcastListServiceResponseLoadingType extends PodcastListServiceResponseLoading<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseCompleteType extends PodcastListServiceResponseComplete<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseFailedType extends PodcastListServiceResponseFailed<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType
            > extends ServiceResponse1.Loading1<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType
            >, PodcastListServiceResponse<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType,
            PodcastItemType
            > {
    }

    interface PodcastListServiceResponseComplete<
            PodcastListServiceResponseLoadingType extends PodcastListServiceResponseLoading<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseCompleteType extends PodcastListServiceResponseComplete<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseFailedType extends PodcastListServiceResponseFailed<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType
            > extends ServiceResponse1.Complete1<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType
            >, PodcastListServiceResponse<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType,
            PodcastItemType
            > {
    }

    interface PodcastListServiceResponseFailed<
            PodcastListServiceResponseLoadingType extends PodcastListServiceResponseLoading<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseCompleteType extends PodcastListServiceResponseComplete<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseFailedType extends PodcastListServiceResponseFailed<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType
            > extends ServiceResponse1.Failed1<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType
            >, PodcastListServiceResponse<
            PodcastListServiceResponseLoadingType,
            PodcastListServiceResponseCompleteType,
            PodcastListServiceResponseFailedType,
            PodcastListType,
            PodcastItemType
            > {
    }

    interface PodcastListServiceResponseFactory<
            PodcastListServiceResponseType extends PodcastListServiceResponse<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseLoadingType extends PodcastListServiceResponseLoading<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseCompleteType extends PodcastListServiceResponseComplete<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListServiceResponseFailedType extends PodcastListServiceResponseFailed<
                    PodcastListServiceResponseLoadingType,
                    PodcastListServiceResponseCompleteType,
                    PodcastListServiceResponseFailedType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType
            > {
        PodcastListServiceResponseType newPodcastListServiceResponse(
                PodcastListType podcastItems
        );
    }
}
