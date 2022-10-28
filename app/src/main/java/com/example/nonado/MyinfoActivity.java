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
import android.widget.EditText;
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
    private Button logoutBtn;
    private TextView nameTv, toName;
    private TextView pointTv;
    private View dlgView;
    private EditText how;


    private String TAG = MyinfoActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myPost = database.getReference("User-Post");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter MultiAdapter;  // 리사이클러뷰에 적용시킬 어댑터


    //사진 업로드용 uri
    // private Uri mlmageCaptureUri;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static int a;

    private FirebaseUser user;
    private DatabaseReference mDatabase, mDatabase2;
    private UserAccount userAccount;
    private String userName, userPoint;
    private List<String> title = new ArrayList<String>();

    private
    String location ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        homebtn = (Button) findViewById(R.id.homeBtn);
        pointBtn = (Button) findViewById(R.id.pointBtn);
        postingBtn = (Button) findViewById(R.id.postingBtn);
        cha = (Button) findViewById(R.id.cha);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
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
                userPoint = dataSnapshot.child("point").getValue().toString();
                location = dataSnapshot.child("location").getValue().toString();
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




        //홈버튼
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("name", user_id);
                intent.putExtra("location", location);
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

        //로그아웃 버튼
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

                Intent intent = new Intent(MyinfoActivity.this, LoginActivity.class);
                startActivity(intent);


                Toast.makeText(MyinfoActivity.this, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
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
            Button okBtn = (Button) convertView.findViewById(R.id.okBtn);

            moneyBtn.setTag(position);
            okBtn.setTag(position);
            moneyBtn.setOnClickListener(onClickListener);
            okBtn.setOnClickListener(onClickListener2);
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
                        dlgView = (View)View.inflate(MyinfoActivity.this, R.layout.dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyinfoActivity.this);
                        builder.setTitle("송금 ");
                        builder.setView(dlgView);
                        toName = dlgView.findViewById(R.id.dlg_id);
                        how = dlgView.findViewById(R.id.dlg_price);
                        toName.setText(ti);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase = FirebaseDatabase.getInstance().getReference("User").child(userName);
                                String input = how.getText().toString().trim();
                                if(input.equals("") == true ) {
                                    Toast.makeText(MyinfoActivity.this, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    for(int k=0;k<input.length();k++){
                                        char chr = input.charAt(k);
                                        Log.d("input",Integer.toString(chr));
                                        if(chr < 48 || chr > 57){
                                            Toast.makeText(MyinfoActivity.this, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if(47 < chr && chr <58 && k==input.length()-1){
                                            int price = Integer.parseInt(input);
                                            a = Integer.parseInt(userPoint)- price;
                                            mDatabase.child("point").setValue(a);
                                            Log.d("price",input);
                                            Toast.makeText(MyinfoActivity.this,"완료 되었습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                        else if(k==input.length()-1){
                                            Toast.makeText(MyinfoActivity.this,"종료 되었습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MyinfoActivity.this,"취소되었습니다.",Toast.LENGTH_SHORT).show();
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
        Button.OnClickListener onClickListener2 = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = Integer.parseInt(v.getTag().toString());
                Log.d("MyTag3", title.get(position));
                String ti = title.get(position);
            }
        };
    }
}
