package com.github.hborders.heathcast.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.fragments.PodcastFragment;
import com.github.hborders.heathcast.fragments.PodcastSearchFragment;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.services.PodcastService;
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

public final class MainActivity extends AppCompatActivity
        implements PodcastSearchFragment.PodcastSearchFragmentListener,
        PodcastFragment.PodcastFragmentListener {

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
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item == null) {
            return super.onOptionsItemSelected(item);
        }

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

    @Override
    public PodcastService podcastService() {
        return podcastService;
    }

    // PodcastFragmentListener

    @Override
    public Single<List<Identified<Episode>>> fetchEpisodes(
            PodcastFragment podcastFragment,
            URL url
    ) {
        return podcastService.fetchEpisodes(url);
    }

    @Override
    public Observable<Optional<Identified<Podcast>>> observeQueryForPodcastIdentified(
            PodcastFragment podcastFragment,
            Identifier<Podcast> podcastIdentifier
    ) {
        return podcastService.observeQueryForPodcastIdentified(podcastIdentifier);
    }

    @Override
    public Observable<Optional<Identifier<Subscription>>> observeQueryForSubscriptionIdentifier(
            PodcastFragment podcastFragment,
            Identifier<Podcast> podcastIdentifier
    ) {
        return podcastService.observeQueryForSubscriptionIdentifier(podcastIdentifier);
    }

    @Override
    public void requestedSubscribe(
            PodcastFragment podcastFragment,
            Identifier<Podcast> podcastIdentifier
    ) {
        podcastService
                .subscribe(podcastIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Optional<Identifier<Subscription>>>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   // don't dispose this should be fast, and disposal is pointless anyway
                               }

                               @Override
                               public void onSuccess(Optional<Identifier<Subscription>> subscriptionIdentifierOptional) {
                                   findContentViewOptional().ifPresent(contentView ->
                                           Snackbar.make(
                                                   contentView,
                                                   subscriptionIdentifierOptional.isPresent() ?
                                                           R.string.fragment_podcast_subscribe_success :
                                                           R.string.fragment_podcast_subscribe_failure,
                                                   Snackbar.LENGTH_LONG
                                           ).show()
                                   );
                               }

                               @Override
                               public void onError(Throwable e) {
                                   findContentViewOptional().ifPresent(contentView ->
                                           Snackbar.make(
                                                   contentView,
                                                   R.string.fragment_podcast_subscribe_failure,
                                                   Snackbar.LENGTH_LONG
                                           ).show()
                                   );
                               }
                           }
                );
    }

    @Override
    public void requestedUnsubscribe(
            PodcastFragment podcastFragment,
            Identifier<Subscription> subscriptionIdentifier
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
                                   findContentViewOptional().ifPresent(contentView ->
                                           Snackbar.make(
                                                   contentView,
                                                   result.<Integer>map(
                                                           success -> R.string.fragment_podcast_unsubscribe_success,
                                                           failure -> R.string.fragment_podcast_unsubscribe_failure
                                                   ),
                                                   Snackbar.LENGTH_LONG
                                           ).show()
                                   );
                               }

                               @Override
                               public void onError(Throwable e) {
                                   findContentViewOptional().ifPresent(contentView ->
                                           Snackbar.make(
                                                   contentView,
                                                   R.string.fragment_podcast_unsubscribe_failure,
                                                   Snackbar.LENGTH_LONG
                                           ).show()
                                   );
                               }
                           }
                );
    }

    // Private API

    private Optional<View> findContentViewOptional() {
        return Optional.ofNullable(findViewById(android.R.id.content));
    }
}
