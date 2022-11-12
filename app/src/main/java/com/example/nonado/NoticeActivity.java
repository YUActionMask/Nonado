package com.example.nonado;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {
    Button home;
    List<String> title = new ArrayList<String>();
    List<String> comment = new ArrayList<String>();
    List<String> writer = new ArrayList<String>();
    ListView listView;
    private ChildEventListener mChild;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Post");
    private DatabaseReference userDatabaseReference;

    private FirebaseUser user;


    String user_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        home = (Button) findViewById(R.id.home);
        listView = (ListView) findViewById(R.id.listView);
        initDatabase();
        adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        String location = "관리자";
        String str = "관리자";

        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(user_id);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_location = dataSnapshot.child("location").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        userDatabaseReference.addValueEventListener(userListener);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("milkyTag", user_location);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("name", user_id);
                intent.putExtra("location", user_location);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("title", title.get(i));
                intent.putExtra("comment", comment.get(i));
                intent.putExtra("writer", writer.get(i));
                intent.putExtra("name",str);
                intent.putExtra("location",location);
                startActivity(intent);
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg2 = messageData.getValue().toString();
                    String msg3[] = msg2.split(",");


                    if("관리자".equals(msg3[2].substring(10).replace("}","")) == true) {
                        comment.add(msg3[1].substring(9).replace("}",""));
                        writer.add(msg3[0].substring(6).replace("}",""));
                        title.add(msg3[3].substring(7).replace("}",""));
                        Array.add(msg3[3].substring(7).replace("}",""));
                        adapter.add(msg3[3].substring(7).replace("}",""));
                   }

                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initDatabase() {
        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mChild);
    }
}
