package dz.islem.githubapi.data.remote;

import java.util.Map;

import dz.islem.githubapi.data.model.RepoModel;
import dz.islem.githubapi.data.remote.rest.RestApi;
import dz.islem.githubapi.data.remote.rest.RestClient;
import io.reactivex.Observable;

public class RemoteManager {
    private static RemoteManager INSTANCE;

    private RemoteManager(){}

    public static RemoteManager newInstance(){
        return (INSTANCE == null ? INSTANCE= new RemoteManager() : INSTANCE);
    }

    public Observable<RepoModel> getRepositories(Map<String, String> map){
        RestApi api = RestClient.getApiService();
        return api.getRepos(map);
    }
}
