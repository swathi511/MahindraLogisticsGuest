package com.hjsoftware.mahindralogisticsguest.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjsoftware.mahindralogisticsguest.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hjsoftware on 22/12/17.
 */

public class RestClient {
    private static String BASE_URL = BuildConfig.BASE_URL;
    //private static String BASE_URL = "http://mllapi.travelsmate.in/api/";
    private static API REST_CLIENT;


    static {
        setupRestClient();

    }


    public static API get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        REST_CLIENT = retrofit.create(API.class);

        System.out.println("Local url is######### " + BASE_URL);

    }
}
