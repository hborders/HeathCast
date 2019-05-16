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
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.idlingresource.BasicIdlingResource;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class PodcastListFragment extends Fragment {
    private static final String TAG = "PodcastList";
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";

    private final BasicIdlingResource basicIdlingResource = BasicIdlingResource.busy(TAG);

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
        listener.onPodcastListFragmentListenerAttached(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        podcastsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        @Nullable final List<Identified<Podcast>> podcastIdentifieds =
                FragmentUtil.getUnparcelableHolderListArgument(
                        this,
                        PodcastIdentifiedHolder.class,
                        PODCAST_PARCELABLES_KEY
                );
        final PodcastRecyclerViewAdapter adapter = new PodcastRecyclerViewAdapter(
                podcastIdentifieds == null ? Collections.emptyList() : podcastIdentifieds,
                identifiedPodcast ->
                        Objects.requireNonNull(this.listener).onClick(
                                this,
                                identifiedPodcast)
        );
        this.adapter = adapter;
        podcastsRecyclerView.setAdapter(adapter);
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
                            Objects.requireNonNull(this.adapter).setPodcastIdentifieds(podcastIdentifieds);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        afterOnSaveInstanceStateOrOnStop();
    }

    @Override
    public void onStop() {
        super.onStop();

        afterOnSaveInstanceStateOrOnStop();

        basicIdlingResource.setBusy();
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
        listener.onPodcastListFragmentListenerWillDetach(this);

        super.onDetach();
    }

    public IdlingResource getPodcastIdentifiedsIdlingResource() {
        return basicIdlingResource;
    }

    public interface PodcastListFragmentListener {
        void onPodcastListFragmentListenerAttached(PodcastListFragment podcastListFragment);

        Observable<List<Identified<Podcast>>> podcastIdentifiedsObservable(
                PodcastListFragment podcastListFragment
        );

        void onPodcastIdentifiedsError(
                PodcastListFragment podcastListFragment,
                Throwable throwable
        );

        void onClick(
                PodcastListFragment podcastListFragment,
                Identified<Podcast> identifiedPodcast
        );

        void onPodcastListFragmentListenerWillDetach(PodcastListFragment podcastListFragment);
    }
}
