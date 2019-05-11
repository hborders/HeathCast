package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public final class PodcastFragment extends Fragment
        implements EpisodeListFragment.EpisodeListFragmentListener {
    private static final String TAG = "podcast";
    private static final String PODCAST_PARCELABLE_KEY = "podcast";

    private BehaviorSubject<Optional<Identified<Podcast>>> podcastIdentifiedOptionalBehaviorSubject =
            BehaviorSubject.create();
    @Nullable
    private PodcastFragmentListener listener;
    @Nullable
    private Disposable podcastIdentifiedDisposable;
    @Nullable
    private Disposable subscriptionIdentifierDisposable;

    public PodcastFragment() {
        // Required empty public constructor
    }

    public static Bundle newArguments(Identified<Podcast> identifiedPodcast) {
        final Bundle args = new Bundle();
        args.putParcelable(
                PODCAST_PARCELABLE_KEY,
                new PodcastIdentifiedHolder(identifiedPodcast)
        );
        return args;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = FragmentUtil.requireFragmentListener(
                this,
                context,
                PodcastFragment.PodcastFragmentListener.class
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_podcast,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {
        final PodcastFragmentListener listener = Objects.requireNonNull(this.listener);

        final ConstraintLayout constraintLayout = view.requireViewById(R.id.fragment_podcast_constraint_layout);
        final ImageView artworkImageView = view.requireViewById(R.id.fragment_podcast_artwork_image_view);
        final TextView nameTextView = view.requireViewById(R.id.fragment_podcast_name_text_view);
        final TextView authorTextView = view.requireViewById(R.id.fragment_podcast_author_text_view);
        final TextView missingTextView = view.requireViewById(R.id.fragment_podcast_missing_text_view);

        final Function<Optional<Identified<Podcast>>, Void> updateWithPodcastIdentifiedOptionalFunction =
                podcastIdentifiedOptional -> {
                    podcastIdentifiedOptionalBehaviorSubject.onNext(podcastIdentifiedOptional);
                    @Nullable final Identified<Podcast> identifiedPodcast =
                            podcastIdentifiedOptional.orElse(null);
                    if (identifiedPodcast == null) {
                        constraintLayout.setVisibility(View.GONE);
                        missingTextView.setVisibility(View.VISIBLE);
                    } else {
                        constraintLayout.setVisibility(View.VISIBLE);
                        missingTextView.setVisibility(View.GONE);

                        final Podcast podcast = identifiedPodcast.model;
                        nameTextView.setText(podcast.name);
                        authorTextView.setText(podcast.author);
                        if (podcast.artworkURL == null) {
                            artworkImageView.setImageDrawable(null);
                        } else {
                            final String artworkURLString = podcast.artworkURL.toExternalForm();
                            Picasso.get().load(artworkURLString).into(artworkImageView);
                        }
                    }
                    return null;
                };

        final Identified<Podcast> identifiedPodcast = FragmentUtil.requireUnparcelableHolderArgument(
                this,
                PodcastIdentifiedHolder.class,
                PODCAST_PARCELABLE_KEY
        );
        updateWithPodcastIdentifiedOptionalFunction.apply(Optional.of(identifiedPodcast));

        @Nullable final Disposable oldPodcastIdentifiedDisposable = podcastIdentifiedDisposable;
        if (oldPodcastIdentifiedDisposable != null) {
            oldPodcastIdentifiedDisposable.dispose();
        }
        podcastIdentifiedDisposable =
                listener
                        .observeQueryForPodcastIdentified(
                                this,
                                identifiedPodcast.identifier
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturnItem(Optional.empty())
                        .subscribe(updateWithPodcastIdentifiedOptionalFunction::apply);

        final Button subscribedButton = view.requireViewById(R.id.fragment_podcast_subscribed_button);

        @Nullable final Disposable oldSubscriptionIdentifierDisposable = subscriptionIdentifierDisposable;
        if (oldSubscriptionIdentifierDisposable != null) {
            oldSubscriptionIdentifierDisposable.dispose();
        }
        subscriptionIdentifierDisposable =
                listener
                        .observeQueryForSubscriptionIdentifier(
                                this,
                                identifiedPodcast.identifier
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturnItem(Optional.empty())
                        .subscribe(
                                subscriptionIdentifierOptional -> {
                                    @Nullable final Identifier<Subscription> subscriptionIdentifier =
                                            subscriptionIdentifierOptional.orElse(null);
                                    @StringRes final int textRes;
                                    final View.OnClickListener onClickListener;
                                    if (subscriptionIdentifier == null) {
                                        textRes = R.string.fragment_podcast_not_subscribed;
                                        onClickListener = button ->
                                                listener.requestedSubscribe(
                                                        this,
                                                        identifiedPodcast.identifier
                                                );
                                    } else {
                                        textRes = R.string.fragment_podcast_subscribed;
                                        onClickListener = button ->
                                                listener.requestedUnsubscribe(
                                                        this,
                                                        subscriptionIdentifier
                                                );
                                    }
                                    subscribedButton.setOnClickListener(onClickListener);
                                    subscribedButton.setText(textRes);
                                }
                        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        @Nullable final Disposable oldPodcastIdentifiedDisposable = podcastIdentifiedDisposable;
        if (oldPodcastIdentifiedDisposable != null) {
            oldPodcastIdentifiedDisposable.dispose();
        }
        podcastIdentifiedDisposable = null;

        @Nullable final Disposable oldSubscriptionIdentifierDisposable = subscriptionIdentifierDisposable;
        if (oldSubscriptionIdentifierDisposable != null) {
            oldSubscriptionIdentifierDisposable.dispose();
        }
        subscriptionIdentifierDisposable = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public Observable<Optional<List<Identified<Episode>>>> episodeIdentifiedsOptionalObservable(
            EpisodeListFragment episodeListFragment
    ) {
        return podcastIdentifiedOptionalBehaviorSubject
                .hide()
                .map(podcastIdentifiedOptional ->
                        podcastIdentifiedOptional.map(podcastIdentified ->
                                podcastIdentified.model.feedURL
                        )
                )
                .distinctUntilChanged()
                .flatMap(feedURLOptional -> {
                    @Nullable final URL feedURL =
                            feedURLOptional.orElse(null);
                    final Single<Optional<List<Identified<Episode>>>> episodeIdentifiedsOptionalSingle;
                    if (feedURL == null) {
                        episodeIdentifiedsOptionalSingle = Single.just(Optional.empty());
                    } else {
                        episodeIdentifiedsOptionalSingle = Objects.requireNonNull(listener).fetchEpisodes(
                                this,
                                feedURL
                        ).map(Optional::of);
                    }
                    return Observable.concat(
                            episodeIdentifiedsOptionalSingle.toObservable(),
                            Observable.never()
                    );
                });
    }

    @Override
    public void onClick(
            EpisodeListFragment episodeListFragment,
            Identified<Episode> episodeIdentified
    ) {

    }

    public interface PodcastFragmentListener {
        Single<List<Identified<Episode>>> fetchEpisodes(
                PodcastFragment podcastFragment,
                URL url
        );

        Observable<Optional<Identified<Podcast>>> observeQueryForPodcastIdentified(
                PodcastFragment podcastFragment,
                Identifier<Podcast> podcastIdentifier
        );

        Observable<Optional<Identifier<Subscription>>> observeQueryForSubscriptionIdentifier(
                PodcastFragment podcastFragment,
                Identifier<Podcast> podcastIdentifier
        );

        void requestedSubscribe(
                PodcastFragment podcastFragment,
                Identifier<Podcast> podcastIdentifier
        );

        void requestedUnsubscribe(
                PodcastFragment podcastFragment,
                Identifier<Subscription> subscriptionIdentifier
        );
    }
}
