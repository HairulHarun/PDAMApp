package com.gorontalo.chair.pdam_app.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AlarmModel {
    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatus="reminderStatus";
    private static final String year="year";
    private static final String month="month";
    private static final String day="day";
    private static final String hour="hour";
    private static final String min="min";
    private static final String code="code";

    public AlarmModel(Context context){
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public boolean getReminderStatus(){
        return appSharedPrefs.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status){
        prefsEditor.putBoolean(reminderStatus, status);
        prefsEditor.commit();
    }

    public int get_code(){
        return appSharedPrefs.getInt(code, 100);
    }

    public void set_code(int h){
        prefsEditor.putInt(code, h);
        prefsEditor.commit();
    }

    public int get_year(){
        return appSharedPrefs.getInt(year, 2019);
    }

    public void set_year(int h){
        prefsEditor.putInt(year, h);
        prefsEditor.commit();
    }

    public int get_month(){
        return appSharedPrefs.getInt(month, 05);
    }

    public void set_month(int h){
        prefsEditor.putInt(month, h);
        prefsEditor.commit();
    }

    public int get_day(){
        return appSharedPrefs.getInt(day, 04);
    }

    public void set_day(int h){
        prefsEditor.putInt(day, h);
        prefsEditor.commit();
    }

    public int get_hour(){
        return appSharedPrefs.getInt(hour, 20);
    }

    public void set_hour(int h){
        prefsEditor.putInt(hour, h);
        prefsEditor.commit();
    }

    public int get_min(){
        return appSharedPrefs.getInt(min, 0);
    }

    public void set_min(int m){
        prefsEditor.putInt(min, m);
        prefsEditor.commit();
    }

    public void reset(){
        prefsEditor.clear();
        prefsEditor.commit();
    }
}
