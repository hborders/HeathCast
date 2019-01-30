package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Episode;

import java.util.List;

public final class EpisodeRecyclerViewAdapter extends RecyclerView.Adapter<EpisodeRecyclerViewAdapter.EpisodeViewHolder> {

    private final List<Episode> mEpisodes;
    private final EpisodeRecyclerViewAdapterListener mListener;

    public EpisodeRecyclerViewAdapter(
            List<Episode> episodes,
            EpisodeRecyclerViewAdapterListener listener
    ) {
        this.mEpisodes = episodes;
        this.mListener = listener;
    }

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_episode,
                parent,
                false
        );
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        final Episode episode = mEpisodes.get(position);
        holder.mTitleTextView.setText(episode.mTitle);
        holder.itemView.setOnClickListener(itemView -> mListener.onClick(episode));
    }

    @Override
    public int getItemCount() {
        return mEpisodes.size();
    }

    public static final class EpisodeViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleTextView;

        public EpisodeViewHolder(View itemView) {
            super(itemView);

            this.mTitleTextView = itemView.requireViewById(R.id.episode_title);
        }
    }

    public interface EpisodeRecyclerViewAdapterListener {
        void onClick(Episode episode);
    }
}
