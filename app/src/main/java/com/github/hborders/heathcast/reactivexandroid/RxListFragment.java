package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.AsyncValue;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class RxListFragment<
        F extends RxListFragment<F, L, U, H>,
        L,
        U,
        H extends UnparcelableHolder<U>
        > extends RxFragment<F, L> {

    protected interface ItemListAsyncValueObservableProvider<
            F extends RxListFragment<F, L, U, H>,
            L,
            U,
            H extends UnparcelableHolder<U>
            > {
        Observable<? extends AsyncValue<? extends List<U>>> itemListAsyncValueObservable(
                L listener,
                F fragment
        );
    }

    protected interface OnItemListAsyncValueError<
            F extends RxListFragment<F, L, U, H>,
            L,
            U,
            H extends UnparcelableHolder<U>
            > {
        void onItemListAsyncValueError(
                L listener,
                F fragment,
                Throwable throwable
        );
    }

    private static final class ItemRanger {
        private final RecyclerView recyclerView;
        private final RecyclerView.Adapter<?> recyclerViewAdapter;
        private final LinearLayoutManager linearLayoutManager;

        private ItemRanger(
                RecyclerView recyclerView,
                RecyclerView.Adapter<?> recyclerViewAdapter,
                LinearLayoutManager linearLayoutManager
        ) {
            this.recyclerView = recyclerView;
            this.recyclerViewAdapter = recyclerViewAdapter;
            this.linearLayoutManager = linearLayoutManager;
        }

        @Override
        public String toString() {
            return "ItemRanger{" +
                    "recyclerView=" + recyclerView +
                    ", recyclerViewAdapter=" + recyclerViewAdapter +
                    ", linearLayoutManager=" + linearLayoutManager +
                    '}';
        }
    }

    private static final String ITEM_HOLDERS_KEY = "itemHolders";

    private final ItemListAsyncValueObservableProvider<F, L, U, H> itemListAsyncValueObservableProvider;
    private final OnItemListAsyncValueError<F, L, U, H> onItemListAsyncValueError;
    private final Class<H> holderClass;
    private final BehaviorSubject<Optional<ItemRanger>> itemRangerOptionalBehaviorSubject =
            BehaviorSubject.createDefault(Optional.empty());

    protected RxListFragment(
            Class<L> listenerClass,
            OnAttached<F, L> onAttached,
            WillDetach<F, L> willDetach,
            int layoutResource,
            ItemListAsyncValueObservableProvider<F, L, U, H> itemListAsyncValueObservableProvider,
            OnItemListAsyncValueError<F, L, U, H> onItemListAsyncValueError,
            Class<H> holderClass
    ) {
        super(
                listenerClass,
                onAttached,
                willDetach,
                layoutResource
        );

        this.itemListAsyncValueObservableProvider = itemListAsyncValueObservableProvider;
        this.onItemListAsyncValueError = onItemListAsyncValueError;
        this.holderClass = holderClass;
    }

    public final Observable<Optional<ItemRange>> getItemRangeOptionalObservable() {
        return itemRangerOptionalBehaviorSubject.switchMap(podcastIdentifiedsItemRangerOptional ->
                podcastIdentifiedsItemRangerOptional.map(itemRanger ->
                        Observable.<Optional<ItemRange>>create(emitter -> {
                            final RecyclerView recyclerView = itemRanger.recyclerView;
                            final LinearLayoutManager linearLayoutManager = itemRanger.linearLayoutManager;
                            final RecyclerView.Adapter<?> recyclerViewAdapter = itemRanger.recyclerViewAdapter;

                            final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    final int itemCount = recyclerViewAdapter.getItemCount();
                                    final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    final Optional<ItemRange> itemRangeOptional;
                                    if (firstVisibleItemPosition == RecyclerView.NO_POSITION) {
                                        itemRangeOptional = Optional.of(
                                                ItemRange.invisible(itemCount)
                                        );
                                    } else {
                                        final int lastVisibleItemPosition =
                                                itemRanger.linearLayoutManager
                                                        .findLastVisibleItemPosition();
                                        if (lastVisibleItemPosition == RecyclerView.NO_POSITION) {
                                            throw new IllegalStateException(
                                                    "A firstVisibleItemPosition: "
                                                            + firstVisibleItemPosition
                                                            + " should imply a " +
                                                            "lastVisibleItemPosition"
                                            );
                                        } else {
                                            itemRangeOptional = Optional.of(
                                                    ItemRange.visible(
                                                            itemCount,
                                                            firstVisibleItemPosition,
                                                            lastVisibleItemPosition
                                                    )
                                            );
                                        }
                                    }
                                    emitter.onNext(itemRangeOptional);
                                }
                            };
                            recyclerView.addOnScrollListener(onScrollListener);
                            emitter.setCancellable(() ->
                                    recyclerView.removeOnScrollListener(onScrollListener)
                            );
                        })
                ).orElse(Observable.empty()));
    }


    protected abstract ProgressBar requireProgressBar(View view);

    protected abstract TextView requireProgressTextView(View view);

    protected abstract TextView requireErrorTextView(View view);

    protected abstract RecyclerView requireRecyclerView(View view);

    protected abstract ListRecyclerViewAdapter<U, ?> createListRecyclerViewAdapter(
            List<U> initialItems,
            L listener
    );


    protected abstract H[] holderArray(List<U> items);


    protected abstract void onItemListAsyncValueError(Throwable throwable);

    protected abstract void subscribeToAttachmentObservable2(
            Observable<Attachment<F, L>> attachmentObservable
    );

    @Override
    protected void subscribeToAttachmentObservable(
            Observable<Attachment<F, L>> attachmentObservable
    ) {
        switchMapToViewCreation(attachmentObservable).subscribe(
                attachmentFragmentCreationViewCreationTriple -> {
                    final Context context =
                            attachmentFragmentCreationViewCreationTriple.first.context;
                    final L listener =
                            attachmentFragmentCreationViewCreationTriple.first.listener;
                    final ViewCreation viewCreation =
                            attachmentFragmentCreationViewCreationTriple.third;

                    final ProgressBar progressBar = requireProgressBar(viewCreation.view);
                    final TextView progressTextView = requireProgressTextView(viewCreation.view);
                    final TextView errorTextView = requireErrorTextView(viewCreation.view);
                    final RecyclerView recyclerView = requireRecyclerView(viewCreation.view);
                    final LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    final List<U> initialItems =
                            FragmentUtil.getUnparcelableHolderListArgumentOptional(
                                    this,
                                    holderClass,
                                    ITEM_HOLDERS_KEY
                            ).orElse(Collections.emptyList());
                    final ListRecyclerViewAdapter<U, ?> listRecyclerViewAdapter =
                            createListRecyclerViewAdapter(
                                    initialItems,
                                    listener
                            );
                    recyclerView.setAdapter(listRecyclerViewAdapter);
                    itemRangerOptionalBehaviorSubject.onNext(
                            Optional.of(
                                    new ItemRanger(
                                            recyclerView,
                                            listRecyclerViewAdapter,
                                            linearLayoutManager
                                    )
                            )
                    );

                    viewCreation.switchMapToStart().subscribe(
                            start -> {
                                final Disposable adapterSetPodcastIdentifiedsDisposable =
                                        itemListAsyncValueObservableProvider.itemListAsyncValueObservable(
                                                listener,
                                                getSelf()
                                        )
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        asyncValue ->
                                                                asyncValue.act(
                                                                        listLoading -> {
                                                                            progressBar.setVisibility(View.VISIBLE);
                                                                            progressTextView.setVisibility(View.VISIBLE);
                                                                            errorTextView.setVisibility(View.GONE);
                                                                            recyclerView.setVisibility(View.GONE);
                                                                        },
                                                                        listFailed -> {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            progressTextView.setVisibility(View.GONE);
                                                                            errorTextView.setVisibility(View.VISIBLE);
                                                                            recyclerView.setVisibility(View.GONE);
                                                                        },
                                                                        listLoadedButUpdating -> {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            progressTextView.setVisibility(View.GONE);
                                                                            errorTextView.setVisibility(View.GONE);
                                                                            recyclerView.setVisibility(View.VISIBLE);

                                                                            start.setArguments(
                                                                                    argumentsBundle(
                                                                                            listLoadedButUpdating.value
                                                                                    )
                                                                            );
                                                                            listRecyclerViewAdapter.setItems(
                                                                                    listLoadedButUpdating.value
                                                                            );
                                                                        },
                                                                        listLoadedButUpdateFailed -> {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            progressTextView.setVisibility(View.GONE);
                                                                            errorTextView.setVisibility(View.GONE);
                                                                            recyclerView.setVisibility(View.VISIBLE);

                                                                            start.setArguments(
                                                                                    argumentsBundle(
                                                                                            listLoadedButUpdateFailed.value
                                                                                    )
                                                                            );
                                                                            listRecyclerViewAdapter.setItems(
                                                                                    listLoadedButUpdateFailed.value
                                                                            );
                                                                        },
                                                                        listLoaded -> {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            progressTextView.setVisibility(View.GONE);
                                                                            errorTextView.setVisibility(View.GONE);
                                                                            recyclerView.setVisibility(View.VISIBLE);

                                                                            start.setArguments(
                                                                                    argumentsBundle(
                                                                                            listLoaded.value
                                                                                    )
                                                                            );
                                                                            listRecyclerViewAdapter.setItems(
                                                                                    listLoaded.value
                                                                            );
                                                                        }
                                                                ),
                                                        throwable -> {
                                                            onItemListAsyncValueError(throwable);
                                                            onItemListAsyncValueError.onItemListAsyncValueError(
                                                                    listener,
                                                                    getSelf(),
                                                                    throwable
                                                            );
                                                        }
                                                );
                                start.onStopCompletable.subscribe(
                                        adapterSetPodcastIdentifiedsDisposable::dispose
                                ).isDisposed();
                            }
                    ).isDisposed();

                    viewCreation.onDestroyViewCompletable.subscribe(
                            () -> itemRangerOptionalBehaviorSubject.onNext(Optional.empty())
                    ).isDisposed();
                }
        ).isDisposed();
        subscribeToAttachmentObservable2(attachmentObservable);
    }

    private Bundle argumentsBundle(List<U> items) {
        final Bundle argumentsBundle = new Bundle();
        argumentsBundle.putParcelableArray(
                ITEM_HOLDERS_KEY,
                holderArray(items)
        );
        return argumentsBundle;
    }
}
