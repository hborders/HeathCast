package com.github.hborders.heathcast.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class PodcastListFragment2 extends RxFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListFragmentListener
        > {
    private static final String TAG = "PodcastList";
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";
    private static final String ITEM_RANGE_ENABLED_KEY = "itemRangeEnabled";

    public interface PodcastListFragmentListener {
        void onPodcastListFragmentListenerAttached(PodcastListFragment2 podcastListFragment);

        Observable<List<Identified<Podcast>>> podcastIdentifiedsObservable(
                PodcastListFragment2 podcastListFragment
        );

        void onPodcastIdentifiedsError(
                PodcastListFragment2 podcastListFragment,
                Throwable throwable
        );

        void onClick(
                PodcastListFragment2 podcastListFragment,
                Identified<Podcast> identifiedPodcast
        );

        void onPodcastListFragmentListenerWillDetach(PodcastListFragment2 podcastListFragment);
    }

    public PodcastListFragment2() {
        super(
                PodcastListFragmentListener.class,
                R.layout.fragment_podcast_list
        );
    }

    @Override
    protected final PodcastListFragment2 getSelf() {
        return this;
    }

    @Override
    protected final void subscribeToAttachmentObservable(
            Observable<
                    Attachment<
                            PodcastListFragment2,
                            PodcastListFragmentListener
                            >
                    > attachmentObservable
    ) {
        attachmentObservable.subscribe(
                attachment -> {
                    final PodcastListFragmentListener listener = attachment.listener;
                    attachment.fragmenCreationObservable.subscribe(
                            fragmentCreation -> {
                                fragmentCreation.viewCreationObservableObservable.subscribe(
                                        viewCreationObservable -> viewCreationObservable.subscribe(
                                                viewCreation -> {
                                                    final RecyclerView podcastsRecyclerView =
                                                            viewCreation.view.requireViewById(
                                                                    R.id.fragment_podcast_list_podcasts_recycler_view
                                                            );
                                                    final LinearLayoutManager linearLayoutManager =
                                                            new LinearLayoutManager(
                                                                    attachment.context
                                                            );
                                                    podcastsRecyclerView.setLayoutManager(linearLayoutManager);
                                                    final List<Identified<Podcast>> initialPodcastIdentifieds =
                                                            FragmentUtil.getUnparcelableHolderListArgumentOptional(
                                                                    this,
                                                                    PodcastIdentifiedHolder.class,
                                                                    PODCAST_PARCELABLES_KEY
                                                            ).orElse(Collections.emptyList());
                                                    final PodcastRecyclerViewAdapter adapter =
                                                            new PodcastRecyclerViewAdapter(
                                                                    initialPodcastIdentifieds,
                                                                    podcastIdentified ->
                                                                            listener.onClick(
                                                                                    this,
                                                                                    podcastIdentified
                                                                            )
                                                            );
                                                    podcastsRecyclerView.setAdapter(adapter);

                                                    viewCreation.startObservable.subscribe(
                                                            start -> {
                                                                final Disposable disposable = listener
                                                                        .podcastIdentifiedsObservable(this)
                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                        .subscribe(
                                                                                podcastIdentifieds -> {
                                                                                    final Bundle args = new Bundle();
                                                                                    args.putParcelableArray(
                                                                                            PODCAST_PARCELABLES_KEY,
                                                                                            podcastIdentifieds
                                                                                                    .stream()
                                                                                                    .map(PodcastIdentifiedHolder::new)
                                                                                                    .toArray(PodcastIdentifiedHolder[]::new)
                                                                                    );
                                                                                    setArguments(args);
                                                                                    adapter.setPodcastIdentifieds(podcastIdentifieds);
                                                                                },
                                                                                throwable -> {
                                                                                    listener.onPodcastIdentifiedsError(
                                                                                            this,
                                                                                            throwable
                                                                                    );
                                                                                    Log.e(
                                                                                            TAG,
                                                                                            "Error loading podcast list",
                                                                                            throwable
                                                                                    );
                                                                                }
                                                                        );
                                                                start.saveInstanceStateObservable.subscribe(
                                                                        saveInstanceState -> disposable.dispose()
                                                                ).isDisposed();
                                                                start.onStopCompletable.subscribe(
                                                                        disposable::dispose
                                                                ).isDisposed();
                                                            }).isDisposed();
                                                }
                                        ).isDisposed()
                                ).isDisposed();
                            }
                    ).isDisposed();

                    listener.onPodcastListFragmentListenerAttached(this);
                    attachment.onDetachCompletable.subscribe(() ->
                            listener.onPodcastListFragmentListenerWillDetach(this)
                    ).isDisposed();
                }
        ).isDisposed();
    }
}
