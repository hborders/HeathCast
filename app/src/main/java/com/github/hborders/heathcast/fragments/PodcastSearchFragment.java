package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.services.PodcastService;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastSearchFragment extends Fragment implements PodcastListFragment.PodcastListFragmentListener {
    private static final String TAG = "PodcastSearch";
    private static final String PODCAST_LIST_FRAGMENT_TAG = "podcastList";

    @Nullable
    private PodcastSearchFragmentListener listener;
    private BehaviorSubject<List<Identified<Podcast>>> podcastIdentifiedsBehaviorSubject =
            BehaviorSubject.create();
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
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = FragmentUtil.requireContextFragmentListener(
                context,
                PodcastSearchFragmentListener.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_podcast_search,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SearchView searchView = view.requireViewById(R.id.fragment_podcast_search_search_view);
        final PodcastListFragment podcastListFragment = Objects.requireNonNull(
                (PodcastListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_podcast_search_podcast_list_fragment)
        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                @Nullable final Disposable oldDisposable = podcastSearchDisposable;
                podcastIdentifiedsBehaviorSubject.onNext(Collections.emptyList());
                if (oldDisposable != null) {
                    oldDisposable.dispose();
                }

                @Nullable final PodcastSearchFragmentListener listener = PodcastSearchFragment.this.listener;
                if (listener != null) {
                    podcastSearchDisposable =
                            listener
                                    .podcastService()
                                    .searchForPodcasts(new PodcastSearch(query))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            podcastIdentifiedsAndServiceRequestState ->
                                                    podcastIdentifiedsBehaviorSubject.onNext(
                                                            podcastIdentifiedsAndServiceRequestState.first
                                                    ),
                                            throwable -> {
                                                Snackbar.make(
                                                        searchView,
                                                        requireContext().getText(R.string.podcast_search_error),
                                                        Snackbar.LENGTH_SHORT
                                                ).show();
                                                Log.e(
                                                        TAG,
                                                        "Error when searching iTunes",
                                                        throwable
                                                );
                                            }
                                    );
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setQueryHint(requireContext().getString(R.string.search_query_hint));
    }

    @Override
    public void onStop() {
        @Nullable final Disposable podcastSearchDisposable = this.podcastSearchDisposable;
        if (podcastSearchDisposable != null) {
            podcastSearchDisposable.dispose();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @Override
    public Observable<List<Identified<Podcast>>> podcastObservable() {
        return podcastIdentifiedsBehaviorSubject;
    }

    public interface PodcastSearchFragmentListener {
        PodcastService podcastService();
    }
}
