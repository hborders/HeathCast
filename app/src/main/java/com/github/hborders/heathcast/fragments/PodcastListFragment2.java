package com.github.hborders.heathcast.fragments;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.reactivexandroid.RxListFragment;
import com.github.hborders.heathcast.services.ServiceResponse1;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.List;

import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public final class PodcastListFragment2 extends RxListFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListFragmentListener,
        Identified<Podcast>,
        PodcastIdentifiedHolder
        > {
    public interface PodcastListFragmentListener {
        void onPodcastListFragmentListenerAttached(PodcastListFragment2 podcastListFragment);

        Observable<ServiceResponse1<PodcastIdentifiedList>> podcastIdentifiedsServiceResponseObservable(
                PodcastListFragment2 podcastListFragment
        );

        void onPodcastIdentifiedsFailed(PodcastListFragment2 podcastListFragment);

        void onClickPodcastIdentified(
                PodcastListFragment2 podcastListFragment,
                Identified<Podcast> podcastIdentified
        );

        void onPodcastListFragmentListenerWillDetach(PodcastListFragment2 podcastListFragment);
    }

    private static final String TAG = "PodcastList";

    public PodcastListFragment2() {
        super(
                PodcastListFragmentListener.class,
                PodcastListFragmentListener::onPodcastListFragmentListenerAttached,
                PodcastListFragmentListener::onPodcastListFragmentListenerWillDetach,
                R.layout.fragment_podcast_list_2,
                PodcastListFragmentListener::podcastIdentifiedsServiceResponseObservable,
                PodcastListFragmentListener::onPodcastIdentifiedsFailed,
                PodcastIdentifiedHolder.class
        );
    }

    @Override
    protected final PodcastListFragment2 getSelf() {
        return this;
    }

    @Override
    protected ProgressBar requireProgressBar(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_progress_bar
        );
    }

    @Override
    protected TextView requireProgressTextView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_progress_text_view
        );
    }

    @Override
    protected TextView requireErrorTextView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_error_text_view
        );
    }

    @Override
    protected RecyclerView requireRecyclerView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_podcasts_recycler_view
        );
    }

    @Override
    protected ListRecyclerViewAdapter<Identified<Podcast>, ?> createListRecyclerViewAdapter(
            List<Identified<Podcast>> initialItems,
            PodcastListFragmentListener listener
    ) {
        return new PodcastRecyclerViewAdapter(
                initialItems,
                podcastIdentified ->
                        listener.onClickPodcastIdentified(
                                this,
                                podcastIdentified
                        )
        );
    }

    @Override
    protected PodcastIdentifiedHolder[] holderArray(List<Identified<Podcast>> items) {
        return items
                .stream()
                .map(PodcastIdentifiedHolder::new)
                .toArray(PodcastIdentifiedHolder[]::new);
    }

    @Override
    protected void onItemListServiceResponseFailed() {
        Log.e(TAG, "Error Loading Podcasts");
    }

    @Override
    protected final void subscribeToAttachmentObservable2(
            Observable<
                    Attachment<
                            PodcastListFragment2,
                            PodcastListFragmentListener
                            >
                    > attachmentObservable
    ) {
        // nothing to do
    }
}
