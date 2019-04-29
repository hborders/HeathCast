package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.Result;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Identifier;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.Subscription;
import com.github.hborders.heathcast.parcelables.PodcastIdentifiedHolder;
import com.github.hborders.heathcast.services.PodcastService;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class PodcastFragment extends Fragment implements EpisodeListFragment.EpisodeListFragmentListener {
    private static final String TAG = "podcast";
    private static final String PODCAST_PARCELABLE_KEY = "podcast";
    private static final String EPISODE_LIST_FRAGMENT_TAG = "episodeList";

    @Nullable
    private PodcastFragmentListener listener;
    @Nullable
    private Disposable podcastIdentifiedDisposable;
    @Nullable
    private Disposable subscriptionIdentifierDisposable;
    @Nullable
    private Disposable fetchEpisodesDisposable;

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
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        @Nullable final View view = inflater.inflate(
                R.layout.fragment_podcast,
                container,
                false
        );
        if (view != null) {
            final ConstraintLayout constraintLayout = view.requireViewById(R.id.fragment_podcast_constraint_layout);
            final ImageView artworkImageView = view.requireViewById(R.id.fragment_podcast_artwork_image_view);
            final TextView nameTextView = view.requireViewById(R.id.fragment_podcast_name_text_view);
            final TextView authorTextView = view.requireViewById(R.id.fragment_podcast_author_text_view);
            final TextView missingTextView = view.requireViewById(R.id.fragment_podcast_missing_text_view);
            final Function<Optional<Identified<Podcast>>, Void> updateWithPodcastIdentifiedOptionalFunction =
                    podcastIdentifiedOptional -> {
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
                            if (podcast.artworkURL != null) {
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

            final PodcastService podcastService = PodcastService.getInstance(inflater.getContext());

            @Nullable final Disposable oldPodcastIdentifiedDisposable = podcastIdentifiedDisposable;
            if (oldPodcastIdentifiedDisposable != null) {
                oldPodcastIdentifiedDisposable.dispose();
            }
            podcastIdentifiedDisposable =
                    podcastService
                            .observeQueryForPodcastIdentified(identifiedPodcast.identifier)
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorReturnItem(Optional.empty())
                            .subscribe(updateWithPodcastIdentifiedOptionalFunction::apply);

            final Button subscribedButton = view.requireViewById(R.id.fragment_podcast_subscribed_button);

            @Nullable final Disposable oldSubscriptionIdentifierDisposable = subscriptionIdentifierDisposable;
            if (oldSubscriptionIdentifierDisposable != null) {
                oldSubscriptionIdentifierDisposable.dispose();
            }
            subscriptionIdentifierDisposable =
                    podcastService
                            .observeQueryForSubscriptionIdentifier(identifiedPodcast.identifier)
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
                                                    podcastService
                                                            .subscribe(identifiedPodcast.identifier)
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new SingleObserver<Optional<Identifier<Subscription>>>() {
                                                                           @Override
                                                                           public void onSubscribe(Disposable d) {
                                                                               // don't dispose this should be fast, and disposal is pointless anyway
                                                                           }

                                                                           @Override
                                                                           public void onSuccess(Optional<Identifier<Subscription>> subscriptionIdentifierOptional) {
                                                                               Snackbar.make(
                                                                                       view,
                                                                                       subscriptionIdentifierOptional.isPresent() ?
                                                                                               R.string.fragment_podcast_subscribe_success :
                                                                                               R.string.fragment_podcast_subscribe_failure,
                                                                                       Snackbar.LENGTH_LONG
                                                                               ).show();
                                                                           }

                                                                           @Override
                                                                           public void onError(Throwable e) {
                                                                               Snackbar.make(
                                                                                       view,
                                                                                       R.string.fragment_podcast_subscribe_failure,
                                                                                       Snackbar.LENGTH_LONG
                                                                               ).show();
                                                                           }
                                                                       }
                                                            );
                                        } else {
                                            textRes = R.string.fragment_podcast_subscribed;
                                            onClickListener = button ->
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
                                                                               Snackbar.make(
                                                                                       view,
                                                                                       result.<Integer>map(
                                                                                               success -> R.string.fragment_podcast_unsubscribe_success,
                                                                                               failure -> R.string.fragment_podcast_unsubscribe_failure
                                                                                       ),
                                                                                       Snackbar.LENGTH_LONG
                                                                               ).show();
                                                                           }

                                                                           @Override
                                                                           public void onError(Throwable e) {
                                                                               Snackbar.make(
                                                                                       view,
                                                                                       R.string.fragment_podcast_unsubscribe_failure,
                                                                                       Snackbar.LENGTH_LONG
                                                                               ).show();
                                                                           }
                                                                       }
                                                            );
                                        }
                                        subscribedButton.setOnClickListener(onClickListener);
                                        subscribedButton.setText(textRes);
                                    }
                            );

            @Nullable final Disposable oldFetchEpisodesDisposable = fetchEpisodesDisposable;
            if (oldFetchEpisodesDisposable != null) {
                oldFetchEpisodesDisposable.dispose();
            }
            fetchEpisodesDisposable =
                    podcastService
                            .fetchEpisodes(identifiedPodcast.model.feedURL)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    identifiedEpisodes -> {
                                        @Nullable final EpisodeListFragment existingEpisodeListFragment =
                                                (EpisodeListFragment) getChildFragmentManager()
                                                        .findFragmentByTag(EPISODE_LIST_FRAGMENT_TAG);
                                        final FragmentTransaction fragmentTransaction =
                                                getChildFragmentManager()
                                                        .beginTransaction();
                                        if (existingEpisodeListFragment != null) {
                                            fragmentTransaction
                                                    .remove(existingEpisodeListFragment);
                                        }
                                        fragmentTransaction
                                                .add(
                                                        R.id.fragment_podcast_episode_list_fragment_container_frame_layout,
                                                        EpisodeListFragment.newInstance(
                                                                identifiedEpisodes
                                                        ),
                                                        EPISODE_LIST_FRAGMENT_TAG
                                                )
                                                .commit();
                                    },
                                    throwable -> {
                                        Snackbar.make(
                                                view,
                                                "Error when fetching episodes",
                                                Snackbar.LENGTH_SHORT
                                        );
                                        Log.e(
                                                TAG,
                                                "Error when fetching episodes",
                                                throwable
                                        );
                                    }
                            );
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        @Nullable final Disposable oldPodcastIdentifiedDisposable = podcastIdentifiedDisposable;
        if (oldPodcastIdentifiedDisposable != null) {
            oldPodcastIdentifiedDisposable.dispose();
        }
        podcastIdentifiedDisposable = null;

        @Nullable final Disposable oldFetchEpisodesDisposable = fetchEpisodesDisposable;
        if (oldFetchEpisodesDisposable != null) {
            oldFetchEpisodesDisposable.dispose();
        }
        fetchEpisodesDisposable = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onClick(Identified<Episode> identifiedEpisode) {

    }

    public interface PodcastFragmentListener {

    }
}
