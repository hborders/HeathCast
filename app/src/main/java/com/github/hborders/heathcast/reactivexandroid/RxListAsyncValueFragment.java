package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import java.util.List;

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
            UnparcelableListValueType extends List<UnparcelableItemType>,
            UnparcelableItemType
            > extends ViewFacade {
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
                    UnparcelableListValueType,
                    UnparcelableItemType
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
            ListViewFacadeType
            > {
        @Override
        public void render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ListViewFacadeType listViewFacade,
                StateType state
        ) {
            final AsyncStateType asyncState = state.getValue();
            final UnparcelableListValueType items = asyncState.getValue();
            listViewFacade.setListValue(items);
            if (items.isEmpty()) {
                state.getValue().act(
                        loading -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(true);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setEmptyItemsCompleteViewVisible(false);
                            listViewFacade.setListViewVisible(false);
                            listViewFacade.setEmptyItemsFailedViewVisible(false);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(false);
                        },
                        complete -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setEmptyItemsCompleteViewVisible(true);
                            listViewFacade.setListViewVisible(false);
                            listViewFacade.setEmptyItemsFailedViewVisible(false);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(false);
                        },
                        failed -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setEmptyItemsCompleteViewVisible(false);
                            listViewFacade.setListViewVisible(false);
                            listViewFacade.setEmptyItemsFailedViewVisible(true);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(false);
                        }
                );
            } else {
                asyncState.act(
                        loading -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(true);
                            listViewFacade.setEmptyItemsCompleteViewVisible(false);
                            listViewFacade.setListViewVisible(true);
                            listViewFacade.setEmptyItemsFailedViewVisible(false);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(false);
                        },
                        complete -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setEmptyItemsCompleteViewVisible(false);
                            listViewFacade.setListViewVisible(true);
                            listViewFacade.setEmptyItemsFailedViewVisible(false);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(false);
                        },
                        failed -> {
                            listViewFacade.setEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setNonEmptyItemsLoadingViewVisible(false);
                            listViewFacade.setEmptyItemsCompleteViewVisible(false);
                            listViewFacade.setListViewVisible(true);
                            listViewFacade.setEmptyItemsFailedViewVisible(false);
                            listViewFacade.setNonEmptyItemsFailedViewVisible(true);
                        }
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
            ListViewFacadeType extends ListViewFacade<
                    UnparcelableListValueType,
                    UnparcelableItemType
                    >,
            UnparcelableListValueType extends List<UnparcelableItemType>,
            UnparcelableItemType
            > RxListAsyncValueFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            ViewFacadeFactoryType viewFacadeFactory,
            StateObservableProviderType stateObservableProvider
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
                null,
                new ListValueRenderer<>()
        );
    }
}
