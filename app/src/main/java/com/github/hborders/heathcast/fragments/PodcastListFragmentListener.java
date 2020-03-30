package com.github.hborders.heathcast.fragments;

import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Observable;

public interface PodcastListFragmentListener<
        PodcastListFragmentType extends PodcastListFragment<
                        PodcastListFragmentType,
                        PodcastListFragmentListenerType,
                        PodcastListAttachmentType,
                        PodcastIdentifiedListStateType,
                        PodcastIdentifiedListAsyncStateType,
                        LoadingType,
                        CompleteType,
                        FailedType
                        >,
        PodcastListFragmentListenerType extends PodcastListFragmentListener<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                PodcastListAttachmentType,
                PodcastIdentifiedListStateType,
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastListAttachmentType extends RxFragment.Attachment<
                PodcastListFragmentType,
                PodcastListFragmentListenerType,
                PodcastListAttachmentType
                >,
        PodcastIdentifiedListStateType extends PodcastListFragment.PodcastIdentifiedListState<
                PodcastIdentifiedListAsyncStateType,
                LoadingType,
                CompleteType,
                FailedType
                >,
        PodcastIdentifiedListAsyncStateType extends PodcastListFragment.PodcastIdentifiedListAsyncState<
                LoadingType,
                CompleteType,
                FailedType
                >,
        LoadingType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateLoading<
                LoadingType,
                CompleteType,
                FailedType
                >,
        CompleteType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateComplete<
                LoadingType,
                CompleteType,
                FailedType
                >,
        FailedType extends PodcastListFragment.PodcastIdentifiedListAsyncState.PodcastIdentifiedListAsyncStateFailed<
                LoadingType,
                CompleteType,
                FailedType
                >
        > {
    void onPodcastListFragmentAttached(PodcastListFragmentType podcastListFragment);

    Observable<PodcastIdentifiedListStateType> podcastIdentifiedListStateObservable(
            PodcastListFragmentType podcastListFragment
    );

    void onClickPodcastIdentified(
            PodcastListFragmentType podcastListFragment,
            PodcastIdentified podcastIdentified
    );

    void onPodcastListFragmentWillDetach(PodcastListFragmentType podcastListFragment);
}
