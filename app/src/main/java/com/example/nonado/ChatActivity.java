package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chat> chatList;
    private FirebaseUser user;

    private String sender;
    private String receiver = "익명1";
    private int size = 0;
    private String postId = "";
    private String postWriter = "";
    private String withPost = "";
    private String msg;
    private String user_id;

    private EditText chatEt;
    private Button sendBtn;
    private Button chamBtn, personBtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private DatabaseReference myPost = database.getReference("User-Post");
    private DatabaseReference jsonDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        postId = getIntent().getStringExtra("postId");
        postWriter = getIntent().getStringExtra("postWriter");

        chatEt = findViewById(R.id.chatEt);
        sendBtn = findViewById(R.id.sendBtn);
        chamBtn = findViewById(R.id.chamBtn);
        personBtn = findViewById(R.id.personBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        sender = user.getEmail().split("@")[0];

        //Log.d("MyTag",sender);
        //채팅 전송 버튼
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 msg = chatEt.getText().toString();

                if (msg != null) {


                    Chat chat = new Chat();
                    chat.setName(sender);
                    chat.setMsg(msg);
                    chat.setReceiver(receiver);
                    chat.setPostId(postId);


                    myRef.push().setValue(chat);


                    user = FirebaseAuth.getInstance().getCurrentUser();
                    user_id = user.getEmail().split("@")[0];

                    if(!(user_id.equals(postWriter))) {
                        sendGson();
                    }

                    chatEt.setText("");

                    //fcm 알림
                }
            }
        });

        personBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, NowActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });

        chamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("참여 확인").setMessage("거래에 참여하려면 확인을 누르십시오. ");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = sender;
                        myRef = database.getReference("Post-User");
                        myRef.child(postId).child(name).setValue("");
                        myPost.child(name).child(postId).setValue(postId);
                        myRef = database.getReference("User-Post");
                        String value = postId + "," + postWriter;
                        myRef.child(name).child(postId).setValue(value);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, sender, postId);
        recyclerView.setAdapter(adapter);

        myRef = database.getReference("message");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chat = snapshot.getValue(Chat.class);
                if (chat.getPostId().equals(postId)) {
                    ((ChatAdapter) adapter).addChat(chat);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendGson() {

        jsonDatabase.child("User").child(postWriter).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fcmToken = snapshot.getValue().toString();
                Log.d("Receiver : ", postWriter);

                jsonDatabase.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        jsonDatabase.child("message").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                       // String msg = chatEt.getText().toString();
                        String fcmTitle = postId;
                        String fcmBody = user_id + " : " + msg;
                        SendNotification.sendNotification(fcmToken, fcmTitle, fcmBody, "ChatActivity");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
