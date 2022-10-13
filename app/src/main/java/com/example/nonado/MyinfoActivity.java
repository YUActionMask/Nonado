package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyinfoActivity extends AppCompatActivity {
    private Button homebtn;
    private Button pointBtn;
    private Button postingBtn;
    private Button cha;
    private Button certifyBtn;
    private TextView nameTv;
    private TextView pointTv;


    private String TAG = MyinfoActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myPost = database.getReference("User-Post");

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter MultiAdapter;  // 리사이클러뷰에 적용시킬 어댑터


    //사진 업로드용 uri
   // private Uri mlmageCaptureUri;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private UserAccount userAccount;
    private String userName;
    private List<String> title = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        homebtn = (Button) findViewById(R.id.homeBtn);
        pointBtn = (Button) findViewById(R.id.pointBtn);
        postingBtn = (Button) findViewById(R.id.postingBtn);
        cha = (Button) findViewById(R.id.cha);
        certifyBtn = (Button) findViewById(R.id.certifyBtn);
        nameTv = (TextView) findViewById(R.id.nameTv);
        pointTv = (TextView) findViewById(R.id.pointTv);
        listView = (ListView) findViewById(R.id.listview);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];
        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(user_id);
       // mDatabase.child("2");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    userName = dataSnapshot.child("name").getValue().toString();
                    nameTv.setText(userName);
                    String userPoint = dataSnapshot.child("point").getValue().toString();
                    pointTv.setText(userPoint);

                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("milky", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);


        nameTv.setText(userName);


        cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeinfoActivity.class);
                startActivity(intent);
            }
        });




        //메시지 정보 버튼
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //포인트 내역 버튼
        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointHistoryActivity.class);
                startActivity(intent);


            }
        });


        //포인트 내역 버튼
        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointHistoryActivity.class);
                startActivity(intent);

            }
        });

        //함께한 글 버튼
        postingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPostingActivity.class);
                startActivity(intent);

            }
        });

        //동네인증 버튼
        certifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NeighborhoodCertificationActivity.class);
                startActivity(intent);
            }
        });

        adapter = new ListViewAdapter();
        myPost.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageData : snapshot.getChildren()){
                    String msg2 = messageData.getValue().toString();
                    Log.d("MyTag2",msg2);
                    adapter.addItem(new Posting(msg2));
                    title.add(msg2);
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                MultiAdapter = new MultiImageAdapter(uriList, getApplicationContext());
                imageView.setAdapter(MultiAdapter);
                imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            }
        }
    }

    public class ListViewAdapter extends BaseAdapter{
        ArrayList <Posting> items = new ArrayList<Posting>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Posting item){
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
            final Posting posting = items.get(position);

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_posting, viewGroup, false);
            } else{
                View view = new View(context);
                view = (View) convertView;
            }

            TextView postingTv = (TextView) convertView.findViewById(R.id.postingTv);
            Button moneyBtn = (Button) convertView.findViewById(R.id.moneyBtn);

            moneyBtn.setTag(position);
            moneyBtn.setOnClickListener(onClickListener);
            postingTv.setText(posting.getPostName());
            Log.d(TAG, "getView() - ["+position+"] "+posting.getPostName());


            return convertView;
        }

        Button.OnClickListener onClickListener = new Button.OnClickListener(){

            @Override
            public void onClick(View v){
                int position = Integer.parseInt(v.getTag().toString());
                Log.d("MyTag3", title.get(position));
                String ti = title.get(position);
                mDatabase = FirebaseDatabase.getInstance().getReference("Post").child(ti);

                ValueEventListener value = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String point = snapshot.child("point").getValue().toString();
                        Log.d("MyTag4",point);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyinfoActivity.this);
                        builder.setTitle("송금 ").setMessage("송금 금액 : " + point);

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                };
                mDatabase.addValueEventListener(value);
            }
        };
    }

}

