package com.github.hborders.heathcast.service;

import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public final class PodcastService {
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter;

    public PodcastService(
            OkHttpClient okHttpClient,
            Gson gson,
            ReactivexOkHttpCallAdapter reactivexOkHttpCallAdapter
    ) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.reactivexOkHttpCallAdapter = reactivexOkHttpCallAdapter;
    }

    public Single<List<PodcastJson>> searchForPodcasts(String query) {
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
        return reactivexOkHttpCallAdapter
                .single(call)
                .subscribeOn(Schedulers.io())
                .flatMap(
                        response -> {
                            @Nullable
                            ResponseBody responseBody = response.body();
                            if (responseBody == null) {
                                return Single.error(new Exception("No responseBody"));
                            } else {
                                PodcastSearchResultsJson podcastSearchResultsJson =
                                        gson.fromJson(
                                                responseBody.charStream(),
                                                PodcastSearchResultsJson.class
                                        );
                                responseBody.close();
                                response.close();

                                @Nullable
                                List<PodcastJson> podcastJsons = podcastSearchResultsJson.results;
                                if (podcastJsons == null) {
                                    return Single.just(Collections.emptyList());
                                } else {
                                    return Single.just(podcastJsons);
                                }
                            }
                        }
                );
    }
}
