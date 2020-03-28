package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.squareup.picasso.Picasso;

public class PodcastRecyclerViewAdapter extends ListRecyclerViewAdapter<
        PodcastIdentifiedList,
        PodcastIdentified,
        PodcastRecyclerViewAdapter.PodcastViewHolder
        > {

    private final PodcastRecyclerViewAdapterListener listener;

    public PodcastRecyclerViewAdapter(
            PodcastIdentifiedList identifiedPodcasts,
            PodcastRecyclerViewAdapterListener listener
    ) {
        super(identifiedPodcasts);
        this.listener = listener;
    }

    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_podcast,
                parent,
                false
        );
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            PodcastViewHolder holder,
            PodcastIdentified podcastIdentified) {
        final Podcast podcast = podcastIdentified.model;
        holder.nameTextView.setText(podcast.name);
        holder.itemView.setOnClickListener(itemView -> listener.onClick(podcastIdentified));
        Picasso.get().cancelRequest(holder.artworkImageView);
        if (podcast.artworkURL != null) {
            final String artworkUrlString = podcast.artworkURL.toExternalForm();
            Picasso.get().load(artworkUrlString).into(holder.artworkImageView);
        }
    }

    public static final class PodcastViewHolder extends RecyclerView.ViewHolder {
        final ImageView artworkImageView;
        final TextView nameTextView;

        PodcastViewHolder(View itemView) {
            super(itemView);
            this.artworkImageView = itemView.requireViewById(R.id.item_podcast_artwork_image_view);
            this.nameTextView = itemView.requireViewById(R.id.item_podcast_name_text_view);
        }
    }

    public interface PodcastRecyclerViewAdapterListener {
        void onClick(PodcastIdentified podcastIdentified);
    }
}
