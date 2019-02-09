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

    private final List<Identified<Podcast>> mIdentifiedPodcasts;
    private final PodcastRecyclerViewAdapterListener mListener;

    public PodcastRecyclerViewAdapter(
            List<Identified<Podcast>> identifiedPodcasts,
            PodcastRecyclerViewAdapterListener listener
    ) {
        this.mIdentifiedPodcasts = identifiedPodcasts;
        this.mListener = listener;
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
        final Identified<Podcast> identifiedPodcast = mIdentifiedPodcasts.get(position);
        final Podcast podcast = identifiedPodcast.mModel;
        holder.mNameTextView.setText(podcast.mName);
        holder.itemView.setOnClickListener(itemView -> mListener.onClick(identifiedPodcast));
        Picasso.get().cancelRequest(holder.mArtworkImageView);
        if (podcast.mArtworkURL != null) {
            final String artworkUrlString = podcast.mArtworkURL.toExternalForm();
            Picasso.get().load(artworkUrlString).into(holder.mArtworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mIdentifiedPodcasts.size();
    }

    static final class PodcastViewHolder extends RecyclerView.ViewHolder {
        final ImageView mArtworkImageView;
        final TextView mNameTextView;

        PodcastViewHolder(View itemView) {
            super(itemView);
            this.mArtworkImageView = itemView.requireViewById(R.id.item_podcast_artwork_image_view);
            this.mNameTextView = itemView.requireViewById(R.id.item_podcast_name_text_view);
        }
    }

    public interface PodcastRecyclerViewAdapterListener {
        void onClick(Identified<Podcast> identifiedPodcast);
    }
}
