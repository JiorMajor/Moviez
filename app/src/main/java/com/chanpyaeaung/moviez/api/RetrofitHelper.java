package com.chanpyaeaung.moviez.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.chanpyaeaung.moviez.api.Api.BASE_URL;

/**
 * Created by Chan Pyae Aung on 5/3/17.
 */

public class RetrofitHelper {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        if(retrofit != null) {
            return retrofit;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }

}
