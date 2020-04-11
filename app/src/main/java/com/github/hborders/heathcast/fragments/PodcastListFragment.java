package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.ViewUtil;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;

// Next, I should consume it in the MainFragment as well
public abstract class PodcastListFragment<
        PodcastListFragmentType extends PodcastListFragment<
                PodcastListFragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                PodcastListFragmentType,
                ListenerType,
                AttachmentType
                >
        > extends RxListAsyncValueFragment<
        PodcastListFragmentType,
        ListenerType,
        AttachmentType
        > {
    public interface PodcastListValueState<
            PodcastListValueEmptyType extends PodcastListValueState.PodcastListValueEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListValueNonEmptyType extends PodcastListValueState.PodcastListValueNonEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem
            > extends ListValueState<
            PodcastListValueEmptyType,
            PodcastListValueNonEmptyType,
            PodcastListType,
            PodcastItemType
            > {
        interface PodcastListValueEmpty<
                PodcastListValueEmptyType extends PodcastListValueEmpty<
                        PodcastListValueEmptyType,
                        PodcastListValueNonEmptyType,
                        PodcastListType,
                        PodcastItemType
                        >,
                PodcastListValueNonEmptyType extends PodcastListValueNonEmpty<
                        PodcastListValueEmptyType,
                        PodcastListValueNonEmptyType,
                        PodcastListType,
                        PodcastItemType
                        >,
                PodcastListType extends List<PodcastItemType>,
                PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem
                > extends ListValueState.ListValueEmpty<
                PodcastListValueEmptyType,
                PodcastListValueNonEmptyType,
                PodcastListType,
                PodcastItemType
                >, PodcastListValueState<
                PodcastListValueEmptyType,
                PodcastListValueNonEmptyType,
                PodcastListType,
                PodcastItemType
                > {
        }

        interface PodcastListValueNonEmpty<
                PodcastListValueEmptyType extends PodcastListValueEmpty<
                        PodcastListValueEmptyType,
                        PodcastListValueNonEmptyType,
                        PodcastListType,
                        PodcastItemType
                        >,
                PodcastListValueNonEmptyType extends PodcastListValueNonEmpty<
                        PodcastListValueEmptyType,
                        PodcastListValueNonEmptyType,
                        PodcastListType,
                        PodcastItemType
                        >,
                PodcastListType extends List<PodcastItemType>,
                PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem
                > extends ListValueState.ListValueNonEmpty<
                PodcastListValueEmptyType,
                PodcastListValueNonEmptyType,
                PodcastListType,
                PodcastItemType
                >, PodcastListValueState<
                PodcastListValueEmptyType,
                PodcastListValueNonEmptyType,
                PodcastListType,
                PodcastItemType
                > {
        }
    }

    private static final class PodcastListAsyncValueViewFacade<
            PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem,
            ListenerType
            > implements ValueViewFacade {
        final AtomicBoolean disposed = new AtomicBoolean();
        final View view;
        final RecyclerView recyclerView;
        final PodcastRecyclerViewAdapter<
                PodcastRecyclerViewAdapterType,
                PodcastViewHolderType,
                PodcastListType,
                PodcastItemType,
                ListenerType
                > podcastRecyclerViewAdapter;
        final View emptyItemsLoadingView;
        final View nonEmptyItemsLoadingView;
        final View emptyItemsCompleteView;
        final View emptyItemsFailedView;
        final View nonEmptyItemsFailedView;

        private PodcastListAsyncValueViewFacade(
                View view,
                RecyclerView recyclerView,
                PodcastRecyclerViewAdapter<
                        PodcastRecyclerViewAdapterType,
                        PodcastViewHolderType,
                        PodcastListType,
                        PodcastItemType,
                        ListenerType
                        > podcastRecyclerViewAdapter,
                View emptyItemsLoadingView,
                View nonEmptyItemsLoadingView,
                View emptyItemsCompleteView,
                View emptyItemsFailedView,
                View nonEmptyItemsFailedView
        ) {
            this.view = view;
            this.recyclerView = recyclerView;
            this.podcastRecyclerViewAdapter = podcastRecyclerViewAdapter;
            this.emptyItemsLoadingView = emptyItemsLoadingView;
            this.nonEmptyItemsLoadingView = nonEmptyItemsLoadingView;
            this.emptyItemsCompleteView = emptyItemsCompleteView;
            this.emptyItemsFailedView = emptyItemsFailedView;
            this.nonEmptyItemsFailedView = nonEmptyItemsFailedView;
        }

        // Disposable

        @Override
        public void dispose() {
            // theoretically, we could clean up after the recyclerView here
            // if it was shared or something
            disposed.set(true);
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    private static final class PodcastListAsyncValueViewFacadeTransaction<
            PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem,
            ListenerType,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            PodcastListValueStateType extends PodcastListValueState<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListValueEmptyType extends PodcastListValueState.PodcastListValueEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListValueNonEmptyType extends PodcastListValueState.PodcastListValueNonEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >
            > implements ValueViewFacadeTransaction {
        private final PodcastListAsyncValueViewFacade<
                PodcastRecyclerViewAdapterType,
                PodcastViewHolderType,
                PodcastListType,
                PodcastItemType,
                ListenerType
                > viewFacade;
        private final AsyncValueStateType state;
        private boolean complete;

        PodcastListAsyncValueViewFacadeTransaction(
                PodcastListAsyncValueViewFacade<
                        PodcastRecyclerViewAdapterType,
                        PodcastViewHolderType,
                        PodcastListType,
                        PodcastItemType,
                        ListenerType
                        > viewFacade,
                AsyncValueStateType state
        ) {
            this.viewFacade = viewFacade;
            this.state = state;
        }

        @Override
        public Completable complete() {
            if (complete) {
                throw new IllegalStateException("Already complete");
            }
            complete = true;

            state.act(
                    loading ->
                            loading.getValue().act(
                                    empty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.VISIBLE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                                        viewFacade.recyclerView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                                    },
                                    nonEmpty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.VISIBLE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                                        viewFacade.recyclerView.setVisibility(View.VISIBLE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                                    }
                            ),
                    complete ->
                            complete.getValue().act(
                                    empty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.VISIBLE);
                                        viewFacade.recyclerView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                                    },
                                    nonEmpty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                                        viewFacade.recyclerView.setVisibility(View.VISIBLE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                                    }
                            ),
                    failed ->
                            failed.getValue().act(
                                    empty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                                        viewFacade.recyclerView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.VISIBLE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.GONE);
                                    },
                                    nonEmpty -> {
                                        viewFacade.emptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsLoadingView.setVisibility(View.GONE);
                                        viewFacade.emptyItemsCompleteView.setVisibility(View.GONE);
                                        viewFacade.recyclerView.setVisibility(View.VISIBLE);
                                        viewFacade.emptyItemsFailedView.setVisibility(View.GONE);
                                        viewFacade.nonEmptyItemsFailedView.setVisibility(View.VISIBLE);
                                    }
                            )
            );

            final CompletableSubject completableSubject = CompletableSubject.create();
            ViewUtil.doOnLayout(
                    viewFacade.view,
                    completableSubject::onComplete
            );

            return completableSubject;
        }
    }

    private static final class PodcastListAsyncValueViewFacadeFactory<
            PodcastListFragmentType extends PodcastListFragment<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastRecyclerViewAdapterFactoryType extends PodcastRecyclerViewAdapter.PodcastRecyclerViewAdapterFactory<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem
            > implements
            ValueViewFacade.ValueViewFacadeFactory<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueViewFacade<
                            PodcastRecyclerViewAdapterType,
                            PodcastViewHolderType,
                            PodcastListType,
                            PodcastItemType,
                            ListenerType
                            >
                    > {
        private final PodcastRecyclerViewAdapterFactoryType podcastRecyclerViewAdapterFactory;

        private PodcastListAsyncValueViewFacadeFactory(
                PodcastRecyclerViewAdapterFactoryType podcastRecyclerViewAdapterFactory
        ) {
            this.podcastRecyclerViewAdapterFactory = podcastRecyclerViewAdapterFactory;
        }

        @Override
        public PodcastListAsyncValueViewFacade<
                PodcastRecyclerViewAdapterType,
                PodcastViewHolderType,
                PodcastListType,
                PodcastItemType,
                ListenerType
                > newViewFacade(
                PodcastListFragmentType fragment,
                ListenerType listener,
                Context context,
                View view
        ) {
            final RecyclerView recyclerView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_podcasts_recycler_view
                    );
            final PodcastRecyclerViewAdapterType podcastRecyclerViewAdapter =
                    podcastRecyclerViewAdapterFactory.newPodcastRecyclerViewAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(podcastRecyclerViewAdapter);
            final View emptyItemsLoadingView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_loading_group
                    );
            final View nonEmptyItemsLoadingView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_non_empty_loading_group
                    );
            final View emptyItemsCompleteView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_complete_text_view
                    );
            final View emptyItemsFailedView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_empty_error_text_view
                    );
            final View nonEmptyItemsFailedView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_non_empty_error_text_view
                    );

            return new PodcastListAsyncValueViewFacade<>(
                    view,
                    recyclerView,
                    podcastRecyclerViewAdapter,
                    emptyItemsLoadingView,
                    nonEmptyItemsLoadingView,
                    emptyItemsCompleteView,
                    emptyItemsFailedView,
                    nonEmptyItemsFailedView
            );
        }
    }

    private static final String TAG = "PodcastList";

    protected <
            AttachmentFactoryType extends Attachment.AttachmentFactory<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            OnAttachedType extends OnAttached<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            WillDetachType extends WillDetach<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            PodcastRecyclerViewAdapterFactoryType extends PodcastRecyclerViewAdapter.PodcastRecyclerViewAdapterFactory<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastRecyclerViewAdapterType extends PodcastRecyclerViewAdapter<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastViewHolderType extends PodcastRecyclerViewAdapter.PodcastViewHolder<
                    PodcastRecyclerViewAdapterType,
                    PodcastViewHolderType,
                    PodcastListType,
                    PodcastItemType,
                    ListenerType
                    >,
            PodcastListType extends List<PodcastItemType>,
            PodcastItemType extends PodcastRecyclerViewAdapter.PodcastItem,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType,
                    AsyncValueStateType
                    >,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastListValueStateType
                    >,
            PodcastListValueStateType extends PodcastListValueState<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListValueEmptyType extends PodcastListValueState.PodcastListValueEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >,
            PodcastListValueNonEmptyType extends PodcastListValueState.PodcastListValueNonEmpty<
                    PodcastListValueEmptyType,
                    PodcastListValueNonEmptyType,
                    PodcastListType,
                    PodcastItemType
                    >
            > PodcastListFragment(
            Class<PodcastListFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            PodcastRecyclerViewAdapterFactoryType podcastRecyclerViewAdapterFactory,
            ValueStateObservableProviderType valueStateObservableProvider
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                R.layout.fragment_podcast_list,
                TAG,
                new PodcastListAsyncValueViewFacadeFactory<>(
                        podcastRecyclerViewAdapterFactory
                ),
                valueStateObservableProvider,
                (ValueRenderer<
                        PodcastListAsyncValueViewFacade<
                                PodcastRecyclerViewAdapterType,
                                PodcastViewHolderType,
                                PodcastListType,
                                PodcastItemType,
                                ListenerType
                                >,
                        AsyncValueStateType,
                        PodcastListAsyncValueViewFacadeTransaction<
                                PodcastRecyclerViewAdapterType,
                                PodcastViewHolderType,
                                PodcastListType,
                                PodcastItemType,
                                ListenerType,
                                AsyncValueStateType,
                                AsyncValueLoadingType,
                                AsyncValueCompleteType,
                                AsyncValueFailedType,
                                PodcastListValueStateType,
                                PodcastListValueEmptyType,
                                PodcastListValueNonEmptyType
                                >
                        >) PodcastListAsyncValueViewFacadeTransaction::new
        );
    }
}
