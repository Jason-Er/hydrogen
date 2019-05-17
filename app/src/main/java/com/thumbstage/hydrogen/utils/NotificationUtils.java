package com.thumbstage.hydrogen.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.thumbstage.hydrogen.view.browse.BrowseActivity;
import com.thumbstage.hydrogen.view.create.CreateActivity;


public class NotificationUtils {

    private static final String CHANNEL_ID = "Hydrogen";
    private static int lastNotificationId = 0;

    public static void showNotification(Context context, String title, String content, Intent intent) {
        showNotification(context, title, content, null, intent);
    }

    public static void showNotification(Context context, String title, String content, Intent intent, int id) {
        showNotification(context, title, content, null, intent, id);
    }

    public static void showNotification(Context context, String title, String content, String sound, Intent intent) {
        lastNotificationId = (lastNotificationId > 10000 ? 0 : lastNotificationId + 1);
        showNotification(context, title, content, sound, intent, lastNotificationId);
    }

    public static void showNotification(Context context, String title, String content, String sound, Intent intent, int id) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CreateActivity.class);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setContentTitle(title).setAutoCancel(true).setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(content);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        if (sound != null && sound.trim().length() > 0) {
            notification.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound);
        }
        manager.notify(id, notification);
    }
}
