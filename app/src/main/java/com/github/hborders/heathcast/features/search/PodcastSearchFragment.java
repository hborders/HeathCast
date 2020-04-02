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
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListState;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseComplete.PodcastIdentifiedListServiceResponseCompleteFactory;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseFailed.PodcastIdentifiedListServiceResponseFailedFactory;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse.PodcastIdentifiedListServiceResponseLoading.PodcastIdentifiedListServiceResponseLoadingFactory;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public abstract class PodcastSearchFragment<
        PodcastSearchFragmentType extends PodcastSearchFragment<
                PodcastSearchFragmentType,
                PodcastSearchFragmentListenerType
                >,
        PodcastSearchFragmentListenerType extends PodcastSearchFragment.PodcastSearchFragmentListener<
                PodcastSearchFragmentType,
                PodcastSearchFragmentListenerType
                >
        > extends Fragment
        implements
        PodcastSearchPodcastListFragment.PodcastSearchPodcastListFragmentListener {

    public interface PodcastSearchFragmentListener<
            PodcastSearchFragmentType extends PodcastSearchFragment<
                    PodcastSearchFragmentType,
                    PodcastSearchFragmentListenerType
                    >,
            PodcastSearchFragmentListenerType extends PodcastSearchFragment.PodcastSearchFragmentListener<
                    PodcastSearchFragmentType,
                    PodcastSearchFragmentListenerType
                    >
            > {
        void onPodcastSearchFragmentAttached(PodcastSearchFragmentType podcastSearchFragment);

        Observable<PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse> searchForPodcasts2(
                PodcastSearchFragmentType podcastSearchFragment,
                PodcastSearch podcastSearch,
                PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory,
                PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory,
                PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory failedFactory
        );

        void onClickPodcastIdentified(
                PodcastSearchFragmentType podcastSearchFragment,
                PodcastIdentified podcastIdentified
        );

        void onPodcastSearchFragmentWillDetach(PodcastSearchFragmentType podcastSearchFragment);
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory
            extends PodcastIdentifiedListServiceResponseLoadingFactory<
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory
            extends PodcastIdentifiedListServiceResponseCompleteFactory<
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
    }

    public interface PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory
            extends PodcastIdentifiedListServiceResponseFailedFactory<
            PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse,
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed
            > {
    }

    protected interface PodcastSearchPodcastListStateFactory {
        PodcastSearchPodcastIdentifiedListState newPodcastSearchPodcastListState(
                boolean enabled,
                PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse value
        );
    }

    private static final String TAG = "PodcastSearch";
    private static final String QUERY_KEY = "query";
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Class<PodcastSearchFragmentType> selfClass;
    private final Class<PodcastSearchFragmentListenerType> listenerClass;
    private final PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory;
    private final PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory;
    private final PodcastSearchPodcastListStateFactory podcastSearchPodcastListStateFactory;

    @Nullable
    private PodcastSearchFragmentListenerType listener;

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
    private final Observable<Optional<PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse>> podcastSearchPodcastIdentifiedListServiceResponseOptionalObservable;
    private final BehaviorSubject<PodcastSearchPodcastIdentifiedListState> podcastIdentifiedListStateBehaviorSubject =
            BehaviorSubject.create();
    @Nullable
    private Disposable podcastIdentifiedListServiceResponseOptionalDisposable;

    protected PodcastSearchFragment(
            Class<PodcastSearchFragmentType> selfClass,
            Class<PodcastSearchFragmentListenerType> listenerClass,
            PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory failedFactory,
            PodcastSearchPodcastListStateFactory podcastSearchPodcastListStateFactory
    ) {
        this.selfClass = selfClass;
        this.listenerClass = listenerClass;
        this.loadingFactory = loadingFactory;
        this.completeFactory = completeFactory;
        this.podcastSearchPodcastListStateFactory = podcastSearchPodcastListStateFactory;

        podcastSearchPodcastIdentifiedListServiceResponseOptionalObservable =
                queryOptionalPublishSubject.switchMap(
                        queryOptional -> queryOptional.map(
                                query -> Objects.requireNonNull(listener)
                                        .searchForPodcasts2(
                                                Objects.requireNonNull(selfClass.cast(this)),
                                                new PodcastSearch(query),
                                                loadingFactory,
                                                completeFactory,
                                                failedFactory
                                        )
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .map(Optional::of)
                        ).orElse(Observable.just(Optional.empty()))
                );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final PodcastSearchFragmentListenerType listener = FragmentUtil.requireContextFragmentListener(
                context,
                listenerClass
        );
        this.listener = listener;
        listener.onPodcastSearchFragmentAttached(Objects.requireNonNull(selfClass.cast(this)));
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
                handler.post(() ->{
                    System.out.println("Immediately after getting event");
                });
                searchView.clearFocus();
                // since the queryOptionalPublishSubject chain is asynchronous,
                // we must disable the podcast list until we get a response back
                // otherwise, we won't have a new state, and the IdlingResources won't update properly
                podcastIdentifiedListStateBehaviorSubject.onNext(
                        podcastSearchPodcastListStateFactory.newPodcastSearchPodcastListState(
                                false,
                                loadingFactory.newPodcastIdentifiedListServiceResponseLoading(
                                        new PodcastIdentifiedList()
                                )
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

        podcastIdentifiedListServiceResponseOptionalDisposable =
                podcastSearchPodcastIdentifiedListServiceResponseOptionalObservable
                        .map(
                                podcastSearchPodcastIdentifiedListServiceResponseOptional ->
                                        podcastSearchPodcastIdentifiedListServiceResponseOptional.map(
                                                podcastSearchPodcastIdentifiedListServiceResponse ->
                                                        podcastSearchPodcastListStateFactory.newPodcastSearchPodcastListState(
                                                                true,
                                                                podcastSearchPodcastIdentifiedListServiceResponse
                                                        )
                                        ).orElseGet(
                                                () ->
                                                        podcastSearchPodcastListStateFactory.newPodcastSearchPodcastListState(
                                                                false,
                                                                completeFactory.newPodcastIdentifiedListServiceResponseComplete(
                                                                        new PodcastIdentifiedList()
                                                                )
                                                        )
                                        )
                        )
                        .subscribe(
                                podcastIdentifiedListStateBehaviorSubject::onNext
                        );
        podcastIdentifiedListStateBehaviorSubject.onNext(
                podcastSearchPodcastListStateFactory.newPodcastSearchPodcastListState(
                        true,
                        completeFactory.newPodcastIdentifiedListServiceResponseComplete(
                                new PodcastIdentifiedList()
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
        final PodcastSearchFragmentListenerType listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onPodcastSearchFragmentWillDetach(Objects.requireNonNull(selfClass.cast(this)));

        super.onDetach();
    }

    // PodcastListFragmentListener

    @Override
    public void onPodcastListFragmentAttached(PodcastSearchPodcastListFragment podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getLoadingIdlingResource()
        );
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                podcastListFragment.getCompleteOrFailedIdlingResource()
        );
    }

    @Override
    public Observable<PodcastSearchPodcastIdentifiedListState> podcastIdentifiedListStateObservable(
            PodcastSearchPodcastListFragment podcastListValueFragment
    ) {
        return podcastIdentifiedListStateBehaviorSubject;
    }

    @Override
    public void onClickPodcastIdentified(
            PodcastSearchPodcastListFragment podcastListFragment,
            PodcastIdentified podcastIdentified
    ) {
        Objects.requireNonNull(listener).onClickPodcastIdentified(
                Objects.requireNonNull(selfClass.cast(this)),
                podcastIdentified
        );
    }

    @Override
    public void onPodcastListFragmentWillDetach(PodcastSearchPodcastListFragment podcastListFragment) {
        searchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(null);
        searchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(null);
    }

    public IdlingResource getSearchResultPodcastListLoadingIdlingResource() {
        return searchResultPodcastListLoadingDelegatingIdlingResource;
    }

    public IdlingResource getSearchResultPodcastListCompleteOrErrorIdlingResource() {
        return searchResultPodcastListCompleteOrErrorDelegatingIdlingResource;
    }
}
