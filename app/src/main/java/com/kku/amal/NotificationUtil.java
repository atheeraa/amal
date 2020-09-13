package com.kku.amal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtil {
    private static final int INTENT_ID = 3417;
    private static String s;
    public static final String UPDATE_CHANNEL_ID = "updates";

    public static final int UPDATE_NOTIFICATION_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showNotification(Context context, String channelId, int notificationId, String s) {

Log.v("1","Notifcatiooooooooooooooooooooooooon");


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context, channelId)
                .setSmallIcon(R.drawable.n)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(largeIcon(context))
                .setContentTitle("دَفْعَة")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(s))
                .setContentText(s)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.COLOR_DEFAULT)
                .setContentIntent(contentIntent(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(context);
        notificationManager.notify(notificationId, mBuilder.build());
        createNotificationChannel(context, channelId);


    }


    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.n);
        return largeIcon;
    }


    public static void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "notify";
            String description = "sentences";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        return pendingIntent;
    }


}