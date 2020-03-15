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
import com.github.hborders.heathcast.core.Triple;
import com.github.hborders.heathcast.core.Tuple;

import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public abstract class RxFragment<
        FragmentType extends RxFragment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        ListenerType,
        AttachmentType extends RxFragment.Attachment<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >,
        AttachmentFactoryType extends RxFragment.Attachment.Factory<
                FragmentType,
                ListenerType,
                AttachmentType,
                AttachmentFactoryType
                >
        > extends Fragment {
    public static abstract class Attachment<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >
            > {

        public interface Factory<
                FragmentType extends RxFragment<
                        FragmentType,
                        ListenerType,
                        AttachmentType,
                        AttachmentFactoryType
                        >,
                ListenerType,
                AttachmentType extends RxFragment.Attachment<
                        FragmentType,
                        ListenerType,
                        AttachmentType,
                        AttachmentFactoryType
                        >,
                AttachmentFactoryType extends RxFragment.Attachment.Factory<
                        FragmentType,
                        ListenerType,
                        AttachmentType,
                        AttachmentFactoryType
                        >
                > {
            AttachmentType newAttachment(
                    FragmentType fragment,
                    Context context,
                    ListenerType listener,
                    Observable<FragmentCreation> fragmenCreationObservable,
                    Completable onDetachCompletable
            );
        }

        public final FragmentType fragment;
        public final Context context;
        public final ListenerType listener;
        public final Observable<FragmentCreation> fragmenCreationObservable;
        public final Completable onDetachCompletable;

        protected Attachment(
                FragmentType fragment,
                Context context,
                ListenerType listener,
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
        public final String toString() {
            @SuppressWarnings("rawtypes") final Class<? extends Attachment> clazz = getClass();
            final String simpleName;
            if (clazz.isAnonymousClass()) {
                simpleName = "Attachment$";
            } else {
                simpleName = clazz.getSimpleName();
            }

            return simpleName + "{" +
                    "fragment=" + fragment +
                    ", context=" + context +
                    ", listener=" + listener +
                    ", fragmenCreationObservable=" + fragmenCreationObservable +
                    ", onDetachCompletable=" + onDetachCompletable +
                    '}';
        }
    }

    public static final class FragmentCreation {
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

    public static class ViewCreation {
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

        public final Observable<Start> switchMapToStart() {
            return activityCreationObservable.switchMap(
                    activityCreation -> activityCreation.startObservable
            );
        }

        public final Observable<Resume> switchMapToResume() {
            return activityCreationObservable.switchMap(
                    activityCreation -> activityCreation.startObservable.switchMap(
                            start -> start.resumeObservable
                    )
            );
        }
    }

    public static final class ActivityCreation {
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

    public static final class Start {
        private final RxFragment<?, ?, ?, ?> fragment;
        public final Observable<Resume> resumeObservable;
        public final Completable onStopCompletable;

        private Start(
                RxFragment<?, ?, ?, ?> fragment,
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

    public static final class Resume {
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

    protected interface OnAttached<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >
            > {
        void onAttached(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface WillDetach<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            ListenerType,
            AttachmentType extends RxFragment.Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >,
            AttachmentFactoryType extends RxFragment.Attachment.Factory<
                    FragmentType,
                    ListenerType,
                    AttachmentType,
                    AttachmentFactoryType
                    >
            > {
        void willDetach(
                ListenerType listener,
                FragmentType fragment
        );
    }

    private final Class<ListenerType> listenerClass;
    private final AttachmentFactoryType attachmentFactory;
    private final OnAttached<FragmentType, ListenerType, AttachmentType, AttachmentFactoryType> onAttached;
    private final WillDetach<FragmentType, ListenerType, AttachmentType, AttachmentFactoryType> willDetach;
    @LayoutRes
    private final int layoutResource;

    private BehaviorSubject<AttachmentType> attachmentBehaviorSubject =
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
            Class<ListenerType> listenerClass,
            AttachmentFactoryType attachmentFactory,
            OnAttached<FragmentType, ListenerType, AttachmentType, AttachmentFactoryType> onAttached,
            WillDetach<FragmentType, ListenerType, AttachmentType, AttachmentFactoryType> willDetach,
            int layoutResource
    ) {
        this.listenerClass = listenerClass;
        this.attachmentFactory = attachmentFactory;
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

    protected final Observable<
            Tuple<
                    AttachmentType,
                    FragmentCreation
                    >
            > switchMapToFragmentCreation(
            Observable<AttachmentType> attachmentObservable
    ) {
        return attachmentObservable.switchMap(
                attachment -> attachment.fragmenCreationObservable.map(
                        fragmentCreation -> new Tuple<>(attachment, fragmentCreation)
                )
        );
    }

    protected final Observable<
            Triple<
                    AttachmentType,
                    FragmentCreation,
                    ViewCreation
                    >
            > switchMapToViewCreation(
            Observable<AttachmentType> attachmentObservable
    ) {
        return attachmentObservable.switchMap(
                attachment -> attachment.fragmenCreationObservable.switchMap(
                        fragmentCreation -> fragmentCreation.viewCreationObservableObservable.switchMap(
                                viewCreationObservable -> viewCreationObservable.map(
                                        viewCreation -> new Triple<>(
                                                attachment,
                                                fragmentCreation,
                                                viewCreation
                                        )
                                )
                        )
                )
        );
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

        final FragmentType self = getSelf();
        if (self.getClass() != getClass()) {
            throw new IllegalStateException(
                    "getSelf().getClass(): "
                            + self.getClass() + " != getClass(): "
                            + getClass()
            );
        }

        final ListenerType listener = FragmentUtil.requireFragmentListener(
                self,
                context,
                listenerClass
        );

        subscribeToAttachmentObservable(
                attachmentBehaviorSubject
        );
        onAttached.onAttached(listener, self);
        final AttachmentType attachment = attachmentFactory.newAttachment(
                self,
                context,
                listener,
                fragmentCreationPublishSubject,
                onDetachCompletableSubject
        );
        attachmentBehaviorSubject.onNext(attachment);
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

        AttachmentType attachment = Objects.requireNonNull(attachmentBehaviorSubject.getValue());
        willDetach.willDetach(attachment.listener, attachment.fragment);
        onDetachCompletableSubject.onComplete();
        attachmentBehaviorSubject.onComplete();

        onDetachCompletableSubject = CompletableSubject.create();
        attachmentBehaviorSubject = BehaviorSubject.create();
    }

    protected final FragmentType getSelf() {
        return (FragmentType) this;
    }

    ;

    protected abstract void subscribeToAttachmentObservable(
            Observable<AttachmentType> attachmentObservable
    );
}
