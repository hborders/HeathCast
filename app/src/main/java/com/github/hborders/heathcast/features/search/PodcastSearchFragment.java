package com.github.hborders.heathcast.features.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.services.PodcastListServiceResponse;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public final class PodcastSearchFragment extends Fragment
        implements PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener {

    public interface PodcastSearchFragmentListener  {
        void onPodcastSearchFragmentAttached(PodcastSearchFragment podcastSearchFragment);

        Observable<PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState> searchForPodcasts2(
                PodcastSearchFragment podcastSearchFragment,
                PodcastSearchImpl podcastSearch,
                PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory,
                PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory,
                PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory failedFactory
        );

        void onClickPodcastIdentified(
                PodcastSearchFragment podcastSearchFragment,
                PodcastImpl.PodcastIdentifiedImpl podcastIdentified
        );

        void onPodcastSearchFragmentWillDetach(PodcastSearchFragment podcastSearchFragment);
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory
            extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory
            extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory
            extends PodcastListServiceResponse.PodcastListServiceResponseFactory<
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete,
            PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl
            > {
    }

    private static final String TAG = "PodcastSearch";
    private static final String QUERY_KEY = "query";
    private final Handler handler = new Handler(Looper.getMainLooper());

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
    private final Observable<Optional<PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState>> podcastSearchSearchPodcastListAsyncValueStateOptionalObservable;
    private final BehaviorSubject<PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState> podcastSearchSearchPodcastListAsyncValueStateBehaviorSubject =
            BehaviorSubject.create();
    @Nullable
    private Disposable podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable;

    public PodcastSearchFragment() {
        podcastSearchSearchPodcastListAsyncValueStateOptionalObservable =
                queryOptionalPublishSubject.switchMap(
                        queryOptional -> queryOptional.map(
                                query ->
                                        Objects.requireNonNull(listener)
                                                .searchForPodcasts2(
                                                        this,
                                                        new PodcastSearchImpl(query),
                                                        PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading::new,
                                                        PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete::new,
                                                        PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateFailed::new
                                                )
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map(Optional::of)
                        ).orElse(Observable.just(Optional.empty()))
                );
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
                handler.post(() -> {
                    System.out.println("Immediately after getting event");
                });
                searchView.clearFocus();
                // since the queryOptionalPublishSubject chain is asynchronous,
                // we must disable the podcast list until we get a response back
                // otherwise, we won't have a new state, and the IdlingResources won't update properly
                podcastSearchSearchPodcastListAsyncValueStateBehaviorSubject.onNext(
                        new PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateLoading(
                                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl()
                        )
                );
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

        podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable =
                podcastSearchSearchPodcastListAsyncValueStateOptionalObservable
                        .map(
                                podcastSearchSearchPodcastListAsyncValueStateOptional ->
                                        podcastSearchSearchPodcastListAsyncValueStateOptional.orElseGet(
                                                () ->
                                                        new PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete(
                                                                new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl()
                                                        )
                                        )
                        )
                        .subscribe(
                                podcastSearchSearchPodcastListAsyncValueStateBehaviorSubject::onNext
                        );
        podcastSearchSearchPodcastListAsyncValueStateBehaviorSubject.onNext(
                new PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueStateComplete(
                        new PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl()
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

        @Nullable final Disposable podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable =
                this.podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable;
        this.podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable = null;
        if (podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable != null) {
            podcastSearchSearchPodcastListAsyncValueStateOptionalDisposable.dispose();
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

    // PodcastSearchPodcastListFragmentListener

    @Override
    public void onPodcastSearchPodcastListFragmentAttached(PodcastSearchPodcastListFragment podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getLoadingIdlingResource()
        );
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getCompleteOrFailedIdlingResource()
        );
    }

    @Override
    public Observable<PodcastSearchPodcastListFragment.PodcastSearchPodcastListAsyncValueState> podcastSearchPodcastListAsyncValueStateObservable(
            PodcastSearchPodcastListFragment podcastListValueFragment
    ) {
        return podcastSearchSearchPodcastListAsyncValueStateBehaviorSubject;
    }

    @Override
    public void onPodcastSearchPodcastListFragmentClickedPodcastIdentified(
            PodcastSearchPodcastListFragment podcastListFragment,
            PodcastImpl.PodcastIdentifiedImpl podcastIdentified
    ) {
        Objects.requireNonNull(listener).onClickPodcastIdentified(
                this,
                podcastIdentified
        );
    }

    @Override
    public void onPodcastSearchPodcastListFragmentWillDetach(PodcastSearchPodcastListFragment podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(null);
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(null);
    }

    // Public API

    public IdlingResource getSearchResultPodcastListLoadingIdlingResource() {
        return searchResultPodcastListLoadingDelegatingIdlingResource;
    }

    public IdlingResource getSearchResultPodcastListCompleteOrErrorIdlingResource() {
        return searchResultPodcastListCompleteOrErrorDelegatingIdlingResource;
    }
}
