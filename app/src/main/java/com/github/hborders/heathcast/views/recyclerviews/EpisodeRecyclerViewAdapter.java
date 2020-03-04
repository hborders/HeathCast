package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Episode;
import com.github.hborders.heathcast.models.EpisodeIdentified;
import com.squareup.picasso.Picasso;

import java.util.List;

public final class EpisodeRecyclerViewAdapter extends RecyclerView.Adapter<EpisodeRecyclerViewAdapter.EpisodeViewHolder> {

    private List<EpisodeIdentified> episodeIdentifieds;
    private final EpisodeRecyclerViewAdapterListener listener;

    public EpisodeRecyclerViewAdapter(
            List<EpisodeIdentified> episodeIdentifieds,
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
        EpisodeIdentified episodeIdentified = episodeIdentifieds.get(position);
        final Episode episode = episodeIdentified.model;
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

    public void setEpisodeIdentifieds(List<EpisodeIdentified> episodeIdentifieds) {
        this.episodeIdentifieds = episodeIdentifieds;
        notifyDataSetChanged();
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

    public interface EpisodeRecyclerViewAdapterListener {
        void onClick(EpisodeIdentified episodeIdentified);
    }
}
