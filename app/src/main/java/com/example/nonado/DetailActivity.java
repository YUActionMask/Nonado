package com.example.nonado;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
        title = (TextView) findViewById(R.id.textView4);
        comment = (TextView) findViewById(R.id.textView5);
        imageView = findViewById(R.id.image);
        str = getIntent().getStringExtra("title");
        title.setText(str);
        storage=FirebaseStorage.getInstance();
        stoRe=storage.getReference();
        patRe=stoRe.child(str);
        btn = (Button) findViewById(R.id.btn);
        ArrayList<Task<Uri>> tasks = new ArrayList<>();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        patRe.listAll().continueWithTask(task -> {
            if (task.getException() != null) {
                throw task.getException();
            }

            for (StorageReference item : task.getResult().getItems()) {
                tasks.add(item.getDownloadUrl());
            }

            return Tasks.whenAllComplete(tasks);

        }).addOnCompleteListener(task -> {
            if (task.getException() != null) {
                Toast.makeText(DetailActivity.this, "실패!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Task<Uri> task2 : tasks) {
                if (task2.getException() != null) {
                    Toast.makeText(DetailActivity.this, "실패!", Toast.LENGTH_SHORT).show();

                } else {
                    uriList.add(task2.getResult());
                }
            }

            adapter = new MultiImageAdapter(uriList, getApplicationContext());
            imageView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
            imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용

        });
        comment.setText(getIntent().getStringExtra("comment"));
    }
}