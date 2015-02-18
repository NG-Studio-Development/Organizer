package com.ngstudio.organaizer.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ngstudio.organaizer.MainActivity;
import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.service.receivers.SampleAlarmReceiver;
import com.ngstudio.organaizer.ui.activities.NotificationActivity;

public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
    }
    
    public static final String TAG = "Scheduling Demo";
    private NotificationManager notificationManager;
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "notificationEE");
        Log.d(TAG,"onHandleIntent = "+intent.getExtras().get(Task.TASK_ID)) ;
        sendNotification("notificationEE", intent);
        SampleAlarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, Intent intent) {
        notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent content = new Intent(this, NotificationActivity.class)
                .putExtras(intent.getExtras())
                .putExtra(MainActivity.IF_START_BY_NOTIFICATION,true);

         PendingIntent contentIntent = PendingIntent.getActivity(this, 0,content,0);

        long taskId = (long) intent.getExtras().get(Task.TASK_ID);
        Task task = Task.load(Task.class, taskId);
        String contentText = task.description.length()>4 ? task.description.substring(0,4)+"...":task.description;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(task.naming)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(task.description))
                        .setContentText(contentText);

        mBuilder.setContentIntent(contentIntent);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


}
