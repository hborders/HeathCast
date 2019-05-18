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

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public abstract class RxFragment<F extends RxFragment<F, L>, L> extends Fragment {
    public interface ListenerCaller<F extends RxFragment<F, L>, L> {
        void callListener(
                L listener,
                Observable<F> fragmentObservable
        );
    }

    public static final class Attachment<F extends RxFragment<F, L>, L> {
        public final F fragment;
        public final Context context;
        public final L listener;
        public final Observable<FragmentCreation<F, L>> fragmenCreationObservable;

        private Attachment(
                F fragment,
                Context context,
                L listener,
                Observable<FragmentCreation<F, L>> fragmenCreationObservable
        ) {
            this.fragment = fragment;
            this.context = context;
            this.listener = listener;
            this.fragmenCreationObservable = fragmenCreationObservable;
        }

        @Override
        public String toString() {
            return "Attachment{" +
                    "fragment=" + fragment +
                    ", context=" + context +
                    ", listener=" + listener +
                    ", fragmenCreationObservable=" + fragmenCreationObservable +
                    '}';
        }
    }

    public static final class FragmentCreation<F extends RxFragment<F, L>, L> {
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

        private FragmentCreation(
                @Nullable Bundle savedInstanceState,
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<ViewCreation> viewCreationObservable
        ) {
            this.savedInstanceState = savedInstanceState;
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.viewCreationObservable = viewCreationObservable;
        }

        @Override
        public String toString() {
            return "FragmentCreation{" +
                    "savedInstanceState=" + savedInstanceState +
                    ", saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", viewCreationObservable=" + viewCreationObservable +
                    '}';
        }
    }

    public static class ViewCreation {
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

        private ViewCreation(
                View view,
                @Nullable Bundle savedInstanceState,
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<Start> startObservable
        ) {
            this.view = view;
            this.savedInstanceState = savedInstanceState;
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.startObservable = startObservable;
        }

        @Override
        public String toString() {
            return "ViewCreation{" +
                    "view=" + view +
                    ", savedInstanceState=" + savedInstanceState +
                    ", saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", startObservable=" + startObservable +
                    '}';
        }
    }

    public static final class Start {
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

        private Start(
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<Resume> resumeObservable
        ) {
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.resumeObservable = resumeObservable;
        }

        @Override
        public String toString() {
            return "Start{" +
                    "saveInstanceStateObservable=" + saveInstanceStateObservable +
                    "resumeObservable=" + resumeObservable +
                    '}';
        }
    }

    public static final class Resume {
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

        private Resume(Observable<SaveInstanceState> saveInstanceStateObservable) {
            this.saveInstanceStateObservable = saveInstanceStateObservable;
        }

        @Override
        public String toString() {
            return "Resume{" +
                    '}';
        }
    }

    private final Class<L> listenerClass;
    private final ListenerCaller<F, L> listenerCaller;
    @LayoutRes
    private final int layoutResource;

    private final PublishSubject<Attachment<F, L>> attachmentPublishSubject =
            PublishSubject.create();
    private final PublishSubject<F> fragmentPublishSubject = PublishSubject.create();

    private final PublishSubject<FragmentCreation<F, L>> fragmentCreationPublishSubject =
            PublishSubject.create();
    private final PublishSubject<FragmentCreation.SaveInstanceState> fragmentCreationSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private PublishSubject<ViewCreation> viewCreationPublishSubject = PublishSubject.create();
    private PublishSubject<ViewCreation.SaveInstanceState> viewCreationSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private PublishSubject<Start> startPublishSubject = PublishSubject.create();
    private PublishSubject<Start.SaveInstanceState> startSaveInstanceStatePublishSubject =
            PublishSubject.create();
    private PublishSubject<Resume> resumePublishSubject = PublishSubject.create();
    private PublishSubject<Resume.SaveInstanceState> resumeSaveInstanceStatePublishSubject =
            PublishSubject.create();

    protected RxFragment(
            Class<L> listenerClass,
            ListenerCaller<F, L> listenerCaller,
            int layoutResource
    ) {
        this.listenerClass = listenerClass;
        this.listenerCaller = listenerCaller;
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
                        fragmentCreationPublishSubject.hide()
                )
        );

        listenerCaller.callListener(
                listener,
                fragmentPublishSubject.hide()
        );
        fragmentPublishSubject.onNext(self);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentCreationPublishSubject.onNext(
                new FragmentCreation<>(
                        savedInstanceState,
                        fragmentCreationSaveInstanceStatePublishSubject.hide(),
                        viewCreationPublishSubject
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
                        startPublishSubject.hide()
                )
        );
    }

    @Override
    public final void onStart() {
        super.onStart();

        startPublishSubject.onNext(
                new Start(
                        startSaveInstanceStatePublishSubject.hide(),
                        resumePublishSubject.hide()
                )
        );
    }

    @Override
    public final void onResume() {
        super.onResume();

        resumePublishSubject.onNext(
                new Resume(
                        resumeSaveInstanceStatePublishSubject.hide()
                )
        );
    }

    @Override
    public final void onPause() {
        super.onPause();

        resumePublishSubject.onComplete();
        resumeSaveInstanceStatePublishSubject.onComplete();
        resumePublishSubject = PublishSubject.create();
        resumeSaveInstanceStatePublishSubject = PublishSubject.create();
    }

    @Override
    public final void onStop() {
        super.onStop();

        startPublishSubject.onComplete();
        startSaveInstanceStatePublishSubject.onComplete();
        startPublishSubject = PublishSubject.create();
        startSaveInstanceStatePublishSubject.onComplete();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();

        viewCreationPublishSubject.onComplete();
        viewCreationSaveInstanceStatePublishSubject.onComplete();
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

        fragmentCreationPublishSubject.onComplete();
        fragmentCreationSaveInstanceStatePublishSubject.onComplete();
    }

    @Override
    public final void onDetach() {
        super.onDetach();

        fragmentPublishSubject.onComplete();
        attachmentPublishSubject.onComplete();
    }

    public abstract F getSelf();

    protected abstract void subscribeToAttachmentObservable(
            Observable<Attachment<F, L>> attachmentObservable
    );
}
