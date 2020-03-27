package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.views.recyclerviews.ListRecyclerViewAdapter;

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

    protected interface RxListViewHolder<
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    UnparcelableItemType,
                    UnparcelableValueType,
                    RecyclerViewViewHolderType
                    >,
            UnparcelableItemType,
            UnparcelableValueType extends List<UnparcelableItemType>,
            RecyclerViewViewHolderType extends RecyclerView.ViewHolder
            > {
        RecyclerView requireRecyclerView();

        ListRecyclerViewAdapterType requireListRecyclerViewAdapter();

        @Nullable
        View findEmptyItemsLoadingView();

        @Nullable
        View findNonEmptyItemsLoadingView();

        @Nullable
        View findEmptyItemsCompleteView();

        @Nullable
        View findEmptyItemsFailedView();

        @Nullable
        View findNonEmptyItemsFailedView();
    }

    private static final class RxListValueRenderer<
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
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    UnparcelableItemType,
                    UnparcelableValueType,
                    RecyclerViewViewHolderType
                    >,
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            ModelType extends RxAsyncValueFragment.Model<UnparcelableValueType>,
            UnparcelableValueType extends List<UnparcelableItemType>,
            ViewHolderType extends RxListViewHolder<
                    ListRecyclerViewAdapterType,
                    UnparcelableItemType,
                    UnparcelableValueType,
                    RecyclerViewViewHolderType
                    >,
            RecyclerViewViewHolderType extends RecyclerView.ViewHolder,
            UnparcelableItemType
            > implements RxListAsyncValueFragment.Renderer<
            FragmentType,
            ListenerType,
            AttachmentType,
            StateType,
            LoadingType,
            CompleteType,
            FailedType,
            ModelType,
            UnparcelableValueType,
            ViewHolderType
            > {
        @Override
        public void render(
                FragmentType fragmentType,
                ListenerType listener,
                Context context,
                ViewHolderType viewHolder,
                StateType state
        ) {
            final UnparcelableValueType items = state.getValue().value;
            viewHolder.requireListRecyclerViewAdapter().setItems(items);
            if (items.isEmpty()) {
                state.act(
                        loading -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                        },
                        complete -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                        },
                        failed -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                        }
                );
            } else {
                state.act(
                        loading -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                        },
                        complete -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                        },
                        failed -> {
                            setEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsLoadingViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setEmptyItemsCompleteViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setRecyclerViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                            setEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.GONE
                            );
                            setNonEmptyItemsFailedViewVisibility(
                                    viewHolder,
                                    View.VISIBLE
                            );
                        }
                );
            }
        }

        private static void setNullableViewVisibility(
                @Nullable View view,
                int visibility
        ) {
            if (view != null) {
                view.setVisibility(visibility);
            }
        }

        private void setEmptyItemsLoadingViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            setNullableViewVisibility(
                    viewHolder.findEmptyItemsLoadingView(),
                    visibility
            );
        }

        private void setNonEmptyItemsLoadingViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            setNullableViewVisibility(
                    viewHolder.findNonEmptyItemsLoadingView(),
                    visibility
            );
        }

        private void setEmptyItemsCompleteViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            setNullableViewVisibility(
                    viewHolder.findEmptyItemsCompleteView(),
                    visibility
            );
        }

        private void setRecyclerViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            viewHolder.requireRecyclerView().setVisibility(visibility);
        }

        private void setEmptyItemsFailedViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            setNullableViewVisibility(
                    viewHolder.findEmptyItemsFailedView(),
                    visibility
            );
        }

        private void setNonEmptyItemsFailedViewVisibility(
                ViewHolderType viewHolder,
                int visibility
        ) {
            setNullableViewVisibility(
                    viewHolder.findNonEmptyItemsFailedView(),
                    visibility
            );
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
            ViewHolderFactoryType extends ViewHolderFactory<
                                FragmentType,
                                ListenerType,
                                AttachmentType,
                                ViewHolderType
                                >,
            StateObservableProviderType extends StateObservableProvider<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    StateType,
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            StateType extends State<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            LoadingType extends State.Loading<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            CompleteType extends State.Complete<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            FailedType extends State.Failed<
                    LoadingType,
                    CompleteType,
                    FailedType,
                    ModelType,
                    UnparcelableValueType
                    >,
            ModelType extends RxAsyncValueFragment.Model<UnparcelableValueType>,
            UnparcelableValueType extends List<UnparcelableItemType>,
            UnparcelableItemType,
            ViewHolderType extends RxListViewHolder<
                    ListRecyclerViewAdapterType,
                    UnparcelableItemType,
                    UnparcelableValueType,
                    RecyclerViewViewHolderType
                    >,
            ListRecyclerViewAdapterType extends ListRecyclerViewAdapter<
                    UnparcelableItemType,
                    UnparcelableValueType,
                    RecyclerViewViewHolderType
                    >,
            RecyclerViewViewHolderType extends RecyclerView.ViewHolder
            > RxListAsyncValueFragment(
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            int layoutResource,
            String idlingResourceNamePrefix,
            ViewHolderFactoryType viewHolderFactory,
            StateObservableProviderType stateObservableProvider
    ) {
        super(
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                layoutResource,
                idlingResourceNamePrefix,
                viewHolderFactory,
                stateObservableProvider,
                new RxListValueRenderer<>()
        );
    }
}
