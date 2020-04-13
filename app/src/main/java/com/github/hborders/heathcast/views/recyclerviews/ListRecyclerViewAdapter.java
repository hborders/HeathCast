package com.github.hborders.heathcast.views.recyclerviews;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public abstract class ListRecyclerViewAdapter<
        ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                ListRecyclerViewAdapterType,
                ViewHolderType,
                ItemType
                >,
        ViewHolderType extends RecyclerView.ViewHolder,
        ItemType
        > extends RecyclerView.Adapter<ViewHolderType> {
    protected interface OnCreateViewHolder<
            ViewHolderType extends RecyclerView.ViewHolder
            > {
        ViewHolderType onCreateViewHolder(
                ViewGroup parent,
                int viewType
        );
    }

    protected interface OnBindViewHolder<
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ItemType
                    >,
            ViewHolderType extends RecyclerView.ViewHolder,
            ItemType
            > {
        void onBindViewHolder(
                ViewHolderType holder,
                ListRecyclerViewAdapterType listRecyclerViewAdapter,
                ItemType item
        );
    }

    private final Class<ListRecyclerViewAdapterType> selfClass;
    private final OnCreateViewHolder<ViewHolderType> onCreateViewHolder;
    private final OnBindViewHolder<
            ListRecyclerViewAdapterType,
            ViewHolderType,
            ItemType
            > onBindViewHolder;
    private List<ItemType> items;

    protected ListRecyclerViewAdapter(
            Class<ListRecyclerViewAdapterType> selfClass,
            OnCreateViewHolder<ViewHolderType> onCreateViewHolder,
            OnBindViewHolder<
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ItemType
                    > onBindViewHolder,
            List<ItemType> items
    ) {
        this.selfClass = selfClass;
        this.onCreateViewHolder = onCreateViewHolder;
        this.onBindViewHolder = onBindViewHolder;
        this.items = items;
    }

    @Override
    public ViewHolderType onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        return onCreateViewHolder.onCreateViewHolder(
                parent,
                viewType);
    }

    @Override
    public final void onBindViewHolder(ViewHolderType holder, int position) {
        final ItemType item = items.get(position);
        onBindViewHolder.onBindViewHolder(
                holder,
                getSelf(),
                item
        );
    }

    public final int getItemCount() {
        return items.size();
    }

    public final void setItems(List<ItemType> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    protected final ListRecyclerViewAdapterType getSelf() {
        return Objects.requireNonNull(selfClass.cast(this));
    }
}
