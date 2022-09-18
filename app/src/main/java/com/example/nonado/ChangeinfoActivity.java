package com.example.nonado;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class ChangeinfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button mBtnLogout, mBtnSignout, mBtnUploadImages;

    private final int GALLERY_CODE = 10;
    private ImageView mPhoto;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfo);

        mBtnLogout = findViewById(R.id.logout_btn);
        mBtnSignout = findViewById(R.id.signout_btn);
        mBtnUploadImages = findViewById(R.id.imageUpload_btn);
        mPhoto = (ImageView)findViewById(R.id.img);
        storage = FirebaseStorage.getInstance();

        mBtnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();

                Intent intent = new Intent(ChangeinfoActivity.this, LoginActivity.class);
                startActivity(intent);


                Toast.makeText(ChangeinfoActivity.this, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
            }
        } );


        mBtnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                DatabaseReference dataRef = database.getReference().child("User");
                if(user!=null){
                    user.delete();

                    Intent intent = new Intent(ChangeinfoActivity.this, LoginActivity.class);
                    startActivity(intent);


                }
            }
        });

        mBtnUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.img:
                        loadAlbum();
                        break;
                }
            }
        });

    }

    public void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY_CODE);
    }
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == GALLERY_CODE){
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            StorageReference riverRef = storageRef.child("UploadImage/");
            UploadTask uploadTask = riverRef.putFile(file);
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                mPhoto.setImageBitmap(img);
            }catch (Exception e){
                e.printStackTrace();
            }
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangeinfoActivity.this,"사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ChangeinfoActivity.this,"사진이 정상적으로 업로드 되었습니다.",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}