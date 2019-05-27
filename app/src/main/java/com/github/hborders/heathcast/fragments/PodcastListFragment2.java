package com.github.hborders.heathcast.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.AsyncValue;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastListFragment2 extends RxFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListFragmentListener
        > {
    private static final String TAG = "PodcastList";
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";
    private static final String ITEM_RANGE_ENABLED_KEY = "itemRangeEnabled";

    public interface PodcastListFragmentListener {
        void onPodcastListFragmentListenerAttached(PodcastListFragment2 podcastListFragment);

        Observable<AsyncValue<PodcastIdentifiedList>> podcastIdentifiedsAsyncValueObservable(
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

    private final BehaviorSubject<Optional<PodcastIdentifiedsItemRanger>> podcastIdentifiedsItemRangerOptionalBehaviorSubject =
            BehaviorSubject.createDefault(Optional.empty());

    public PodcastListFragment2() {
        super(
                PodcastListFragmentListener.class,
                R.layout.fragment_podcast_list_2
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
                    listener.onPodcastListFragmentListenerAttached(this);
                    attachment.fragmenCreationObservable.subscribe(
                            fragmentCreation -> {
                                fragmentCreation.viewCreationObservableObservable.subscribe(
                                        viewCreationObservable -> viewCreationObservable.subscribe(
                                                viewCreation -> {
                                                    final ProgressBar progressBar =
                                                            viewCreation.view.requireViewById(
                                                                    R.id.fragment_podcast_list_progress_bar
                                                            );
                                                    final TextView progressTextView =
                                                            viewCreation.view.requireViewById(
                                                                    R.id.fragment_podcast_list_progress_text_view
                                                            );
                                                    final TextView errorTextView =
                                                            viewCreation.view.requireViewById(
                                                                    R.id.fragment_podcast_list_error_text_view
                                                            );
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
                                                    podcastIdentifiedsItemRangerOptionalBehaviorSubject.onNext(
                                                            Optional.of(
                                                                    new PodcastIdentifiedsItemRanger(
                                                                            podcastsRecyclerView,
                                                                            adapter,
                                                                            linearLayoutManager
                                                                    )
                                                            )
                                                    );
                                                    viewCreation.activityCreationObservable.subscribe(
                                                            activityCreation -> {
                                                                activityCreation.startObservable.subscribe(
                                                                        start -> {
                                                                            final Disposable adapterSetPodcastIdentifiedsDisposable = listener
                                                                                    .podcastIdentifiedsAsyncValueObservable(this)
                                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                                    .subscribe(
                                                                                            podcastIdentifiedsAsyncValue ->
                                                                                                    podcastIdentifiedsAsyncValue.act(
                                                                                                            podcastIdentifiedListLoading -> {
                                                                                                                progressBar.setVisibility(View.VISIBLE);
                                                                                                                progressTextView.setVisibility(View.VISIBLE);
                                                                                                                errorTextView.setVisibility(View.GONE);
                                                                                                                podcastsRecyclerView.setVisibility(View.GONE);
                                                                                                            },
                                                                                                            podcastIdentifiedListFailed -> {
                                                                                                                progressBar.setVisibility(View.GONE);
                                                                                                                progressTextView.setVisibility(View.GONE);
                                                                                                                errorTextView.setVisibility(View.VISIBLE);
                                                                                                                podcastsRecyclerView.setVisibility(View.GONE);
                                                                                                            },
                                                                                                            podcastIdentifiedListLoaded -> {
                                                                                                                progressBar.setVisibility(View.GONE);
                                                                                                                progressTextView.setVisibility(View.GONE);
                                                                                                                errorTextView.setVisibility(View.GONE);
                                                                                                                podcastsRecyclerView.setVisibility(View.VISIBLE);

                                                                                                                final Bundle args = new Bundle();
                                                                                                                args.putParcelableArray(
                                                                                                                        PODCAST_PARCELABLES_KEY,
                                                                                                                        podcastIdentifiedListLoaded.value
                                                                                                                                .stream()
                                                                                                                                .map(PodcastIdentifiedHolder::new)
                                                                                                                                .toArray(PodcastIdentifiedHolder[]::new)
                                                                                                                );
                                                                                                                setArguments(args);
                                                                                                                adapter.setPodcastIdentifieds(podcastIdentifiedListLoaded.value);
                                                                                                            }
                                                                                                    ),
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
                                                                            fragmentCreation.setArgumentsCompletable.subscribe(
                                                                                    adapterSetPodcastIdentifiedsDisposable::dispose
                                                                            ).isDisposed();
                                                                        }
                                                                ).isDisposed();
                                                            }
                                                    ).isDisposed();
                                                },
                                                Functions.ON_ERROR_MISSING,
                                                () -> podcastIdentifiedsItemRangerOptionalBehaviorSubject.onNext(Optional.empty())
                                        ).isDisposed()
                                ).isDisposed();
                            }
                    ).isDisposed();

                    attachment.onDetachCompletable.subscribe(() ->
                            listener.onPodcastListFragmentListenerWillDetach(this)
                    ).isDisposed();
                }
        ).isDisposed();
    }

    private static final class PodcastIdentifiedsItemRanger {
        private final RecyclerView recyclerView;
        private final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        private final LinearLayoutManager linearLayoutManager;

        private PodcastIdentifiedsItemRanger(
                RecyclerView recyclerView,
                PodcastRecyclerViewAdapter podcastRecyclerViewAdapter,
                LinearLayoutManager linearLayoutManager
        ) {
            this.recyclerView = recyclerView;
            this.podcastRecyclerViewAdapter = podcastRecyclerViewAdapter;
            this.linearLayoutManager = linearLayoutManager;
        }

        @Override
        public String toString() {
            return "RecyclerViewItemRangeCollector{" +
                    "recyclerView=" + recyclerView +
                    ", podcastRecyclerViewAdapter=" + podcastRecyclerViewAdapter +
                    ", linearLayoutManager=" + linearLayoutManager +
                    '}';
        }
    }

    public Observable<Optional<ItemRange>> getItemRangeOptionalObservable() {
        return podcastIdentifiedsItemRangerOptionalBehaviorSubject.flatMap(podcastIdentifiedsItemRangerOptional ->
                podcastIdentifiedsItemRangerOptional.map(podcastIdentifiedsItemRanger ->
                        Observable.<Optional<ItemRange>>create(emitter -> {
                            final RecyclerView recyclerView = podcastIdentifiedsItemRanger.recyclerView;
                            final LinearLayoutManager linearLayoutManager = podcastIdentifiedsItemRanger.linearLayoutManager;
                            final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter = podcastIdentifiedsItemRanger.podcastRecyclerViewAdapter;

                            final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    final int itemCount = podcastRecyclerViewAdapter.getItemCount();
                                    final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    final Optional<ItemRange> itemRangeOptional;
                                    if (firstVisibleItemPosition == RecyclerView.NO_POSITION) {
                                        itemRangeOptional = Optional.of(
                                                ItemRange.invisible(itemCount)
                                        );
                                    } else {
                                        final int lastVisibleItemPosition =
                                                podcastIdentifiedsItemRanger.linearLayoutManager
                                                        .findLastVisibleItemPosition();
                                        if (lastVisibleItemPosition == RecyclerView.NO_POSITION) {
                                            throw new IllegalStateException(
                                                    "A firstVisibleItemPosition: "
                                                            + firstVisibleItemPosition
                                                            + " should imply a " +
                                                            "lastVisibleItemPosition"
                                            );
                                        } else {
                                            itemRangeOptional = Optional.of(
                                                    ItemRange.visible(
                                                            itemCount,
                                                            firstVisibleItemPosition,
                                                            lastVisibleItemPosition
                                                    )
                                            );
                                        }
                                    }
                                    emitter.onNext(itemRangeOptional);
                                }
                            };
                            recyclerView.addOnScrollListener(onScrollListener);
                            emitter.setCancellable(() ->
                                    recyclerView.removeOnScrollListener(onScrollListener)
                            );
                        })
                ).orElse(Observable.empty()));
    }
}
