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
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.github.hborders.heathcast.models.EpisodeIdentifiedList;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedOpt;
import com.github.hborders.heathcast.models.PodcastIdentifier;
import com.github.hborders.heathcast.models.SubscriptionIdentifier;
import com.github.hborders.heathcast.models.URLOpt;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private BehaviorSubject<PodcastIdentifiedOpt> podcastIdentifiedOptBehaviorSubject =
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

    public static Bundle newArguments(PodcastIdentified identifiedPodcast) {
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

        final PodcastFragmentListener listener = FragmentUtil.requireFragmentListener(
                this,
                context,
                PodcastFragment.PodcastFragmentListener.class
        );
        this.listener = listener;
        listener.onPodcastFragmentAttached(this);
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

        final Function<PodcastIdentifiedOpt, Void> updateWithPodcastIdentifiedOptionalFunction =
                podcastIdentifiedOpt -> {
                    podcastIdentifiedOptBehaviorSubject.onNext(podcastIdentifiedOpt);
                    @Nullable final PodcastIdentified identifiedPodcast =
                            podcastIdentifiedOpt.orNull();
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
        final PodcastIdentified identifiedPodcast = FragmentUtil.requireUnparcelableArgument(
                this,
                PodcastIdentifiedHolder.class,
                PODCAST_PARCELABLE_KEY
        );
        updateWithPodcastIdentifiedOptionalFunction.apply(new PodcastIdentifiedOpt(identifiedPodcast));

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
                        .onErrorReturnItem(PodcastIdentifiedOpt.EMPTY)
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
                                    @Nullable final SubscriptionIdentifier subscriptionIdentifier =
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
        final PodcastFragmentListener listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onPodcastFragmentWillDetach(this);

        super.onDetach();
    }

    @Override
    public void onEpisodeListFragmentAttached(EpisodeListFragment episodeListFragment) {
        final PodcastIdentified identifiedPodcast = FragmentUtil.requireUnparcelableArgument(
                this,
                PodcastIdentifiedHolder.class,
                PODCAST_PARCELABLE_KEY
        );
        podcastIdentifiedOptBehaviorSubject.onNext(new PodcastIdentifiedOpt(identifiedPodcast));
    }

    @Override
    public Observable<Optional<List<EpisodeIdentified>>> episodeIdentifiedsOptionalObservable(
            EpisodeListFragment episodeListFragment
    ) {
        return podcastIdentifiedOptBehaviorSubject
                .map(podcastIdentifiedOpt ->
                        podcastIdentifiedOpt.map(
                                URLOpt.FACTORY,
                                podcastIdentified -> podcastIdentified.model.feedURL
                        )
                )
                .distinctUntilChanged()
                .switchMap(feedURLOpt -> {
                    @Nullable final URL feedURL =
                            feedURLOpt.orNull();
                    final Single<Optional<List<EpisodeIdentified>>> episodeIdentifiedsOptionalSingle;
                    if (feedURL == null) {
                        episodeIdentifiedsOptionalSingle = Single.just(Optional.empty());
                    } else {
                        episodeIdentifiedsOptionalSingle = Objects.requireNonNull(listener).fetchEpisodes(
                                this,
                                feedURL
                        ).map(Optional::of);
                    }
                    return episodeIdentifiedsOptionalSingle.toObservable();
                });
    }

    @Override
    public void onEpisodeIdentifiedsOptionalError(
            EpisodeListFragment episodeListFragment,
            Throwable throwable
    ) {
        Snackbar.make(
                requireView(),
                requireContext().getText(R.string.fragment_podcast_episode_list_error),
                Snackbar.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onClick(
            EpisodeListFragment episodeListFragment,
            EpisodeIdentified episodeIdentified
    ) {
    }

    @Override
    public void onEpisodeListFragmentWillDetach(EpisodeListFragment episodeListFragment) {
        podcastIdentifiedOptBehaviorSubject.onNext(PodcastIdentifiedOpt.EMPTY);
    }

    public interface PodcastFragmentListener {
        void onPodcastFragmentAttached(PodcastFragment podcastFragment);

        Single<EpisodeIdentifiedList> fetchEpisodes(
                PodcastFragment podcastFragment,
                URL url
        );

        Observable<PodcastIdentifiedOpt> observeQueryForPodcastIdentified(
                PodcastFragment podcastFragment,
                PodcastIdentifier podcastIdentifier
        );

        Observable<Optional<SubscriptionIdentifier>> observeQueryForSubscriptionIdentifier(
                PodcastFragment podcastFragment,
                PodcastIdentifier podcastIdentifier
        );

        void requestedSubscribe(
                PodcastFragment podcastFragment,
                PodcastIdentifier podcastIdentifier
        );

        void requestedUnsubscribe(
                PodcastFragment podcastFragment,
                SubscriptionIdentifier subscriptionIdentifier
        );

        void onPodcastFragmentWillDetach(PodcastFragment podcastFragment);
    }
}
