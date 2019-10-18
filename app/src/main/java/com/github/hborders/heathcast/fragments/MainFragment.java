package com.github.hborders.heathcast.fragments;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.reactivexandroid.RxFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.Observable;

public class MainFragment extends RxFragment<
        MainFragment,
        MainFragment.MainFragmentListener> {

    public MainFragment() {
        super(
                MainFragmentListener.class,
                MainFragmentListener::onMainFragmentAttached,
                MainFragmentListener::onMainFragmentWillDetach,
                R.layout.fragment_main
        );
    }

    @Override
    protected MainFragment getSelf() {
        return this;
    }

    @Override
    protected void subscribeToAttachmentObservable(
            Observable<
                    Attachment<
                            MainFragment,
                            MainFragmentListener
                            >
                    > attachmentObservable
    ) {
        attachmentObservable.subscribe(
                attachment -> {
                    final MainFragmentListener listener = attachment.listener;
                    attachment.fragmenCreationObservable.subscribe(
                            fragmentCreation -> {
                                fragmentCreation.viewCreationObservableObservable.subscribe(
                                        viewCreationObservable -> viewCreationObservable.subscribe(
                                                viewCreation -> {
                                                    final FloatingActionButton fab =
                                                            viewCreation.view.requireViewById(R.id.fragment_main_add_podcast_fab);
                                                    fab.setOnClickListener(__ ->
                                                            listener.onClickSearch(MainFragment.this)
                                                    );
                                                }
                                        ).isDisposed()
                                ).isDisposed();
                            }
                    ).isDisposed();
                }
        ).isDisposed();
    }

    public interface MainFragmentListener {
        void onMainFragmentAttached(MainFragment mainFragment);

        void onClickSearch(MainFragment mainFragment);

        void onMainFragmentWillDetach(MainFragment mainFragment);
    }
}
