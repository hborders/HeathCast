package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.squareup.picasso.Picasso;

public final class EpisodeRecyclerViewAdapter
        extends RecyclerView.Adapter<EpisodeRecyclerViewAdapter.EpisodeViewHolder> {
    public interface EpisodeRecyclerViewAdapterListener {
        void onClick(EpisodeImpl.EpisodeIdentifiedImpl episodeIdentified);
    }

    static final class EpisodeViewHolder extends RecyclerView.ViewHolder {
        final ImageView artworkImageView;
        final TextView titleTextView;

        EpisodeViewHolder(View itemView) {
            super(itemView);

            this.artworkImageView = itemView.requireViewById(R.id.item_episode_artwork_image_view);
            this.titleTextView = itemView.requireViewById(R.id.item_episode_title_text_view);
        }
    }

    private EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl episodeIdentifieds;
    private final EpisodeRecyclerViewAdapterListener listener;

    public EpisodeRecyclerViewAdapter(
            EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl episodeIdentifieds,
            EpisodeRecyclerViewAdapterListener listener
    ) {
        this.episodeIdentifieds = episodeIdentifieds;
        this.listener = listener;
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
        EpisodeImpl.EpisodeIdentifiedImpl episodeIdentified = episodeIdentifieds.get(position);
        final EpisodeImpl episode = episodeIdentified.model;
        holder.titleTextView.setText(episode.title);
        holder.itemView.setOnClickListener(itemView -> listener.onClick(episodeIdentified));
        Picasso.get().cancelRequest(holder.artworkImageView);
        if (episode.artworkURL != null) {
            final String artworkUrlString = episode.artworkURL.toExternalForm();
            Picasso.get().load(artworkUrlString).into(holder.artworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return episodeIdentifieds.size();
    }

    public void setEpisodeIdentifieds(EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl episodeIdentifieds) {
        this.episodeIdentifieds = episodeIdentifieds;
        notifyDataSetChanged();
    }
}
