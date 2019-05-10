package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.parcelables.EpisodeIdentifiedHolder;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.views.recyclerviews.EpisodeRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public final class EpisodeListFragment extends Fragment {
    private static final String EPISODE_PARCELABLES_KEY = "episodeParcelables";

    @Nullable
    private EpisodeListFragmentListener listener;

    public EpisodeListFragment() {
        // Required empty public constructor
    }

    public static EpisodeListFragment newInstance(List<Identified<Episode>> episodeIdentifieds) {
        final EpisodeListFragment fragment = new EpisodeListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(
                EPISODE_PARCELABLES_KEY,
                episodeIdentifieds
                        .stream()
                        .map(EpisodeIdentifiedHolder::new)
                        .toArray(EpisodeIdentifiedHolder[]::new));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = Objects.requireNonNull(
                inflater.inflate(
                        R.layout.fragment_episode_list,
                        container,
                        false
                )
        );
        final RecyclerView episodessRecyclerView = view.requireViewById(R.id.fragment_episode_list_episodes_recycler_view);
        episodessRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        @Nullable final List<Identified<Episode>> episodeIdentifieds =
                FragmentUtil.getUnparcelableHolderListArgument(
                        this,
                        EpisodeIdentifiedHolder.class,
                        EPISODE_PARCELABLES_KEY
                );
        if (episodeIdentifieds != null) {
            final EpisodeRecyclerViewAdapter adapter = new EpisodeRecyclerViewAdapter(
                    episodeIdentifieds,
                    identifiedEpisode ->
                            Objects.requireNonNull(this.listener).onClick(
                                    this,
                                    identifiedEpisode
                            )
            );
            episodessRecyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = FragmentUtil.requireFragmentListener(
                this,
                context,
                EpisodeListFragment.EpisodeListFragmentListener.class
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    public interface EpisodeListFragmentListener {
        void onClick(
                EpisodeListFragment episodeListFragment,
                Identified<Episode> episodeIdentified
        );
    }
}
