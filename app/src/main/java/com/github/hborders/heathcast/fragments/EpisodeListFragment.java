package com.github.hborders.heathcast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hborders.heathcast.R;
import com.github.hborders.heathcast.android.FragmentUtil;
import com.github.hborders.heathcast.features.model.EpisodeIdentifiedHolder;
import com.github.hborders.heathcast.features.model.EpisodeImpl;
import com.github.hborders.heathcast.views.recyclerviews.EpisodeRecyclerViewAdapter;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public final class EpisodeListFragment extends Fragment {

    public interface EpisodeListFragmentListener {
        void onEpisodeListFragmentAttached(EpisodeListFragment episodeListFragment);

        Observable<Optional<EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl>> episodeIdentifiedsOptionalObservable(
                EpisodeListFragment episodeListFragment
        );

        void onEpisodeIdentifiedsOptionalError(
                EpisodeListFragment episodeListFragment,
                Throwable throwable
        );

        void onEpisodeFragmentClickedEpisodeIdentified(
                EpisodeListFragment episodeListFragment,
                EpisodeImpl.EpisodeIdentifiedImpl episodeIdentified
        );

        void onEpisodeListFragmentWillDetach(EpisodeListFragment episodeListFragment);
    }

    private static final String TAG = "EpisodeList";
    private static final String EPISODE_PARCELABLES_KEY = "episodeParcelables";

    @Nullable
    private EpisodeListFragmentListener listener;

    @Nullable
    private EpisodeRecyclerViewAdapter adapter;

    @Nullable
    private Disposable disposable;

    public EpisodeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final EpisodeListFragmentListener listener = FragmentUtil.requireFragmentListener(
                this,
                context,
                EpisodeListFragment.EpisodeListFragmentListener.class
        );
        this.listener = listener;
        listener.onEpisodeListFragmentAttached(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_episode_list,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView episodesRecyclerView =
                view.requireViewById(R.id.fragment_episode_list_episodes_recycler_view);
        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        @Nullable final EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl episodeIdentifieds =
                FragmentUtil.getUnparcelableListArgument(
                        this,
                        EpisodeIdentifiedHolder.class,
                        EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl::new,
                        EPISODE_PARCELABLES_KEY
                );
        final EpisodeRecyclerViewAdapter adapter = new EpisodeRecyclerViewAdapter(
                episodeIdentifieds == null ? new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl() : episodeIdentifieds,
                episodeIdentified ->
                        Objects.requireNonNull(this.listener).onEpisodeFragmentClickedEpisodeIdentified(
                                this,
                                episodeIdentified
                        )
        );
        this.adapter = adapter;
        episodesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        disposable = Objects.requireNonNull(this.listener)
                .episodeIdentifiedsOptionalObservable(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        episodeIdentifiedsOptional -> {
                            final Bundle args = new Bundle();
                            args.putParcelableArray(
                                    EPISODE_PARCELABLES_KEY,
                                    episodeIdentifiedsOptional
                                            .map(episodeIdentifieds ->
                                                    episodeIdentifieds
                                                            .stream()
                                                            .map(EpisodeIdentifiedHolder::new)
                                                            .toArray(EpisodeIdentifiedHolder[]::new)
                                            )
                                            .orElse(null)
                            );
                            setArguments(args);
                            Objects.requireNonNull(this.adapter).setEpisodeIdentifieds(
                                    episodeIdentifiedsOptional.orElse(
                                            new EpisodeImpl.EpisodeIdentifiedImpl.EpisodeIdentifiedListImpl()
                                    )
                            );
                        },
                        throwable -> {
                            Objects.requireNonNull(this.listener).onEpisodeIdentifiedsOptionalError(
                                    this,
                                    throwable
                            );
                            Log.e(
                                    TAG,
                                    "Error loading episodes",
                                    throwable
                            );
                        }
                );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        afterOnSaveInstanceStateOrOnStop();
    }

    @Override
    public void onStop() {
        super.onStop();

        afterOnSaveInstanceStateOrOnStop();
    }

    // Note that `onStop` is only called before `onSaveInstanceState()` on Android 28+ devices.
    // Prior to that, the order can be reversed - this is a case Lifecycle specifically handles.
    // I was under the impression that `AutoDispose` does as well
    // https://androidstudygroup.slack.com/archives/C09HE40J0/p1551849597051100

    private void afterOnSaveInstanceStateOrOnStop() {
        @Nullable final Disposable disposable = this.disposable;
        if (disposable != null) {
            disposable.dispose();
            this.disposable = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        final EpisodeListFragmentListener listener = Objects.requireNonNull(this.listener);
        this.listener = null;
        listener.onEpisodeListFragmentWillDetach(this);

        super.onDetach();
    }
}
