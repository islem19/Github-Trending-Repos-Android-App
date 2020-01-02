package dz.islem.githubapi.remote.rest;

import java.util.Map;

import dz.islem.githubapi.models.RepoModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RestApi {

    @GET("/search/repositories")
    Observable<RepoModel> getRepos(@QueryMap Map<String, String> filter);
}
