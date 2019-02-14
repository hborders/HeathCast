package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Identified;
import com.github.hborders.heathcast.models.Podcast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PodcastRecyclerViewAdapter extends RecyclerView.Adapter<PodcastRecyclerViewAdapter.PodcastViewHolder> {

    private final List<Identified<Podcast>> podcastIdentifieds;
    private final PodcastRecyclerViewAdapterListener listener;

    public PodcastRecyclerViewAdapter(
            List<Identified<Podcast>> identifiedPodcasts,
            PodcastRecyclerViewAdapterListener listener
    ) {
        this.podcastIdentifieds = identifiedPodcasts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_podcast,
                parent,
                false
        );
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder holder, int position) {
        final Identified<Podcast> identifiedPodcast = podcastIdentifieds.get(position);
        final Podcast podcast = identifiedPodcast.model;
        holder.nameTextView.setText(podcast.name);
        holder.itemView.setOnClickListener(itemView -> listener.onClick(identifiedPodcast));
        Picasso.get().cancelRequest(holder.artworkImageView);
        if (podcast.artworkURL != null) {
            final String artworkUrlString = podcast.artworkURL.toExternalForm();
            Picasso.get().load(artworkUrlString).into(holder.artworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return podcastIdentifieds.size();
    }

    static final class PodcastViewHolder extends RecyclerView.ViewHolder {
        final ImageView artworkImageView;
        final TextView nameTextView;

        PodcastViewHolder(View itemView) {
            super(itemView);
            this.artworkImageView = itemView.requireViewById(R.id.item_podcast_artwork_image_view);
            this.nameTextView = itemView.requireViewById(R.id.item_podcast_name_text_view);
        }
    }

    public interface PodcastRecyclerViewAdapterListener {
        void onClick(Identified<Podcast> identifiedPodcast);
    }
}
