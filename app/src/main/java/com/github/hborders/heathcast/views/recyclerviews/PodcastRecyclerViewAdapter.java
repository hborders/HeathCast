package com.github.hborders.heathcast.views.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.models.Podcast;

import java.util.List;

public class PodcastRecyclerViewAdapter extends RecyclerView.Adapter<PodcastRecyclerViewAdapter.PodcastViewHolder> {

    private final List<Podcast> podcasts;

    public PodcastRecyclerViewAdapter(List<Podcast> podcasts) {
        this.podcasts = podcasts;
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
        final Podcast podcast = podcasts.get(position);
        holder.nameTextView.setText(podcast.name);
    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;

        public PodcastViewHolder(View itemView) {
            super(itemView);
            this.nameTextView = itemView.requireViewById(R.id.podcast_name);
        }
    }
}
