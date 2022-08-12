package com.example.nonado;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    TextView title, comment;
    String str;
    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    private static final String TAG = "MultiImageActivity";
    FirebaseStorage storage;
    StorageReference stoRe;
    StorageReference patRe;
    Button btn;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        showLoading(DetailActivity.this, true);
        title = (TextView) findViewById(R.id.textView4);
        comment = (TextView) findViewById(R.id.textView5);
        imageView = findViewById(R.id.image);
        imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
        str = getIntent().getStringExtra("title");
        title.setText(str);
        storage=FirebaseStorage.getInstance();
        stoRe=storage.getReference();
        patRe=stoRe.child(str);
        btn = (Button) findViewById(R.id.btn);
        ArrayList<Task<Uri>> tasks = new ArrayList<>();

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });*/

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
        }, 1900);// 0.6초 정도 딜레이를 준 후 시작

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
}

