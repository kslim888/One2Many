package com.kslimweb.firebasemessasging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kslimweb.one2many.R;
import com.kslimweb.one2many.client.ClientTranslation;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kslimweb.one2many.host.ShowQRCode.SUBSCRIBE_TOPIC;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "FCM Data: " + remoteMessage.getData());
            Log.d(TAG, "FCM Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "FCM Message: " + remoteMessage.getNotification().getBody());

            // show notification if app is in foreground
            if(remoteMessage.getData().isEmpty()) {
               setSpeechToText(remoteMessage.getNotification().getBody());
//                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }
    }

    void setSpeechToText(String body) {
        Log.d(TAG, "setSpeechToText: " + body);
        Intent intent = new Intent("MESSAGE");
        intent.putExtra("MESSAGE_BODY", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.kslimweb.ucts";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                   NOTIFICATION_CHANNEL_ID,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("One2Many Channel");
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this
                , NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.BLUE)
                .setLights(Color.BLUE, 1000, 300)
                .setDefaults(Notification.DEFAULT_ALL);
//               .setSmallIcon(R.drawable.ic_message_black_24dp);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

     public void sendNotification(String title, String body) {
        NotifyData notifyData = new NotifyData(title, body);
        FirebaseMessage firebaseMessage = new FirebaseMessage("/topics/" + SUBSCRIBE_TOPIC, notifyData);

        FirebaseAPI firebaseAPI = FirebaseClient.getClient().create(FirebaseAPI.class);
        Call<FirebaseMessage> call = firebaseAPI.sendMessage(firebaseMessage);
        call.enqueue(new Callback<FirebaseMessage>() {
            @Override
            public void onResponse(Call<FirebaseMessage> call, Response<FirebaseMessage> response) {
                Log.d(TAG, call.request().toString());
            }

            @Override
            public void onFailure(Call<FirebaseMessage> call, Throwable t) {
                Log.d(TAG,"Message Failed send");
                Log.d(TAG, t.getMessage());
            }
        });
    }

    /**
     * Persist token to third-party servers.
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token
     */
//    public void sendRegistrationToServer(String token) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(USERS_TOKEN).child(CHILD_TOKEN);
//
//        myRef.push().setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()) {
//                    Log.d(TAG, "onComplete Save Token: Token Saved");
//                }
//            }
//        });
//    }


}