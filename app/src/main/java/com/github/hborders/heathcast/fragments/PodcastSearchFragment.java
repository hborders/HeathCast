package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.android.FragmentUtil;

import javax.annotation.Nullable;

import io.reactivex.disposables.Disposable;

public final class PodcastSearchFragment extends Fragment implements PodcastListFragment.PodcastListFragmentListener {
    private static final String TAG = "PodcastSearch";
    private static final String PODCAST_LIST_FRAGMENT_TAG = "podcastList";

    @Nullable
    private PodcastSearchFragmentListener listener;
    @Nullable
    private Disposable podcastSearchDisposable;

    public PodcastSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PodcastSearchFragment.
     */
    public static PodcastSearchFragment newInstance() {
        PodcastSearchFragment fragment = new PodcastSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @Nullable
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        @Nullable final View view = inflater.inflate(
                R.layout.fragment_podcast_search,
                container,
                false
        );
        if (view != null) {
            final SearchView searchView = view.requireViewById(R.id.fragment_podcast_search_search_view);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    @Nullable final Disposable oldDisposable = podcastSearchDisposable;
                    if (oldDisposable != null) {
                        oldDisposable.dispose();
                    }
//                    podcastSearchDisposable = PodcastService
//                            .getInstance(inflater.getContext())
//                            .searchForPodcasts(query)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(
//                                    podcasts -> {
//                                        @Nullable final PodcastListFragment existingPodcastListFragment =
//                                                (PodcastListFragment) getChildFragmentManager()
//                                                        .findFragmentByTag(PODCAST_LIST_FRAGMENT_TAG);
//                                        final FragmentTransaction fragmentTransaction =
//                                                getChildFragmentManager()
//                                                        .beginTransaction();
//                                        if (existingPodcastListFragment != null) {
//                                            fragmentTransaction
//                                                    .remove(existingPodcastListFragment);
//                                        }
//                                        fragmentTransaction
//                                                .add(
//                                                        R.id.fragment_podcast_search_podcast_list_fragment_container_frame_layout,
//                                                        PodcastListFragment.newInstance(
//                                                                podcasts
//                                                        ),
//                                                        PODCAST_LIST_FRAGMENT_TAG
//                                                )
//                                                .commit();
//                                    },
//                                    throwable -> {
//                                        Snackbar.make(
//                                                searchView,
//                                                "Error when searching iTunes",
//                                                Snackbar.LENGTH_SHORT
//                                        );
//                                        Log.e(
//                                                TAG,
//                                                "Error when searching iTunes",
//                                                throwable
//                                        );
//                                    }
//                            );

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
            searchView.setQueryHint(inflater.getContext().getString(R.string.search_query_hint));


        }
        return view;
    }

    @Override
    public void onDestroyView() {
        @Nullable final Disposable podcastSearchDisposable = this.podcastSearchDisposable;
        if (podcastSearchDisposable != null) {
            podcastSearchDisposable.dispose();
        }

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = FragmentUtil.requireContextFragmentListener(
                context,
                PodcastSearchFragmentListener.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onClick(Identified<Podcast> identifiedPodcast) {
        @Nullable final View view = getView();
        if (view != null) {
            Navigation
                    .findNavController(view)
                    .navigate(
                            R.id.action_podcastSearchFragment_to_podcastFragment,
                            PodcastFragment.newArguments(identifiedPodcast)
                    );
        }
    }


    public interface PodcastSearchFragmentListener {
    }
}
