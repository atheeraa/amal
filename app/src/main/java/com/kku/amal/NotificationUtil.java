package com.kku.amal;

import android.app.AlarmManager;
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

import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


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

import static android.content.Context.ALARM_SERVICE;


public class NotificationUtil {
    private static final String CHANNEL_ID = "123";
    private static final int INTENT_ID = 3417;
    private static String s;
    public static final String UPDATE_CHANNEL_ID = "updates";

    public static final int UPDATE_NOTIFICATION_ID = 1;

    public static final int NOTIFICATION_REQUEST_CODE = 50;


    public static void showNotification(Context context, String channelId, int notificationId) {


        createNotificationChannel(context, channelId);
        SharedPreferences prefs = context.getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int arpref = prefs.getInt("ar", 1);
        int enpref = prefs.getInt("en", 1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String CHANNEL_ID = "newsentence";


        if (arpref == 1 && enpref == 1) {
            DatabaseReference ref = database.getReference("sentences");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> list;
                    list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot dss : ds.getChildren()) {
                            String sentence = dss.getValue(String.class);
                            list.add(sentence);
                        }
                    }
                    Random r = new Random();
                    int n = list.size();
                    if (!list.isEmpty()) {
                        s = list.get((r.nextInt(n)));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());

                }
            });
        }
      if(s!=""){
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
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(context);
        notificationManager.notify(notificationId, mBuilder.build());
    }}


    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.n);
        return largeIcon;
    }


    public static void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //TODO change channel name and description
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
        PendingIntent pendingIntent =PendingIntent.getActivity(context, INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        return pendingIntent;
    }




}

    /*

    public static void showNotification(Context context, String title, String message, int reqCode) {
        SharedPreferences prefs = context.getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int arpref = prefs.getInt("ar", 1);
        int enpref = prefs.getInt("en", 1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String CHANNEL_ID = "newsentence";


        if (arpref == 1 && enpref == 1) {
            DatabaseReference ref = database.getReference("sentences");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> list;
                    list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot dss : ds.getChildren()) {
                            String sentence = dss.getValue(String.class);
                            list.add(sentence);
                        }
                    }
                    Random r = new Random();
                    int n = list.size();
                    if (!list.isEmpty()) {
                        s = list.get((r.nextInt(n)));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());

                }
            });
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.n)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(largeIcon(context))
                .setContentTitle("دَفْعَة")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(s))
                .setContentText(s)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static PendingIntent contentIntent(Context context) {
         Intent startActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        return pendingIntent;
                }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.n);
        return largeIcon;
    }


}


/*

     public static void remindUserBecauseCharging(Context context) {
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
         // Create the NotificationChannel, but only on API 26+ because
         // the NotificationChannel class is new and not in the support library
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             CharSequence name = "A";
             String description = "aag";
             int importance = NotificationManager.IMPORTANCE_DEFAULT;
             NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
             channel.setDescription(description);
             // Register the channel with the system; you can't change the importance
             // or other notification behaviors after this
             NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
             notificationManager.createNotificationChannel(channel);
         }
     }


    // COMPLETED (4) Create a helper method called largeIcon which takes in a Context as a parameter and
// returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.


        /*
    private static final int NOTIFICATION_ID = 1138;
    private static final int INTENT_ID = 3417;
    private static final String ID = "reminder_notification_channel";


    public static void makeNotification(Context context) {
 NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(ID, "notification",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
        }
      /*


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.n)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("")
                .setContentText("nkidxo" )
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Dafa'a"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());


    }




*/