package com.github.hborders.heathcast.features.search;

import android.content.Context;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class PodcastSearchPodcastListAttachment extends RxFragment.Attachment<
        PodcastSearchPodcastListFragment,
        PodcastSearchPodcastListFragmentListener,
        PodcastSearchPodcastListAttachment
        > {
    PodcastSearchPodcastListAttachment(
            PodcastSearchPodcastListFragment fragment,
            Context context,
            PodcastSearchPodcastListFragmentListener listener,
            Observable<RxFragment.FragmentCreation> fragmenCreationObservable,
            Completable onDetachCompletable
    ) {
        super(
                fragment,
                context,
                listener,
                fragmenCreationObservable,
                onDetachCompletable
        );
    }
}
