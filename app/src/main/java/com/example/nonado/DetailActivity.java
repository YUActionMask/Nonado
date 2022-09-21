package com.example.nonado;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView title, comment;
    String str, name, location;
    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    private static final String TAG = "MultiImageActivity";
    FirebaseStorage storage;
    private ChildEventListener mChild;
    StorageReference stoRe;
    StorageReference patRe;
    ListView comment2;
    ListView listView;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    
    Button reg_button;
    private ArrayAdapter<String> adapter2;
    private ArrayList<Comment> com = new ArrayList<>();
    List<Object> Array = new ArrayList<Object>();
    Button btn;
    EditText comment_et;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Comment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        showLoading(DetailActivity.this, true);
        title = (TextView) findViewById(R.id.textView4);
        comment = (TextView) findViewById(R.id.textView5);
        comment2 = (ListView) findViewById(R.id.comment);
        adapter2 =  new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        comment2.setAdapter(adapter2);
        imageView = findViewById(R.id.image);
        reg_button = findViewById(R.id.reg_button);
        comment_et = findViewById(R.id.comment_et);
        imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
        str = getIntent().getStringExtra("title");
        name = getIntent().getStringExtra("name");
        title.setText(str);
        storage=FirebaseStorage.getInstance();
        stoRe=storage.getReference();
        patRe=stoRe.child(str);
        btn = (Button) findViewById(R.id.btn);
        initDatabase();
        ArrayList<Task<Uri>> tasks = new ArrayList<>();

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });*/

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter2.clear();
                plus(str, name, comment_et.getText().toString(),getTime());
            }
        });

        databaseReference.child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg = messageData.getValue().toString();
                    String msg2[] = msg.split(",");
                    com.add(new Comment(msg2[1].substring(6),msg2[2].substring(9).replace("}",""),msg2[0].substring(6)));
                }
                CommentAdapter commentAdapter = new CommentAdapter(com);
                comment2.setAdapter(commentAdapter);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        comment.setText(getIntent().getStringExtra("comment"));
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showLoading(DetailActivity.this, false);
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

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void plus(String title, String name, String comment, String date){
        Plusfirebase Pf = new Plusfirebase(name,comment,date);
        databaseReference.child(title).child(date + comment).setValue(Pf);
    }
}

