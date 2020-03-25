package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.services.ServiceResponse1;
import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class RxListFragment<
        FragmentType extends RxListFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                UnparcelableType,
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
                AttachmentType
                >,
        UnparcelableType,
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
        AttachmentType
        > {

    protected interface ItemListServiceResponseObservableProvider<
            FragmentType extends RxListFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    UnparcelableType,
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
                    AttachmentType
                    >,
            UnparcelableType,
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

    private final MutableIdlingResource loadingMutableIdlingResource;
    private final MutableIdlingResource completeOrFailedMutableIdlingResource;

    protected <
            AttachmentFactoryType extends Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            OnAttachedType extends OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            WillDetachType extends WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >
            > RxListFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            UnparcelableCapacityCollectionFactoryType unparcelableCapacityCollectionFactory,
            ItemListServiceResponseObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    UnparcelableType,
                    UnparcelableListType,
                    UnparcelableCapacityCollectionFactoryType,
                    ListRecyclerViewAdapterType,
                    ViewHolderType,
                    ServiceResponseType,
                    ServiceResponseLoadingType,
                    ServiceResponseCompleteType,
                    ServiceResponseFailedType
                    > itemListServiceResponseObservableProvider
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource
        );

        loadingMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading");

        completeOrFailedMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrFailed");

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
            final View emptyItemsFailedView;
            @Nullable
            final View nonEmptyItemsFailedView;

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
                    @Nullable View emptyItemsFailedView,
                    @Nullable View nonEmptyItemsFailedView
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
                this.emptyItemsFailedView = emptyItemsFailedView;
                this.nonEmptyItemsFailedView = nonEmptyItemsFailedView;
            }
        }

        final Observable<Prez> prezObservable = beginRxGraph().switchMap(
                attachmentTypeObservable -> attachmentTypeObservable
        ).switchMap(
                Attachment::switchMapToViewCreation
        ).map(
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
                    return new Prez(
                            context,
                            listener,
                            viewCreation,
                            recyclerView,
                            listRecyclerViewAdapter,
                            linearLayoutManager,
                            findEmptyItemsLoadingView(view),
                            findNonEmptyItemsLoadingView(view),
                            findEmptyItemsCompleteView(view),
                            findEmptyItemsFailedView(view),
                            findNonEmptyItemsFailedView(view)
                    );
                }
        );

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
                prezObservable.switchMap(
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
        renderObservable.subscribe(
                render -> {
                    markIdlingResourcesAsBusyBeforeRender();

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
                    setEmptyItemsFailedViewVisibility(
                            render.prez.emptyItemsFailedView,
                            render.itemListServiceResponse
                    );
                    setNonEmptyItemsFailedViewVisibility(
                            render.prez.nonEmptyItemsFailedView,
                            render.itemListServiceResponse
                    );
                    setRecyclerViewItemsAndVisibility(
                            render.prez.recyclerView,
                            render.prez.listRecyclerViewAdapter,
                            render.itemListServiceResponse
                    );

                    updateIdlingResourcesPostRender(
                            render.itemListServiceResponse
                    );
                }
        ).isDisposed();
    }

    /**
     * Useful for testing loading states
     */
    public final IdlingResource getLoadingIdlingResource() {
        return loadingMutableIdlingResource;
    }

    /**
     * Useful for testing not loading states
     */
    public final IdlingResource getCompleteOrFailedIdlingResource() {
        return completeOrFailedMutableIdlingResource;
    }

    @Nullable
    protected abstract View findEmptyItemsLoadingView(View view);

    @Nullable
    protected abstract View findNonEmptyItemsLoadingView(View view);

    @Nullable
    protected abstract View findEmptyItemsCompleteView(View view);

    @Nullable
    protected abstract View findEmptyItemsFailedView(View view);

    @Nullable
    protected abstract View findNonEmptyItemsFailedView(View view);

    protected abstract RecyclerView requireRecyclerView(View view);

    protected abstract ListRecyclerViewAdapterType createListRecyclerViewAdapter(
            UnparcelableListType initialItems,
            ListenerType listener
    );

    private void markIdlingResourcesAsBusyBeforeRender() {
        loadingMutableIdlingResource.setBusy();
        completeOrFailedMutableIdlingResource.setBusy();
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

    private void setEmptyItemsFailedViewVisibility(
            @Nullable View emptyItemsFailedView,
            ServiceResponseType serviceResponse
    ) {
        if (emptyItemsFailedView != null) {
            emptyItemsFailedView.setVisibility(
                    serviceResponse.reduce(
                            loading -> View.GONE,
                            complete -> View.GONE,
                            failed -> failed.value.isEmpty() ? View.VISIBLE : View.GONE
                    )
            );
        }
    }

    private void setNonEmptyItemsFailedViewVisibility(
            @Nullable View nonEmptyItemsFailedView,
            ServiceResponseType serviceResponse
    ) {
        if (nonEmptyItemsFailedView != null) {
            nonEmptyItemsFailedView.setVisibility(
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

    private void updateIdlingResourcesPostRender(
            ServiceResponseType serviceResponse
    ) {
        serviceResponse.act(
                loading -> {
                    loadingMutableIdlingResource.setIdle();
                    completeOrFailedMutableIdlingResource.setBusy();
                },
                complete -> {
                    loadingMutableIdlingResource.setBusy();
                    completeOrFailedMutableIdlingResource.setIdle();
                },
                failed -> {
                    loadingMutableIdlingResource.setBusy();
                    completeOrFailedMutableIdlingResource.setIdle();
                }
        );
    }
}
