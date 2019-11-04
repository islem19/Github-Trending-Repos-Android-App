package dz.islem.githubapi.remote.rest;

import dz.islem.githubapi.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {


    private static Retrofit getRetroInstance(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RestApi getApiService(){
        return getRetroInstance().create(RestApi.class);
    }
}
