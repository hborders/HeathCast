package com.github.hborders.heathcast.fragments;

import android.annotation.SuppressLint;

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
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.PublishSubject;

public final class PodcastListFragment2 extends RxFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListFragmentListener
        > {
    private static final String TAG = "PodcastList";
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";
    private static final String ITEM_RANGE_ENABLED_KEY = "itemRangeEnabled";

    public interface PodcastListFragmentListener {
        void subscribeToPodcastListFragmentObservable(
                Observable<PodcastListFragment2> podcastListFragment2Observable
        );
    }

    private final PublishSubject<Identified<Podcast>> onClickPodcastIdentifiedPublishSubject =
            PublishSubject.create();

    public PodcastListFragment2() {
        super(
                PodcastListFragmentListener.class,
                PodcastListFragmentListener::subscribeToPodcastListFragmentObservable,
                R.layout.fragment_podcast_list
        );
    }

    @Override
    public PodcastListFragment2 getSelf() {
        return this;
    }

    @Override
    protected void subscribeToAttachmentObservable(
            Observable<
                    Attachment<
                            PodcastListFragment2,
                            PodcastListFragmentListener
                            >
                    > attachmentObservable
    ) {
        attachmentObservable.subscribe(
                attachment -> attachment.fragmenCreationObservable.subscribe(
                        fragmentCreation -> {
                            fragmentCreation.saveInstanceStateObservable.subscribe(

                            );
                            fragmentCreation.viewCreationObservable.subscribe(
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
                                        final List<Identified<Podcast>> podcastIdentifieds =
                                                FragmentUtil.getUnparcelableHolderListArgumentOptional(
                                                        this,
                                                        PodcastIdentifiedHolder.class,
                                                        PODCAST_PARCELABLES_KEY
                                                ).orElse(Collections.emptyList());
                                        final PodcastRecyclerViewAdapter adapter =
                                                new PodcastRecyclerViewAdapter(
                                                        podcastIdentifieds,
                                                        identifiedPodcast ->
                                                                attachment.listener.onClick(
                                                                        this,
                                                                        identifiedPodcast)
                                                );
                                    }
                            ).isDisposed();
                        },
                        Functions.ON_ERROR_MISSING,
                        () -> {

                        }
                ).isDisposed()
        ).isDisposed();
    }
}
