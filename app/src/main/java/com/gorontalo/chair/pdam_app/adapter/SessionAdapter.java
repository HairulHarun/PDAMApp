package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gorontalo.chair.pdam_app.LoginActivity;
import com.gorontalo.chair.pdam_app.RiwayatTagihanActivity;

public class SessionAdapter {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Sesi";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_SHOW = "IsShow";
    public static final String KEY_ID = "id";
    public static final String KEY_ID_PDAM = "id_pdam";
    public static final String KEY_ID_KLASIFIKASI = "id_klasifikasi";
    public static final String KEY_NOSAMBUNGAN = "nosambungan";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_KTP = "ktp";
    public static final String KEY_HP = "hp";
    public static final String KEY_SEX = "sex";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_PHOTO = "photo";

    public SessionAdapter(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String id_pdam, String id_klasifikasi, String nosambungan, String nama, String ktp, String hp, String sex, String alamat, String photo){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ID_PDAM, id_pdam);
        editor.putString(KEY_ID_KLASIFIKASI, id_klasifikasi);
        editor.putString(KEY_NOSAMBUNGAN, nosambungan);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_KTP, ktp);
        editor.putString(KEY_HP, hp);
        editor.putString(KEY_SEX, sex);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_PHOTO, photo);
        editor.commit();
    }

    public void createDialogSession(){
        editor.putBoolean(IS_SHOW, true);
        editor.commit();
    }

    public void checkLoginMain(String id, String nama, String kepala, String hp, String email, String alamat, String deskripsi, double latitude, double longitude, String photo){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.putExtra("id", id);
            i.putExtra("nama", nama);
            i.putExtra("kepala", kepala);
            i.putExtra("hp", hp);
            i.putExtra("email", email);
            i.putExtra("alamat", alamat);
            i.putExtra("deskripsi", deskripsi);
            i.putExtra("lat", latitude);
            i.putExtra("long", longitude);
            i.putExtra("photo", photo);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void checkLogin(String id, String nama, String kepala, String hp, String email, String alamat, String deskripsi, double latitude, double longitude, String photo){
        if(this.isLoggedIn()){
            Intent i = new Intent(_context, RiwayatTagihanActivity.class);
            i.putExtra("id", id);
            i.putExtra("nama", nama);
            i.putExtra("kepala", kepala);
            i.putExtra("hp", hp);
            i.putExtra("email", email);
            i.putExtra("alamat", alamat);
            i.putExtra("deskripsi", deskripsi);
            i.putExtra("lat", latitude);
            i.putExtra("long", longitude);
            i.putExtra("photo", photo);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public String getId(){
        String user = pref.getString(KEY_ID, null);
        return user;
    }

    public String getIdPdam(){
        String user = pref.getString(KEY_ID_PDAM, null);
        return user;
    }

    public String getIdKlasifikasi(){
        String user = pref.getString(KEY_ID_KLASIFIKASI, null);
        return user;
    }

    public String getNoSambungan(){
        String user = pref.getString(KEY_NOSAMBUNGAN, null);
        return user;
    }

    public String getNama(){
        String user = pref.getString(KEY_NAMA, null);
        return user;
    }

    public String getKtp(){
        String user = pref.getString(KEY_KTP, null);
        return user;
    }

    public String getHp(){
        String user = pref.getString(KEY_HP, null);
        return user;
    }

    public String getSex(){
        String user = pref.getString(KEY_SEX, null);
        return user;
    }

    public String getAlamat(){
        String user = pref.getString(KEY_ALAMAT, null);
        return user;
    }

    public String getPhoto(){
        String user = pref.getString(KEY_PHOTO, null);
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isShow(){
        return pref.getBoolean(IS_SHOW, false);
    }
}
