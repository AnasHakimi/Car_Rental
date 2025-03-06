package com.example.carrental.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * initialize Retrofit
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    /**
     * Return instance of retrofit
     * @param URL REST API URL
     * @return retrofit instance
     */
    public static Retrofit getClient(String URL) {

        // first API call, no retrofit instance yet?
        if (retrofit == null) {
            // initialize retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // return instance of retrofit
        return retrofit;
    }

}
