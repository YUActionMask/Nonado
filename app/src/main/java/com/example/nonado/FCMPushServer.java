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
import  com.example.nonado.MyinfoActivity;


public class FCMPushServer extends FirebaseMessagingService {

    private DatabaseReference postDatabase;
    private DatabaseReference userDatabase;
    private
    String comment, post_name, location;
    private
    Intent intent = null;
    private PendingIntent pendingIntent = null;

    //private  static final String TAG = "FCMPushServer";

//    private FirebaseAuth firebaseAuth;
//    private DatabaseReference userDatabase;
//    private FirebaseUser user;
//    private String user_id;


//    public FCMPushServer() {
//        super();
//        Task<String> token = FirebaseMessaging.getInstance().getToken();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        userDatabase = FirebaseDatabase.getInstance().getReference();
//
//
//        token.addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if(task.isSuccessful()){
//
//
//                    user = FirebaseAuth.getInstance().getCurrentUser();
//                    user_id = user.getEmail().split("@")[0];
//                    userDatabase.child("User").child(user_id).child("token").setValue(task.getResult());
//
//                    FirebaseMessaging.getInstance().getToken()
//                            .addOnCompleteListener(new OnCompleteListener<String>() {
//                                @Override
//                                public void onComplete(@NonNull Task<String> task) {
//                                    if (!task.isSuccessful()) {
//                                        //Log.w("milkyLog", "Fetching FCM registration token failed", task.getException());
//                                        return;
//                                    }
//
//                                    // Get new FCM registration token
//                                    String token = task.getResult();
//
//                                }
//                            });
//
//                }
//            }
//        });
//    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //수신한 메시지를 처리
        super.onMessageReceived(remoteMessage);
        //if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("click_action"));
        //}

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }




    public void showNotification(String title, String message, String intent_str){

        if(intent_str.equals("DetailActivity")){

            intent = new Intent(this, DetailActivity.class);
            postDatabase = FirebaseDatabase.getInstance().getReference("Post").child(title);

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comment = dataSnapshot.child("comment").getValue().toString();
                    post_name = dataSnapshot.child("name").getValue().toString();

                    userDatabase = FirebaseDatabase.getInstance().getReference("User").child(post_name);

                    ValueEventListener userListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            location = dataSnapshot.child("location").getValue().toString();

                            intent.putExtra("title", title);
                            intent.putExtra("comment", comment);
                            intent.putExtra("writer", post_name);
                            Log.d("milkyLog", "dd " + title);
                            intent.putExtra("name",post_name);
                            intent.putExtra("location",location);
                            pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_IMMUTABLE);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    userDatabase.addValueEventListener(userListener);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            postDatabase.addValueEventListener(postListener);


        }


        String channel_id = getString(R.string.default_notification_channel_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        // 기본 사운드 알람음 설정.
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



}



