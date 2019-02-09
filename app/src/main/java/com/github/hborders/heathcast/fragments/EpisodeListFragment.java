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
import com.github.hborders.heathcast.parcelables.IdentifiedEpisodeHolder;
import com.github.hborders.heathcast.utils.FragmentUtil;
import com.github.hborders.heathcast.views.recyclerviews.EpisodeRecyclerViewAdapter;

import java.util.List;

import javax.annotation.Nullable;

public final class EpisodeListFragment extends Fragment {
    private static final String EPISODE_PARCELABLES_KEY = "episodeParcelables";

    @Nullable
    private EpisodeListFragmentListener mListener;

    public EpisodeListFragment() {
        // Required empty public constructor
    }

    public static EpisodeListFragment newInstance(List<Identified<Episode>> identifiedEpisodes) {
        final EpisodeListFragment fragment = new EpisodeListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(
                EPISODE_PARCELABLES_KEY,
                identifiedEpisodes
        .stream()
        .map(IdentifiedEpisodeHolder::new)
        .toArray(IdentifiedEpisodeHolder[]::new));
        fragment.setArguments(args);
        return fragment;
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
                R.layout.fragment_episode_list,
                container,
                false
        );
        if (view != null) {
            final RecyclerView episodessRecyclerView = view.requireViewById(R.id.fragment_episode_list_episodes_recycler_view);
            episodessRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            @Nullable final List<Identified<Episode>> identifiedEpisodes =
                    FragmentUtil.getUnparcelableHolderListArgument(
                            this,
                            IdentifiedEpisodeHolder.class,
                            EPISODE_PARCELABLES_KEY
                    );
            if (identifiedEpisodes != null) {
                final EpisodeRecyclerViewAdapter adapter = new EpisodeRecyclerViewAdapter(
                        identifiedEpisodes,
                        identifiedEpisode -> {
                            @Nullable final EpisodeListFragment.EpisodeListFragmentListener listener = mListener;
                            if (listener != null) {
                                listener.onClick(identifiedEpisode);
                            }
                        }
                );
                episodessRecyclerView.setAdapter(adapter);
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = FragmentUtil.requireFragmentListener(
                this,
                context,
                EpisodeListFragment.EpisodeListFragmentListener.class
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface EpisodeListFragmentListener {
        void onClick(Identified<Episode> identifiedEpisode);
    }
}
