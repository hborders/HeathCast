package com.github.hborders.heathcast.service;

import android.util.Log;

import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ITunesService {
    private final OkHttpClient okHttpClient;
    private final ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter;

    public ITunesService(OkHttpClient okHttpClient, ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter) {
        this.okHttpClient = okHttpClient;
        this.reactivexOkHttpCallAdapter = reactivexOkHttpCallAdapter;
    }

    public Single<Response> searchForPodcasts(String query) {
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
        Call call = okHttpClient.newCall(request);
        return reactivexOkHttpCallAdapter.single(call);
    }
}
