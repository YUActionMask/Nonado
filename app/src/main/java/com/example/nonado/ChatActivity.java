package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chat> chatList;
    private FirebaseUser user;

    private String sender;
    private String receiver = "익명1";
    private String postId = "";

    private EditText chatEt;
    private Button sendBtn;
    private Button chamBtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        postId = getIntent().getStringExtra("postId");

        chatEt = findViewById(R.id.chatEt);
        sendBtn = findViewById(R.id.sendBtn);
        chamBtn = findViewById(R.id.chamBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        sender = user.getEmail();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = chatEt.getText().toString();

                if(msg != null){
                    Chat chat = new Chat();
                    chat.setName(sender);
                    chat.setMsg(msg);
                    chat.setReceiver(receiver);
                    chat.setPostId(postId);

                    myRef.push().setValue(chat);

                    chatEt.setText("");
                }
            }
        });

        chamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("Post-User");
                myRef.child("Post-User").child(postId).child(sender).setValue("");
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
                if(chat.getPostId().equals(postId)){
                    ((ChatAdapter)adapter).addChat(chat);
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
}