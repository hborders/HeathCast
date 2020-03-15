package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;
import com.github.hborders.heathcast.services.ServiceResponse;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class RxListFragment<
        FragmentType extends RxListFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType,
                UnparcelableType,
                UnparcelableHolderType,
                UnparcelableHolderFactoryType,
                UnparcelableHolderArrayFactoryType,
                UnparcelableListType,
                UnparcelableCapacityCollectionFactoryType,
                ListRecyclerViewAdapterType,
                ViewHolderType,
                ServiceResponseType,
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        AttachmentFactoryType extends RxFragment.Attachment.Factory<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        UnparcelableType,
        UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
        UnparcelableHolderFactoryType extends UnparcelableHolder.Factory<
                UnparcelableType,
                UnparcelableHolderType
                >,
        UnparcelableHolderArrayFactoryType extends UnparcelableHolder.ArrayFactory<
                UnparcelableType,
                UnparcelableHolderType
                >,
        UnparcelableListType extends List<UnparcelableType>,
        UnparcelableCapacityCollectionFactoryType extends CollectionFactory.Capacity<
                UnparcelableListType,
                UnparcelableType
                >,
        ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                UnparcelableType,
                UnparcelableListType,
                ViewHolderType
                >,
        ViewHolderType extends RecyclerView.ViewHolder,
        ServiceResponseType extends ServiceResponse<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType,
                UnparcelableListType,
                UnparcelableListType
                >,
        ServiceResponseLoadingType extends ServiceResponse.Loading<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType,
                UnparcelableListType,
                UnparcelableListType
                >,
        ServiceResponseCompleteType extends ServiceResponse.Complete<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType,
                UnparcelableListType,
                UnparcelableListType
                >,
        ServiceResponseFailedType extends ServiceResponse.Failed<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType,
                UnparcelableListType,
                UnparcelableListType
                >
        > extends RxFragment<
        FragmentType,
        ListenerType,
        AttachmentType,
        AttachmentFactoryType
        > {

    protected interface ItemListServiceResponseObservableProvider<
            FragmentType extends RxListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    UnparcelableType,
                    UnparcelableHolderType,
                    UnparcelableHolderFactoryType,
                    UnparcelableHolderArrayFactoryType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableHolderFactoryType extends UnparcelableHolder.Factory<
                    UnparcelableType,
                    UnparcelableHolderType
                    >,
            UnparcelableHolderArrayFactoryType extends UnparcelableHolder.ArrayFactory<
                    UnparcelableType,
                    UnparcelableHolderType
                    >,
            UnparcelableListType extends List<UnparcelableType>,
            UnparcelableCapacityCollectionFactoryType extends CollectionFactory.Capacity<
                    UnparcelableListType,
                    UnparcelableType
                    >,
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    UnparcelableType,
                    UnparcelableListType,
                    ViewHolderType
                    >,
            ViewHolderType extends RecyclerView.ViewHolder,
            ServiceResponseType extends ServiceResponse<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseLoadingType extends ServiceResponse.Loading<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseCompleteType extends ServiceResponse.Complete<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseFailedType extends ServiceResponse.Failed<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >
            > {
        Observable<ServiceResponseType> itemListServiceResponseObservable(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface OnItemListServiceResponseFailed<
            FragmentType extends RxListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    UnparcelableType,
                    UnparcelableHolderType,
                    UnparcelableHolderFactoryType,
                    UnparcelableHolderArrayFactoryType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            UnparcelableType,
            UnparcelableHolderType extends UnparcelableHolder<UnparcelableType>,
            UnparcelableHolderFactoryType extends UnparcelableHolder.Factory<
                    UnparcelableType,
                    UnparcelableHolderType
                    >,
            UnparcelableHolderArrayFactoryType extends UnparcelableHolder.ArrayFactory<
                    UnparcelableType,
                    UnparcelableHolderType
                    >,
            UnparcelableListType extends List<UnparcelableType>,
            UnparcelableCapacityCollectionFactoryType extends CollectionFactory.Capacity<
                    UnparcelableListType,
                    UnparcelableType
                    >,
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    UnparcelableType,
                    UnparcelableListType,
                    ViewHolderType
                    >,
            ViewHolderType extends RecyclerView.ViewHolder,
            ServiceResponseType extends ServiceResponse<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseLoadingType extends ServiceResponse.Loading<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseCompleteType extends ServiceResponse.Complete<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >,
            ServiceResponseFailedType extends ServiceResponse.Failed<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType,
                    UnparcelableListType,
                    UnparcelableListType
                    >
            > {
        void onItemListServiceResponseFailed(
                ListenerType listener,
                FragmentType fragment
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

    private final UnparcelableHolderFactoryType unparcelableHolderFactory;
    private final UnparcelableHolderArrayFactoryType unparcelableHolderArrayFactory;
    private final UnparcelableCapacityCollectionFactoryType unparcelableCapacityCollectionFactory;
    private final ItemListServiceResponseObservableProvider<
            FragmentType,
            ListenerType,
            AttachmentType,
            AttachmentFactoryType,
            UnparcelableType,
            UnparcelableHolderType,
            UnparcelableHolderFactoryType,
            UnparcelableHolderArrayFactoryType,
            UnparcelableListType,
            UnparcelableCapacityCollectionFactoryType,
            ListRecyclerViewAdapterType,
            ViewHolderType,
            ServiceResponseType,
            ServiceResponseLoadingType,
            ServiceResponseCompleteType,
            ServiceResponseFailedType
            > itemListServiceResponseObservableProvider;
    private final OnItemListServiceResponseFailed<
            FragmentType,
            ListenerType,
            AttachmentType,
            AttachmentFactoryType,
            UnparcelableType,
            UnparcelableHolderType,
            UnparcelableHolderFactoryType,
            UnparcelableHolderArrayFactoryType,
            UnparcelableListType,
            UnparcelableCapacityCollectionFactoryType,
            ListRecyclerViewAdapterType,
            ViewHolderType,
            ServiceResponseType,
            ServiceResponseLoadingType,
            ServiceResponseCompleteType,
            ServiceResponseFailedType
            > onItemListServiceResponseFailed;
    private final Class<UnparcelableHolderType> holderClass;
    private final BehaviorSubject<Optional<ItemRanger>> itemRangerOptionalBehaviorSubject =
            BehaviorSubject.createDefault(Optional.empty());

    protected RxListFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    > onAttached,
            WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    > willDetach,
            int layoutResource,
            UnparcelableHolderFactoryType unparcelableHolderFactory,
            UnparcelableHolderArrayFactoryType unparcelableHolderArrayFactory,
            UnparcelableCapacityCollectionFactoryType unparcelableCapacityCollectionFactory,
            ItemListServiceResponseObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    UnparcelableType,
                    UnparcelableHolderType,
                    UnparcelableHolderFactoryType,
                    UnparcelableHolderArrayFactoryType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    > itemListServiceResponseObservableProvider,
            OnItemListServiceResponseFailed<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    UnparcelableType,
                    UnparcelableHolderType,
                    UnparcelableHolderFactoryType,
                    UnparcelableHolderArrayFactoryType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    > onItemListServiceResponseFailed,
            Class<UnparcelableHolderType> holderClass
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );

        this.unparcelableHolderFactory = unparcelableHolderFactory;
        this.unparcelableHolderArrayFactory = unparcelableHolderArrayFactory;
        this.unparcelableCapacityCollectionFactory = unparcelableCapacityCollectionFactory;
        this.itemListServiceResponseObservableProvider = itemListServiceResponseObservableProvider;
        this.onItemListServiceResponseFailed = onItemListServiceResponseFailed;
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

    protected abstract ListRecyclerViewAdapterType createListRecyclerViewAdapter(
            List<UnparcelableType> initialItems,
            ListenerType listener
    );

    protected abstract void onItemListServiceResponseFailed();

    protected abstract void subscribeToAttachmentObservable2(
            Observable<AttachmentType> attachmentObservable
    );

    @Override
    protected final void subscribeToAttachmentObservable(
            Observable<AttachmentType> attachmentObservable
    ) {
        switchMapToViewCreation(attachmentObservable).subscribe(
                attachmentFragmentCreationViewCreationTriple -> {
                    final Context context =
                            attachmentFragmentCreationViewCreationTriple.first.context;
                    final ListenerType listener =
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
                    final UnparcelableListType initialItems =
                            FragmentUtil.getUnparcelableListArgumentOptional(
                                    this,
                                    holderClass,
                                    unparcelableCapacityCollectionFactory,
                                    ITEM_HOLDERS_KEY
                            ).orElse(unparcelableCapacityCollectionFactory.newCollection(0));
                    final ListRecyclerViewAdapterType listRecyclerViewAdapter =
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
                                        itemListServiceResponseObservableProvider.itemListServiceResponseObservable(
                                                listener,
                                                getSelf()
                                        )
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        itemListServiceResponse -> itemListServiceResponse.act(
                                                                loading -> {
                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                    progressTextView.setVisibility(View.VISIBLE);
                                                                    errorTextView.setVisibility(View.GONE);
                                                                    recyclerView.setVisibility(View.VISIBLE);

                                                                    start.setArguments(
                                                                            argumentsBundle(
                                                                                    loading.value
                                                                            )
                                                                    );
                                                                    listRecyclerViewAdapter.setItems(
                                                                            loading.value
                                                                    );
                                                                },
                                                                complete -> {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    progressTextView.setVisibility(View.GONE);
                                                                    errorTextView.setVisibility(View.GONE);
                                                                    recyclerView.setVisibility(View.VISIBLE);

                                                                    start.setArguments(
                                                                            argumentsBundle(
                                                                                    complete.value
                                                                            )
                                                                    );
                                                                    listRecyclerViewAdapter.setItems(
                                                                            complete.value
                                                                    );
                                                                },
                                                                failed -> {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    progressTextView.setVisibility(View.GONE);
                                                                    errorTextView.setVisibility(View.VISIBLE);
                                                                    recyclerView.setVisibility(View.VISIBLE);

                                                                    start.setArguments(
                                                                            argumentsBundle(
                                                                                    failed.value
                                                                            )
                                                                    );
                                                                    listRecyclerViewAdapter.setItems(
                                                                            failed.value
                                                                    );
                                                                }
                                                        ),
                                                        throwable -> {
                                                            onItemListServiceResponseFailed();
                                                            onItemListServiceResponseFailed.onItemListServiceResponseFailed(
                                                                    listener,
                                                                    getSelf()
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

    private UnparcelableHolderType[] holderArray(UnparcelableListType items) {
        return items
                .stream()
                .map(unparcelableHolderFactory::newUnparcelableHolder)
                .toArray(unparcelableHolderArrayFactory::newUnparcelableHolderArray);
    }

    private Bundle argumentsBundle(UnparcelableListType items) {
        final Bundle argumentsBundle = new Bundle();
        argumentsBundle.putParcelableArray(
                ITEM_HOLDERS_KEY,
                holderArray(items)
        );
        return argumentsBundle;
    }
}
