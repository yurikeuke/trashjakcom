package trash.jak.id.go.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

import trash.jak.id.go.R;
import trash.jak.id.go.activity.MainActivity;
import trash.jak.id.go.activity.TakePicture;

import static android.R.attr.smallIcon;

/**
 * Created by itp on 06/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private String title;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String myCustomKey = data.get("body");
        String type = data.get("type");


        if ( type.equalsIgnoreCase("1")) {
            //for data payload
            // Check if message contains a data payload.

            //for notification payload so I did not use here
            // Check if message contains a notification payload.
            if (myCustomKey != null && !myCustomKey.equalsIgnoreCase("")) {

                title = "TrashJak";
                sendNotificationSignature(myCustomKey, title);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        // optional, this is to make beautiful icon
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(smallIcon);
                Log.d(TAG, "Message Notification Body: " + myCustomKey);

            }
        } else if (type.equalsIgnoreCase("2")){
            if (myCustomKey != null && !myCustomKey.equalsIgnoreCase("")) {

                title = "TrashJak";
                sendNotificationPicture(myCustomKey, title);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        // optional, this is to make beautiful icon
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(smallIcon);
                Log.d(TAG, "Message Notification Body: " + myCustomKey);
            }

        } else if (type.equalsIgnoreCase("3")){
            if (myCustomKey != null && !myCustomKey.equalsIgnoreCase("")) {

                title = "TrashJak";
                sendNotificationFirebase(myCustomKey, title);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        // optional, this is to make beautiful icon
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(smallIcon);
                Log.d(TAG, "Message Notification Body: " + myCustomKey);
            }

        } else {
            Toast.makeText(MyFirebaseMessagingService.this, "Error, try again!", Toast.LENGTH_SHORT).show();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotificationSignature(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.primary);
            notificationBuilder.setColor(color) .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify( 0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationPicture(String messageBody, String title) {
        Intent intent = new Intent(this, TakePicture.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.primary);
            notificationBuilder.setColor(color);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify( 0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationFirebase(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.primary);
            notificationBuilder.setColor(color);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify( 0 /* ID of notification */, notificationBuilder.build());
    }


}

