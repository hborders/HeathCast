package com.github.hborders.heathcast.features.main;

import android.content.Context;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public final class MainFragment extends RxFragment<
        MainFragment,
        MainFragment.MainFragmentListener,
        MainFragment.MainAttachment
        > {
    public interface MainFragmentListener {
        void onMainFragmentAttached(MainFragment mainFragment);

        void onClickSearch(MainFragment mainFragment);

        void onMainFragmentWillDetach(MainFragment mainFragment);
    }

    public static final class MainAttachment extends RxFragment.Attachment<
            MainFragment,
            MainFragment.MainFragmentListener,
            MainAttachment
            > {
        MainAttachment(
                MainFragment fragment,
                Context context,
                MainFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
                    MainAttachment.class,
                    fragment,
                    context,
                    listener,
                    fragmenCreationObservable,
                    onDetachCompletable
            );
        }
    }

    public MainFragment() {
        super(
                MainFragment.class,
                MainFragmentListener.class,
                MainAttachment::new,
                MainFragmentListener::onMainFragmentAttached,
                MainFragmentListener::onMainFragmentWillDetach,
                R.layout.fragment_main
        );
    }

    @Override
    protected final Disposable subscribe(Observable<MainAttachment> attachmentObservable) {
        return attachmentObservable.switchMap(
                RxFragment.Attachment::switchMapToViewCreation
        ).subscribe(
                attachmentFragmentCreationViewCreationTriple -> {
                    final MainFragmentListener listener =
                            attachmentFragmentCreationViewCreationTriple.first.listener;
                    final ViewCreation viewCreation =
                            attachmentFragmentCreationViewCreationTriple.third;

                    final FloatingActionButton fab =
                            viewCreation.view.requireViewById(R.id.fragment_main_add_podcast_fab);
                    fab.setOnClickListener(__ ->
                            listener.onClickSearch(MainFragment.this)
                    );
                }
        );
    }
}
