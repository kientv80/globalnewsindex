package com.vns.webstore.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

import com.vns.webstore.ui.activity.MainActivity;
import com.webstore.webstore.R;

/**
 * Created by LAP10572-local on 9/11/2016.
 */
public class Notification {
    public static final int NOTIFY_ID_NEWS_UPDATE = 123;
    public static void notifyNewUpate(Context context, String title, String content, int icon, int notifyId) {
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context).setSmallIcon(icon).setContentTitle(title).setContentText(content);
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("from","notification");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }
}
