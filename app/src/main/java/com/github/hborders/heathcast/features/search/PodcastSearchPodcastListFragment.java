package com.github.hborders.heathcast.features.search;

import android.content.Context;

import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListAttachment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener;
import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class PodcastSearchPodcastListFragment extends PodcastListFragment<
        PodcastSearchPodcastListFragment,
        PodcastSearchPodcastListFragmentListener,
        PodcastSearchPodcastListAttachment
        > {
    public interface PodcastSearchPodcastListFragmentListener {
        void onPodcastSearchPodcastListFragmentAttached(
                PodcastSearchPodcastListFragment fragment
        );

        void onPodcastSearchPodcastListFragmentClickedPodcastIdentified(
                PodcastIdentified podcastIdentified
        );

        void onPodcastSearchPodcastListFragmentWillDetach(
                PodcastSearchPodcastListFragment fragment
        );
    }

    public static final class PodcastSearchPodcastListAttachment extends Attachment<
            PodcastSearchPodcastListFragment,
            PodcastSearchPodcastListFragmentListener,
            PodcastSearchPodcastListAttachment
            > {
        PodcastSearchPodcastListAttachment(
                PodcastSearchPodcastListFragment fragment,
                Context context,
                PodcastSearchPodcastListFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    PodcastSearchPodcastListAttachment.class,
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

//    public interface PodcastSearchPodcastIdentifiedListState extends PodcastIdentifiedListState<
//            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
//            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
//            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
//            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
//            > {
//    }

    public interface PodcastSearchPodcastListState extends AsyncValueState<
            PodcastSearchPodcastListStateLoading,
            PodcastSearchPodcastListStateComplete,
            PodcastSearchPodcastListStateFailed,
            PodcastIdentifiedPodcastListState
            >, PodcastIdentifiedListServiceResponse<
            PodcastSearchPodcastListStateLoading,
            PodcastSearchPodcastListStateComplete,
            PodcastSearchPodcastListStateFailed
            > {
    }

    public interface PodcastSearchPodcastListStateLoading extends
            PodcastIdentifiedListServiceResponseLoading<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed
                    >,
            AsyncValueState.AsyncValueLoading<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed
                    >,
            PodcastSearchPodcastListState {
    }

    public interface PodcastSearchPodcastListStateComplete extends
            PodcastIdentifiedListServiceResponseComplete<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed
                    >,
            AsyncValueState.AsyncValueComplete<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed
                    >,
            PodcastSearchPodcastListState {
    }

    public interface PodcastSearchPodcastListStateFailed extends
            PodcastIdentifiedListServiceResponseFailed<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed
                    >,
            AsyncValueState.AsyncValueFailed<
                    PodcastSearchPodcastListStateLoading,
                    PodcastSearchPodcastListStateComplete,
                    PodcastSearchPodcastListStateFailed,
                    PodcastIdentified
                    >,
            PodcastSearchPodcastListState {
    }

    public interface PodcastIdentifiedPodcastListValueState extends PodcastListValueState<
            PodcastIdentifiedPodcastListValueEmpty,
            PodcastIdentifiedPodcastListValueNonEmpty,
            PodcastIdentifiedList,
            PodcastIdentified
            > {

    }

    public interface PodcastIdentifiedPodcastListValueEmpty extends PodcastListValueState.PodcastListValueEmpty<
            PodcastIdentifiedPodcastListValueEmpty,
            PodcastIdentifiedPodcastListValueNonEmpty,
            PodcastIdentifiedList,
            PodcastIdentified
            >, PodcastIdentifiedPodcastListValueState {

    }

    public interface PodcastIdentifiedPodcastListValueNonEmpty extends PodcastListValueState.PodcastListValueNonEmpty<
            PodcastIdentifiedPodcastListValueEmpty,
            PodcastIdentifiedPodcastListValueNonEmpty,
            PodcastIdentifiedList,
            PodcastIdentified
            >, PodcastIdentifiedPodcastListValueState {

    }

    public PodcastSearchPodcastListFragment() {
        super(
                PodcastSearchPodcastListFragment.class,
                PodcastSearchPodcastListFragmentListener.class,
                PodcastSearchPodcastListAttachment::new,
                PodcastSearchPodcastListFragmentListener::onPodcastSearchPodcastListFragmentAttached,
                PodcastSearchPodcastListFragmentListener::onPodcastSearchPodcastListFragmentWillDetach,
                PodcastSearchPodcastListFragmentListener::podcastIdentifiedListStateObservable
        );
    }
}
