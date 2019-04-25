package com.thumbstage.hydrogen.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


public class NotificationUtils {

    private static final String CHANNEL_ID = "Hydrogen";
    private static int lastNotificationId = 0;

    public static void showNotification(Context context, String title, String content, Intent intent) {
        showNotification(context, title, content, null, intent);
    }

    public static void showNotification(Context context, String title, String content, String sound, Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setContentTitle(title).setAutoCancel(true).setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(content);
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        if (sound != null && sound.trim().length() > 0) {
            notification.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound);
        }
        lastNotificationId = (lastNotificationId > 10000 ? 0 : lastNotificationId + 1);
        manager.notify(lastNotificationId, notification);
    }
}
