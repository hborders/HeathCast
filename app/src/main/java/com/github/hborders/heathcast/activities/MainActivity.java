package com.github.hborders.heathcast.activities;

import android.os.Bundle;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.fragments.PodcastListFragment;
import com.github.hborders.heathcast.fragments.PodcastSearchFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public final class MainActivity extends AppCompatActivity
        implements PodcastSearchFragment.OnPodcastSearchFragmentInteractionListener,
        PodcastListFragment.OnPodcastListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(@Nullable Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item == null) {
            return super.onOptionsItemSelected(item);
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPodcastSearchFragmentInteraction() {

    }

    @Override
    public void onPodcastListFragmentInteraction() {

    }
}
