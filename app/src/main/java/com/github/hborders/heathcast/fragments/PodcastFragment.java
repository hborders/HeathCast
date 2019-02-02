package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastHolder;
import com.github.hborders.heathcast.services.PodcastService;
import com.github.hborders.heathcast.utils.FragmentUtil;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class PodcastFragment extends Fragment implements EpisodeListFragment.EpisodeListFragmentListener {
    private static final String TAG = "podcast";
    private static final String PODCAST_PARCELABLE_KEY = "podcast";
    private static final String EPISODE_LIST_FRAGMENT_TAG = "episodeList";

    @Nullable
    private PodcastFragmentListener mListener;
    @Nullable
    private Disposable mFetchEpisodesDisposable;

    public PodcastFragment() {
        // Required empty public constructor
    }

    public static PodcastFragment newInstance(Podcast podcast) {
        final PodcastFragment fragment = new PodcastFragment();
        fragment.setArguments(newArguments(podcast));
        return fragment;
    }

    public static Bundle newArguments(Podcast podcast) {
        final Bundle args = new Bundle();
        args.putParcelable(
                PODCAST_PARCELABLE_KEY,
                new PodcastHolder(podcast)
        );
        return args;
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
            final ImageView artworkImageView = view.requireViewById(R.id.fragment_podcast_artwork_image_view);
            final TextView nameTextView = view.requireViewById(R.id.fragment_podcast_name_text_view);
            final TextView authorTextView = view.requireViewById(R.id.fragment_podcast_author_text_view);

            @Nullable final Podcast podcast = FragmentUtil.getUnparcelableHolderArgument(
                    this,
                    PodcastHolder.class,
                    PODCAST_PARCELABLE_KEY
            );
            if (podcast != null) {
                nameTextView.setText(podcast.mName);
                authorTextView.setText(podcast.mAuthor);
                if (podcast.mArtworkURL != null) {
                    final String artworkURLString = podcast.mArtworkURL.toExternalForm();
                    Picasso.get().load(artworkURLString).into(artworkImageView);
                }

                @Nullable final Disposable oldDisposable = mFetchEpisodesDisposable;
                if (oldDisposable != null) {
                    oldDisposable.dispose();
                }
                mFetchEpisodesDisposable =
                        PodcastService.instance
                                .fetchEpisodes(podcast.mFeedURL)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        episodes -> {
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
                                                                    episodes
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
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        @Nullable final Disposable fetchEpisodesDisposable = mFetchEpisodesDisposable;
        if (fetchEpisodesDisposable != null) {
            fetchEpisodesDisposable.dispose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = FragmentUtil.requireFragmentListener(
                this,
                context,
                PodcastFragment.PodcastFragmentListener.class
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(Episode episode) {

    }

    public interface PodcastFragmentListener {

    }
}
