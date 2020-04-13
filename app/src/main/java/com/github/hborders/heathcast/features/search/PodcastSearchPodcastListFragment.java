package com.github.hborders.heathcast.features.search;

import android.content.Context;

import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Nothing;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListAttachment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener;
import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.services.PodcastListServiceResponse;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseComplete;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseFailed;
import com.github.hborders.heathcast.services.PodcastListServiceResponse.PodcastListServiceResponseLoading;

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

    public interface PodcastSearchPodcastListAsyncValueState extends AsyncValueState<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastIdentifiedPodcastListState
            >, PodcastListServiceResponse<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed
                        > {
    }

    public static final class PodcastSearchPodcastListAsyncValueStateLoading
        extends Either31.LeftImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastIdentifiedPodcastListState
            > implements PodcastListServiceResponseLoading<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed,
                    PodcastIdentifiedPodcastListState
                                >,
            AsyncValueState.AsyncValueLoading<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed
                    >,
            PodcastSearchPodcastListAsyncValueState {
    }

    public static final class PodcastSearchPodcastListAsyncValueStateComplete
            extends Either31.MiddleImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastIdentifiedPodcastListState
            > implements PodcastListServiceResponseComplete<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed
                                >,
            AsyncValueState.AsyncValueComplete<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed
                    >,
            PodcastSearchPodcastListAsyncValueState {
    }

    public static final class PodcastSearchPodcastListAsyncValueStateFailed
            extends Either31.RightImpl<
            PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastIdentifiedPodcastListState
            > implements PodcastListServiceResponseFailed<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed
                                >,
            AsyncValueState.AsyncValueFailed<
                    PodcastSearchPodcastListAsyncValueStateLoading,
                    PodcastSearchPodcastListAsyncValueStateComplete,
                    PodcastSearchPodcastListAsyncValueStateFailed,
                    PodcastIdentified
                    >,
            PodcastSearchPodcastListAsyncValueState {
    }

    public interface PodcastIdentifiedPodcastListValueState extends PodcastListValueState<
            PodcastIdentifiedPodcastListValueState.PodcastIdentifiedPodcastListValueEmpty,
            PodcastIdentifiedPodcastListValueState.PodcastIdentifiedPodcastListValueNonEmpty,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
        final class PodcastIdentifiedPodcastListValueEmpty
                extends Either.LeftImpl<
                PodcastIdentifiedPodcastListValueEmpty,
                PodcastIdentifiedPodcastListValueNonEmpty,
                Nothing,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                > implements PodcastListValueState.PodcastListValueEmpty<
                PodcastIdentifiedPodcastListValueEmpty,
                PodcastIdentifiedPodcastListValueNonEmpty,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                PodcastImpl.PodcastIdentifiedImpl
                >, PodcastIdentifiedPodcastListValueState {

        }

        final class PodcastIdentifiedPodcastListValueNonEmpty
                extends Either.RightImpl<
                PodcastIdentifiedPodcastListValueEmpty,
                PodcastIdentifiedPodcastListValueNonEmpty,
                Nothing,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl
                > implements PodcastListValueState.PodcastListValueNonEmpty<
                PodcastIdentifiedPodcastListValueEmpty,
                PodcastIdentifiedPodcastListValueNonEmpty,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
                PodcastImpl.PodcastIdentifiedImpl
                >, PodcastIdentifiedPodcastListValueState {

        }
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
