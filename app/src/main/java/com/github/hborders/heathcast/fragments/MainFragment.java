package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import javax.annotation.Nullable;

~~~~~~~~~
// Make this an RxFragment, and then finally start showing subscriptions
public class MainFragment extends Fragment {
    @Nullable
    private MainFragmentListener listener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final MainFragmentListener listener = FragmentUtil.requireContextFragmentListener(
                context,
                MainFragment.MainFragmentListener.class
        );
        this.listener = listener;
        listener.onMainFragmentAttached(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = Objects.requireNonNull(
                inflater.inflate(
                        R.layout.fragment_main,
                        container,
                        false
                )
        );

        final FloatingActionButton fab = view.requireViewById(R.id.fragment_main_add_podcast_fab);
        fab.setOnClickListener(__ ->
                Objects.requireNonNull(listener).onClickSearch(MainFragment.this)
        );

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        final MainFragmentListener listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onMainFragmentWillDetach(this);

        super.onDetach();
    }

    public interface MainFragmentListener {
        void onMainFragmentAttached(MainFragment mainFragment);

        void onClickSearch(MainFragment mainFragment);

        void onMainFragmentWillDetach(MainFragment mainFragment);
    }
}
