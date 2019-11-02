package dz.islem.githubapi.remote.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private final static String BASE_URL = "https://api.github.com";

    private static Retrofit getRetroInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RestApi getApiService(){
        return getRetroInstance().create(RestApi.class);
    }
}
