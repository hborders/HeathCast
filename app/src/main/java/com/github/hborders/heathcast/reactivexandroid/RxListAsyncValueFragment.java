package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;

import androidx.annotation.LayoutRes;

import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.EmptyEither;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.Nothing;
import com.github.hborders.heathcast.core.VoidFunction;

import java.util.List;

import io.reactivex.Completable;

// Extract EmptyLoading, NonEmptyLoading, EmptyComplete, NonEmptyComplete,
// EmptyFailed, NonEmptyFailed into RxAsyncValue
// RxAsyncValueFragment can have its own ViewFacade that accepts a value
// and RxListAsyncValueFragment can specify that to be a List
public abstract class RxListAsyncValueFragment<
        ListAsyncValueFragmentType extends RxListAsyncValueFragment<
                ListAsyncValueFragmentType,
                ListenerType,
                AttachmentType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                ListAsyncValueFragmentType,
                ListenerType,
                AttachmentType
                >
        > extends RxAsyncValueFragment<
        ListAsyncValueFragmentType,
        ListenerType,
        AttachmentType
        > {
    // even though ListAsyncState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ListAsyncValueState<
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType
            > extends Either<
            ListAsyncValueEmptyType,
            ListAsyncValueNonEmptyType,
            Nothing,
            ListAsyncValueListType
            > {
        interface ListAsyncValueEmpty<
                ListAsyncValueEmptyType extends ListAsyncValueEmpty<
                        ListAsyncValueEmptyType,
                        ListAsyncValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueNonEmptyType extends ListAsyncValueNonEmpty<
                        ListAsyncValueEmptyType,
                        ListAsyncValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueListType extends List<ListAsyncValueItemType>,
                ListAsyncValueItemType
                > extends ListAsyncValueState<
                ListAsyncValueEmptyType,
                ListAsyncValueNonEmptyType,
                ListAsyncValueListType,
                ListAsyncValueItemType
                >, Either.Left<
                ListAsyncValueEmptyType,
                ListAsyncValueNonEmptyType,
                Nothing,
                ListAsyncValueListType
                > {
        }

        interface ListAsyncValueNonEmpty<
                ListAsyncValueEmptyType extends ListAsyncValueEmpty<
                        ListAsyncValueEmptyType,
                        ListAsyncValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueNonEmptyType extends ListAsyncValueNonEmpty<
                        ListAsyncValueEmptyType,
                        ListAsyncValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueListType extends List<ListAsyncValueItemType>,
                ListAsyncValueItemType
                > extends ListAsyncValueState<
                ListAsyncValueEmptyType,
                ListAsyncValueNonEmptyType,
                ListAsyncValueListType,
                ListAsyncValueItemType
                >, Either.Right<
                ListAsyncValueEmptyType,
                ListAsyncValueNonEmptyType,
                Nothing,
                ListAsyncValueListType
                > {
        }

        // Either

        @Override
        <T> T reduce(
                Function<
                        ? super ListAsyncValueEmptyType,
                        ? extends T
                        > emptyReducer,
                Function<
                        ? super ListAsyncValueNonEmptyType,
                        ? extends T
                        > nonEmptyReducer
        );

        @Override
        void act(
                VoidFunction<? super ListAsyncValueEmptyType> emptyAction,
                VoidFunction<? super ListAsyncValueNonEmptyType> nonEmptyAction
        );
    }

    protected interface ListAsyncValueViewFacadeTransaction<
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >
            > extends EmptyEither<
            ListAsyncValueEmptyViewFacadeTransactionType,
            ListAsyncValueNonEmptyViewFacadeTransactionType
            > {
        interface ListAsyncValueEmptyViewFacadeTransaction<
                ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        >,
                ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        >
                > extends ListAsyncValueViewFacadeTransaction<
                ListAsyncValueEmptyViewFacadeTransactionType,
                ListAsyncValueNonEmptyViewFacadeTransactionType
                >,
                EmptyEither.Left<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        > {
        }

        interface ListAsyncValueNonEmptyViewFacadeTransaction<
                ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        >,
                ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        >
                > extends ListAsyncValueViewFacadeTransaction<
                ListAsyncValueEmptyViewFacadeTransactionType,
                ListAsyncValueNonEmptyViewFacadeTransactionType
                >,
                EmptyEither.Right<
                        ListAsyncValueEmptyViewFacadeTransactionType,
                        ListAsyncValueNonEmptyViewFacadeTransactionType
                        > {
        }

        // EmptyEither

        <T> T reduce(
                Function<
                        ? super ListAsyncValueEmptyViewFacadeTransactionType,
                        ? extends T
                        > emptyReducer,
                Function<
                        ? super ListAsyncValueNonEmptyViewFacadeTransactionType,
                        ? extends T
                        > nonEmptyReducer
        );

        void act(
                VoidFunction<? super ListAsyncValueEmptyViewFacadeTransactionType> emptyAction,
                VoidFunction<? super ListAsyncValueNonEmptyViewFacadeTransactionType> nonEmptyAction
        );

        // Public API


    }

    protected interface ListAsyncValueViewEmptyFacade {
    }

    protected interface ListAsyncValueViewNonEmptyFacade {
    }

    protected interface ListAsyncValueCompletable<
            AsyncValueCompletableType extends AsyncValueCompletable<
                    ValueCompletableType
                    >,
            ValueCompletableType extends ValueCompletable
            > {
        Completable toCompletable();

        AsyncValueCompletableType toAsyncValueCompletable();
    }

    protected interface ListAsyncValueViewFacadeCompletableTransaction<
            ListAsyncValueViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueStateType extends ListAsyncValueState<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        ListAsyncValueViewFacadeTransactionType toListAsyncValueViewFacadeTransaction(
                ListAsyncValueStateType listAsyncValueState
        );

        ListAsyncValueCompletableType complete();
    }

    protected interface ListAsyncValueViewFacadeCompletableTransactionFactory<
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            ListAsyncValueStateType extends ListAsyncValueState<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            ListAsyncValueViewFacadeCompletableTransactionType extends ListAsyncValueViewFacadeCompletableTransaction<
                    ListAsyncValueViewFacadeTransactionType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueStateType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        ListAsyncValueViewFacadeCompletableTransactionType newListAsyncValueViewFacadeCompletableTransaction(
                AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction
        );
    }

    protected interface ListAsyncValueEmptyRenderer<
            ListAsyncValueFragmentType extends RxListAsyncValueFragment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        ListAsyncValueCompletableType render(
                ListAsyncValueFragmentType fragment,
                ListenerType listener,
                Context context,
                ListAsyncValueEmptyType listAsyncValueEmptyState,
                ListAsyncValueEmptyViewFacadeTransactionType listAsyncValueEmptyViewFacadeTransaction
        );
    }

    protected interface ListAsyncValueNonEmptyRenderer<
            ListAsyncValueFragmentType extends RxListAsyncValueFragment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable
            > {
        ListAsyncValueCompletableType render(
                ListAsyncValueFragmentType fragment,
                ListenerType listener,
                Context context,
                ListAsyncValueNonEmptyType listAsyncValueNonEmptyState,
                ListAsyncValueNonEmptyViewFacadeTransactionType listAsyncValueNonEmptyViewFacadeTransaction
        );
    }

    private static final class AsyncValueRendererImpl<
            ListAsyncValueFragmentType extends RxListAsyncValueFragment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListAsyncValueStateType extends ListAsyncValueState<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            ListAsyncValueViewFacadeCompletableTransactionFactoryType extends ListAsyncValueViewFacadeCompletableTransactionFactory<
                    AsyncValueViewFacadeTransactionType,
                    ListAsyncValueStateType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueViewFacadeCompletableTransactionType,
                    ListAsyncValueViewFacadeTransactionType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueViewFacadeCompletableTransactionType extends ListAsyncValueViewFacadeCompletableTransaction<
                    ListAsyncValueViewFacadeTransactionType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueStateType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ValueCompletableType extends ValueCompletable,
            ListAsyncValueEmptyRendererType extends ListAsyncValueEmptyRenderer<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueNonEmptyRendererType extends ListAsyncValueNonEmptyRenderer<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >
            > implements AsyncValueRenderer<
            ListAsyncValueFragmentType,
            ListenerType,
            AttachmentType,
            ListAsyncValueStateType,
            AsyncValueViewFacadeTransactionType,
            AsyncValueCompletableType,
            ValueCompletableType
            > {
        private final ListAsyncValueViewFacadeCompletableTransactionFactoryType listAsyncValueViewFacadeCompletableTransactionFactory;
        private final ListAsyncValueEmptyRendererType listAsyncValueEmptyRenderer;
        private final ListAsyncValueNonEmptyRendererType listAsyncValueNonEmptyRenderer;

        private AsyncValueRendererImpl(
                ListAsyncValueViewFacadeCompletableTransactionFactoryType listAsyncValueViewFacadeCompletableTransactionFactory,
                ListAsyncValueEmptyRendererType listAsyncValueEmptyRenderer,
                ListAsyncValueNonEmptyRendererType listAsyncValueNonEmptyRenderer
        ) {
            this.listAsyncValueViewFacadeCompletableTransactionFactory = listAsyncValueViewFacadeCompletableTransactionFactory;
            this.listAsyncValueEmptyRenderer = listAsyncValueEmptyRenderer;
            this.listAsyncValueNonEmptyRenderer = listAsyncValueNonEmptyRenderer;
        }

        @Override
        public AsyncValueCompletableType render(
                ListAsyncValueFragmentType fragment,
                ListenerType listener,
                Context context,
                ListAsyncValueStateType listAsyncValueState,
                AsyncValueViewFacadeTransactionType asyncValueViewFacadeTransaction
        ) {
            final ListAsyncValueViewFacadeCompletableTransactionType listAsyncValueViewFacadeCompletableTransaction =
                    listAsyncValueViewFacadeCompletableTransactionFactory.newListAsyncValueViewFacadeCompletableTransaction(
                            asyncValueViewFacadeTransaction
                    );
            final ListAsyncValueViewFacadeTransactionType listAsyncValueViewFacadeTransaction =
                    listAsyncValueViewFacadeCompletableTransaction.toListAsyncValueViewFacadeTransaction(
                            listAsyncValueState
                    );
            final ListAsyncValueCompletableType listAsyncValueCompletable =
                    listAsyncValueViewFacadeTransaction.reduce(
                            empty ->
                                    listAsyncValueEmptyRenderer.render(
                                            fragment,
                                            listener,
                                            context,
                                            // TODO move listAsyncValueState into completableTransactionFactory
                                            (ListAsyncValueEmptyType) listAsyncValueState,
                                            empty
                                    ),
                            nonEmpty ->
                                    listAsyncValueNonEmptyRenderer.render(
                                            fragment,
                                            listener,
                                            context,
                                            // TODO move listAsyncValueState into completableTransactionFactory
                                            (ListAsyncValueNonEmptyType) listAsyncValueState,
                                            nonEmpty
                                    )
                    );
            return listAsyncValueCompletable.toAsyncValueCompletable();
        }
    }

    protected <
            ListAsyncValueAttachmentFactoryType extends Attachment.AttachmentFactory<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListAsyncValueOnAttachedType extends OnAttached<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListAsyncValueWillDetachType extends WillDetach<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ValueViewFacadeFactoryType extends ValueViewFacade.ValueViewFacadeFactory<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType
                    >,
            ValueViewFacadeType extends ValueViewFacade,
            ValueStateObservableProviderType extends ValueStateObservableProvider<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueStateType,
                    AsyncValueStateType
                    >,
            ValueStateType extends ValueState<AsyncValueStateType>,
            AsyncValueStateType extends AsyncValueState<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType
                    >,
            ListAsyncValueStateType extends ListAsyncValueState<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueEmptyType extends ListAsyncValueState.ListAsyncValueEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueNonEmptyType extends ListAsyncValueState.ListAsyncValueNonEmpty<
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType,
            ValueViewFacadeCompletableTransactionFactoryType extends ValueViewFacadeCompletableTransactionFactory<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ValueViewFacadeType,
                    ValueViewFacadeCompletableTransactionType,
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeCompletableTransactionType extends ValueViewFacadeCompletableTransaction<
                    ValueViewFacadeTransactionType,
                    ValueStateType,
                    AsyncValueStateType,
                    ValueCompletableType
                    >,
            ValueViewFacadeTransactionType extends ValueViewFacadeTransaction,
            ValueCompletableType extends ValueCompletable,
            AsyncValueViewFacadeCompletableTransactionFactoryType extends AsyncValueViewFacadeCompletableTransactionFactory<
                    ValueViewFacadeTransactionType,
                    AsyncValueViewFacadeCompletableTransactionType,
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeCompletableTransactionType extends AsyncValueViewFacadeCompletableTransaction<
                    AsyncValueViewFacadeTransactionType,
                    AsyncValueStateType,
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListAsyncValueStateType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            AsyncValueViewFacadeTransactionType extends AsyncValueViewFacadeTransaction,
            AsyncValueCompletableType extends AsyncValueCompletable<ValueCompletableType>,
            ListAsyncValueViewFacadeCompletableTransactionFactoryType extends ListAsyncValueViewFacadeCompletableTransactionFactory<
                    AsyncValueViewFacadeTransactionType,
                    ListAsyncValueStateType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueViewFacadeCompletableTransactionType,
                    ListAsyncValueViewFacadeTransactionType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueViewFacadeCompletableTransactionType extends ListAsyncValueViewFacadeCompletableTransaction<
                    ListAsyncValueViewFacadeTransactionType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueStateType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueEmptyRendererType extends ListAsyncValueEmptyRenderer<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueNonEmptyViewFacadeTransactionType extends ListAsyncValueViewFacadeTransaction.ListAsyncValueNonEmptyViewFacadeTransaction<
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType
                    >,
            ListAsyncValueCompletableType extends ListAsyncValueCompletable<
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >,
            ListAsyncValueNonEmptyRendererType extends ListAsyncValueNonEmptyRenderer<
                    ListAsyncValueFragmentType,
                    ListenerType,
                    AttachmentType,
                    ListAsyncValueEmptyType,
                    ListAsyncValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType,
                    ListAsyncValueEmptyViewFacadeTransactionType,
                    ListAsyncValueNonEmptyViewFacadeTransactionType,
                    ListAsyncValueCompletableType,
                    AsyncValueCompletableType,
                    ValueCompletableType
                    >
            > RxListAsyncValueFragment(
            Class<ListAsyncValueFragmentType> selfClass,
            Class<ListenerType> listenerClass,
            ListAsyncValueAttachmentFactoryType attachmentFactory,
            ListAsyncValueOnAttachedType onAttached,
            ListAsyncValueWillDetachType willDetach,
            @LayoutRes int layoutResource,
            String idlingResourceNamePrefix,
            ValueViewFacadeFactoryType valueViewFacadeFactory,
            ValueStateObservableProviderType valueStateObservableProvider,
            ValueViewFacadeCompletableTransactionFactoryType valueViewFacadeCompletableTransactionFactory,
            AsyncValueViewFacadeCompletableTransactionFactoryType asyncValueViewFacadeCompletableTransactionFactory,
            ListAsyncValueViewFacadeCompletableTransactionFactoryType listAsyncValueViewFacadeCompletableTransactionFactory,
            ListAsyncValueEmptyRendererType listAsyncValueEmptyRenderer,
            ListAsyncValueNonEmptyRendererType listAsyncValueNonEmptyRenderer
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
                valueViewFacadeCompletableTransactionFactory,
                asyncValueViewFacadeCompletableTransactionFactory,
                new AsyncValueRendererImpl<>(
                        listAsyncValueViewFacadeCompletableTransactionFactory,
                        listAsyncValueEmptyRenderer,
                        listAsyncValueNonEmptyRenderer
                )
        );
    }
}
