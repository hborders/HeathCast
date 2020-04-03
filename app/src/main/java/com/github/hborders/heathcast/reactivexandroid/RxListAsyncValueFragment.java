package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import androidx.annotation.LayoutRes;

import java.util.List;

import io.reactivex.Completable;

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
    protected interface ListViewFacade<
            ListViewFacadeType extends ListViewFacade<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType,
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            UnparcelableListValueType extends List<UnparcelableItemType>,
            UnparcelableItemType
            > extends ViewFacade<
            ListViewFacadeType,
            ViewFacadeTransactionType,
            ViewFacadeEmptyActionType
            > {
        void setListValue(UnparcelableListValueType listValue);

        void setEmptyItemsLoadingViewVisible(boolean visible);

        void setNonEmptyItemsLoadingViewVisible(boolean visible);

        void setEmptyItemsCompleteViewVisible(boolean visible);

        void setListViewVisible(boolean visible);

        void setEmptyItemsFailedViewVisible(boolean visible);

        void setNonEmptyItemsFailedViewVisible(boolean visible);
    }

    private static final class ListValueRenderer<
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
                    >,
            StateType extends State<AsyncStateType>,
            UnparcelableListValueType extends List<UnparcelableItemType>,
            ListViewFacadeType extends ListViewFacade<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType,
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
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
            UnparcelableItemType
            > implements Renderer<
            FragmentType,
            ListenerType,
            AttachmentType,
            StateType,
            AsyncStateType,
            ListViewFacadeType,
            ViewFacadeTransactionType,
            ViewFacadeEmptyActionType
            > {
        @Override
        public Completable render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                StateType state,
                ViewFacadeTransactionType viewFacadeTransaction
        ) {
            final AsyncStateType asyncState = state.getValue();
            final UnparcelableListValueType items = asyncState.getValue();
            viewFacadeTransaction.act(
                    ListViewFacadeType::setListValue,
                    items
            );
            if (items.isEmpty()) {
                return state.getValue().reduce(
                        loading ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .complete(),
                        complete ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .complete(),
                        failed ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .complete()
                );
            } else {
                return asyncState.reduce(
                        loading ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .complete(),
                        complete ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .complete(),
                        failed ->
                                viewFacadeTransaction
                                        .act(
                                                ListViewFacadeType::setEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsLoadingViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsCompleteViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setListViewVisible,
                                                true
                                        )
                                        .act(
                                                ListViewFacadeType::setEmptyItemsFailedViewVisible,
                                                false
                                        )
                                        .act(
                                                ListViewFacadeType::setNonEmptyItemsFailedViewVisible,
                                                true
                                        )
                                        .complete()
                );
            }
        }
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
            ListViewFacadeType extends ListViewFacade<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType,
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            ViewFacadeTransactionType extends ViewFacade.ViewFacadeTransaction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
                    >,
            ViewFacadeEmptyActionType extends ViewFacade.ViewFacadeTransaction.ViewFacadeEmptyAction<
                    ListViewFacadeType,
                    ViewFacadeTransactionType,
                    ViewFacadeEmptyActionType
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
                viewFacadeTransactionFactory,
                new ListValueRenderer<>()
        );
    }
}
