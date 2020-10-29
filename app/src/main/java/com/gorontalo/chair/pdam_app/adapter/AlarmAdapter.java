package com.gorontalo.chair.pdam_app.adapter;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.gorontalo.chair.pdam_app.MainActivity;
import com.gorontalo.chair.pdam_app.model.AlarmModel;

import java.util.Calendar;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmAdapter extends BroadcastReceiver {
    String TAG = "Alarm Adapter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                AlarmModel localData = new AlarmModel(context);
                AlarmNotificationAdapter.setReminder(context, AlarmAdapter.class, localData.get_code(), localData.get_year(), localData.get_month(), localData.get_day(), localData.get_hour(), localData.get_min());
                return;
            }
        }

        Toast.makeText(context, "Notifikasi Diterima !", Toast.LENGTH_LONG).show();

        AlarmModel localData = new AlarmModel(context);
        AlarmNotificationAdapter.showNotification(context, MainActivity.class, localData.get_code(),"PDAM-App", "Notifikasi pengingat pembayaran !");
    }

}
