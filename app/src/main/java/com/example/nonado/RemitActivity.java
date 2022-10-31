package com.example.nonado;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class RemitActivity extends AppCompatActivity {
    TextView textView4, textView5, textwriter;
    RecyclerView imageView;
    EditText remitEdit;
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    Button remitBtn;
    private ChildEventListener mChild;
    StorageReference stoRe;
    StorageReference patRe;
    FirebaseStorage storage;
    String title, name, userPoint;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference myPoint = database.getReference("Point");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remit);
        showLoading(RemitActivity.this, true);
        textView4 = findViewById(R.id.textView4); // 제목
        textView5 = findViewById(R.id.textView5); // 내용
        textwriter = findViewById(R.id.textwriter);
        imageView = findViewById(R.id.image);
        remitEdit = findViewById(R.id.remitEdit);
        remitBtn = findViewById(R.id.remitBtn);
        ArrayList<Task<Uri>> tasks = new ArrayList<>();
        ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
        title = getIntent().getStringExtra("title");
        name = getIntent().getStringExtra("name");
        userPoint = getIntent().getStringExtra("userPoint");
        storage=FirebaseStorage.getInstance();
        stoRe=storage.getReference();
        patRe=stoRe.child(title);
        databaseReference = database.getReference("Post").child(title);
        databaseReference2 = database.getReference("User").child(name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String msg = snapshot.getValue().toString();
                String msg3[] = msg.split(",");
                Log.d("msg",msg);
                textView4.setText(title);
                textView5.setText(msg3[1].substring(9).replace("}",""));
                textwriter.setText("작성자 : " + msg3[0].substring(6).replace("}",""));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        remitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RemitActivity.this);
                builder.setTitle("송금 ").setMessage("송금 금액 : " + remitEdit.getText().toString());

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = remitEdit.getText().toString();

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User").child(name);
                        if(input.equals("")==true){
                            Toast.makeText(RemitActivity.this, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for(int k=0;k<input.length();k++){
                                char chr = input.charAt(k);
                                if(Character.isDigit(chr)!=true){
                                    Toast.makeText(RemitActivity.this, "숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else if(Character.isDigit(chr)==true && k==input.length()-1) {
                                    int price = Integer.parseInt(input);
                                    int a = Integer.parseInt(userPoint) - price;
                                    mDatabase.child("point").setValue(a);
                                    Point point = new Point();
                                    point.setBalance(Integer.parseInt(input));
                                    point.setCertification("0");
                                    String writer[] = textwriter.getText().toString().split(": ");
                                    point.setReceiver(writer[1]);
                                    point.setSender(name);
                                    point.setTitle(title);
                                    myPoint.push().setValue(point);
                                    Toast.makeText(RemitActivity.this, "송금이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RemitActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        patRe.listAll().continueWithTask(task -> {
            for (StorageReference item : task.getResult().getItems()) {
                tasks.add(item.getDownloadUrl());
            }
            return Tasks.whenAllComplete(tasks);
        }).addOnCompleteListener(task -> {
            for (Task<Uri> task2 : tasks) {
                uriList.add(task2.getResult());
            }
            adapter = new MultiImageAdapter(uriList, getApplicationContext());
            imageView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
        });
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showLoading(RemitActivity.this, false);
            }
        }, 1900);//

    }
    void showLoading(Activity activity, boolean isShow) {
        if(isShow) {
            LinearLayout linear = new LinearLayout(activity);
            linear.setTag("MyProgressBar");
            linear.setGravity(Gravity.CENTER);
            linear.setBackgroundColor(0xFFFFFFFF);
            ProgressBar progressBar = new ProgressBar(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            linear.addView(progressBar);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) { /*클릭방지*/ }
            });

            FrameLayout rootView = activity.findViewById(android.R.id.content);
            rootView.addView(linear);

        } else {
            FrameLayout rootView = activity.findViewById(android.R.id.content);
            LinearLayout linear = rootView.findViewWithTag("MyProgressBar");
            if(linear != null) {
                rootView.removeView(linear);
            }
        }
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