package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListValueFragment;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import io.reactivex.Completable;
import io.reactivex.Observable;

// Next, I should consume it in the MainFragment as well
public final class PodcastListValueFragment2 extends RxListValueFragment<
        PodcastListValueFragment2,
        PodcastListValueFragment2.PodcastListFragmentListener,
        PodcastListValueFragment2.Attachment,
        PodcastListValueFragment2.Attachment.Factory,
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
            PodcastListValueFragment2,
            PodcastListValueFragment2.PodcastListFragmentListener,
            Attachment,
            Attachment.Factory> {
        public interface Factory extends RxListValueFragment.Attachment.Factory<
                PodcastListValueFragment2,
                PodcastListValueFragment2.PodcastListFragmentListener,
                Attachment,
                Attachment.Factory> {
        }

        private Attachment(
                Class<Attachment> attachmentClass,
                PodcastListValueFragment2 fragment,
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
        void onPodcastListFragmentAttached(PodcastListValueFragment2 podcastListFragment);

        Observable<PodcastIdentifiedListServiceResponse> podcastIdentifiedsServiceResponseObservable(
                PodcastListValueFragment2 podcastListFragment
        );

        void onClickPodcastIdentified(
                PodcastListValueFragment2 podcastListFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastListFragmentWillDetach(PodcastListValueFragment2 podcastListFragment);
    }

    private static final String TAG = "PodcastList";

    public PodcastListValueFragment2() {
        super(
                PodcastListValueFragment2.class,
                PodcastListFragmentListener.class,
                Attachment.class,
                PodcastListValueFragment2.Attachment::new,
                PodcastListFragmentListener::onPodcastListFragmentAttached,
                PodcastListFragmentListener::onPodcastListFragmentWillDetach,
                R.layout.fragment_podcast_list_2,
                TAG,
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
    protected View findEmptyItemsFailedView(View view) {
        return view.requireViewById(
                R.id.fragment_podcast_list_empty_error_text_view
        );
    }

    @Override
    protected View findNonEmptyItemsFailedView(View view) {
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
