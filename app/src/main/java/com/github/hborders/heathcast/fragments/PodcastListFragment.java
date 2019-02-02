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
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastHolder;
import com.github.hborders.heathcast.utils.FragmentUtil;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.List;

import javax.annotation.Nullable;

public final class PodcastListFragment extends Fragment {
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";

    @Nullable
    private PodcastListFragmentListener mListener;

    public PodcastListFragment() {
        // Required empty public constructor
    }

    public static PodcastListFragment newInstance(List<Podcast> podcasts) {
        final PodcastListFragment fragment = new PodcastListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(
                PODCAST_PARCELABLES_KEY,
                podcasts
                        .stream()
                .map(PodcastHolder::new)
                .toArray(PodcastHolder[]::new)
        );
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
                R.layout.fragment_podcast_list,
                container,
                false
        );
        if (view != null) {
            final RecyclerView podcastsRecyclerView =
                    view.requireViewById(R.id.fragment_podcast_list_podcasts_recycler_view);
            podcastsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            @Nullable final List<Podcast> podcasts =
                    FragmentUtil.getUnparcelableHolderListArgument(
                            this,
                            PodcastHolder.class,
                            PODCAST_PARCELABLES_KEY
                    );
            if (podcasts != null) {
                final PodcastRecyclerViewAdapter adapter = new PodcastRecyclerViewAdapter(
                        podcasts,
                        podcast -> {
                            @Nullable final PodcastListFragmentListener listener = mListener;
                            if (listener != null) {
                                listener.onClick(podcast);
                            }
                        }
                );
                podcastsRecyclerView.setAdapter(adapter);
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
                PodcastListFragmentListener.class
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface PodcastListFragmentListener {
        void onClick(Podcast podcast);
    }
}
