package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.CollectionFactory;
import com.github.hborders.heathcast.idlingresource.MutableIdlingResource;
import com.github.hborders.heathcast.parcelables.UnparcelableHolder;
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

    private final MutableIdlingResource loadingMutableIdlingResource;
    private final MutableIdlingResource completeOrErrorMutableIdlingResource;

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
            String idlingResourceNamePrefix,
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

        loadingMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "Loading");

        completeOrErrorMutableIdlingResource = MutableIdlingResource.idle(idlingResourceNamePrefix + "CompleteOrError");

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

        final Observable<Prez> prezObservable = beginRxGraph().switchMap(
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
                            findEmptyItemsErrorView(view),
                            findNonEmptyItemsErrorView(view)
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
    public final IdlingResource getCompleteOrErrorIdlingResource() {
        return completeOrErrorMutableIdlingResource;
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

    private void markIdlingResourcesAsBusyBeforeRender() {
        loadingMutableIdlingResource.setBusy();
        completeOrErrorMutableIdlingResource.setBusy();
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

    private void updateIdlingResourcesPostRender(
            ServiceResponseType serviceResponse
    ) {
        serviceResponse.act(
                loading -> {
                    loadingMutableIdlingResource.setIdle();
                    completeOrErrorMutableIdlingResource.setBusy();
                },
                complete -> {
                    loadingMutableIdlingResource.setBusy();
                    completeOrErrorMutableIdlingResource.setIdle();
                },
                failed -> {
                    loadingMutableIdlingResource.setBusy();
                    completeOrErrorMutableIdlingResource.setIdle();
                }
        );
    }
}
