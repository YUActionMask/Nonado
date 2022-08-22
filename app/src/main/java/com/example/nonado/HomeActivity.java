package com.example.nonado;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class HomeActivity extends AppCompatActivity {
    Button plus, info, notice;
    List<String> title = new ArrayList<String>();
    List<String> comment = new ArrayList<String>();
    ListView listView;
    private ChildEventListener mChild;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Post");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        plus = (Button) findViewById(R.id.plus);
        notice = (Button) findViewById(R.id.notice);
        info = (Button) findViewById(R.id.info);
        listView = (ListView) findViewById(R.id.listView);
        initDatabase();

        String name = user.getDisplayName();

        adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);
       notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
                startActivity(intent);
            }
        });

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("title", title.get(i));
                intent.putExtra("comment", comment.get(i));
                startActivity(intent);
            }
        });

       plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
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
                    msg2 = msg2.substring(9);
                    String msg3[] = msg2.split(",");
                    comment.add(msg3[0]);
                    String msg4 = msg3[1].substring(7, msg3[1].length()-1);
                    title.add(msg3[1].substring(7, msg3[1].length()-1));
                    Array.add(msg4);
                    adapter.add(msg4);
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
