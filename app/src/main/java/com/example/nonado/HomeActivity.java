package com.example.nonado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class HomeActivity extends AppCompatActivity {
    Button plus, info, notice;
    List<String> title = new ArrayList<String>();
    List<String> comment = new ArrayList<String>();
    List<String> writer = new ArrayList<String>();
    ListView listView;
    private ChildEventListener mChild;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    String str, location;
    EditText edit;

    private FirebaseUser user;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = database.getReference("Post");
    private DatabaseReference databaseReference2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //by재은, fcm 서비스 시작 - 220930
        //MainActivity가 없어서 HomeActivity에 추가함.
        Intent fcm = new Intent(getApplicationContext(), FCMPushServer.class);
        startService(fcm);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];
        databaseReference2 = FirebaseDatabase.getInstance().getReference("User").child(user_id);



        str = getIntent().getStringExtra("name");
        location = getIntent().getStringExtra("location");
        plus = (Button) findViewById(R.id.plus);
        notice = (Button) findViewById(R.id.notice);
        info = (Button) findViewById(R.id.info);
        listView = (ListView) findViewById(R.id.listView);
        edit = (EditText) findViewById(R.id.posi);
        edit.setText(location);
        initDatabase();
        adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                //intent.putExtra("name",str);
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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
                intent.putExtra("name",str);
                intent.putExtra("location",location);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyinfoActivity.class);
                startActivity(intent);
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg2 = messageData.getValue().toString();
                    String msg3[] = msg2.split(",");
                    Log.d("write",msg2);
                    Log.d("write",edit.getText().toString());

                    if(edit.getText().toString().equals(msg3[2].substring(10).replace("}","")) == true) {
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