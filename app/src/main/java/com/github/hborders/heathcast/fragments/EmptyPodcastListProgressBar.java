package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class EmptyPodcastListProgressBar extends ProgressBar {
    public EmptyPodcastListProgressBar(Context context) {
        super(context);
    }

    public EmptyPodcastListProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyPodcastListProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmptyPodcastListProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }
}
