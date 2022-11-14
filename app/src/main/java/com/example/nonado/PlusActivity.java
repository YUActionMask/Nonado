package com.example.nonado;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlusActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "MultiImageActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    EditText EditText4, EditText5;
    Button btn2;
    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        EditText4 = findViewById(R.id.EditText4);
        EditText5 = findViewById(R.id.EditText5);
        btn2 = findViewById(R.id.btn2);
        imageView = findViewById(R.id.image);
        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        storage = FirebaseStorage.getInstance();
        Button btn_getImage = findViewById(R.id.btn);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        //게시글 등록
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = EditText4.getText().toString();
                plus(tit, EditText5.getText().toString(), name, location);
                databaseReference = database.getReference("Post-User");
                databaseReference.child(tit).child(name).setValue("");
                databaseReference.child(name).child(tit).setValue(tit);
                databaseReference = database.getReference("User-Post");
                String value = tit + "," + name;
                databaseReference.child(name).child(tit).setValue(value);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("location",location);
                startActivity(intent);
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

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                imageView.setAdapter(adapter);
                imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            } else {      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");
                    StorageReference storageRef = storage.getReference();

                    for (int i =0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        String path = EditText4.getText().toString()+"/"+i+".png";
                        StorageReference riversRef = storageRef.child(path);
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                            UploadTask uploadTask = riversRef.putFile(imageUri);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    imageView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }

    public void plus(String title, String comment, String id, String location){
        Plusfirebase Pf = new Plusfirebase(title,comment,id, location);
        databaseReference.child("Post").child(title).setValue(Pf);
        databaseReference.child("Post-User").child(title).setValue("");
    }
}