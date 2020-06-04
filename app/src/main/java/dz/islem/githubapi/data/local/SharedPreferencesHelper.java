package dz.islem.githubapi.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String DATE = "date";
    private final SharedPreferences sharedPreferences;
    private static SharedPreferencesHelper mInstance = null;

    public static synchronized SharedPreferencesHelper getInstance(Context context){
        return mInstance == null ? mInstance = new SharedPreferencesHelper(context) : mInstance;
    }

    private SharedPreferencesHelper(Context context){
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    public void setDate(String date){
        sharedPreferences.edit()
                .putString(DATE, date)
                .apply();
    }

    public String getDate(){
        return sharedPreferences.getString(DATE,null);
    }


}
