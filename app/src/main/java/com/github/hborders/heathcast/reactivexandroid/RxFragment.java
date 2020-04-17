package com.github.hborders.heathcast.reactivexandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.core.ClassUtil;
import com.github.hborders.heathcast.core.Function5;
import com.github.hborders.heathcast.core.Triple;
import com.github.hborders.heathcast.core.Tuple;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public abstract class RxFragment<
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
                >
        > extends Fragment {
    public static abstract class Attachment<
            FragmentType extends RxFragment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >,
            ListenerType,
            AttachmentType extends Attachment<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    >
            > {
        public interface AttachmentFactory<
                // even though we don't use these bounds in our declarations,
                // javac gives better error messages with boundary enforcement
                FragmentType extends RxFragment<
                        FragmentType,
                        ListenerType,
                        AttachmentType
                        >,
                ListenerType,
                AttachmentType extends Attachment<
                        FragmentType,
                        ListenerType,
                        AttachmentType
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

        private final Class<AttachmentType> selfClass;
        public final FragmentType fragment;
        public final Context context;
        public final ListenerType listener;
        public final Observable<FragmentCreation> fragmenCreationObservable;
        public final Completable onDetachCompletable;
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        protected Attachment(
                Class<AttachmentType> selfClass,
                FragmentType fragment,
                Context context,
                ListenerType listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            this.selfClass = selfClass;
            this.fragment = fragment;
            this.context = context;
            this.listener = listener;
            this.fragmenCreationObservable = fragmenCreationObservable;
            this.onDetachCompletable = onDetachCompletable;
        }

        @Override
        public final String toString() {
            final String simpleName = ClassUtil.getSpecificSimpleName(
                    Attachment.class,
                    getClass()
            );

            return simpleName + "{" +
                    "fragment=" + fragment +
                    ", context=" + context +
                    ", listener=" + listener +
                    ", fragmenCreationObservable=" + fragmenCreationObservable +
                    ", onDetachCompletable=" + onDetachCompletable +
                    '}';
        }

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
        }

        public final Observable<
                Tuple<
                        AttachmentType,
                        FragmentCreation
                        >
                > mapToFragmentCreation() {
            return fragmenCreationObservable.map(
                    fragmentCreation -> new Tuple<>(
                            getSelf(),
                            fragmentCreation
                    )
            );
        }

        public final Observable<
                Triple<
                        AttachmentType,
                        FragmentCreation,
                        ViewCreation
                        >
                > switchMapToViewCreation() {
            return fragmenCreationObservable.switchMap(
                    fragmentCreation -> fragmentCreation.viewCreationObservableObservable.switchMap(
                            viewCreationObservable -> viewCreationObservable.map(
                                    viewCreation -> new Triple<>(
                                            getSelf(),
                                            fragmentCreation,
                                            viewCreation
                                    )
                            )
                    )
            );
        }

        private AttachmentType getSelf() {
            return Objects.requireNonNull(selfClass.cast(this));
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
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
        }
    }

    public static class ViewCreation {
        public final View view;
        @Nullable
        public final Bundle savedInstanceState;
        public final Observable<ActivityCreation> activityCreationObservable;
        public final Completable onDestroyViewCompletable;
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
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
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
        }
    }

    public static final class Start {
        private final RxFragment<?, ?, ?> fragment;
        public final Observable<Resume> resumeObservable;
        public final Completable onStopCompletable;
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        private Start(
                RxFragment<?, ?, ?> fragment,
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

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
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

    // RxFragment can also add a generic ObservableSource here
    // so that clients can emit events through a single observable.
    public static final class Resume {
        public final Completable onPauseCompletable;
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        private Resume(Completable onPauseCompletable) {
            this.onPauseCompletable = onPauseCompletable;
        }

        @Override
        public String toString() {
            return "Resume{" +
                    "onPauseCompletable=" + onPauseCompletable +
                    '}';
        }

        public final void addDisposable(Disposable disposable) {
            compositeDisposable.add(disposable);
        }
    }

    protected interface OnAttached<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
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
                    >
            > {
        void onAttached(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface WillDetach<
            // even though we don't use these bounds in our declarations,
            // javac gives better error messages with boundary enforcement
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
                    >
            > {
        void willDetach(
                ListenerType listener,
                FragmentType fragment
        );
    }

    protected interface Subscriber<T> {
        Disposable doSubscribe(Observable<T> attachmentObservable);
    }

    protected static <
            SubscribeType extends Subscriber<T>,
            T
            > Disposable checkSubscribe(
            SubscribeType subscribe,
            Observable<T> observable
    ) {
        final AtomicBoolean subscribed = new AtomicBoolean();
        final Disposable disposable = subscribe.doSubscribe(
                observable.doOnSubscribe(
                        ignored -> {
                            subscribed.set(true);
                        }
                )
        );
        if (false == subscribed.get()) {
            throw new IllegalStateException("You must subscribe within subscribe");
        }
        return disposable;
    }

    private final Class<FragmentType> selfClass;
    private final Class<ListenerType> listenerClass;
    private final OnAttached<
            FragmentType,
            ListenerType,
            AttachmentType
            > onAttached;
    private final WillDetach<
            FragmentType,
            ListenerType,
            AttachmentType
            > willDetach;
    private final Function5<
            FragmentType,
            Context,
            ListenerType,
            Observable<FragmentCreation>,
            Completable,
            AttachmentType
            > attachmentFactory;
    @LayoutRes
    private final int layoutResource;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // Don't use a BehaviorSubjects here. Even though we need to store the attachment,
    // fragmentCreation, viewCreation, activityCreation, start, and resume,
    // it's better to store them outside to force Publish semantics on the Observable.
    private PublishSubject<AttachmentType> attachmentPublishSubject = PublishSubject.create();
    private CompletableSubject onDetachCompletableSubject = CompletableSubject.create();
    @Nullable
    private AttachmentType attachment;

    private PublishSubject<FragmentCreation> fragmentCreationPublishSubject =
            PublishSubject.create();
    private boolean setInitialArgumentsLegal = true;
    private PublishSubject<FragmentCreation.SaveInstanceState> saveInstanceStatePublishSubject =
            PublishSubject.create();
    private CompletableSubject onDestroyCompletableSubject = CompletableSubject.create();
    @Nullable
    private FragmentCreation fragmentCreation;

    private PublishSubject<Observable<ViewCreation>> viewCreationObservablePublishSubject =
            PublishSubject.create();

    private PublishSubject<ViewCreation> viewCreationPublishSubject = PublishSubject.create();
    private CompletableSubject viewCreationOnDestroyViewCompletableSubject = CompletableSubject.create();
    @Nullable
    private ViewCreation viewCreation;

    private PublishSubject<ActivityCreation> activityCreationPublishSubject = PublishSubject.create();
    private CompletableSubject activityCreationOnDestroyViewCompletableSubject = CompletableSubject.create();
    @Nullable
    private ActivityCreation activityCreation;

    private PublishSubject<Start> startPublishSubject = PublishSubject.create();
    private CompletableSubject onStopCompletableSubject = CompletableSubject.create();
    @Nullable
    private Start start;

    private PublishSubject<Resume> resumePublishSubject = PublishSubject.create();
    private CompletableSubject onPauseCompletableSubject = CompletableSubject.create();
    @Nullable
    private Resume resume;

    protected RxFragment(
            Class<FragmentType> selfClass,
            Class<ListenerType> listenerClass,
            Attachment.AttachmentFactory<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > attachmentFactory,
            OnAttached<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > onAttached,
            WillDetach<
                    FragmentType,
                    ListenerType,
                    AttachmentType
                    > willDetach,
            @LayoutRes int layoutResource
    ) {
        this.selfClass = selfClass;
        this.listenerClass = listenerClass;
        this.onAttached = onAttached;
        this.willDetach = willDetach;
        this.attachmentFactory = attachmentFactory::newAttachment;
        this.layoutResource = layoutResource;
    }

    // Fragment

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
        final ListenerType listener = FragmentUtil.requireFragmentListener(
                self,
                context,
                listenerClass
        );
        final Disposable disposable = checkSubscribe(
                this::subscribe,
                attachmentPublishSubject
        );
        compositeDisposable.add(
                disposable
        );
        onAttached.onAttached(listener, self);
        final AttachmentType attachment = attachmentFactory.apply(
                self,
                context,
                listener,
                fragmentCreationPublishSubject,
                onDetachCompletableSubject
        );
        this.attachment = attachment;
        attachmentPublishSubject.onNext(attachment);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInitialArgumentsLegal = false;
        final FragmentCreation fragmentCreation = new FragmentCreation(
                savedInstanceState,
                saveInstanceStatePublishSubject,
                viewCreationObservablePublishSubject,
                onDestroyCompletableSubject
        );
        this.fragmentCreation = fragmentCreation;
        fragmentCreationPublishSubject.onNext(
                fragmentCreation
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
        final ViewCreation viewCreation = new ViewCreation(
                view,
                savedInstanceState,
                activityCreationPublishSubject,
                viewCreationOnDestroyViewCompletableSubject
        );
        this.viewCreation = viewCreation;
        viewCreationPublishSubject.onNext(
                viewCreation
        );
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ActivityCreation activityCreation = new ActivityCreation(
                savedInstanceState,
                startPublishSubject,
                activityCreationOnDestroyViewCompletableSubject
        );
        this.activityCreation = activityCreation;
        activityCreationPublishSubject.onNext(
                activityCreation
        );
    }

    @Override
    public final void onStart() {
        super.onStart();

        final Start start = new Start(
                this,
                resumePublishSubject,
                onStopCompletableSubject
        );
        this.start = start;
        startPublishSubject.onNext(
                start
        );
    }

    @Override
    public final void onResume() {
        super.onResume();

        final Resume resume = new Resume(onPauseCompletableSubject);
        this.resume = resume;
        resumePublishSubject.onNext(
                resume
        );
    }

    @Override
    public final void onPause() {
        super.onPause();

        final Resume resume = Objects.requireNonNull(this.resume);
        this.resume = null;
        onPauseCompletableSubject.onComplete();
        resumePublishSubject.onComplete();
        resume.compositeDisposable.dispose();

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

        final Start start = Objects.requireNonNull(this.start);
        this.start = null;
        onStopCompletableSubject.onComplete();
        startPublishSubject.onComplete();
        start.compositeDisposable.dispose();

        onStopCompletableSubject = CompletableSubject.create();
        startPublishSubject = PublishSubject.create();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();

        final ActivityCreation activityCreation = Objects.requireNonNull(this.activityCreation);
        this.activityCreation = null;
        activityCreationOnDestroyViewCompletableSubject.onComplete();
        activityCreationPublishSubject.onComplete();
        activityCreation.compositeDisposable.dispose();

        activityCreationOnDestroyViewCompletableSubject = CompletableSubject.create();
        activityCreationPublishSubject = PublishSubject.create();

        final ViewCreation viewCreation = Objects.requireNonNull(this.viewCreation);
        this.viewCreation = null;
        viewCreationOnDestroyViewCompletableSubject.onComplete();
        viewCreationPublishSubject.onComplete();
        viewCreation.compositeDisposable.dispose();

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

        final FragmentCreation fragmentCreation = Objects.requireNonNull(this.fragmentCreation);
        this.fragmentCreation = null;
        onDestroyCompletableSubject.onComplete();
        viewCreationObservablePublishSubject.onComplete();
        fragmentCreationPublishSubject.onComplete();
        saveInstanceStatePublishSubject.onComplete();
        fragmentCreation.compositeDisposable.dispose();

        onDestroyCompletableSubject = CompletableSubject.create();
        viewCreationObservablePublishSubject = PublishSubject.create();
        fragmentCreationPublishSubject = PublishSubject.create();
        saveInstanceStatePublishSubject = PublishSubject.create();
    }

    @Override
    public final void onDetach() {
        super.onDetach();

        final AttachmentType attachment = Objects.requireNonNull(this.attachment);
        this.attachment = null;
        willDetach.willDetach(
                attachment.listener,
                attachment.fragment
        );
        onDetachCompletableSubject.onComplete();
        attachmentPublishSubject.onComplete();
        attachment.compositeDisposable.dispose();

        // this isn't really necessary because we complete the attachmentBehaviorSubject,
        // but I think people will feel better knowing
        // their disposables are disposed somewhere
        compositeDisposable.clear();

        onDetachCompletableSubject = CompletableSubject.create();
        attachmentPublishSubject = PublishSubject.create();
    }

    // Public API

    public final void setInitialArguments(@Nullable Bundle args) {
        if (setInitialArgumentsLegal) {
            super.setArguments(args);
        } else {
            throw new IllegalStateException();
        }
    }

    // Protected API

    /**
     * Use instead of <code>this</code> when you want to refer to the subclass type.
     */
    protected final FragmentType getSelf() {
        return Objects.requireNonNull(selfClass.cast(this));
    }

    /**
     * The beginning of the Rx graph.
     */
    protected abstract Disposable subscribe(Observable<AttachmentType> attachmentObservable);

    // Private API

    private void superSetArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }
}
