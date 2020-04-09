package com.github.hborders.heathcast.views.recyclerviews;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public abstract class ListRecyclerViewAdapter<
        ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                ListRecyclerViewAdapterType,
                ViewHolderType,
                ListType,
                ItemType
                >,
        ViewHolderType extends RecyclerView.ViewHolder,
        ListType extends List<ItemType>,
        ItemType
        > extends RecyclerView.Adapter<ViewHolderType> {
    protected interface OnCreateViewHolder<
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ListType,
                    ItemType
                    >,
            ViewHolderType extends RecyclerView.ViewHolder,
            ListType extends List<ItemType>,
            ItemType
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
                    ListType,
                    ItemType
                    >,
            ViewHolderType extends RecyclerView.ViewHolder,
            ListType extends List<ItemType>,
            ItemType
            > {
        void onBindViewHolder(
                ViewHolderType holder,
                ListRecyclerViewAdapterType listRecyclerViewAdapter,
                ItemType item
        );
    }

    private final Class<ListRecyclerViewAdapterType> selfClass;
    private final OnCreateViewHolder<
            ListRecyclerViewAdapterType,
            ViewHolderType,
            ListType,
            ItemType
            > onCreateViewHolder;
    private final OnBindViewHolder<
            ListRecyclerViewAdapterType,
            ViewHolderType,
            ListType,
            ItemType
            > onBindViewHolder;
    private ListType items;

    protected ListRecyclerViewAdapter(
            Class<ListRecyclerViewAdapterType> selfClass,
            OnCreateViewHolder<
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ListType,
                    ItemType
                    > onCreateViewHolder,
            OnBindViewHolder<
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ListType,
                    ItemType
                    > onBindViewHolder,
            ListType items
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

    public final void setItems(ListType items) {
        this.items = items;
        notifyDataSetChanged();
    }

    protected final ListRecyclerViewAdapterType getSelf() {
        return Objects.requireNonNull(selfClass.cast(this));
    }
}
