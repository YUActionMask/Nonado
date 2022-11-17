package com.example.nonado;


import android.app.Notification;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.nonado.MyinfoActivity;


public class FCMPushServer extends FirebaseMessagingService {

    private DatabaseReference postDatabase;
    private DatabaseReference userDatabase;
    private
    Intent intent = null;
    private PendingIntent pendingIntent = null;

    private static final String TAG = "FCMPushServer";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String user_id;


    public FCMPushServer() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();

        firebaseAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference();


        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {


                    user = FirebaseAuth.getInstance().getCurrentUser();
                    user_id = user.getEmail().split("@")[0];
                    userDatabase.child("User").child(user_id).child("token").setValue(task.getResult());

                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        //Log.w("milkyLog", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }

                                    // Get new FCM registration token
                                    String token = task.getResult();

                                }
                            });

                }
            }
        });
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //수신한 메시지를 처리
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("click_action"));
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }


    public void showNotification(String title, String message, String intent_str) {

        final String[] comment = new String[1];
        final String[] post_name = new String[1];
        final String[] location = new String[1];

        if (intent_str.equals("DetailActivity")) {

            intent = new Intent(this, DetailActivity.class);
            String channel_id = getString(R.string.default_notification_channel_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 기본 사운드 알람음 설정.
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(uri)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{1000, 1000, 1000})
                            .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("nonado_channel", "nonado_channel", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(0, notificationBuilder.build());
            /** postDatabase = FirebaseDatabase.getInstance().getReference("Post").child(title);

             ValueEventListener postListener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
            comment[0] = dataSnapshot.child("comment").getValue().toString();
            post_name[0] = dataSnapshot.child("name").getValue().toString();

            userDatabase = FirebaseDatabase.getInstance().getReference("User").child(post_name[0]);

            ValueEventListener userListener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
            location[0] = dataSnapshot.child("location").getValue().toString();

            intent.putExtra("title", title);
            intent.putExtra("comment", comment[0]);
            intent.putExtra("writer", post_name[0]);
            intent.putExtra("name", post_name[0]);
            intent.putExtra("location",location[0]);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_IMMUTABLE);
            String channel_id = getString(R.string.default_notification_channel_id);

            // 기본 사운드 알람음 설정.y
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(getApplicationContext(), channel_id)
            .setSmallIcon(R.drawable.ic_bear)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(uri)
            .setVibrate(new long[]{1000,1000,1000})
            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CHN_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(0 ,notificationBuilder.build());
            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
            };
            userDatabase.addValueEventListener(userListener);


            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
            };
             postDatabase.addValueEventListener(postListener);
             **/
        } else if (intent_str.equals("PointHistoryActivity")) {
            intent = new Intent(this, PointHistoryActivity.class);
            String channel_id = getString(R.string.default_notification_channel_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 기본 사운드 알람음 설정.
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(uri)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{1000, 1000, 1000})
                            .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("nonado_channel", "nonado_channel", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(0, notificationBuilder.build());


        }
        else if(intent_str.equals("ChatActivity")){
            intent = new Intent(this, ChatActivity.class);
            String channel_id = getString(R.string.default_notification_channel_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 기본 사운드 알람음 설정.
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(uri)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{1000, 1000, 1000})
                            .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("nonado_channel", "nonado_channel", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(0, notificationBuilder.build());
        }


    }


}



