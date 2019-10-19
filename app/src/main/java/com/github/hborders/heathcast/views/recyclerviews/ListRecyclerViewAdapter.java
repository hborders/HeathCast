package com.github.hborders.heathcast.views.recyclerviews;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListRecyclerViewAdapter<
        U,
        VH extends RecyclerView.ViewHolder
        > extends RecyclerView.Adapter<VH> {
    private List<U> items;

    protected ListRecyclerViewAdapter(List<U> items) {
        this.items = items;
    }

    protected abstract void onBindViewHolder(
            VH holder,
            U item
    );

    public final void setItems(List<U> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        final U item = items.get(position);
        onBindViewHolder(
                holder,
                item
        );
    }

    public final int getItemCount() {
        return items.size();
    }
}
