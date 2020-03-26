package com.github.hborders.heathcast.fragments;

import android.content.Context;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class MainFragment extends RxFragment<
        MainFragment,
        MainFragment.MainFragmentListener,
        MainFragment.Attachment> {
    public static final class Attachment extends RxFragment.Attachment<
            MainFragment,
            MainFragment.MainFragmentListener,
            Attachment> {
        public interface Factory extends RxFragment.Attachment.Factory<
                MainFragment,
                MainFragment.MainFragmentListener,
                Attachment> {
        }

        public Attachment(
                MainFragment fragment,
                Context context,
                MainFragmentListener listener,
                Observable<FragmentCreation> fragmenCreationObservable,
                Completable onDetachCompletable
        ) {
            super(
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
                MainFragmentListener.class,
                Attachment::new,
                MainFragmentListener::onMainFragmentAttached,
                MainFragmentListener::onMainFragmentWillDetach,
                R.layout.fragment_main
        );

        beginRxGraph().switchMap(
                attachmentTypeObservable -> attachmentTypeObservable
        ).switchMap(
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
        ).isDisposed();
    }

    public interface MainFragmentListener {
        void onMainFragmentAttached(MainFragment mainFragment);

        void onClickSearch(MainFragment mainFragment);

        void onMainFragmentWillDetach(MainFragment mainFragment);
    }
}
