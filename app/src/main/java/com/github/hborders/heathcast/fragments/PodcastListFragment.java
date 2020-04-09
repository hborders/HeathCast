package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.Nothing;
import com.github.hborders.heathcast.models.PodcastIdentified;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.github.hborders.heathcast.reactivexandroid.RxListAsyncValueFragment;
import com.github.hborders.heathcast.views.recyclerviews.PodcastRecyclerViewAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;

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
    public interface PodcastIdentifiedListValueState extends ListValueState<
            PodcastIdentifiedListValueState.PodcastIdentifiedListValueEmpty,
            PodcastIdentifiedListValueState.PodcastIdentifiedListValueNonEmpty,
            PodcastIdentifiedList,
            PodcastIdentified
            > {
        final class PodcastIdentifiedListValueEmpty extends Either.LeftImpl<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                Nothing,
                PodcastIdentifiedList
                > implements ListValueState.ListValueEmpty<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                PodcastIdentifiedList,
                PodcastIdentified
                >, PodcastIdentifiedListValueState {
            public PodcastIdentifiedListValueEmpty() {
                super(
                        PodcastIdentifiedListValueEmpty.class,
                        Nothing.INSTANCE
                );
            }
        }

        final class PodcastIdentifiedListValueNonEmpty extends Either.RightImpl<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                Nothing,
                PodcastIdentifiedList
                > implements ListValueState.ListValueNonEmpty<
                PodcastIdentifiedListValueEmpty,
                PodcastIdentifiedListValueNonEmpty,
                PodcastIdentifiedList,
                PodcastIdentified
                >, PodcastIdentifiedListValueState {
            public PodcastIdentifiedListValueNonEmpty(PodcastIdentifiedList value) {
                super(
                        PodcastIdentifiedListValueNonEmpty.class,
                        value
                );
            }
        }
    }

    protected interface PodcastListFragmentOnClickPodcastIdentified<
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
            > {
        void onClickPodcastIdentified(
                ListenerType listener,
                PodcastListFragmentType fragment,
                PodcastIdentified podcastIdentified
        );
    }

    private static final class PodcastListAsyncValueViewFacade implements ValueViewFacade {
        final AtomicBoolean disposed = new AtomicBoolean();
        final View view;
        final RecyclerView recyclerView;
        final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter;
        final View emptyItemsLoadingView;
        final View nonEmptyItemsLoadingView;
        final View emptyItemsCompleteView;
        final View emptyItemsFailedView;
        final View nonEmptyItemsFailedView;

        private PodcastListAsyncValueViewFacade(
                View view,
                RecyclerView recyclerView,
                PodcastRecyclerViewAdapter podcastRecyclerViewAdapter,
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
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >
            > implements ValueViewFacadeTransaction {
        private final PodcastListAsyncValueViewFacade viewFacade;
        private final AsyncValueStateType state;
        private boolean complete;

        private PodcastListAsyncValueViewFacadeTransaction(
                PodcastListAsyncValueViewFacade viewFacade,
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


            // TODO implement!
            return null;
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
            PodcastListFragmentOnClickPodcastIdentifiedType extends PodcastListFragmentOnClickPodcastIdentified<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >
            > implements
            ValueViewFacade.ValueViewFacadeFactory<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType,
                    PodcastListAsyncValueViewFacade
                    > {
        private final PodcastListFragmentOnClickPodcastIdentifiedType onClickPodcastIdentified;

        private PodcastListAsyncValueViewFacadeFactory(PodcastListFragmentOnClickPodcastIdentifiedType onClickPodcastIdentified) {
            this.onClickPodcastIdentified = onClickPodcastIdentified;
        }

        @Override
        public PodcastListAsyncValueViewFacade newViewFacade(
                PodcastListFragmentType fragment,
                ListenerType listener,
                Context context,
                View view
        ) {
            final RecyclerView recyclerView =
                    view.requireViewById(
                            R.id.fragment_podcast_list_podcasts_recycler_view
                    );
            final PodcastRecyclerViewAdapter podcastRecyclerViewAdapter =
                    new PodcastRecyclerViewAdapter(
                            new PodcastIdentifiedList(),
                            podcastIdentified ->
                                    onClickPodcastIdentified.onClickPodcastIdentified(
                                            listener,
                                            fragment,
                                            podcastIdentified
                                    )
                    );
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

            return new PodcastListAsyncValueViewFacade(
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
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            PodcastListFragmentOnClickPodcastIdentifiedType extends PodcastListFragmentOnClickPodcastIdentified<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >
            > PodcastListFragment(
            Class<PodcastListFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            ValueStateObservableProviderType valueStateObservableProvider,
            PodcastListFragmentOnClickPodcastIdentifiedType onClickPodcastIdentified
    ) {
        this(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                valueStateObservableProvider,
                PodcastListAsyncValueViewFacadeTransaction::new,
                onClickPodcastIdentified
        );
    }

    // need this constructor to avoid a cast for [asyncValueRenderer]
    private <
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
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    PodcastIdentifiedListValueState
                    >,
            PodcastListFragmentOnClickPodcastIdentifiedType extends PodcastListFragmentOnClickPodcastIdentified<
                    PodcastListFragmentType,
                    ListenerType,
                    AttachmentType
                    >
            > PodcastListFragment(
            Class<PodcastListFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueRenderer<
                    PodcastListAsyncValueViewFacade,
                    AsyncValueStateType,
                    PodcastListAsyncValueViewFacadeTransaction<
                            AsyncValueStateType,
                            AsyncValueLoadingType,
                            AsyncValueCompleteType,
                            AsyncValueFailedType
                            >
                    > asyncValueRenderer,
            PodcastListFragmentOnClickPodcastIdentifiedType onClickPodcastIdentified
    ) {
        super(
                selfClass,
                listenerClass,
                attachmentFactory,
                onAttached,
                willDetach,
                R.layout.fragment_podcast_list,
                TAG,
                new PodcastListAsyncValueViewFacadeFactory<>(onClickPodcastIdentified),
                valueStateObservableProvider,
                asyncValueRenderer
        );
    }
}
