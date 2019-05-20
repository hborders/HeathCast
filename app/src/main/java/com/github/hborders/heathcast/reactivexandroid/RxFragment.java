package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.android.FragmentUtil;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public abstract class RxFragment<F extends RxFragment<F, L>, L> extends Fragment {
    protected static final class Attachment<F extends RxFragment<F, L>, L> {
        public final F fragment;
        public final Context context;
        public final L listener;
        public final Observable<FragmentCreation<F, L>> fragmenCreationObservable;
        public final Completable onDetachCompletable;

        private Attachment(
                F fragment,
                Context context,
                L listener,
                Observable<FragmentCreation<F, L>> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            this.fragment = fragment;
            this.context = context;
            this.listener = listener;
            this.fragmenCreationObservable = fragmenCreationObservable;
            this.onDetachCompletable = onDetachCompletable;
        }

        @Override
        public String toString() {
            return "Attachment{" +
                    "fragment=" + fragment +
                    ", context=" + context +
                    ", listener=" + listener +
                    ", fragmenCreationObservable=" + fragmenCreationObservable +
                    ", onDetachCompletable=" + onDetachCompletable +
                    '}';
        }
    }

    protected static final class FragmentCreation<F extends RxFragment<F, L>, L> {
        public static final class SaveInstanceState {
            public final Bundle outState;

            private SaveInstanceState(Bundle outState) {
                this.outState = outState;
            }

            @Override
            public String toString() {
                return "SaveInstanceState{" +
                        "outState=" + outState +
                        '}';
            }
        }

        @Nullable
        public final Bundle savedInstanceState;
        public final Observable<SaveInstanceState> saveInstanceStateObservable;
        public final Observable<ViewCreation> viewCreationObservable;
        public final Completable onDestroyCompletable;

        private FragmentCreation(
                @Nullable Bundle savedInstanceState,
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<ViewCreation> viewCreationObservable,
                Completable onDestroyCompletable
        ) {
            this.savedInstanceState = savedInstanceState;
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.viewCreationObservable = viewCreationObservable;
            this.onDestroyCompletable = onDestroyCompletable;
        }

        @Override
        public String toString() {
            return "FragmentCreation{" +
                    "savedInstanceState=" + savedInstanceState +
                    ", saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", viewCreationObservable=" + viewCreationObservable +
                    ", onDestroyCompletable=" + onDestroyCompletable +
                    '}';
        }
    }

    protected static class ViewCreation {
        public static final class SaveInstanceState {
            public final Bundle outState;

            private SaveInstanceState(Bundle outState) {
                this.outState = outState;
            }

            @Override
            public String toString() {
                return "SaveInstanceState{" +
                        "outState=" + outState +
                        '}';
            }
        }

        public final View view;
        @Nullable
        public final Bundle savedInstanceState;
        public final Observable<SaveInstanceState> saveInstanceStateObservable;
        public final Observable<Start> startObservable;
        public final Completable onDestroyViewCompletable;

        private ViewCreation(
                View view,
                @Nullable Bundle savedInstanceState,
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<Start> startObservable,
                Completable onDestroyViewCompletable
        ) {
            this.view = view;
            this.savedInstanceState = savedInstanceState;
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.startObservable = startObservable;
            this.onDestroyViewCompletable = onDestroyViewCompletable;
        }

        @Override
        public String toString() {
            return "ViewCreation{" +
                    "view=" + view +
                    ", savedInstanceState=" + savedInstanceState +
                    ", saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", startObservable=" + startObservable +
                    ", onDestroyViewCompletable=" + onDestroyViewCompletable +
                    '}';
        }
    }

    protected static final class Start {
        public static final class SaveInstanceState {
            public final Bundle outState;

            private SaveInstanceState(Bundle outState) {
                this.outState = outState;
            }

            @Override
            public String toString() {
                return "SaveInstanceState{" +
                        "outState=" + outState +
                        '}';
            }
        }

        public final Observable<SaveInstanceState> saveInstanceStateObservable;
        public final Observable<Resume> resumeObservable;
        public final Completable onStopCompletable;

        private Start(
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<Resume> resumeObservable,
                Completable onStopCompletable
        ) {
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.resumeObservable = resumeObservable;
            this.onStopCompletable = onStopCompletable;
        }

        @Override
        public String toString() {
            return "Start{" +
                    "saveInstanceStateObservable=" + saveInstanceStateObservable +
                    "resumeObservable=" + resumeObservable +
                    "onStopCompletable=" + onStopCompletable +
                    '}';
        }
    }

    protected static final class Resume {
        public static final class SaveInstanceState {
            public final Bundle outState;

            private SaveInstanceState(Bundle outState) {
                this.outState = outState;
            }

            @Override
            public String toString() {
                return "SaveInstanceState{" +
                        "outState=" + outState +
                        '}';
            }
        }

        public final Observable<SaveInstanceState> saveInstanceStateObservable;
        public final Completable onPauseCompletable;

        private Resume(
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Completable onPauseCompletable
        ) {
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.onPauseCompletable = onPauseCompletable;
        }

        @Override
        public String toString() {
            return "Resume{" +
                    "saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", onPauseCompletable=" + onPauseCompletable +
                    '}';
        }
    }

    private final Class<L> listenerClass;
    @LayoutRes
    private final int layoutResource;

    private final PublishSubject<Attachment<F, L>> attachmentPublishSubject =
            PublishSubject.create();
    private final CompletableSubject onDetachCompletableSubject = CompletableSubject.create();

    private final PublishSubject<FragmentCreation<F, L>> fragmentCreationPublishSubject =
            PublishSubject.create();
    private final PublishSubject<FragmentCreation.SaveInstanceState> fragmentCreationSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private final CompletableSubject onDestroyCompletableSubject = CompletableSubject.create();

    private PublishSubject<ViewCreation> viewCreationPublishSubject = PublishSubject.create();
    private PublishSubject<ViewCreation.SaveInstanceState> viewCreationSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private CompletableSubject onDestroyViewCompletableSubject = CompletableSubject.create();

    private PublishSubject<Start> startPublishSubject = PublishSubject.create();
    private PublishSubject<Start.SaveInstanceState> startSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private CompletableSubject onStopCompletableSubject = CompletableSubject.create();

    private PublishSubject<Resume> resumePublishSubject = PublishSubject.create();
    private PublishSubject<Resume.SaveInstanceState> resumeSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private CompletableSubject onPauseCompletableSubject = CompletableSubject.create();

    protected RxFragment(
            Class<L> listenerClass,
            int layoutResource
    ) {
        this.listenerClass = listenerClass;
        this.layoutResource = layoutResource;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);

        final F self = getSelf();
        if (self.getClass() != getClass()) {
            throw new IllegalStateException(
                    "getSelf().getClass(): "
                            + self.getClass() + " != getClass(): "
                            + getClass()
            );
        }

        final L listener = FragmentUtil.requireFragmentListener(
                self,
                context,
                listenerClass
        );

        subscribeToAttachmentObservable(
                attachmentPublishSubject.hide()
        );
        attachmentPublishSubject.onNext(
                new Attachment<>(
                        self,
                        context,
                        listener,
                        fragmentCreationPublishSubject.hide(),
                        onDetachCompletableSubject.hide()
                )
        );
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentCreationPublishSubject.onNext(
                new FragmentCreation<>(
                        savedInstanceState,
                        fragmentCreationSaveInstanceStatePublishSubject.hide(),
                        viewCreationPublishSubject,
                        onDestroyCompletableSubject.hide()
                )
        );
    }

    @Nullable
    @Override
    public final View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                layoutResource,
                container,
                false
        );
    }

    @Override
    public final void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        viewCreationPublishSubject.onNext(
                new ViewCreation(
                        view,
                        savedInstanceState,
                        viewCreationSaveInstanceStatePublishSubject.hide(),
                        startPublishSubject.hide(),
                        onDestroyViewCompletableSubject.hide()
                )
        );
    }

    @Override
    public final void onStart() {
        super.onStart();

        startPublishSubject.onNext(
                new Start(
                        startSaveInstanceStatePublishSubject.hide(),
                        resumePublishSubject.hide(),
                        onStopCompletableSubject.hide()
                )
        );
    }

    @Override
    public final void onResume() {
        super.onResume();

        resumePublishSubject.onNext(
                new Resume(
                        resumeSaveInstanceStatePublishSubject.hide(),
                        onPauseCompletableSubject.hide()
                )
        );
    }

    @Override
    public final void onPause() {
        super.onPause();

        onPauseCompletableSubject.onComplete();
        resumePublishSubject.onComplete();
        resumeSaveInstanceStatePublishSubject.onComplete();

        onPauseCompletableSubject = CompletableSubject.create();
        resumePublishSubject = PublishSubject.create();
        resumeSaveInstanceStatePublishSubject = PublishSubject.create();
    }

    @Override
    public final void onStop() {
        super.onStop();

        onStopCompletableSubject.onComplete();
        startPublishSubject.onComplete();
        startSaveInstanceStatePublishSubject.onComplete();

        onStopCompletableSubject = CompletableSubject.create();
        startPublishSubject = PublishSubject.create();
        startSaveInstanceStatePublishSubject.onComplete();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();

        onDestroyViewCompletableSubject.onComplete();
        viewCreationPublishSubject.onComplete();
        viewCreationSaveInstanceStatePublishSubject.onComplete();

        onDestroyViewCompletableSubject = CompletableSubject.create();
        viewCreationPublishSubject = PublishSubject.create();
        viewCreationSaveInstanceStatePublishSubject = PublishSubject.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        fragmentCreationSaveInstanceStatePublishSubject.onNext(
                new FragmentCreation.SaveInstanceState(outState)
        );
        viewCreationSaveInstanceStatePublishSubject.onNext(
                new ViewCreation.SaveInstanceState(outState)
        );
        startSaveInstanceStatePublishSubject.onNext(
                new Start.SaveInstanceState(outState)
        );
        resumeSaveInstanceStatePublishSubject.onNext(
                new Resume.SaveInstanceState(outState)
        );
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();

        onDestroyCompletableSubject.onComplete();
        fragmentCreationPublishSubject.onComplete();
        fragmentCreationSaveInstanceStatePublishSubject.onComplete();
    }

    @Override
    public final void onDetach() {
        super.onDetach();

        onDetachCompletableSubject.onComplete();
        attachmentPublishSubject.onComplete();
    }

    protected abstract F getSelf();

    protected abstract void subscribeToAttachmentObservable(
            Observable<Attachment<F, L>> attachmentObservable
    );
}
