package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.Identified;
import com.squareup.picasso.Picasso;

import java.util.List;

public final class EpisodeRecyclerViewAdapter extends RecyclerView.Adapter<EpisodeRecyclerViewAdapter.EpisodeViewHolder> {

    private final List<Identified<Episode>> mIdentifiedEpisodes;
    private final EpisodeRecyclerViewAdapterListener mListener;

    public EpisodeRecyclerViewAdapter(
            List<Identified<Episode>> identifiedEpisodes,
            EpisodeRecyclerViewAdapterListener listener
    ) {
        this.mIdentifiedEpisodes = identifiedEpisodes;
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
        Identified<Episode> identifiedEpisode = mIdentifiedEpisodes.get(position);
        final Episode episode = identifiedEpisode.mModel;
        holder.mTitleTextView.setText(episode.mTitle);
        holder.itemView.setOnClickListener(itemView -> mListener.onClick(identifiedEpisode));
        Picasso.get().cancelRequest(holder.mArtworkImageView);
        if (episode.mArtworkURL != null) {
            final String artworkUrlString = episode.mArtworkURL.toExternalForm();
            Picasso.get().load(artworkUrlString).into(holder.mArtworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mIdentifiedEpisodes.size();
    }

    static final class EpisodeViewHolder extends RecyclerView.ViewHolder {
        final ImageView mArtworkImageView;
        final TextView mTitleTextView;

        EpisodeViewHolder(View itemView) {
            super(itemView);

            this.mArtworkImageView = itemView.requireViewById(R.id.item_episode_artwork_image_view);
            this.mTitleTextView = itemView.requireViewById(R.id.item_episode_title_text_view);
        }
    }

    public interface EpisodeRecyclerViewAdapterListener {
        void onClick(Identified<Episode> identifiedEpisode);
    }
}
