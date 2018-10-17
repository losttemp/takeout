package com.baidu.iov.dueros.waimai.net;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 8:52
 * @change
 * @class describe
 */

public class ApiInstance {

    private static final String TAG = "ApiInstance";
//    public static final String TV_Host_Url = "http://tv.ichunsun.com";

    Retrofit mRetrofit;

    private ApiInstance() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Config.getHost())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    public static ApiInstance getApiInstance() {
        return SingletonHandler.sApiInstance;
    }

    private TakeawayApi mFilmApi;

    public static TakeawayApi getApi() {
        ApiInstance instance = getApiInstance();
        if (instance.mFilmApi == null) {
            instance.mFilmApi = instance.mRetrofit.create(TakeawayApi.class);
        }
        return instance.mFilmApi;
    }



    private static class SingletonHandler {
        private static final ApiInstance sApiInstance = new ApiInstance();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //set log Level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //set Cookie and Content-Type
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader(Config.CONTENT_TYPE_KEY, Config.CONTENT_TYPE_VALUE)
                        .addHeader(Config.COOKIE_KEY, Config.COOKIE_VALUE)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(logging);
        return httpClient.build();
    }


}
