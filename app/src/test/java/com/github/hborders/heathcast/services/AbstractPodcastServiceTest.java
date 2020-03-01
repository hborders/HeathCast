package com.github.hborders.heathcast.services;

import androidx.test.core.app.ApplicationProvider;

import com.github.hborders.heathcast.dao.Database;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractPodcastServiceTest {
    @Nullable
    private PodcastService podcastService;

    protected final PodcastService getPodcastService() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException {
        @Nullable final PodcastService initialPodcastService = this.podcastService;
        if (initialPodcastService == null) {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            String fileSep = File.separator;
            String defaultStorePath = System.getProperty("java.home") + fileSep + "lib" + fileSep + "security";
            String defaultStore = defaultStorePath + fileSep + "cacerts";
            String jsseDefaultStore = defaultStorePath + fileSep + "jssecacerts";
            String storePropName = System.getProperty("javax.net.ssl.trustStore", jsseDefaultStore);
            String storePropType = System.getProperty("javax.net.ssl.trustStoreType", KeyStore.getDefaultType());
            String storePropProvider = System.getProperty("javax.net.ssl.trustStoreProvider", "");
            String storePropPassword = System.getProperty("javax.net.ssl.trustStorePassword", "");
            System.out.println(storePropName);
            System.out.println(storePropType);
            System.out.println(storePropProvider);
            System.out.println(storePropPassword);
            try {
                trustManagerFactory.init((KeyStore) null);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();
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
                    .addQueryParameter("term", "Planet Money")
                    .addQueryParameter("version", "2")
                    .build();
            final Request request = new Request.Builder().url(url).build();
            final Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                System.out.println(response);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            final Database<Object> database = new Database<>(
                    ApplicationProvider.getApplicationContext(),
                    null, // in-memory
                    Schedulers.trampoline()
            );
            final PodcastService newPodcastService = new PodcastService(
                    database,
                    Schedulers.trampoline()
            );

            this.podcastService = newPodcastService;
            return newPodcastService;
        } else {
            return initialPodcastService;
        }
    }
}
