package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;
import com.github.hborders.heathcast.reactivex.RxUtil;
import com.github.hborders.heathcast.services.ServiceResponse1;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

import java.util.List;
import java.util.Optional;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;

public abstract class RxListFragment<
        FragmentType extends RxListFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType,
                UnparcelableType,
                UnparcelableHolderType,
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
        ServiceResponseType extends ServiceResponse1<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType
                >,
        ServiceResponseLoadingType extends ServiceResponse1.Loading<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType
                >,
        ServiceResponseCompleteType extends ServiceResponse1.Complete<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
                UnparcelableListType
                >,
        ServiceResponseFailedType extends ServiceResponse1.Failed<
                ServiceResponseLoadingType,
                ServiceResponseCompleteType,
                ServiceResponseFailedType,
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
            ServiceResponseType extends ServiceResponse1<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType
                    >,
            ServiceResponseLoadingType extends ServiceResponse1.Loading<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType
                    >,
            ServiceResponseCompleteType extends ServiceResponse1.Complete<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType
                    >,
            ServiceResponseFailedType extends ServiceResponse1.Failed<
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType,
                    UnparcelableListType
                    >
            > {
        Observable<ServiceResponseType> itemListServiceResponseObservable(
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
    private final Observable<Optional<ItemRanger>> itemRangerOptionalObservable;
    private final Observable<Boolean> loadingObservable;

    protected RxListFragment(
            Class<FragmentType> fragmentClass,
            Class<ListenerType> listenerClass,
            Class<AttachmentType> attachmentClass,
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
            UnparcelableCapacityCollectionFactoryType unparcelableCapacityCollectionFactory,
            ItemListServiceResponseObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType,
                    UnparcelableType,
                    UnparcelableHolderType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    > itemListServiceResponseObservableProvider,
            Class<UnparcelableHolderType> holderClass
    ) {
        super(
                fragmentClass,
                listenerClass,
                attachmentClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );

        final class Prez {
            final Context context;
            final ListenerType listener;
            final ViewCreation viewCreation;
            final RecyclerView recyclerView;
            final ListRecyclerViewAdapterType listRecyclerViewAdapter;
            final LinearLayoutManager linearLayoutManager;
            @Nullable
            final View emptyItemsLoadingView;
            @Nullable
            final View nonEmptyItemsLoadingView;
            @Nullable
            final View emptyItemsCompleteView;
            @Nullable
            final View emptyItemsErrorView;
            @Nullable
            final View nonEmptyItemsErrorView;

            Prez(
                    Context context,
                    ListenerType listener,
                    ViewCreation viewCreation,
                    RecyclerView recyclerView,
                    ListRecyclerViewAdapterType listRecyclerViewAdapter,
                    LinearLayoutManager linearLayoutManager,
                    @Nullable View emptyItemsLoadingView,
                    @Nullable View nonEmptyItemsLoadingView,
                    @Nullable View emptyItemsCompleteView,
                    @Nullable View emptyItemsErrorView,
                    @Nullable View nonEmptyItemsErrorView
            ) {
                this.context = context;
                this.listener = listener;
                this.viewCreation = viewCreation;
                this.recyclerView = recyclerView;
                this.listRecyclerViewAdapter = listRecyclerViewAdapter;
                this.linearLayoutManager = linearLayoutManager;
                this.emptyItemsLoadingView = emptyItemsLoadingView;
                this.nonEmptyItemsLoadingView = nonEmptyItemsLoadingView;
                this.emptyItemsCompleteView = emptyItemsCompleteView;
                this.emptyItemsErrorView = emptyItemsErrorView;
                this.nonEmptyItemsErrorView = nonEmptyItemsErrorView;
            }
        }

        final ConnectableObservable<Optional<Prez>> prezOptionalConnectableObservable = beginRxGraph().switchMap(
                Attachment::switchMapToViewCreation
        ).switchMap(
                attachmentFragmentCreationViewCreationTriple -> {
                    final Context context =
                            attachmentFragmentCreationViewCreationTriple.first.context;
                    final ListenerType listener =
                            attachmentFragmentCreationViewCreationTriple.first.listener;
                    final ViewCreation viewCreation =
                            attachmentFragmentCreationViewCreationTriple.third;
                    final View view = viewCreation.view;
                    final RecyclerView recyclerView = requireRecyclerView(view);
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
                    return Observable.just(
                            Optional.of(
                                    new Prez(
                                            context,
                                            listener,
                                            viewCreation,
                                            recyclerView,
                                            listRecyclerViewAdapter,
                                            linearLayoutManager,
                                            findEmptyItemsLoadingView(view),
                                            findNonEmptyItemsLoadingView(view),
                                            findEmptyItemsCompleteView(view),
                                            findEmptyItemsErrorView(view),
                                            findNonEmptyItemsErrorView(view)
                                    )
                            )
                    ).concatWith(Single.just(Optional.empty()));
                }
        ).publish();
        prezOptionalConnectableObservable.connect().isDisposed();
        itemRangerOptionalObservable = prezOptionalConnectableObservable.map(
                prezOptional ->
                        prezOptional.map(
                                prez ->
                                        new ItemRanger(
                                                prez.recyclerView,
                                                prez.listRecyclerViewAdapter,
                                                prez.linearLayoutManager
                                        )
                        )
        ).startWith(Optional.empty());

        final Observable<Prez> prezObervable = prezOptionalConnectableObservable.switchMapMaybe(RxUtil::maybeFromOptional);

        final class Render {
            final Prez prez;
            final Start start;
            final ServiceResponseType itemListServiceResponse;

            Render(
                    Prez prez,
                    Start start,
                    ServiceResponseType itemListServiceResponse
            ) {
                this.prez = prez;
                this.start = start;
                this.itemListServiceResponse = itemListServiceResponse;
            }
        }

        final Observable<Render> renderObservable =
                prezObervable.switchMap(
                        prez ->
                                prez.viewCreation.switchMapToStart().switchMap(
                                        start ->
                                                itemListServiceResponseObservableProvider
                                                        .itemListServiceResponseObservable(
                                                                prez.listener,
                                                                getSelf()
                                                        )
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .map(
                                                                itemListServiceResponse ->
                                                                        new Render(
                                                                                prez,
                                                                                start,
                                                                                itemListServiceResponse
                                                                        )
                                                        )
                                )
                );


        // Order matters here.
        // We want to subscribe directly to itemListServiceResponseObservable
        // from eagerLoadingConnectableObservable, then RendererObservable, then lazyNotLoadingConnectableObservable
        // because we want eagerLoadingConnectableObservable to be reported first, then RendererObserable,
        // then lazyNotLoadingConnectableObservable
        // If we try to abstract eagerLoadingConnectableObservable and lazyNotLoadingConnectableObservable into a one
        // common Observable, we'll only have a single subscription, and we won't report
        // not loading after Render, which is what we want.
        final ConnectableObservable<Boolean> eagerLoadingConnectableObservable = renderObservable
                .switchMapMaybe(
                        render -> {
                            if (isServiceResponseLoading(render.itemListServiceResponse)) {
                                return Maybe.just(true);
                            } else {
                                return Maybe.empty();
                            }
                        }
                )
                .publish();
        eagerLoadingConnectableObservable.connect().isDisposed();

        renderObservable.subscribe(
                render -> {
                    setEmptyItemsLoadingViewVisibility(
                            render.prez.emptyItemsLoadingView,
                            render.itemListServiceResponse
                    );
                    setNonEmptyItemsLoadingViewVisibility(
                            render.prez.nonEmptyItemsLoadingView,
                            render.itemListServiceResponse
                    );
                    setEmptyItemsCompleteViewVisibility(
                            render.prez.emptyItemsCompleteView,
                            render.itemListServiceResponse
                    );
                    setEmptyItemsErrorViewVisibility(
                            render.prez.emptyItemsErrorView,
                            render.itemListServiceResponse
                    );
                    setNonEmptyItemsErrorViewVisibility(
                            render.prez.nonEmptyItemsErrorView,
                            render.itemListServiceResponse
                    );
                    render.prez.listRecyclerViewAdapter.setItems(
                            render.itemListServiceResponse.getValue()
                    );
                }
        ).isDisposed();

        final ConnectableObservable<Boolean> lazyNotLoadingConnectableObservable = renderObservable.switchMapMaybe(
                render -> {
                    if (isServiceResponseLoading(render.itemListServiceResponse)) {
                        return Maybe.empty();
                    } else {
                        return Maybe.just(false);
                    }
                }
        ).publish();
        lazyNotLoadingConnectableObservable.connect().isDisposed();
        loadingObservable = eagerLoadingConnectableObservable.mergeWith(lazyNotLoadingConnectableObservable);
    }

    public final Observable<Optional<ItemRange>> getItemRangeOptionalObservable() {
        return itemRangerOptionalObservable.switchMap(podcastIdentifiedsItemRangerOptional ->
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

    public Observable<Boolean> getLoadingObservable() {
        return loadingObservable;
    }

    @Nullable
    protected abstract View findEmptyItemsLoadingView(View view);

    @Nullable
    protected abstract View findNonEmptyItemsLoadingView(View view);

    @Nullable
    protected abstract View findEmptyItemsCompleteView(View view);

    @Nullable
    protected abstract View findEmptyItemsErrorView(View view);

    @Nullable
    protected abstract View findNonEmptyItemsErrorView(View view);

    protected abstract RecyclerView requireRecyclerView(View view);

    protected abstract ListRecyclerViewAdapterType createListRecyclerViewAdapter(
            UnparcelableListType initialItems,
            ListenerType listener
    );

    private boolean isServiceResponseLoading(ServiceResponseType serviceResponse) {
        return serviceResponse.reduce(
                loading -> true,
                complete -> false,
                failed -> false
        );
    }

    private void setEmptyItemsLoadingViewVisibility(
            @Nullable View emptyItemsLoadingView,
            ServiceResponseType serviceResponse
    ) {
        if (emptyItemsLoadingView != null) {
            emptyItemsLoadingView.setVisibility(
                    serviceResponse.reduce(
                            loading -> loading.value.isEmpty() ? View.VISIBLE : View.GONE,
                            complete -> View.GONE,
                            failed -> View.GONE
                    )
            );
        }
    }

    private void setNonEmptyItemsLoadingViewVisibility(
            @Nullable View nonEmptyItemsLoadingView,
            ServiceResponseType serviceResponse
    ) {
        if (nonEmptyItemsLoadingView != null) {
            nonEmptyItemsLoadingView.setVisibility(
                    serviceResponse.reduce(
                            loading -> loading.value.isEmpty() ? View.GONE : View.VISIBLE,
                            complete -> View.GONE,
                            failed -> View.GONE
                    )
            );
        }
    }

    private void setEmptyItemsCompleteViewVisibility(
            @Nullable View emptyItemsCompleteView,
            ServiceResponseType serviceResponse
    ) {
        if (emptyItemsCompleteView != null) {
            emptyItemsCompleteView.setVisibility(
                    serviceResponse.reduce(
                            loading -> View.GONE,
                            complete -> complete.value.isEmpty() ? View.VISIBLE : View.GONE,
                            failed -> View.GONE
                    )
            );
        }
    }

    private void setEmptyItemsErrorViewVisibility(
            @Nullable View emptyItemsErrorView,
            ServiceResponseType serviceResponse
    ) {
        if (emptyItemsErrorView != null) {
            emptyItemsErrorView.setVisibility(
                    serviceResponse.reduce(
                            loading -> View.GONE,
                            complete -> View.GONE,
                            failed -> failed.value.isEmpty() ? View.VISIBLE : View.GONE
                    )
            );
        }
    }

    private void setNonEmptyItemsErrorViewVisibility(
            @Nullable View nonEmptyItemsErrorView,
            ServiceResponseType serviceResponse
    ) {
        if (nonEmptyItemsErrorView != null) {
            nonEmptyItemsErrorView.setVisibility(
                    serviceResponse.reduce(
                            loading -> View.GONE,
                            complete -> View.GONE,
                            failed -> failed.value.isEmpty() ? View.GONE : View.VISIBLE
                    )
            );
        }
    }
}
