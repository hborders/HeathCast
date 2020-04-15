package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public abstract class PodcastRecyclerViewAdapter<
        PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                PodcastRecyclerViewAdapterType,
                PodcastViewHolderType,
                PodcastListType,
                PodcastItemType,
                ListenerType
                >,
        PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                PodcastRecyclerViewAdapterType,
                PodcastViewHolderType,
                PodcastListType,
                PodcastItemType,
                ListenerType
                >,
        PodcastListType extends List<PodcastItemType>,
        PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem,
        ListenerType
        > extends ListRecyclerViewAdapter<
        PodcastRecyclerViewAdapterType,
        PodcastViewHolderType,
        PodcastItemType
        > {
    public interface PodcastItem {
        String getName();

        @Nullable
        URL getArtworkURL();
    }

    protected interface OnClick<
            ListenerType,
            PodcastItemType
            > {
        void onClick(
                ListenerType listener,
                PodcastItemType podcastItem
        );
    }

    final ListenerType listener;
    final OnClick<
            ListenerType,
            PodcastItemType
            > onClick;

    protected PodcastRecyclerViewAdapter(
            Class<PodcastRecyclerViewAdapterType> selfClass,
            OnCreateViewHolder<PodcastViewHolderType> onCreateViewHolder,
            PodcastListType podcastItems,
            ListenerType listener,
            OnClick<
                    ListenerType,
                    PodcastItemType
                    > onClick
    ) {
        super(
                selfClass,
                onCreateViewHolder,
                PodcastViewHolder::onBind,
                podcastItems
        );
        this.listener = listener;
        this.onClick = onClick;
    }

    public static abstract class PodcastViewHolder<
            PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem,
            ListenerType
            > extends RecyclerView.ViewHolder {
        final ImageView artworkImageView;
        final TextView nameTextView;

        protected PodcastViewHolder(
                ViewGroup parent,
                int viewType
        ) {
            this(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_podcast,
                            parent,
                            false
                    )
            );
        }

        private PodcastViewHolder(View itemView) {
            super(itemView);
            this.artworkImageView = itemView.requireViewById(R.id.item_podcast_artwork_image_view);
            this.nameTextView = itemView.requireViewById(R.id.item_podcast_name_text_view);
        }

        final void onBind(
                PodcastRecyclerViewAdapterType podcastRecyclerViewAdapter,
                PodcastItemType podcastItem
        ) {
            nameTextView.setText(podcastItem.getName());
            itemView.setOnClickListener(
                    itemView ->
                            podcastRecyclerViewAdapter.onClick.onClick(
                                    podcastRecyclerViewAdapter.listener,
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
}
