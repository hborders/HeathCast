package com.github.hborders.heathcast.mvp.async;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;
import com.github.hborders.heathcast.mvp.ModelObservableProvider;
import com.github.hborders.heathcast.mvp.Presentation;
import com.github.hborders.heathcast.mvp.Presenter;
import com.github.hborders.heathcast.mvp.Vista;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public abstract class AsyncPresenter<
        FragmentType extends RxFragment<
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
        VistaType extends Vista<
                FragmentType,
                ListenerType,
                AttachmentType
                >
        > implements Presenter<
        FragmentType,
        ListenerType,
        AttachmentType,
        VistaType
        > {
    public interface AsyncState<
            AsyncLoadingType extends AsyncState.AsyncLoading<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            AsyncCompleteType extends AsyncState.AsyncComplete<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            AsyncFailedType extends AsyncState.AsyncFailed<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            ModelType
            > extends Either31<
            AsyncLoadingType,
            AsyncCompleteType,
            AsyncFailedType,
            ModelType
            > {
        interface AsyncLoading<
                AsyncLoadingType extends AsyncLoading<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncCompleteType extends AsyncComplete<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncFailedType extends AsyncFailed<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                ModelType
                > extends Either31.Left<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                >, AsyncState<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                > {
        }

        interface AsyncComplete<
                AsyncLoadingType extends AsyncLoading<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncCompleteType extends AsyncComplete<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncFailedType extends AsyncFailed<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                ModelType
                > extends Either31.Middle<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                >, AsyncState<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                > {
        }

        interface AsyncFailed<
                AsyncLoadingType extends AsyncLoading<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncCompleteType extends AsyncComplete<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                AsyncFailedType extends AsyncFailed<
                        AsyncLoadingType,
                        AsyncCompleteType,
                        AsyncFailedType,
                        ModelType
                        >,
                ModelType
                > extends Either31.Right<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                >, AsyncState<
                AsyncLoadingType,
                AsyncCompleteType,
                AsyncFailedType,
                ModelType
                > {
        }

        // redefine reduce/act to get better parameter names

        <T> T reduce(
                Function<
                        ? super AsyncLoadingType,
                        ? extends T
                        > loadingReducer,
                Function<
                        ? super AsyncCompleteType,
                        ? extends T
                        > completeReducer,
                Function<
                        ? super AsyncFailedType,
                        ? extends T
                        > failedReducer
        );

        void act(
                VoidFunction<? super AsyncLoadingType> loadingAction,
                VoidFunction<? super AsyncCompleteType> completeAction,
                VoidFunction<? super AsyncFailedType> failedAction
        );
    }

    protected interface AsyncStateObservableProvider<
            FragmentType extends RxFragment<
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
            AsyncStateType extends AsyncState<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            AsyncLoadingType extends AsyncState.AsyncLoading<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            AsyncCompleteType extends AsyncState.AsyncComplete<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            AsyncFailedType extends AsyncState.AsyncFailed<
                    AsyncLoadingType,
                    AsyncCompleteType,
                    AsyncFailedType,
                    ModelType
                    >,
            ModelType
            > extends ModelObservableProvider<
            FragmentType,
            ListenerType,
            AttachmentType,
            AsyncStateType
            > {
    }

    @Override
    public Disposable subscribe(
            Observable<
                    Presentation<
                            FragmentType,
                            ListenerType,
                            AttachmentType,
                            VistaType
                            >
                    > previstaObservable
    ) {
        return null;
    }
}
