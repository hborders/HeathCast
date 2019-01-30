package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Podcast;

import java.util.List;

public class PodcastRecyclerViewAdapter extends RecyclerView.Adapter<PodcastRecyclerViewAdapter.PodcastViewHolder> {

    private final List<Podcast> mPodcasts;
    private final PodcastRecyclerViewAdapterListener mListener;

    public PodcastRecyclerViewAdapter(
            List<Podcast> podcasts,
            PodcastRecyclerViewAdapterListener listener
    ) {
        this.mPodcasts = podcasts;
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
        final Podcast podcast = mPodcasts.get(position);
        holder.mNameTextView.setText(podcast.mName);
        holder.itemView.setOnClickListener(itemView -> mListener.onClick(podcast));
    }

    @Override
    public int getItemCount() {
        return mPodcasts.size();
    }

    public static final class PodcastViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameTextView;

        PodcastViewHolder(View itemView) {
            super(itemView);
            this.mNameTextView = itemView.requireViewById(R.id.podcast_name);
        }
    }

    public interface PodcastRecyclerViewAdapterListener {
        void onClick(Podcast podcast);
    }
}
