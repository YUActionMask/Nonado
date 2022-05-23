package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private EditText mName, mId, mPwd;
    private Button mBtnRegister;
    private ArrayList<UserAccount> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mfirebaseAuth.getCurrentUser();


        mId = findViewById(R.id.signup_id);
        mName = findViewById(R.id.signup_name);
        mPwd = findViewById(R.id.signup_password);
        mBtnRegister = findViewById(R.id.signupRes);

        arrayList = new ArrayList<>(); // UserAccount 객체를 담을 arraylist


        

        mBtnRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                databaseReference = database.getReference("User");

                //회원가입 처리 시작
                String strId = mId.getText().toString().trim();
                String strPwd = mPwd.getText().toString().trim();
                String strName = mName.getText().toString().trim();



                databaseReference.child("UserAccount").child(strId).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 DB의 데이터를 받아오는 곳
                        arrayList.clear(); //기존 배열리스트가 존재하지않게 초기화
                        for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            UserAccount user = snapshot1.getValue(UserAccount.class);
                            arrayList.add(user);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //데이터베이스 오류
                        Log.e("SignupActivity",String.valueOf(databaseError.toException()));

                    }
                });
                
            }
        });

    }
}