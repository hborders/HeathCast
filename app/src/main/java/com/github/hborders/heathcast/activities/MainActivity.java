package com.github.hborders.heathcast.activities;

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
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.fragments.MainFragment;
import com.github.hborders.heathcast.fragments.PodcastFragment;
import com.github.hborders.heathcast.fragments.PodcastSearchFragment;
import com.github.hborders.heathcast.idlingresource.DelegatingIdlingResource;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedOpt;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.github.hborders.heathcast.models.SubscriptionIdentifierOpt;
import com.github.hborders.heathcast.services.NetworkPauser;
import com.github.hborders.heathcast.services.PodcastIdentifiedListServiceResponse;
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

public final class MainActivity extends AppCompatActivity
        implements
        MainFragment.MainFragmentListener,
        PodcastSearchFragment.PodcastSearchFragmentListener,
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

    private final PodcastService podcastService = new PodcastService(this);

    private final DelegatingIdlingResource podcastSearchResultPodcastListLoadingDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListLoading"
            );
    private final DelegatingIdlingResource podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource =
            new DelegatingIdlingResource(
                    "PodcastSearchResultPodcastListCompleteOrError"
            );

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

    // PodcastSearchFragmentListener

    @Override
    public void onPodcastSearchFragmentAttached(PodcastSearchFragment podcastSearchFragment) {
        podcastSearchResultPodcastListLoadingDelegatingIdlingResource.setDelegateIdlingResource(
                podcastSearchFragment.getSearchResultPodcastListLoadingIdlingResource()
        );
        podcastSearchResultPodcastListCompleteOrErrorDelegatingIdlingResource.setDelegateIdlingResource(
                podcastSearchFragment.getSearchResultPodcastListCompleteOrErrorIdlingResource()
        );
    }

    @Override
    public Observable<PodcastIdentifiedListServiceResponse> searchForPodcasts2(
            PodcastSearchFragment podcastSearchFragment,
            PodcastSearch podcastSearch
    ) {
        @Nullable NetworkPauser networkPauser = searchForPodcasts2NetworkPausers.poll();
        return podcastService.searchForPodcasts2(
                networkPauser,
                podcastSearch
        );
    }

    @Override
    public void onClickPodcastIdentified(
            PodcastSearchFragment podcastSearchFragment,
            PodcastIdentified podcastIdentified
    ) {
        requireNavController().navigate(
                R.id.action_podcastSearchFragment_to_podcastFragment,
                PodcastFragment.newArguments(podcastIdentified)
        );
    }

    @Override
    public void onPodcastSearchFragmentWillDetach(PodcastSearchFragment podcastSearchFragment) {
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
