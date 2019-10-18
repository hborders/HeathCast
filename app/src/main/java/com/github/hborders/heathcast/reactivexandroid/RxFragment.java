package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.android.FragmentUtil;

import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public abstract class RxFragment<F extends RxFragment<F, L>, L> extends Fragment {
    protected static final class Attachment<F extends RxFragment<F, L>, L> {
        public final F fragment;
        public final Context context;
        public final L listener;
        public final Observable<FragmentCreation> fragmenCreationObservable;
        public final Completable onDetachCompletable;

        private Attachment(
                F fragment,
                Context context,
                L listener,
                Observable<FragmentCreation> fragmenCreationObservable,
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

    protected static final class FragmentCreation {
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
        public final Observable<Observable<ViewCreation>> viewCreationObservableObservable;
        public final Completable onDestroyCompletable;

        private FragmentCreation(
                @Nullable Bundle savedInstanceState,
                Observable<SaveInstanceState> saveInstanceStateObservable,
                Observable<Observable<ViewCreation>> viewCreationObservableObservable,
                Completable onDestroyCompletable
        ) {
            this.savedInstanceState = savedInstanceState;
            this.saveInstanceStateObservable = saveInstanceStateObservable;
            this.viewCreationObservableObservable = viewCreationObservableObservable;
            this.onDestroyCompletable = onDestroyCompletable;
        }

        @Override
        public String toString() {
            return "FragmentCreation{" +
                    "savedInstanceState=" + savedInstanceState +
                    ", saveInstanceStateObservable=" + saveInstanceStateObservable +
                    ", viewCreationObservableObservable=" + viewCreationObservableObservable +
                    ", onDestroyCompletable=" + onDestroyCompletable +
                    '}';
        }
    }

    protected static class ViewCreation {
        public final View view;
        @Nullable
        public final Bundle savedInstanceState;
        public final Observable<ActivityCreation> activityCreationObservable;
        public final Completable onDestroyViewCompletable;

        private ViewCreation(
                View view,
                @Nullable Bundle savedInstanceState,
                Observable<ActivityCreation> activityCreationObservable,
                Completable onDestroyViewCompletable
        ) {
            this.view = view;
            this.savedInstanceState = savedInstanceState;
            this.activityCreationObservable = activityCreationObservable;
            this.onDestroyViewCompletable = onDestroyViewCompletable;
        }

        @Override
        public String toString() {
            return "ViewCreation{" +
                    "view=" + view +
                    ", savedInstanceState=" + savedInstanceState +
                    ", activityCreationObservable=" + activityCreationObservable +
                    ", onDestroyViewCompletable=" + onDestroyViewCompletable +
                    '}';
        }
    }

    protected static final class ActivityCreation {
        @Nullable
        public final Bundle savedInstanceState;
        public final Observable<Start> startObservable;
        public final Completable onDestroyViewCompletable;

        private ActivityCreation(
                @Nullable Bundle savedInstanceState,
                Observable<Start> startObservable,
                Completable onDestroyViewCompletable
        ) {
            this.savedInstanceState = savedInstanceState;
            this.startObservable = startObservable;
            this.onDestroyViewCompletable = onDestroyViewCompletable;
        }

        @Override
        public String toString() {
            return "ActivityCreation{" +
                    "savedInstanceState=" + savedInstanceState +
                    ", startObservable=" + startObservable +
                    ", onDestroyViewCompletable=" + onDestroyViewCompletable +
                    '}';
        }
    }

    protected static final class Start {
        private final RxFragment<?, ?> fragment;
        public final Observable<Resume> resumeObservable;
        public final Completable onStopCompletable;

        private Start(
                RxFragment<?, ?> fragment,
                Observable<Resume> resumeObservable,
                Completable onStopCompletable
        ) {
            this.fragment = fragment;
            this.resumeObservable = resumeObservable;
            this.onStopCompletable = onStopCompletable;
        }

        @Override
        public String toString() {
            return "Start{" +
                    "fragment=" + fragment +
                    ", resumeObservable=" + resumeObservable +
                    ", onStopCompletable=" + onStopCompletable +
                    '}';
        }

        @RequiresApi(28)
        public final void setArguments(@Nullable Bundle args) {
            // It isn't legal to update arguments except for after onStart
            // because there is no callback before onSaveInstanceState,
            // and once onSaveInstanceState is called, it's too late to update arguments.
            // Only onStart has a guaranteed callback first: onStop.
            // Also:
            // If we used LifecycleObserver for onStop, we could have guarantees about
            // onStop getting called before onSaveInstanceState, and thus
            // we could know for sure that we can't call setArguments anymore.
            // However, LifecycleObserver requires an annotation processor, and that
            // kills build performance, so we're not going to do that.
            // https://androidstudygroup.slack.com/archives/C09HE40J0/p1568259507014000?thread_ts=1551849597.051100&cid=C09HE40J0
            fragment.superSetArguments(args);
        }
    }

    protected static final class Resume {
        public final Completable onPauseCompletable;

        private Resume(Completable onPauseCompletable) {
            this.onPauseCompletable = onPauseCompletable;
        }

        @Override
        public String toString() {
            return "Resume{" +
                    "onPauseCompletable=" + onPauseCompletable +
                    '}';
        }
    }

    protected interface OnAttached<F extends RxFragment<F, L>, L> {
        void onAttached(L listener, F fragment);
    }

    protected interface WillDetach<F extends RxFragment<F, L>, L> {
        void willDetach(L listener, F fragment);
    }

    private final Class<L> listenerClass;
    private final OnAttached<F, L> onAttached;
    private final WillDetach<F, L> willDetach;
    @LayoutRes
    private final int layoutResource;

    private BehaviorSubject<Attachment<F, L>> attachmentBehaviorSubject =
            BehaviorSubject.create();
    private CompletableSubject onDetachCompletableSubject = CompletableSubject.create();

    private PublishSubject<FragmentCreation> fragmentCreationPublishSubject =
            PublishSubject.create();
    private boolean setInitialArgumentsLegal = true;
    private PublishSubject<FragmentCreation.SaveInstanceState> saveInstanceStatePublishSubject =
            PublishSubject.create();
    private CompletableSubject onDestroyCompletableSubject = CompletableSubject.create();

    private PublishSubject<Observable<ViewCreation>> viewCreationObservablePublishSubject =
            PublishSubject.create();

    private PublishSubject<ViewCreation> viewCreationPublishSubject = PublishSubject.create();
    private CompletableSubject viewCreationOnDestroyViewCompletableSubject = CompletableSubject.create();

    private PublishSubject<ActivityCreation> activityCreationPublishSubject = PublishSubject.create();
    private CompletableSubject activityCreationOnDestroyViewCompletableSubject = CompletableSubject.create();

    private PublishSubject<Start> startPublishSubject = PublishSubject.create();
    private CompletableSubject onStopCompletableSubject = CompletableSubject.create();

    private PublishSubject<Resume> resumePublishSubject = PublishSubject.create();
    private CompletableSubject onPauseCompletableSubject = CompletableSubject.create();

    protected RxFragment(
            Class<L> listenerClass,
            OnAttached<F, L> onAttached,
            WillDetach<F, L> willDetach,
            int layoutResource
    ) {
        this.listenerClass = listenerClass;
        this.onAttached = onAttached;
        this.willDetach = willDetach;
        this.layoutResource = layoutResource;
    }

    public final void setInitialArguments(@Nullable Bundle args) {
        if (setInitialArgumentsLegal) {
            super.setArguments(args);
        } else {
            throw new IllegalStateException();
        }
    }

    private void superSetArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Deprecated
    @Override
    @SuppressWarnings("DeprecatedIsStillUsed")
    public final void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
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
                attachmentBehaviorSubject
        );
        onAttached.onAttached(listener, self);
        attachmentBehaviorSubject.onNext(
                new Attachment<>(
                        self,
                        context,
                        listener,
                        fragmentCreationPublishSubject,
                        onDetachCompletableSubject
                )
        );
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInitialArgumentsLegal = false;
        fragmentCreationPublishSubject.onNext(
                new FragmentCreation(
                        savedInstanceState,
                        saveInstanceStatePublishSubject,
                        viewCreationObservablePublishSubject,
                        onDestroyCompletableSubject
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

        viewCreationObservablePublishSubject.onNext(viewCreationPublishSubject);
        viewCreationPublishSubject.onNext(
                new ViewCreation(
                        view,
                        savedInstanceState,
                        activityCreationPublishSubject,
                        viewCreationOnDestroyViewCompletableSubject
                )
        );
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activityCreationPublishSubject.onNext(
                new ActivityCreation(
                        savedInstanceState,
                        startPublishSubject,
                        activityCreationOnDestroyViewCompletableSubject
                )
        );
    }

    @Override
    public final void onStart() {
        super.onStart();

        startPublishSubject.onNext(
                new Start(
                        this,
                        resumePublishSubject,
                        onStopCompletableSubject
                )
        );
    }

    @Override
    public final void onResume() {
        super.onResume();

        resumePublishSubject.onNext(
                new Resume(onPauseCompletableSubject)
        );
    }

    @Override
    public final void onPause() {
        super.onPause();

        onPauseCompletableSubject.onComplete();
        resumePublishSubject.onComplete();

        onPauseCompletableSubject = CompletableSubject.create();
        resumePublishSubject = PublishSubject.create();
    }

    // If we used LifecycleObserver here, we could have guarantees about
    // onStop getting called before onSaveInstanceState, and thus
    // we could know for sure that we can't call setArguments anymore.
    // However, LifecycleObserver requires an annotation processor, and that
    // kills build performance, so we're not going to do that.
    // https://androidstudygroup.slack.com/archives/C09HE40J0/p1568259507014000?thread_ts=1551849597.051100&cid=C09HE40J0
    @Override
    public final void onStop() {
        super.onStop();

        onStopCompletableSubject.onComplete();
        startPublishSubject.onComplete();

        onStopCompletableSubject = CompletableSubject.create();
        startPublishSubject = PublishSubject.create();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();

        activityCreationOnDestroyViewCompletableSubject.onComplete();
        activityCreationPublishSubject.onComplete();

        viewCreationOnDestroyViewCompletableSubject.onComplete();
        viewCreationPublishSubject.onComplete();

        activityCreationOnDestroyViewCompletableSubject = CompletableSubject.create();
        activityCreationPublishSubject = PublishSubject.create();

        viewCreationOnDestroyViewCompletableSubject = CompletableSubject.create();
        viewCreationPublishSubject = PublishSubject.create();
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceStatePublishSubject.onNext(
                new FragmentCreation.SaveInstanceState(outState)
        );
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();

        onDestroyCompletableSubject.onComplete();
        viewCreationObservablePublishSubject.onComplete();
        fragmentCreationPublishSubject.onComplete();
        saveInstanceStatePublishSubject.onComplete();

        onDestroyCompletableSubject = CompletableSubject.create();
        viewCreationObservablePublishSubject = PublishSubject.create();
        fragmentCreationPublishSubject = PublishSubject.create();
        saveInstanceStatePublishSubject = PublishSubject.create();
    }

    @Override
    public final void onDetach() {
        super.onDetach();

        Attachment<F, L> attachment = Objects.requireNonNull(attachmentBehaviorSubject.getValue());
        willDetach.willDetach(attachment.listener, attachment.fragment);
        onDetachCompletableSubject.onComplete();
        attachmentBehaviorSubject.onComplete();

        onDetachCompletableSubject = CompletableSubject.create();
        attachmentBehaviorSubject = BehaviorSubject.create();
    }

    protected abstract F getSelf();

    protected abstract void subscribeToAttachmentObservable(
            Observable<Attachment<F, L>> attachmentObservable
    );
}
