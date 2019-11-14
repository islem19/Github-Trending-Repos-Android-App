package dz.islem.githubapi.remote;

import java.util.Map;

import dz.islem.githubapi.models.RepoModel;
import dz.islem.githubapi.remote.rest.RestApi;
import dz.islem.githubapi.remote.rest.RestClient;
import retrofit2.Call;

public class RemoteManager {
    private static RemoteManager INSTANCE;

    private RemoteManager(){}

    public static RemoteManager newInstance(){
        return (INSTANCE == null ? INSTANCE= new RemoteManager() : INSTANCE);
    }

    public Call<RepoModel> getRepositories(Map<String, String> map){
        RestApi api = RestClient.getApiService();
        return api.getRepos(map);
    }
}
