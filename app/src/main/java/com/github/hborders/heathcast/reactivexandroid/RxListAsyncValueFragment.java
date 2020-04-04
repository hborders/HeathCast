package com.github.hborders.heathcast.reactivexandroid;

import androidx.annotation.LayoutRes;

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
    protected interface ListAsyncValueViewFacade<
            ListViewFacadeType extends ListAsyncValueViewFacade<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            ViewFacadeTransactionType extends ViewFacadeTransaction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            UnparcelableListValueType extends List<UnparcelableItemType>,
            UnparcelableItemType
            > extends AsyncValueViewFacade<
            ListViewFacadeType,
            ViewFacadeTransactionType,
            UnparcelableListValueType
            > {
    }

    protected <
            AttachmentFactoryType extends Attachment.AttachmentFactory<
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
                    >,
            ViewFacadeFactoryType extends ViewFacadeFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    ListViewFacadeType
                    >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    AsyncStateType
                    >,
            ViewFacadeTransactionFactoryType extends ViewFacadeTransactionFactory<
                    ListViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            StateType extends State<AsyncStateType>,
            AsyncStateType extends AsyncState<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableListValueType
                    >,
            LoadingType extends AsyncState.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableListValueType
                    >,
            CompleteType extends AsyncState.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableListValueType
                    >,
            FailedType extends AsyncState.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    UnparcelableListValueType
                    >,
            UnparcelableListValueType extends List<UnparcelableItemType>,
            ListViewFacadeType extends ListAsyncValueViewFacade<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            ViewFacadeTransactionType extends ViewFacadeTransaction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType
                    >,
            UnparcelableItemType
            > RxListAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider,
            ViewFacadeTransactionFactoryType viewFacadeTransactionFactory
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                idlingResourceNamePrefix,
                viewFacadeFactory,
                stateObservableProvider,
                viewFacadeTransactionFactory
        );
    }
}
