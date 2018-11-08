package edu.osu.cse5236.group10.packmap;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";

    public FirebaseNotificationService() { }

    public static void subscribeToTopic(String userId) {
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + userId);
        Log.d(TAG, "Subscribed to topic: user_" + userId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = getString(R.string.notification_channel_new_group_id);
            CharSequence name = getString(R.string.notification_channel_new_group_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // String description = getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setLightColor(Color.MAGENTA);
            // channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (!notificationManager.getNotificationChannels().contains(channel))
                notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + message);
            sendNotification(message, getString(R.string.notification_channel_new_group_id));
        }
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "Deleted messages");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String channelId) {
        Intent intent = new Intent(this, LogInSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_map_black_24dp)
                        .setContentTitle(getString(R.string.notification_added_to_group))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationManager.notify(messageBody.hashCode() /* ID of notification */, notificationBuilder.build());
    }
}
