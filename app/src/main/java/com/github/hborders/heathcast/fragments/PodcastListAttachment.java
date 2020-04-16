package com.github.hborders.heathcast.fragments;

import android.content.Context;

import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class PodcastListAttachment extends RxFragment.Attachment<
        PodcastListFragment,
        PodcastListFragment.PodcastListFragmentListener,
        PodcastListAttachment
        > {
    public PodcastListAttachment(
            PodcastListFragment fragment,
            Context context,
            PodcastListFragment.PodcastListFragmentListener listener,
            Observable<RxFragment.FragmentCreation> fragmenCreationObservable,
            Completable onDetachCompletable
    ) {
        super(
                PodcastListAttachment.class,
                fragment,
                context,
                listener,
                fragmenCreationObservable,
                onDetachCompletable
        );
    }
}
