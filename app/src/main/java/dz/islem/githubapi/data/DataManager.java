package dz.islem.githubapi.data;

import android.content.Context;

import java.util.Map;

import dz.islem.githubapi.data.local.SharedPreferencesHelper;
import dz.islem.githubapi.data.model.RepoModel;
import dz.islem.githubapi.data.remote.RemoteManager;
import io.reactivex.Observable;

public class DataManager {
    private static DataManager mInstance = null;
    private RemoteManager remoteManager;
    private SharedPreferencesHelper sharedPreferencesHelper;


    private DataManager(Context context){
        remoteManager = RemoteManager.getInstance();
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
    }

    public static synchronized DataManager getInstance(Context context){
        return mInstance == null ? mInstance = new DataManager(context) : mInstance;
    }

    public void setDate(String date){
        sharedPreferencesHelper.setDate(date);
    }

    public String getDate(){
        return sharedPreferencesHelper.getDate();
    }

    public Observable<RepoModel> getRepos(Map<String, String> map){
        return remoteManager.getRepos(map);
    }
}
