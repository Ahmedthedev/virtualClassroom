package com.esgi.virtualclassroom.data.api;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.esgi.virtualclassroom.BuildConfig;
//import com.esgi.virtualclassroom..data.dto.EPeople;
//import com.esgi.virtualclassroom..data.dto.EPeopleResult;
//import com.esgi.virtualclassroom..data.dto.ERoot;
//import com.esgi.virtualclassroom..data.dto.mapper.PeopleMapper;
//import com.esgi.virtualclassroom..data.dto.mapper.PeopleResultMapper;
//import com.esgi.virtualclassroom..data.model.People;
//import com.esgi.virtualclassroom..data.model.PeopleResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SWProvider {
    private static final String BASE_URL = "https://swapi.co/api/";
    private static final String FORMAT = "json";

    private SWService swService;

    public SWProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swService = retrofit.create(SWService.class);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();
    }

//    public void getPeopleList(final ApiListener<PeopleResult> listener) {
//        swService.getPeopleList(FORMAT).enqueue(new Callback<EPeopleResult>() {
//            @Override
//            public void onResponse(@NonNull Call<EPeopleResult> call, @NonNull Response<EPeopleResult> response) {
//                if (listener != null) {
//                    PeopleResultMapper mapper = new PeopleResultMapper();
//                    PeopleResult peopleResult = mapper.map(response.body());
//                    listener.onSuccess(peopleResult);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<EPeopleResult> call, @NonNull Throwable t) {
//                if (listener != null) {
//                    listener.onError(t);
//                }
//            }
//        });
//    }
}
