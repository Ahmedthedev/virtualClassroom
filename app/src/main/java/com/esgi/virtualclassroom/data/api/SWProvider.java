package com.esgi.virtualclassroom.data.api;


import com.esgi.virtualclassroom.BuildConfig;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SWProvider {
    private static final String BASE_URL = "https://us-central1-virtualclassroom-aa052.cloudfunctions.net/";

    private RxService rxService;

    public SWProvider() {
        Retrofit rxRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        rxService = rxRetrofit.create(RxService.class);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();
    }

    public Single<Classroom> getRxPeopleResult() {
        return null;
//        return rxService.getClassrooms().map(ePeopleResult -> new PeopleResultMapper().map(ePeopleResult));
    }

    public Single<Classroom> getPeople(String id) {
        return null;
//        return rxService.getClassroom(id).map(ePeople -> new PeopleMapper().map(ePeople));
    }
}
