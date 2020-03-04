package com.github.hborders.heathcast.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.fragments.MainFragment;
import com.github.hborders.heathcast.fragments.PodcastFragment;
import com.github.hborders.heathcast.fragments.PodcastSearchFragment;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.PodcastSearch;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.github.hborders.heathcast.services.PodcastService;
import com.github.hborders.heathcast.services.ServiceResponse1;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public final class MainActivity extends AppCompatActivity
        implements
        MainFragment.MainFragmentListener,
        PodcastSearchFragment.PodcastSearchFragmentListener,
        PodcastFragment.PodcastFragmentListener {

    private final BehaviorSubject<Optional<PodcastSearchFragment>> podcastSearchFragmentOptionalBehaviorSubject =
            BehaviorSubject.createDefault(Optional.empty());
    private final PodcastService podcastService = new PodcastService(this);

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
        podcastSearchFragmentOptionalBehaviorSubject.onNext(Optional.of(podcastSearchFragment));
//        podcastSearchDelegatingIdlingResource.setState(
//                DelegatingIdlingResource.State.hasInnerIdlingResource(
//                        podcastSearchFragment.getSearchResultPodcastIdentifiedsIdlingResource()
//                )
//        );
    }

    @Override
    public Observable<ServiceResponse1<PodcastIdentifiedList>> searchForPodcasts2(
            PodcastSearchFragment podcastSearchFragment,
            PodcastSearch podcastSearch
    ) {
        return podcastService.searchForPodcasts2(podcastSearch);
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
        podcastSearchFragmentOptionalBehaviorSubject.onNext(Optional.empty());
    }

    // PodcastFragmentListener

    @Override
    public void onPodcastFragmentAttached(PodcastFragment podcastFragment) {
    }

    @Override
    public Single<List<EpisodeIdentified>> fetchEpisodes(
            PodcastFragment podcastFragment,
            URL url
    ) {
        return podcastService.fetchEpisodes(url);
    }

    @Override
    public Observable<Optional<PodcastIdentified>> observeQueryForPodcastIdentified(
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
                .subscribe(new SingleObserver<Optional<SubscriptionIdentifier>>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   // don't dispose this should be fast, and disposal is pointless anyway
                               }

                               @Override
                               public void onSuccess(Optional<SubscriptionIdentifier> subscriptionIdentifierOptional) {
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
                                               result.<Integer>map(
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

    public Observable<Boolean> getPodcastSearchingObservable() {
        return podcastSearchFragmentOptionalBehaviorSubject.switchMap(
                podcastSearchFragmentOptional ->
                        podcastSearchFragmentOptional.map(
                                PodcastSearchFragment::getSearchingObservable
                        ).orElse(Observable.just(false))
        );
    }
}
