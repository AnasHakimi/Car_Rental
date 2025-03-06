package com.example.carrental.remote;


public class ApiClient {
    private static final String BASE_URL = "http://178.128.220.20/2023607556/api/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}