package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.AsyncValue;
import com.github.hborders.heathcast.core.NonnullPair;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.services.ServiceRequestState;
import com.github.hborders.heathcast.services.ServiceResponse;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastSearchFragment extends Fragment
        implements
        PodcastListFragment2.PodcastListFragmentListener {
    private static final String TAG = "PodcastSearch";
    private static final String QUERY_KEY = "query";


    @Nullable
    private PodcastSearchFragmentListener listener;

    private final BehaviorSubject<Optional<PodcastListFragment2>> searchResultPodcastListFragmentOptionalBehaviorSubject =
            BehaviorSubject.create();
    // Bookmark - This fundamentally doesn't work.
    // I thought I could use AndIdlingResource to combine
    // the search state with the podcast list state
    // but that doesn't work because I'm not guaranteed to get
    // another signal from the podcast list when the next results
    // come through
    // Instead, I should expose an Observable for the podcast list's
    // podcast identifieds.
    // This fragment can combine that observable with its search state
    // and publish a new state to say whether it's searching or whether
    // it has search results.
    // Later, I'll expose different state to say whether we're searching locally
    // or remotely.
    // We'll remove IdlingResource from the app directly, and just
    // make custom adapters that adapt the Rx state.
    private final BehaviorSubject<Optional<String>> queryOptionalBehaviorSubject =
            BehaviorSubject.create();
    // We can't use a ConnectableObservable here (via Observable#replay) because every
    // ConnectableObservable#connect call resets all state and throws away past subscriptions.
    // Instead we have to manage our own BehaviorSubject manually.
    // See com.github.hborders.heathcast.reactivexdemo.ConnectableObservableTest
    private final Observable<Optional<ServiceResponse<PodcastIdentifiedList>>> podcastIdentifiedListServiceResponseOptionalObservable =
            queryOptionalBehaviorSubject.switchMap(
                    queryOptional -> queryOptional.map(
                            query -> Objects.requireNonNull(listener)
                                    .searchForPodcasts2(
                                            PodcastSearchFragment.this,
                                            new PodcastSearch(query)
                                    ).map(Optional::of)
                    ).orElse(Observable.just(Optional.empty()))
            );
    private final BehaviorSubject<NonnullPair<AsyncValue<PodcastIdentifiedList>, Boolean>> podcastIdentifiedListAsyncValueAndSearchingBehaviorSubject =
            BehaviorSubject.create();
    @Nullable
    private Disposable podcastIdentifiedListServiceResponseOptionalDisposable;

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
        if (savedInstanceState != null) {
            @Nullable final CharSequence query = savedInstanceState.getCharSequence(QUERY_KEY);
            searchView.setQuery(query, false);
            queryOptionalBehaviorSubject.onNext(
                    Optional.ofNullable(query).map(CharSequence::toString)
            );
        }

        podcastIdentifiedListServiceResponseOptionalDisposable =
                podcastIdentifiedListServiceResponseOptionalObservable.subscribe(
                        podcastIdentifiedListServiceResponseOptional ->
                                podcastIdentifiedListAsyncValueAndSearchingBehaviorSubject.onNext(
                                        podcastIdentifiedListServiceResponseOptional
                                                .map(
                                                        podcastIdentifiedListServiceResponse ->
                                                                podcastIdentifiedListServiceResponse.remoteStatus.reduce(
                                                                        loading ->
                                                                                new NonnullPair<>(
                                                                                        podcastIdentifiedListServiceResponse.value.isEmpty() ?
                                                                                                AsyncValue.loading(PodcastIdentifiedList.class) :
                                                                                                AsyncValue.loaded(podcastIdentifiedListServiceResponse.value),
                                                                                        true
                                                                                ),
                                                                        failed ->
                                                                                new NonnullPair<>(
                                                                                        podcastIdentifiedListServiceResponse.value.isEmpty() ?
                                                                                                AsyncValue.failed(
                                                                                                        PodcastIdentifiedList.class,
                                                                                                        failed.throwable
                                                                                                ) :
                                                                                                AsyncValue.loadedButUpdateFailed(
                                                                                                        podcastIdentifiedListServiceResponse.value,
                                                                                                        failed.throwable
                                                                                                ),
                                                                                        false
                                                                                ),
                                                                        complete ->
                                                                                new NonnullPair<>(
                                                                                        AsyncValue.loaded(podcastIdentifiedListServiceResponse.value),
                                                                                        false
                                                                                )
                                                                )
                                                )
                                                .orElse(
                                                        new NonnullPair<>(
                                                                AsyncValue.loaded(new PodcastIdentifiedList(Collections.emptyList())),
                                                                false
                                                        )
                                                )
                                )
                );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final SearchView searchView = requireView().requireViewById(R.id.fragment_podcast_search_search_view);
        @Nullable final CharSequence queryCharSequence = searchView.getQuery();
        outState.putCharSequence(
                QUERY_KEY,
                queryCharSequence
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        queryOptionalBehaviorSubject.onNext(Optional.empty());

        @Nullable final Disposable podcastIdentifiedListServiceResponseOptionalDisposable =
                this.podcastIdentifiedListServiceResponseOptionalDisposable;
        this.podcastIdentifiedListServiceResponseOptionalDisposable = null;
        if (podcastIdentifiedListServiceResponseOptionalDisposable != null) {
            podcastIdentifiedListServiceResponseOptionalDisposable.dispose();
        }
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
    public void onPodcastListFragmentListenerAttached(PodcastListFragment2 podcastListFragment) {
        searchResultPodcastListFragmentOptionalBehaviorSubject.onNext(
                Optional.of(podcastListFragment)
        );
    }

    @Override
    public Observable<AsyncValue<PodcastIdentifiedList>> podcastIdentifiedsAsyncValueObservable(
            PodcastListFragment2 podcastListFragment
    ) {
        return podcastIdentifiedListAsyncValueAndSearchingBehaviorSubject.map(
                NonnullPair::getFirst
        );
    }

    @Override
    public void onPodcastIdentifiedsError(
            PodcastListFragment2 podcastListFragment,
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
            PodcastListFragment2 podcastListFragment,
            Identified<Podcast> podcastIdentified
    ) {
        Objects.requireNonNull(listener).onClickPodcastIdentified(
                this,
                podcastIdentified
        );
    }

    @Override
    public void onPodcastListFragmentListenerWillDetach(PodcastListFragment2 podcastListFragment) {
        searchResultPodcastListFragmentOptionalBehaviorSubject.onNext(
                Optional.empty()
        );
    }

//    public void enableSearchResultItemRangeObservable() {
//        searchResultItemRangeEnabled = true;
//        @Nullable final PodcastListFragment searchResultPodcastListFragment =
//                this.searchResultPodcastListFragment;
//        if (searchResultPodcastListFragment != null) {
//            searchResultPodcastListFragment.enableItemRangeObservable();
//        }
//    }

    public Observable<Optional<ItemRange>> getSearchResultItemRangeOptionalObservable() {
        return searchResultPodcastListFragmentOptionalBehaviorSubject.switchMap(
                searchResultPodcastListFragmentOptional ->
                        searchResultPodcastListFragmentOptional.map(
                                PodcastListFragment2::getItemRangeOptionalObservable
                        ).orElse(Observable.empty())
        );
    }


    public Observable<Boolean> getSearchingObservable() {
        return podcastIdentifiedListAsyncValueAndSearchingBehaviorSubject.map(
                NonnullPair::getSecond
        );
    }

    public interface PodcastSearchFragmentListener {
        void onPodcastSearchFragmentAttached(PodcastSearchFragment podcastSearchFragment);

        Observable<NonnullPair<List<Identified<Podcast>>, ServiceRequestState>> searchForPodcasts(
                PodcastSearchFragment podcastSearchFragment,
                PodcastSearch podcastSearch
        );

        Observable<ServiceResponse<PodcastIdentifiedList>> searchForPodcasts2(
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
