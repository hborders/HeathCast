package com.github.hborders.heathcast.features.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.dao.Database;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.github.hborders.heathcast.features.model.PodcastSearchImpl;
import com.github.hborders.heathcast.features.model.SubscriptionImpl;
import com.github.hborders.heathcast.fragments.PodcastFragment;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.services.NetworkPauser;
import com.github.hborders.heathcast.services.PodcastService;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class MainActivity extends AppCompatActivity
        implements
        MainFragment.MainFragmentListener,
        MainPodcastSearchFragment.MainPodcastSearchFragmentListener,
        PodcastFragment.PodcastFragmentListener {
    private final ConcurrentLinkedQueue<NetworkPauser> searchForPodcasts2NetworkPausers = new ConcurrentLinkedQueue<>();

    @VisibleForTesting
    public void setSearchForPodcasts2NetworkPausers(NetworkPauser... networkPausers) {
        searchForPodcasts2NetworkPausers.clear();
        searchForPodcasts2NetworkPausers.addAll(Arrays.asList(networkPausers));
    }

    @VisibleForTesting
    public void clearNetworkPausers() {
        searchForPodcasts2NetworkPausers.clear();
    }

    private final DelegatingIdlingResource podcastSearchResultPodcastListLoadingDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListLoading"
            );
    private final DelegatingIdlingResource podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListCompleteOrError"
            );

    private final PodcastService<
            EpisodeImpl,
            EpisodeImpl.EpisodeIdentifiedImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl,
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl,
            EpisodeImpl.EpisodeIdentifierImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl,
            EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl,
            EpisodeImpl.EpisodeListImpl,
            PodcastImpl,
            PodcastImpl.PodcastIdentifiedImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl,
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl,
            PodcastImpl.PodcastIdentifierImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl,
            PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl,
            PodcastImpl.PodcastListImpl,
            PodcastSearchImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl,
            PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl,
            PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl,
            SubscriptionImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl,
            SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedOptImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl,
            SubscriptionImpl.SubscriptionIdentifierImpl.SubscriptionIdentifierOptImpl
            > podcastService;

    public MainActivity() {
        podcastService = new PodcastService<>(
                EpisodeImpl::new,
                EpisodeImpl.EpisodeListImpl::new,
                EpisodeImpl.EpisodeListImpl::new,
                PodcastImpl::new,
                PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl::new,
                PodcastImpl.PodcastListImpl::new,
                new Database<>(
                        this,
                        null,
                        Schedulers.io(),
                        EpisodeImpl::new,
                        EpisodeImpl.EpisodeIdentifierImpl::new,
                        EpisodeImpl.EpisodeIdentifiedImpl::new,
                        EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl::new,
                        EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl::new,
                        EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl::new,
                        EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedSetImpl::new,
                        EpisodeImpl.EpisodeIdentifierImpl.EpisodeIdentifierOptImpl.EpisodeIdentifierOptListImpl::new,
                        PodcastImpl::new,
                        PodcastImpl.PodcastIdentifierImpl::new,
                        PodcastImpl.PodcastIdentifiedImpl::new,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl::new,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl::new,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedOptImpl::new,
                        PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl::new,
                        PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl::new,
                        PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedSetImpl::new,
                        PodcastImpl.PodcastIdentifierImpl.PodcastIdentifierOptImpl.PodcastIdentifierOptListImpl::new,
                        PodcastSearchImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifierImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifiedImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedListImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifiedImpl.PodcastSearchIdentifiedOptImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl::new,
                        PodcastSearchImpl.PodcastSearchIdentifierImpl.PodcastSearchIdentifierOptImpl::new,
                        SubscriptionImpl::new,
                        SubscriptionImpl.SubscriptionIdentifierImpl::new,
                        SubscriptionImpl.SubscriptionIdentifiedImpl::new,
                        SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedOptImpl::new,
                        SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedOptImpl::new,
                        SubscriptionImpl.SubscriptionIdentifierImpl.SubscriptionIdentifierOptImpl::new,
                        SubscriptionImpl.SubscriptionIdentifierImpl.SubscriptionIdentifierOptImpl::new,
                        SubscriptionImpl.SubscriptionIdentifiedImpl.SubscriptionIdentifiedListImpl::new
                ),
                Schedulers.io()
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(@Nullable Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private NavController requireNavController() {
        return Navigation.findNavController(
                this,
                R.id.activity_main_nav_host_fragment
        );
    }

    @Nullable
    private View findContentView() {
        return findViewById(android.R.id.content);
    }

    // MainFragment

    @Override
    public void onMainFragmentAttached(MainFragment mainFragment) {
    }

    @Override
    public void onClickSearch(MainFragment mainFragment) {
        requireNavController().navigate(
                R.id.action_mainFragment_to_podcastSearchFragment
        );
    }

    @Override
    public void onMainFragmentWillDetach(MainFragment mainFragment) {
    }

    // MainPodcastSearchFragmentListener

    @Override
    public void onPodcastSearchFragmentAttached(MainPodcastSearchFragment podcastSearchFragment) {
        podcastSearchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                podcastSearchFragment.getSearchResultPodcastListLoadingIdlingResource()
        );
        podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                podcastSearchFragment.getSearchResultPodcastListCompleteOrErrorIdlingResource()
        );
    }

    @Override
    public Observable<PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse> searchForPodcasts2(
            MainPodcastSearchFragment podcastSearchFragment,
            PodcastSearch podcastSearch,
            PodcastSearchPodcastIdentifiedListServiceResponseLoadingFactory loadingFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseCompleteFactory completeFactory,
            PodcastSearchPodcastIdentifiedListServiceResponseFailedFactory failedFactory
    ) {
        @Nullable NetworkPauser networkPauser = searchForPodcasts2NetworkPausers.poll();
        return podcastService.searchForPodcasts2(
                loadingFactory,
                completeFactory,
                failedFactory,
                networkPauser,
                podcastSearch
        );
    }

    @Override
    public void onClickPodcastIdentified(
            MainPodcastSearchFragment podcastSearchFragment,
            PodcastIdentified podcastIdentified
    ) {
        requireNavController().navigate(
                R.id.action_podcastSearchFragment_to_podcastFragment,
                PodcastFragment.newArguments(podcastIdentified)
        );
    }

    @Override
    public void onPodcastSearchFragmentWillDetach(MainPodcastSearchFragment podcastSearchFragment) {
        podcastSearchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                null
        );
        podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                null
        );
    }

    // PodcastFragmentListener

    @Override
    public void onPodcastFragmentAttached(PodcastFragment podcastFragment) {
    }

    @Override
    public Single<EpisodeIdentifiedList> fetchEpisodes(
            PodcastFragment podcastFragment,
            URL url
    ) {
        return podcastService.fetchEpisodes(url);
    }

    @Override
    public Observable<PodcastIdentifiedOpt> observeQueryForPodcastIdentified(
            PodcastFragment podcastFragment,
            PodcastIdentifier podcastIdentifier
    ) {
        return podcastService.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    @Override
    public Observable<Optional<SubscriptionIdentifier>> observeQueryForSubscriptionIdentifier(
            PodcastFragment podcastFragment,
            PodcastIdentifier podcastIdentifier
    ) {
        return podcastService.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    @Override
    public void requestedSubscribe(
            PodcastFragment podcastFragment,
            PodcastIdentifier podcastIdentifier
    ) {
        podcastService
                .subscribe(podcastIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SubscriptionIdentifierOpt>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   // don't dispose this should be fast, and disposal is pointless anyway
                               }

                               @Override
                               public void onSuccess(SubscriptionIdentifierOpt subscriptionIdentifierOptional) {
                                   @Nullable final View contentView = findContentView();
                                   if (contentView != null) {
                                       Snackbar.make(
                                               contentView,
                                               subscriptionIdentifierOptional.isPresent() ?
                                                       R.string.fragment_podcast_subscribe_success :
                                                       R.string.fragment_podcast_subscribe_failure,
                                               Snackbar.LENGTH_LONG
                                       ).show();
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   @Nullable final View contentView = findContentView();
                                   if (contentView != null) {
                                       Snackbar.make(
                                               contentView,
                                               R.string.fragment_podcast_subscribe_failure,
                                               Snackbar.LENGTH_LONG
                                       ).show();
                                   }
                               }
                           }
                );
    }

    @Override
    public void requestedUnsubscribe(
            PodcastFragment podcastFragment,
            SubscriptionIdentifier subscriptionIdentifier
    ) {
        podcastService
                .unsubscribe(subscriptionIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   // don't dispose this should be fast, and disposal is pointless anyway
                               }

                               @Override
                               public void onSuccess(Result result) {
                                   @Nullable final View contentView = findContentView();
                                   if (contentView != null) {
                                       Snackbar.make(
                                               contentView,
                                               result.<Integer>reduce(
                                                       success -> R.string.fragment_podcast_unsubscribe_success,
                                                       failure -> R.string.fragment_podcast_unsubscribe_failure
                                               ),
                                               Snackbar.LENGTH_LONG
                                       ).show();
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   @Nullable final View contentView = findContentView();
                                   if (contentView != null) {
                                       Snackbar.make(
                                               contentView,
                                               R.string.fragment_podcast_unsubscribe_failure,
                                               Snackbar.LENGTH_LONG
                                       ).show();
                                   }
                               }
                           }
                );
    }

    @Override
    public void onPodcastFragmentWillDetach(PodcastFragment podcastFragment) {
    }

    public IdlingResource getPodcastSearchResultPodcastListLoadingIdlingResource() {
        return podcastSearchResultPodcastListLoadingDelegatingIdlingResource;
    }

    public IdlingResource getPodcastSearchResultPodcastListCompleteOrErrorIdlingResource() {
        return podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource;
    }
}
