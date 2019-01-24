package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastParcelable;
import com.github.hborders.heathcast.utils.ArrayUtil;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PodcastListFragment.OnPodcastListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PodcastListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class PodcastListFragment extends Fragment {
    private static final String PODCAST_PARCELABLES_KEY = "podcastParcelables";

    @Nullable
    private OnPodcastListFragmentInteractionListener mListener;

    @Nullable
    private RecyclerView mRecyclerView;

    public PodcastListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PodcastListFragment.
     */
    public static PodcastListFragment newInstance(List<PodcastParcelable> podcastParcelables) {
        final PodcastListFragment fragment = new PodcastListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(
                PODCAST_PARCELABLES_KEY,
                podcastParcelables.toArray(new PodcastParcelable[0])
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
            final RecyclerView podcastsRecyclerView = view.requireViewById(R.id.podcasts_recycler_view);
            podcastsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            @Nullable final Bundle arguments = getArguments();
            if (arguments != null) {
                @Nullable final PodcastParcelable[] podcastParcelables =
                        (PodcastParcelable[]) arguments.getParcelableArray(PODCAST_PARCELABLES_KEY);
                @Nonnull final List<Podcast> podcasts = ArrayUtil
                        .asList(podcastParcelables)
                        .stream()
                        .map(PodcastParcelable::getPodcast)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                final PodcastRecyclerViewAdapter adapter = new PodcastRecyclerViewAdapter(podcasts);
                podcastsRecyclerView.setAdapter(adapter);
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPodcastListFragmentInteractionListener) {
            mListener = (OnPodcastListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPodcastListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPodcastListFragmentInteraction();
    }
}
