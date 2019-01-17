package com.github.hborders.heathcast;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.github.hborders.heathcast.service.PodcastJson;
import com.github.hborders.heathcast.service.PodcastService;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PodcastSearchFragment.OnPodcastSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PodcastSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class PodcastSearchFragment extends Fragment {
    private static final String TAG = "PodcastSearch";

    @Nullable
    private OnPodcastSearchFragmentInteractionListener mListener;
    @Nullable
    private SearchView mSearchView;
    @Nullable
    private RecyclerView mSearchResultsRecyclerView;

    private final PodcastService podcastService = new PodcastService(
            new OkHttpClient(),
            new Gson(),
            ReactivexOkHttpCallAdapter.createWithScheduler(Schedulers.io())
    );

    public PodcastSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PodcastSearchFragment.
     */
    public static PodcastSearchFragment newInstance() {
        PodcastSearchFragment fragment = new PodcastSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        @Nullable View view = inflater.inflate(
                R.layout.fragment_podcast_search,
                container,
                false
        );
        if (view != null) {
            @Nullable SearchView searchView = view.findViewById(R.id.search_view);
            if (searchView != null) {
                mSearchView = searchView;
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Disposable disposable =
                                podcastService.searchForPodcasts(query).subscribe(
                                        podcastJsons -> {

                                            Log.d(TAG, "podcastJsons:\n==========\n");
                                            for (PodcastJson podcastJson : podcastJsons) {
                                                Log.d(TAG, podcastJson.toString());
                                            }
                                        },
                                        throwable -> {
                                            Log.e(TAG, "Error when searching iTunes", throwable);
                                        }
                                );

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                @Nullable Context context = getContext();
                if (context != null) {
                    searchView.setQueryHint(context.getString(R.string.search_query_hint));
                }
            }
            mSearchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        mSearchView = null;
        mSearchResultsRecyclerView = null;

        super.onDestroyView();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onPodcastSearchFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPodcastSearchFragmentInteractionListener) {
            mListener = (OnPodcastSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPodcastSearchFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPodcastSearchFragmentInteraction();
    }
}
