package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.NonnullPair;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.services.ServiceRequestState;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastSearchFragment extends Fragment
        implements
        PodcastListFragment.PodcastListFragmentListener {
    private static final String TAG = "PodcastSearch";
    private static final String QUERY_KEY = "query";

    private final DelegatingIdlingResource podcastListDelegatingIdlingResource =
            DelegatingIdlingResource.expectingInnerIdlingResource("podcastList");
    private final BehaviorSubject<Optional<String>> queryOptionalBehaviorSubject =
            BehaviorSubject.create();

    @Nullable
    private PodcastSearchFragmentListener listener;

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

        final PodcastSearchFragmentListener listener = FragmentUtil.requireContextFragmentListener(
                context,
                PodcastSearchFragmentListener.class
        );
        this.listener = listener;
        listener.onPodcastSearchFragmentAttached(this);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                queryOptionalBehaviorSubject.onNext(Optional.of(query));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setQueryHint(requireContext().getString(R.string.fragment_podcast_search_query_hint));
        @Nullable final CharSequence query;
        if (savedInstanceState == null) {
            query = null;
        } else {
            query = savedInstanceState.getCharSequence(QUERY_KEY);
        }
        searchView.setQuery(query, false);
        queryOptionalBehaviorSubject.onNext(
                Optional.ofNullable(query).map(CharSequence::toString)
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final SearchView searchView = requireView().requireViewById(R.id.fragment_podcast_search_search_view);
        @Nullable final CharSequence queryCharSequence = searchView.getQuery();
        outState.putCharSequence(QUERY_KEY, queryCharSequence);

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        final PodcastSearchFragmentListener listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onPodcastSearchFragmentWillDetach(this);

        super.onDetach();
    }

    @Override
    public void onPodcastListFragmentListenerAttached(PodcastListFragment podcastListFragment) {
        podcastListDelegatingIdlingResource.setState(
                DelegatingIdlingResource.State.hasInnerIdlingResource(
                        podcastListFragment.getPodcastIdentifiedsIdlingResource()
                )
        );
    }

    @Override
    public Observable<List<Identified<Podcast>>> podcastIdentifiedsObservable(
            PodcastListFragment podcastListFragment
    ) {
        return queryOptionalBehaviorSubject
                .hide()
                .distinctUntilChanged()
                .flatMap(queryOptional -> {
                    @Nullable final String query = queryOptional.orElse(null);
                    Observable<List<Identified<Podcast>>> podcastIdentifiedsObservable;
                    if (query == null) {
                        podcastIdentifiedsObservable = Observable.just(Collections.emptyList());
                    } else {
                        podcastIdentifiedsObservable = Objects.requireNonNull(listener)
                                .searchForPodcasts(
                                        PodcastSearchFragment.this,
                                        new PodcastSearch(query)
                                ).flatMap(
                                        podcastIdentifiedsAndServiceRequestState ->
                                                podcastIdentifiedsAndServiceRequestState.second.reduce(
                                                        loading ->
                                                                Observable.just(Collections.emptyList()),
                                                        loaded ->
                                                                Observable.just(podcastIdentifiedsAndServiceRequestState.first),
                                                        localFailure -> {
                                                            Log.e(
                                                                    TAG,
                                                                    "Local error loading podcasts for query: " + query,
                                                                    localFailure.throwable
                                                            );
                                                            return Observable.error(localFailure.throwable);
                                                        },
                                                        remoteFailure -> {
                                                            Log.e(
                                                                    TAG,
                                                                    "Remote error loading podcasts for query: " + query,
                                                                    remoteFailure.throwable
                                                            );
                                                            return Observable.error(remoteFailure.throwable);
                                                        }

                                                )
                                );
                    }
                    return podcastIdentifiedsObservable;
                });
    }

    @Override
    public void onPodcastIdentifiedsError(
            PodcastListFragment podcastListFragment,
            Throwable throwable
    ) {
        Snackbar.make(
                requireView(),
                requireContext().getText(R.string.fragment_podcast_search_error),
                Snackbar.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onClick(
            PodcastListFragment podcastListFragment,
            Identified<Podcast> podcastIdentified
    ) {
        Objects.requireNonNull(listener).onClickPodcastIdentified(
                this,
                podcastIdentified
        );
    }

    @Override
    public void onPodcastListFragmentListenerWillDetach(PodcastListFragment podcastListFragment) {
        podcastListDelegatingIdlingResource.setState(
                DelegatingIdlingResource.State.notExpectingInnerIdlingResource()
        );
    }

    public IdlingResource getSearchResultPodcastIdentifiedsIdlingResource() {
        return podcastListDelegatingIdlingResource;
    }

    public interface PodcastSearchFragmentListener {
        void onPodcastSearchFragmentAttached(PodcastSearchFragment podcastSearchFragment);

        Observable<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> searchForPodcasts(
                PodcastSearchFragment podcastSearchFragment,
                PodcastSearch podcastSearch
        );

        void onClickPodcastIdentified(
                PodcastSearchFragment podcastSearchFragment,
                Identified<Podcast> podcastIdentified
        );

        void onPodcastSearchFragmentWillDetach(PodcastSearchFragment podcastSearchFragment);
    }
}
