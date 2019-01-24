package com.github.hborders.heathcast.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.hborders.heathcast.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @Nullable final View view = inflater.inflate(
                R.layout.fragment_main,
                container,
                false
        );

        if (view != null) {
            FloatingActionButton fab = view.findViewById(R.id.add_podcast_fab);
            fab.setOnClickListener(
                    Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_podcastSearchFragment)
            );
        }

        return view;
    }
}
