package com.jiajie.design.api;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * GankService
 * Created by jiajie on 16/7/7.
 */
public class GankService {

    public static final String BASE_URL = "http://gank.io/api/";


    private static GankServiceApi api;

    public GankService() {
    }

    public static GankServiceApi getGankApi() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GankServiceApi.class);
        }
        return api;
    }

}
