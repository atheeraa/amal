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
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


public class NotificationUtil {
    private static final String CHANNEL_ID = "123";
    private static final int INTENT_ID = 3417;

    public static void showNotification(Context context, String title, String message, int reqCode) {

        //   PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.n)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
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

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
      return  PendingIntent.getActivity(context, INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
    private static Bitmap largeIcon(Context context) {
        // COMPLETED (5) Get a Resources object from the context.
        Resources res = context.getResources();
        // COMPLETED (6) Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable.ic_local_drink_black_24px
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.n);
        return largeIcon;
    }
}


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
      /*  SharedPreferences prefs = context.getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int arpref = prefs.getInt("ar", 1);
        int enpref = prefs.getInt("en", 1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                        String Select = list.get((r.nextInt(n)));


                    }
                }

                @Override
                // لو صار فيه أي خطأ في القراءة من الديتابيس نقدر نحدد اش يصير هنا
                // حالياً ما سوينا شيء فعلي، ما راح يظهر شيء للمستخدم
                // بس سوينا جملة طباعة عشان نعرف احنا كمبرمجين اش الخطأ اللي صار
                // بس ممكن نظهر للمستخدم عبارة نقول له فيه خطا؟ ليش لا
                // لتس دو ات !
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());

                }
            });
        }


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



    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.n);
        return largeIcon;
    }
}
*/