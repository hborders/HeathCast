package com.github.hborders.heathcast.views.recyclerviews;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListRecyclerViewAdapter<
        UnparcelableItemType,
        UnparcelableListType extends List<UnparcelableItemType>,
        ViewHolderType extends RecyclerView.ViewHolder
        > extends RecyclerView.Adapter<ViewHolderType> {
    private UnparcelableListType items;

    protected ListRecyclerViewAdapter(UnparcelableListType items) {
        this.items = items;
    }

    protected abstract void onBindViewHolder(
            ViewHolderType holder,
            UnparcelableItemType item
    );

    public final void setItems(UnparcelableListType items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public final void onBindViewHolder(ViewHolderType holder, int position) {
        final UnparcelableItemType item = items.get(position);
        onBindViewHolder(
                holder,
                item
        );
    }

    public final int getItemCount() {
        return items.size();
    }
}
