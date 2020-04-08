package com.github.hborders.heathcast.reactivexandroid;

import com.github.hborders.heathcast.core.Either;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.Nothing;
import com.github.hborders.heathcast.core.VoidFunction;

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
    // even though ListAsyncState could be protected for us, we should make it public
    // so that subclasses can use it in public generic boundaries
    // Java bugs: 9064231, 9064232
    // https://github.com/hborders/HeathCast/tree/jdk_8_bug
    public interface ListValueState<
            ListValueEmptyType extends ListValueState.ListValueEmpty<
                    ListValueEmptyType,
                    ListValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListValueNonEmptyType extends ListValueState.ListValueNonEmpty<
                    ListValueEmptyType,
                    ListValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListAsyncValueListType extends List<ListAsyncValueItemType>,
            ListAsyncValueItemType
            > extends Either<
            ListValueEmptyType,
            ListValueNonEmptyType,
            Nothing,
            ListAsyncValueListType
            > {
        interface ListValueEmpty<
                ListValueEmptyType extends ListValueEmpty<
                        ListValueEmptyType,
                        ListValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListValueNonEmptyType extends ListValueNonEmpty<
                        ListValueEmptyType,
                        ListValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueListType extends List<ListAsyncValueItemType>,
                ListAsyncValueItemType
                > extends ListValueState<
                ListValueEmptyType,
                ListValueNonEmptyType,
                ListAsyncValueListType,
                ListAsyncValueItemType
                >, Either.Left<
                ListValueEmptyType,
                ListValueNonEmptyType,
                Nothing,
                ListAsyncValueListType
                > {
        }

        interface ListValueNonEmpty<
                ListValueEmptyType extends ListValueEmpty<
                        ListValueEmptyType,
                        ListValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListValueNonEmptyType extends ListValueNonEmpty<
                        ListValueEmptyType,
                        ListValueNonEmptyType,
                        ListAsyncValueListType,
                        ListAsyncValueItemType
                        >,
                ListAsyncValueListType extends List<ListAsyncValueItemType>,
                ListAsyncValueItemType
                > extends ListValueState<
                ListValueEmptyType,
                ListValueNonEmptyType,
                ListAsyncValueListType,
                ListAsyncValueItemType
                >, Either.Right<
                ListValueEmptyType,
                ListValueNonEmptyType,
                Nothing,
                ListAsyncValueListType
                > {
        }

        // Either

        @Override
        <T> T reduce(
                Function<
                        ? super ListValueEmptyType,
                        ? extends T
                        > emptyReducer,
                Function<
                        ? super ListValueNonEmptyType,
                        ? extends T
                        > nonEmptyReducer
        );

        @Override
        void act(
                VoidFunction<? super ListValueEmptyType> emptyAction,
                VoidFunction<? super ListValueNonEmptyType> nonEmptyAction
        );
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
                    ListValueStateType
                    >,
            AsyncValueLoadingType extends AsyncValueState.AsyncValueLoading<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListValueStateType
                    >,
            AsyncValueCompleteType extends AsyncValueState.AsyncValueComplete<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListValueStateType
                    >,
            AsyncValueFailedType extends AsyncValueState.AsyncValueFailed<
                    AsyncValueLoadingType,
                    AsyncValueCompleteType,
                    AsyncValueFailedType,
                    ListValueStateType
                    >,
            ListValueStateType extends ListValueState<
                    ListValueEmptyType,
                    ListValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListValueEmptyType extends ListValueState.ListValueEmpty<
                    ListValueEmptyType,
                    ListValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
                    >,
            ListValueNonEmptyType extends ListValueState.ListValueNonEmpty<
                    ListValueEmptyType,
                    ListValueNonEmptyType,
                    ListAsyncValueListType,
                    ListAsyncValueItemType
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
            AttachmentFactoryType attachmentFactory,
            OnAttachedType onAttached,
            WillDetachType willDetach,
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
