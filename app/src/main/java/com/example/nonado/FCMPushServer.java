package com.example.nonado;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMPushServer extends FirebaseMessagingService {

    //private  static final String TAG = "FCMPushServer";

    public FCMPushServer() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){

                    Log.d("FCM Token", task.getResult());
                }
            }
        });
    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //수신한 메시지를 처리
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }


    public void showNotification(String title, String message){

        //팝업 터치 시 HomeActivity로 이동
        Intent intent = new Intent(this, HomeActivity.class);

        String channel_id = getString(R.string.default_notification_channel_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // 기본 사운드 알람음 설정.
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("nodado_channel", "nodado_channel", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}



