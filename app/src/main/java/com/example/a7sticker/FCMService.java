package com.example.a7sticker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = FCMService.class.getSimpleName();
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String newToken) {

        Log.d(TAG, "Refreshed token: " + newToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(newToken);
    }


    /**
     * Called when message is received.
     * Mainly what you need to implement
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    //
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        myClassifier(remoteMessage);
    }
    // [END receive_message]

    private void myClassifier(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0){
            String title = remoteMessage.getData().get("title");
            String receiver = remoteMessage.getData().get("receiver");
            String sender = remoteMessage.getData().get("Sender");
            int stickerId = Integer.parseInt(remoteMessage.getData().get("stickerId"));
            showNotification(title, sender, receiver, stickerId);
        }
    }

    private void showNotification(String title, String sender, String receiver, int stickerId) {

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("username", receiver);
        intent.putExtra("Sender", sender);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(Utils.getSticker(getApplicationContext(),stickerId)))
                        .setContentTitle(title)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        Random random = new Random();
        notificationManager.notify(random.nextInt(), builder.build());

    }

}
