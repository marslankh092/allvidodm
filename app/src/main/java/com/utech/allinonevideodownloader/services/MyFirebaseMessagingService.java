package com.utech.allinonevideodownloader.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.utech.allinonevideodownloader.MainActivity;
import com.utech.allinonevideodownloader.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String MyTag = "MessagingService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("mytoken", "Refreshed token: " + s);
        sendRegistrationToServer(s);

    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(MyTag, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(MyTag, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

            } else {
                // Handle message within 10 seconds

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(MyTag, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            showNotiFication(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());


        }


    }

    public void showNotiFication(String notificationTitle, String notificationBody) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getPackageName() + "-" + getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_download_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(getPackageName() + "--" + "FirebaseMessageing", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.auto_download_title_notification));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        Random random = new Random();
        int num = random.nextInt(5);
        mNotificationManager.notify(num, notification);

    }


}
