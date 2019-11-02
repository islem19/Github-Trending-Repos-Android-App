package dz.islem.githubapi.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static void putSharedPrefes(Context context, int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String mDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("date",mDate).apply();
    }

    public static String getSharedPrefs(Context context){
        String mDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()-24*60*60*1000));
        return PreferenceManager.getDefaultSharedPreferences(context).getString("date",mDate);
    }

    public static int getYear(Context context) {
        String mDate = getSharedPrefs(context);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
    }

    public static int getMonth(Context context) {
        String mDate = getSharedPrefs(context);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(new SimpleDateFormat("MM").format(date));
    }

    public static int getDay(Context context) {
        String mDate = getSharedPrefs(context);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(new SimpleDateFormat("dd").format(date));
    }
}
