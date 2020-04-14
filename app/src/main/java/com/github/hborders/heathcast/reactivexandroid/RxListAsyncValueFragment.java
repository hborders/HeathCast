package com.github.hborders.heathcast.reactivexandroid;

import java.util.List;

// Extract EmptyLoading, NonEmptyLoading, EmptyComplete, NonEmptyComplete,
// EmptyFailed, NonEmptyFailed into RxAsyncValue
// RxAsyncValueFragment can have its own ViewFacade that accepts a value
// and RxListAsyncValueFragment can specify that to be a List
public abstract class RxListAsyncValueFragment<
        FragmentType extends RxListAsyncValueFragment<
                FragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType
                >
        > extends RxAsyncValueFragment<
        FragmentType,
        ListenerType,
        AttachmentType
        > {
    protected <
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueStateType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueListType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueListType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueListType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueListType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            AsyncValueRendererType extends ValueRenderer<
                    ValueViewFacadeType,
                    AsyncValueStateType,
                    AsyncValueViewFacadeTransactionType
                    >,
            AsyncValueViewFacadeTransactionType extends ValueViewFacadeTransaction
            > RxListAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            Attachment.AttachmentFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > attachmentFactory,
            OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > onAttached,
            WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            AsyncValueRendererType asyncValueRenderer
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                idlingResourceNamePrefix,
                valueViewFacadeFactory,
                valueStateObservableProvider,
                asyncValueRenderer
        );
    }
}
