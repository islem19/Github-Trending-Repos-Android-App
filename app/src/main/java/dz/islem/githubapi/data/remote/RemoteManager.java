package dz.islem.githubapi.data.remote;

import java.util.Map;

import dz.islem.githubapi.data.model.RepoModel;
import dz.islem.githubapi.data.remote.rest.RestApi;
import dz.islem.githubapi.data.remote.rest.RestClient;
import io.reactivex.Observable;

public class RemoteManager {
    private static RemoteManager mInstance = null;

    private RemoteManager(){}

    public static RemoteManager getInstance(){
        return mInstance == null ? mInstance= new RemoteManager() : mInstance;
    }

    public Observable<RepoModel> getRepos(Map<String, String> map){
        RestApi api = RestClient.getApiService();
        return api.getRepos(map);
    }
}
