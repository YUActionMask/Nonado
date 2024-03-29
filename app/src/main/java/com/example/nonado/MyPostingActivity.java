package com.example.nonado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostingActivity extends AppCompatActivity {

    private ListView listView = null;
    private ListViewAdapter adapter = null;
    private List<String> post_ids = new ArrayList<String>();

    private FirebaseUser user;
    private DatabaseReference databaseReference ;
    private DatabaseReference postReference;

    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posting);

        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = user.getEmail().split("@")[0];
        databaseReference = FirebaseDatabase.getInstance().getReference("User-Chat").child(user_id);

        listView = (ListView) findViewById(R.id.listview);

        //채팅 띄우기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter = new ListViewAdapter();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String chat_id = messageData.getKey();
                    post_ids.add(chat_id);
                    adapter.addItem(chat_id);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        listView.setAdapter(adapter);


        //클릭했을 때 채팅으로 넘어감
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String postId = post_ids.get(i);
                postReference = FirebaseDatabase.getInstance().getReference("Post").child(postId);

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MyPostingActivity.this);
                            builder.setTitle("이미 삭제된 게시글 입니다. ").setMessage("그래도 채팅에 접속하시겠습니까?");

                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent.putExtra("postId", postId);
                                    intent.putExtra("postWriter" ,"");
                                    startActivity(intent);
                                    finish();
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

                        else {
                            String postWriter = dataSnapshot.child("name").getValue().toString();
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.putExtra("postId", postId);
                            intent.putExtra("postWriter" ,postWriter);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                postReference.addValueEventListener(postListener);



            }
        });

    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<String> items = new ArrayList<String>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(String item){
            items.add(item);
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final String posting = items.get(position);

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_my_posting, viewGroup, false);
            } else{
                View view = new View(context);
                view = (View) convertView;
            }

            TextView postNameTv = (TextView) convertView.findViewById(R.id.postNameTv);

            postNameTv.setText(posting);


            return convertView;
        }
    }
}