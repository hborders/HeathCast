package com.github.hborders.heathcast.services;

import com.github.hborders.heathcast.models.Podcast;
import com.github.hborders.heathcast.reactivexokhttp.ReactivexOkHttpCallAdapter;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Single<List<Podcast>> searchForPodcasts(String query) {
        // https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("itunes.apple.com")
                .addPathSegments("search")
                // either titleTerm, languageTerm, authorTerm, genreIndex,
                // artistTerm, ratingIndex, keywordsTerm, descriptionTerm
                // .addQueryParameter("attribute", "titleTerm")
                .addQueryParameter("entity", "podcast")
                .addQueryParameter("explicit", "Yes")
                .addQueryParameter("limit", "200") // max is 200
                .addQueryParameter("media", "mPodcast")
                .addQueryParameter("term", query)
                .addQueryParameter("version", "2")
                .build();

        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);
        return reactivexOkHttpCallAdapter
                .single(call)
                .subscribeOn(Schedulers.io())
                .flatMap(
                        response -> {
                            final int responseCode = response.code();
                            if (responseCode == 200) {
                                @Nullable final ResponseBody responseBody = response.body();
                                if (responseBody == null) {
                                    return Single.error(new Exception("No responseBody"));
                                } else {
                                    final PodcastSearchResultsJson podcastSearchResultsJson =
                                            gson.fromJson(
                                                    responseBody.charStream(),
                                                    PodcastSearchResultsJson.class
                                            );
                                    responseBody.close();
                                    response.close();

                                    final List<Podcast> podcasts =
                                            Optional
                                                    .ofNullable(podcastSearchResultsJson.results)
                                                    .map(Stream::of)
                                                    .orElseGet(Stream::empty)
                                                    .flatMap(Collection::stream)
                                                    .map(PodcastJson::toPodcast)
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                    return Single.just(podcasts);
                                }
                            } else {
                                return Single.error(new Exception("HTTP Error: " + responseCode));
                            }
                        }
                );
    }
}
