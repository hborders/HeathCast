package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.parcelables.PodcastHolder;
import com.github.hborders.heathcast.utils.ArrayUtil;
import com.github.hborders.heathcast.utils.FragmentUtil;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PodcastFragment extends Fragment {
    private static final String PODCAST_PARCELABLE_KEY = "podcast";

    @Nullable
    private PodcastFragmentListener mListener;

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
            final TextView nameTextView = view.requireViewById(R.id.name_text_view);
            final TextView authorTextView = view.requireViewById(R.id.author_text_view);

            @Nullable final Podcast podcast = FragmentUtil.getUnparcelableHolderArgument(
                    this,
                    PodcastHolder.class,
                    PODCAST_PARCELABLE_KEY
            );
            if (podcast != null) {
                nameTextView.setText(podcast.mName);
                authorTextView.setText(podcast.mAuthor);
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
                PodcastFragment.PodcastFragmentListener.class
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface PodcastFragmentListener {

    }
}
