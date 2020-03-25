package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public final class PodcastSearchFragment extends Fragment
        implements
        PodcastListValueFragment2.PodcastListFragmentListener {
    private static final String TAG = "PodcastSearch";
    private static final String QUERY_KEY = "query";


    @Nullable
    private PodcastSearchFragmentListener listener;

    private final DelegatingIdlingResource searchResultPodcastListLoadingDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListLoading"
            );
    private final DelegatingIdlingResource searchResultPodcastListCompleteOrErrorDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListCompleteOrError"
            );

    // Need to refactor queryOptionalPublishSubject and  podcastIdentifiedListServiceResponseOptionalObservable
    // into a separate object so it can be easier to recreate on every onViewCreated onViewDestroyed pair


    private final PublishSubject<Optional<String>> queryOptionalPublishSubject =
            PublishSubject.create();
    // We can't use a ConnectableObservable here (via Observable#replay) because every
    // ConnectableObservable#connect call resets all state and throws away past subscriptions.
    // Instead we have to manage our own BehaviorSubject manually.
    // See com.github.hborders.heathcast.reactivexdemo.ConnectableObservableTest
    // Also, we need to reset this every time we create a view so that we can throw away
    // past PodcastIdentifiedLists. All that data is saved in the PodcastListFragment.
    private final Observable<Optional<PodcastIdentifiedListServiceResponse>> podcastIdentifiedListServiceResponseOptionalObservable =
            queryOptionalPublishSubject.switchMap(
                    queryOptional -> queryOptional.map(
                            query -> Objects.requireNonNull(listener)
                                    .searchForPodcasts2(
                                            PodcastSearchFragment.this,
                                            new PodcastSearch(query)
                                    ).map(Optional::of)
                    ).orElse(Observable.just(Optional.empty()))
            );
    private final BehaviorSubject<PodcastIdentifiedListServiceResponse> podcastIdentifiedListServiceResponseBehaviorSubject =
            BehaviorSubject.create();
    @Nullable
    private Disposable podcastIdentifiedListServiceResponseOptionalDisposable;

    public PodcastSearchFragment() {
        // Required empty public constructor
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
                queryOptionalPublishSubject.onNext(Optional.of(query));

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
            queryOptionalPublishSubject.onNext(
                    Optional.ofNullable(query).map(CharSequence::toString)
            );
        }

        podcastIdentifiedListServiceResponseOptionalDisposable =
                podcastIdentifiedListServiceResponseOptionalObservable.subscribe(
                        podcastIdentifiedListServiceResponseOptional ->
                                podcastIdentifiedListServiceResponseBehaviorSubject.onNext(
                                        podcastIdentifiedListServiceResponseOptional
                                                .orElse(
                                                        new PodcastIdentifiedListServiceResponse.Complete(
                                                                new PodcastIdentifiedList()
                                                        )
                                                )
                                )
                );
        podcastIdentifiedListServiceResponseBehaviorSubject.onNext(
                new PodcastIdentifiedListServiceResponse.Complete(
                        new PodcastIdentifiedList()
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

    // PodcastListFragmentListener

    @Override
    public void onPodcastListFragmentAttached(PodcastListValueFragment2 podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getLoadingIdlingResource()
        );
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getCompleteOrFailedIdlingResource()
        );
    }

    @Override
    public Observable<PodcastIdentifiedListServiceResponse> podcastIdentifiedsServiceResponseObservable(
            PodcastListValueFragment2 podcastListFragment
    ) {
        return podcastIdentifiedListServiceResponseBehaviorSubject;
    }

    @Override
    public void onClickPodcastIdentified(
            PodcastListValueFragment2 podcastListFragment,
            PodcastIdentified podcastIdentified
    ) {
        Objects.requireNonNull(listener).onClickPodcastIdentified(
                this,
                podcastIdentified
        );
    }

    @Override
    public void onPodcastListFragmentWillDetach(PodcastListValueFragment2 podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(null);
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(null);
    }

//    public void enableSearchResultItemRangeObservable() {
//        searchResultItemRangeEnabled = true;
//        @Nullable final PodcastListFragment searchResultPodcastListFragment =
//                this.searchResultPodcastListFragment;
//        if (searchResultPodcastListFragment != null) {
//            searchResultPodcastListFragment.enableItemRangeObservable();
//        }
//    }

    public IdlingResource getSearchResultPodcastListLoadingIdlingResource() {
        return searchResultPodcastListLoadingDelegatingIdlingResource;
    }

    public IdlingResource getSearchResultPodcastListCompleteOrErrorIdlingResource() {
        return searchResultPodcastListCompleteOrErrorDelegatingIdlingResource;
    }

    public interface PodcastSearchFragmentListener {
        void onPodcastSearchFragmentAttached(PodcastSearchFragment podcastSearchFragment);

        Observable<PodcastIdentifiedListServiceResponse> searchForPodcasts2(
                PodcastSearchFragment podcastSearchFragment,
                PodcastSearch podcastSearch
        );

        void onClickPodcastIdentified(
                PodcastSearchFragment podcastSearchFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastSearchFragmentWillDetach(PodcastSearchFragment podcastSearchFragment);
    }
}
