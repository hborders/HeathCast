package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.core.EmptyEither6;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;
import com.github.hborders.heathcast.reactivex.RxObservableUtil;
import com.github.hborders.heathcast.services.ServiceResponse1;
import com.github.hborders.heathcast.views.recyclerviews.ItemRange;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

import java.util.List;
import java.util.Optional;

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

    public interface Mode {
        Mode LOADING_EMPTY = new LoadingEmpty();
        Mode LOADING_NON_EMPTY = new LoadingNonEmpty();
        Mode COMPLETE_EMPTY = new CompleteEmpty();
        Mode COMPLETE_NON_EMPTY = new CompleteNonEmpty();
        Mode ERROR_EMPTY = new ErrorEmpty();
        Mode ERROR_NON_EMPTY = new ErrorNonEmpty();

        final class LoadingEmpty extends EmptyEither6.LeftLeftLeft<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        final class LoadingNonEmpty extends EmptyEither6.LeftLeft<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        final class CompleteEmpty extends EmptyEither6.Left<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        final class CompleteNonEmpty extends EmptyEither6.Right<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        final class ErrorEmpty extends EmptyEither6.RightRight<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        final class ErrorNonEmpty extends EmptyEither6.RightRightRight<
                LoadingEmpty,
                LoadingNonEmpty,
                CompleteEmpty,
                CompleteNonEmpty,
                ErrorEmpty,
                ErrorNonEmpty
                > implements Mode {
        }

        <T> T reduce(
                Function<
                        ? super LoadingEmpty,
                        ? extends T
                        > leftLeftReducer,
                Function<
                        ? super LoadingNonEmpty,
                        ? extends T
                        > leftReducer,
                Function<
                        ? super CompleteEmpty,
                        ? extends T
                        > middleReducer,
                Function<
                        ? super CompleteNonEmpty,
                        ? extends T
                        > rightReducer,
                Function<
                        ? super ErrorEmpty,
                        ? extends T
                        > rightRightReducer,
                Function<
                        ? super ErrorNonEmpty,
                        ? extends T
                        > rightRightRightReducer
        );

        void act(
                VoidFunction<? super LoadingEmpty> leftLeftAction,
                VoidFunction<? super LoadingNonEmpty> leftAction,
                VoidFunction<? super CompleteEmpty> middleAction,
                VoidFunction<? super CompleteNonEmpty> rightAction,
                VoidFunction<? super ErrorEmpty> rightRightAction,
                VoidFunction<? super ErrorNonEmpty> rightRightRightAction
        );
    }

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

        public Observable<ItemRange> itemRangeObservable() {
            return Observable.create(
                    emitter -> {
                        final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                final int itemCount = recyclerViewAdapter.getItemCount();
                                final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                final ItemRange itemRange;
                                if (
                                        (firstVisibleItemPosition == RecyclerView.NO_POSITION) ||
                                                (recyclerView.getVisibility() != View.VISIBLE)
                                ) {
                                    itemRange = ItemRange.invisible(itemCount);
                                } else {
                                    final int lastVisibleItemPosition =
                                            linearLayoutManager.findLastVisibleItemPosition();
                                    if (lastVisibleItemPosition == RecyclerView.NO_POSITION) {
                                        throw new IllegalStateException(
                                                "A firstVisibleItemPosition: "
                                                        + firstVisibleItemPosition
                                                        + " should imply a " +
                                                        "lastVisibleItemPosition"
                                        );
                                    } else {
                                        itemRange = ItemRange.visible(
                                                itemCount,
                                                firstVisibleItemPosition,
                                                lastVisibleItemPosition
                                        );
                                    }
                                }
                                emitter.onNext(itemRange);
                            }
                        };
                        recyclerView.addOnScrollListener(onScrollListener);
                        emitter.setCancellable(() ->
                                recyclerView.removeOnScrollListener(onScrollListener)
                        );
                    }
            );
        }
    }

    private final Observable<Optional<ItemRanger>> itemRangerOptionalObservable;
    private final Observable<Mode> modeObservable;

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
                    final ListRecyclerViewAdapterType listRecyclerViewAdapter =
                            createListRecyclerViewAdapter(
                                    unparcelableCapacityCollectionFactory.newCollection(0),
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

        final Observable<Prez> prezObervable = prezOptionalConnectableObservable.switchMapMaybe(RxObservableUtil::maybeFromOptional);

        final class Render {
            final Prez prez;
            final ServiceResponseType itemListServiceResponse;

            Render(
                    Prez prez,
                    ServiceResponseType itemListServiceResponse
            ) {
                this.prez = prez;
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
                                                                                itemListServiceResponse
                                                                        )
                                                        )
                                )
                );

        // Order matters here.
        // We want to subscribe directly to itemListServiceResponseObservable
        // from preRenderModeObservable, then RendererObservable, then postRenderModeConnectableObservable
        // because we want preRenderModeObservable to be reported first, then RendererObserable,
        // then postRenderModeConnectableObservable
        // If we try to abstract preRenderModeObservable and postRenderModeConnectableObservable into a one
        // common Observable, we'll only have a single subscription, and we won't report
        // our rendered TestingState after Render, which is what we want.

//        final Observable<Mode> preRenderModeObservable = renderObservable
//                .map(
//                        render ->
//                                Mode.RENDERING
//                );
        final ConnectableObservable<Mode> postRenderModeConnectableObservable =
                renderObservable.map(
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
                            setRecyclerViewItemsAndVisibility(
                                    render.prez.recyclerView,
                                    render.prez.listRecyclerViewAdapter,
                                    render.itemListServiceResponse
                            );
                            return modeFromServiceResponse(render.itemListServiceResponse);
                        }
                ).publish();
        postRenderModeConnectableObservable.connect().isDisposed();

        modeObservable = postRenderModeConnectableObservable;
    }

    public final Observable<Optional<ItemRange>> getItemRangeOptionalObservable() {
        return itemRangerOptionalObservable.switchMap(
                podcastIdentifiedsItemRangerOptional ->
                        podcastIdentifiedsItemRangerOptional
                                .map(
                                        itemRanger ->
                                                itemRanger.itemRangeObservable()
                                                        .map(Optional::of)
                                )
                                .orElse(Observable.just(Optional.empty()))
        );
    }

    public final Observable<Mode> getModeObservable() {
        return modeObservable;
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

    private Mode modeFromServiceResponse(ServiceResponseType serviceResponse) {
        return serviceResponse.reduce(
                loading -> loading.value.isEmpty() ? Mode.LOADING_EMPTY : Mode.LOADING_NON_EMPTY,
                complete -> complete.value.isEmpty() ? Mode.COMPLETE_EMPTY : Mode.COMPLETE_NON_EMPTY,
                failed -> failed.value.isEmpty() ? Mode.ERROR_EMPTY : Mode.ERROR_NON_EMPTY
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

    private void setRecyclerViewItemsAndVisibility(
            RecyclerView recyclerView,
            ListRecyclerViewAdapterType listRecyclerViewAdapter,
            ServiceResponseType serviceResponse
    ) {
        final UnparcelableListType items = serviceResponse.getValue();
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        listRecyclerViewAdapter.setItems(items);
    }
}
