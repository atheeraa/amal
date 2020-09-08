package com.kku.amal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {


    public NotificationWorker(@NonNull Context context,
                               @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationUtil.showNotification(getApplicationContext(), NotificationUtil.UPDATE_CHANNEL_ID,
                        NotificationUtil.UPDATE_NOTIFICATION_ID);
        return Result.success();
    }

}