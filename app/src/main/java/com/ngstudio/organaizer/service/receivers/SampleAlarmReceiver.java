package com.ngstudio.organaizer.service.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ngstudio.organaizer.SampleBootReceiver;
import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.service.SampleSchedulingService;

import java.util.Calendar;

public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;

    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {   
        Intent service = new Intent(context, SampleSchedulingService.class);
        service.putExtras(intent);

        startWakefulService(context, service);
    }

    public void setAlarm(Context context,long dateUnix, long idTask) {

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);

        intent.putExtra(Task.TASK_ID,idTask);
        alarmIntent = PendingIntent.getBroadcast(context, 9, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateUnix);
        //Log.d("SET_ALARM","In Millis"+dateUnix);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),  alarmIntent);


        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {

        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
