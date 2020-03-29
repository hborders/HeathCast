package com.github.hborders.heathcast.features.main;

import androidx.annotation.Nullable;

import com.github.hborders.heathcast.core.Either31;
import com.github.hborders.heathcast.features.main.MainPodcastSearchFragment.MainPodcastSearchFragmentListener;
import com.github.hborders.heathcast.features.search.PodcastSearchFragment;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading;
import com.github.hborders.heathcast.features.search.PodcastSearchPodcastListFragment.PodcastSearchPodcastIdentifiedListState;
import com.github.hborders.heathcast.models.PodcastIdentifiedList;

import java.util.Objects;

public final class MainPodcastSearchFragment extends PodcastSearchFragment<
        MainPodcastSearchFragment,
        MainPodcastSearchFragmentListener
        > {
    public interface MainPodcastSearchFragmentListener extends PodcastSearchFragmentListener<
            MainPodcastSearchFragment,
            MainPodcastSearchFragmentListener
            > {
    }

    private static final class MainPodcastSearchPodcastIdentifiedListServiceResponseLoading
            extends Either31.LeftImpl<
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed,
            PodcastIdentifiedList
            >
            implements PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading {
        MainPodcastSearchPodcastIdentifiedListServiceResponseLoading(PodcastIdentifiedList value) {
            super(value);
        }
    }

    private static final class MainPodcastSearchPodcastIdentifiedListServiceResponseComplete
            extends Either31.MiddleImpl<
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed,
            PodcastIdentifiedList
            >
            implements PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete {
        MainPodcastSearchPodcastIdentifiedListServiceResponseComplete(PodcastIdentifiedList value) {
            super(value);
        }
    }

    private static final class MainPodcastSearchPodcastIdentifiedListServiceResponseFailed
            extends Either31.MiddleImpl<
            PodcastSearchPodcastIdentifiedListAsyncStateLoadingJoinPodcastIdentifiedListServiceResponseLoading,
            PodcastSearchPodcastIdentifiedListAsyncStateCompleteJoinPodcastIdentifiedListServiceResponseComplete,
            PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed,
            PodcastIdentifiedList
            >
            implements PodcastSearchPodcastIdentifiedListAsyncStateFailedJoinPodcastIdentifiedListServiceResponseFailed {
        MainPodcastSearchPodcastIdentifiedListServiceResponseFailed(PodcastIdentifiedList value) {
            super(value);
        }
    }

    private static final class MainPodcastSearchPodcastIdentifiedListState implements PodcastSearchPodcastIdentifiedListState {
        private final boolean enabled;
        private final PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse value;

        MainPodcastSearchPodcastIdentifiedListState(
                boolean enabled,
                PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse value
        ) {
            this.enabled = enabled;
            this.value = value;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MainPodcastSearchPodcastIdentifiedListState that = (MainPodcastSearchPodcastIdentifiedListState) o;
            return enabled == that.enabled &&
                    value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(enabled, value);
        }

        @Override
        public String toString() {
            return "MainPodcastSearchPodcastIdentifiedListState{" +
                    "enabled=" + enabled +
                    ", value=" + value +
                    '}';
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public PodcastSearchPodcastIdentifiedListAsyncStateJoinPodcastIdentifiedListServiceResponse getValue() {
            return value;
        }
    }

    public MainPodcastSearchFragment(
            PodcastSearchPodcastListStateFactory podcastSearchPodcastListStateFactory
    ) {
        super(
                MainPodcastSearchFragmentListener.class,
                MainPodcastSearchPodcastIdentifiedListServiceResponseLoading::new,
                MainPodcastSearchPodcastIdentifiedListServiceResponseComplete::new,
                MainPodcastSearchPodcastIdentifiedListServiceResponseFailed::new,
                MainPodcastSearchPodcastIdentifiedListState::new
        );
    }
}
