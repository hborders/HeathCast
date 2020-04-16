package com.github.hborders.heathcast.views.recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.features.model.PodcastImpl;
import com.squareup.picasso.Picasso;

import java.net.URL;

public final class PodcastRecyclerViewAdapter extends ListRecyclerViewAdapter<
        PodcastRecyclerViewAdapter,
        PodcastRecyclerViewAdapter.PodcastViewHolder,
        PodcastImpl.PodcastIdentifiedImpl
        > {
    public interface PodcastRecyclerViewAdapterListener {
        void onClick(PodcastImpl.PodcastIdentifiedImpl podcastIdentified);
    }

    public static final class PodcastViewHolder extends RecyclerView.ViewHolder {
        static PodcastViewHolder newPodcastViewHolder(
                ViewGroup parent,
                int viewType
        ) {
            final Context context = parent.getContext();
            final View itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_podcast,
                    parent,
                    false
            );
            final ImageView artworkImageView = itemView.requireViewById(R.id.item_podcast_artwork_image_view);
            final TextView nameTextView = itemView.requireViewById(R.id.item_podcast_name_text_view);
            return new PodcastViewHolder(
                    itemView,
                    artworkImageView,
                    nameTextView
            );
        }

        final ImageView artworkImageView;
        final TextView nameTextView;

        private PodcastViewHolder(
                View itemView,
                ImageView artworkImageView,
                TextView nameTextView
        ) {
            super(itemView);

            this.artworkImageView = artworkImageView;
            this.nameTextView = nameTextView;
        }

        final void onBind(
                PodcastRecyclerViewAdapter podcastRecyclerViewAdapter,
                PodcastImpl.PodcastIdentifiedImpl podcastItem
        ) {
            nameTextView.setText(podcastItem.getName());
            itemView.setOnClickListener(
                    itemView ->
                            podcastRecyclerViewAdapter.listener.onClick(
                                    podcastItem
                            )
            );
            Picasso.get().cancelRequest(artworkImageView);
            @Nullable final URL artworkURL = podcastItem.getArtworkURL();
            if (artworkURL != null) {
                final String artworkUrlString = artworkURL.toExternalForm();
                Picasso.get().load(artworkUrlString).into(artworkImageView);
            }
        }
    }

    final PodcastRecyclerViewAdapterListener listener;

    public PodcastRecyclerViewAdapter(
            PodcastImpl.PodcastIdentifiedImpl.PodcastIdentifiedListImpl podcastItems,
            PodcastRecyclerViewAdapterListener listener
    ) {
        super(
                PodcastRecyclerViewAdapter.class,
                PodcastViewHolder::newPodcastViewHolder,
                PodcastViewHolder::onBind,
                podcastItems
        );
        this.listener = listener;
    }
}
