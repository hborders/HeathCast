package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastListFragment extends Fragment {
    private static final String TAG = "PodcastList";
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";
    private static final String ITEM_RANGE_ENABLED_KEY = "itemRangeEnabled";

    private final BehaviorSubject<Optional<ItemRange>> itemRangeOptionalBehaviorSubject =
            BehaviorSubject.createDefault(Optional.empty());
    private boolean itemRangeEnabled;

    @Nullable
    private PodcastListFragmentListener listener;

    @Nullable
    private PodcastRecyclerViewAdapter adapter;

    @Nullable
    private Disposable disposable;

    public PodcastListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final PodcastListFragmentListener listener = FragmentUtil.requireFragmentListener(
                this,
                context,
                PodcastListFragmentListener.class
        );
        this.listener = listener;
        listener.onPodcastListFragmentAttached(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            itemRangeEnabled = savedInstanceState.getBoolean(ITEM_RANGE_ENABLED_KEY);
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_podcast_list,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView podcastsRecyclerView =
                view.requireViewById(R.id.fragment_podcast_list_podcasts_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        podcastsRecyclerView.setLayoutManager(linearLayoutManager);
        @Nullable final PodcastIdentifiedList podcastIdentifieds =
                FragmentUtil.getUnparcelableListArgument(
                        this,
                        PodcastIdentifiedHolder.class,
                        PodcastIdentifiedList::new,
                        PODCAST_PARCELABLES_KEY
                );
        final PodcastRecyclerViewAdapter adapter = new PodcastRecyclerViewAdapter(
                podcastIdentifieds == null ? new PodcastIdentifiedList() : podcastIdentifieds,
                identifiedPodcast ->
                        Objects.requireNonNull(this.listener).onClick(
                                this,
                                identifiedPodcast)
        );
        this.adapter = adapter;
        podcastsRecyclerView.setAdapter(adapter);
        podcastsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (itemRangeEnabled) {
                    @Nullable final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
                    final Optional<ItemRange> itemRangeOptional;
                    if (adapter == null) {
                        itemRangeOptional = Optional.empty();
                    } else {
                        @Nullable final RecyclerView.LayoutManager recyclerViewLayoutManager =
                                recyclerView.getLayoutManager();
                        if (linearLayoutManager != recyclerViewLayoutManager) {
                            throw new IllegalStateException("RecyclerView.layoutManager should be: "
                                    + linearLayoutManager +
                                    " but is: "
                                    + recyclerViewLayoutManager
                            );
                        }

                        final int itemCount = adapter.getItemCount();
                        final int firstVisibleItemPosition =
                                linearLayoutManager.findFirstVisibleItemPosition();
                        if (firstVisibleItemPosition == RecyclerView.NO_POSITION) {
                            itemRangeOptional = Optional.of(
                                    ItemRange.invisible(itemCount)
                            );
                        } else {
                            final int lastVisibleItemPosition =
                                    linearLayoutManager.findLastVisibleItemPosition();
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
                    }
                    itemRangeOptionalBehaviorSubject.onNext(itemRangeOptional);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        disposable = Objects.requireNonNull(this.listener)
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
                            Objects.requireNonNull(this.adapter).setItems(podcastIdentifieds);
                        },
                        throwable -> {
                            Objects.requireNonNull(this.listener).onPodcastIdentifiedsError(
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(
                ITEM_RANGE_ENABLED_KEY,
                itemRangeEnabled
        );

        afterOnSaveInstanceStateOrOnStop();
    }

    @Override
    public void onStop() {
        super.onStop();

        afterOnSaveInstanceStateOrOnStop();
    }

    // Note that `onStop` is only called before `onSaveInstanceState()` on Android 28+ devices.
    // Prior to that, the order can be reversed - this is a case Lifecycle specifically handles.
    // I was under the impression that `AutoDispose` does as well
    // https://androidstudygroup.slack.com/archives/C09HE40J0/p1551849597051100

    private void afterOnSaveInstanceStateOrOnStop() {
        @Nullable final Disposable disposable = this.disposable;
        if (disposable != null) {
            disposable.dispose();
            this.disposable = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        final PodcastListFragmentListener listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onPodcastListFragmentWillDetach(this);

        super.onDetach();
    }

    public void enableItemRangeObservable() {
        itemRangeEnabled = true;
    }

    public Observable<Optional<ItemRange>> getItemRangeOptionalObservable() {
        return itemRangeOptionalBehaviorSubject;
    }

    public interface PodcastListFragmentListener {
        void onPodcastListFragmentAttached(PodcastListFragment podcastListFragment);

        Observable<PodcastIdentifiedList> podcastIdentifiedsObservable(
                PodcastListFragment podcastListFragment
        );

        void onPodcastIdentifiedsError(
                PodcastListFragment podcastListFragment,
                Throwable throwable
        );

        void onClick(
                PodcastListFragment podcastListFragment,
                PodcastIdentified identifiedPodcast
        );

        void onPodcastListFragmentWillDetach(PodcastListFragment podcastListFragment);
    }
}
