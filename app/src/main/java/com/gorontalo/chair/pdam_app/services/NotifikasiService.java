package com.gorontalo.chair.pdam_app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gorontalo.chair.pdam_app.MainActivity;
import com.gorontalo.chair.pdam_app.adapter.AlarmNotificationAdapter;
import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.adapter.VolleyAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotifikasiService extends Service {
    private static final String TAG = NotifikasiService.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_TANGGAL = "tanggal";
    private static final String TAG_ISI = "isi";
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "hasil";

    private SessionAdapter sessionAdapter;
    private Timer timer = new Timer();

    int success;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sessionAdapter = new SessionAdapter(getApplicationContext());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (sessionAdapter.isLoggedIn()){
                    getNotifikasi(sessionAdapter.getId());
                }
            }
        }, 0, 20000);
    }

    private void getNotifikasi(final String id) {
        StringRequest strReq = new StringRequest(Request.Method.POST, new URLAdapter().getSingleNotifikasi(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Data Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.e(TAG, jObj.getString(TAG_MESSAGE));
                        String id = jObj.getString(TAG_ID);
                        String tanggal = jObj.getString(TAG_TANGGAL);
                        String isi = jObj.getString(TAG_ISI);
                        String status = jObj.getString(TAG_STATUS);

                        AlarmNotificationAdapter.showNotification(getApplicationContext(), MainActivity.class, 250,"PDAM-App", isi);
                    } else {
                        Log.e(TAG, jObj.getString(TAG_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Data Errorrrr: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pelanggan", id);

                return params;
            }

        };

        VolleyAdapter.getInstance().addToRequestQueue(strReq, "pdam-app");
    }
}
