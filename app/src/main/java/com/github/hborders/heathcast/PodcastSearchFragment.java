package com.github.hborders.heathcast;

import android.app.SearchableInfo;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PodcastSearchFragment.OnPodcastSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PodcastSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PodcastSearchFragment extends Fragment {
    private static final String TAG = "PodcastSearch";

    @Nullable
    private OnPodcastSearchFragmentInteractionListener mListener;
    @Nullable
    private SearchView mSearchView;
    @Nullable
    private RecyclerView mSearchResultsRecyclerView;

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
                        // https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/
                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("https")
                                .host("itunes.apple.com")
                                .addPathSegments("search")
                                // either titleTerm, languageTerm, authorTerm, genreIndex,
                                // artistTerm, ratingIndex, keywordsTerm, descriptionTerm
                                // .addQueryParameter("attribute", "titleTerm")
                                .addQueryParameter("entity", "podcast")
                                .addQueryParameter("explicit", "Yes")
                                .addQueryParameter("limit", "200") // max is 200
                                .addQueryParameter("media", "podcast")
                                .addQueryParameter("term", query)
                                .addQueryParameter("version", "2")
                                .build();

                        Request request = new Request.Builder().url(url).build();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e(TAG, "Error when searching iTunes", e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                ResponseBody body = response.body();
                                if (body != null) {
                                    String bodyString = body.string();
                                    Log.d(TAG, "response:\n==========\n" + bodyString + "\n==========");
                                }
                                response.close();
                            }
                        });

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
