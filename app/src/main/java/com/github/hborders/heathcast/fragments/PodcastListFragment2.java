package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListFragment;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import io.reactivex.Completable;
import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public final class PodcastListFragment2 extends RxListFragment<
        PodcastListFragment2,
        PodcastListFragment2.PodcastListFragmentListener,
        PodcastListFragment2.Attachment,
        PodcastListFragment2.Attachment.Factory,
        PodcastIdentified,
        PodcastIdentifiedHolder,
        PodcastIdentifiedList,
        PodcastIdentifiedList.CapacityFactory,
        PodcastRecyclerViewAdapter,
        PodcastRecyclerViewAdapter.PodcastViewHolder,
        PodcastIdentifiedListServiceResponse,
        PodcastIdentifiedListServiceResponse.Loading,
        PodcastIdentifiedListServiceResponse.Complete,
        PodcastIdentifiedListServiceResponse.Failed
        > {
    public static final class Attachment extends RxFragment.Attachment<
            PodcastListFragment2,
            PodcastListFragment2.PodcastListFragmentListener,
            Attachment,
            Attachment.Factory> {
        public interface Factory extends RxListFragment.Attachment.Factory<
                PodcastListFragment2,
                PodcastListFragment2.PodcastListFragmentListener,
                Attachment,
                Attachment.Factory> {
        }

        private Attachment(
                Class<Attachment> attachmentClass,
                PodcastListFragment2 fragment,
                Context context,
                PodcastListFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    attachmentClass,
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

    public interface PodcastListFragmentListener {
        void onPodcastListFragmentListenerAttached(PodcastListFragment2 podcastListFragment);

        Observable<PodcastIdentifiedListServiceResponse> podcastIdentifiedsServiceResponseObservable(
                PodcastListFragment2 podcastListFragment
        );

        void onClickPodcastIdentified(
                PodcastListFragment2 podcastListFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentListenerWillDetach(PodcastListFragment2 podcastListFragment);
    }

    private static final String TAG = "PodcastList";

    public PodcastListFragment2() {
        super(
                PodcastListFragment2.class,
                PodcastListFragmentListener.class,
                Attachment.class,
                PodcastListFragment2.Attachment::new,
                PodcastListFragmentListener::onPodcastListFragmentListenerAttached,
                PodcastListFragmentListener::onPodcastListFragmentListenerWillDetach,
                R.layout.fragment_podcast_list_2,
                PodcastIdentifiedList::new,
                PodcastListFragmentListener::podcastIdentifiedsServiceResponseObservable,
                PodcastIdentifiedHolder.class
        );
    }

    @Override
    protected View findEmptyItemsLoadingView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_empty_loading_group
        );
    }

    @Override
    protected View findNonEmptyItemsLoadingView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_non_empty_loading_group
        );
    }

    @Override
    protected View findEmptyItemsCompleteView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_empty_complete_text_view
        );
    }

    @Override
    protected View findEmptyItemsErrorView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_empty_error_text_view
        );
    }

    @Override
    protected View findNonEmptyItemsErrorView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_non_empty_error_text_view
        );
    }

    @Override
    protected RecyclerView requireRecyclerView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_podcasts_recycler_view
        );
    }

    @Override
    protected PodcastRecyclerViewAdapter createListRecyclerViewAdapter(
            PodcastIdentifiedList initialItems,
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
}
