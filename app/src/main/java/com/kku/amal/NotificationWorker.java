package com.kku.amal;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {
String s;
    public NotificationWorker(@NonNull Context context,
                               @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Result doWork() {
        Log.v("1", "called notification");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                    Log.v("1", "query" + s+ "legnth is "+n);
                    NotificationUtil.showNotification(getApplicationContext(), NotificationUtil.UPDATE_CHANNEL_ID,
                            NotificationUtil.UPDATE_NOTIFICATION_ID,  s);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
        return Result.success();
    }

}