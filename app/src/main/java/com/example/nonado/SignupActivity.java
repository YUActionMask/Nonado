package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mName, mId, mPwd;
    private Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Nonado");


        mId = findViewById(R.id.signup_id);
        mName = findViewById(R.id.signup_name);
        mPwd = findViewById(R.id.signup_password);
        mBtnRegister = findViewById(R.id.signupRes);

        mBtnRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //회원가입 처리 시작
                String strId = mId.getText().toString();
                String strPwd = mPwd.getText().toString();
                String strName = mName.getText().toString();

                //firebaseAuth 진행
                mfirebaseAuth.createUserWithEmailAndPassword(strId, strPwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();

                            UserAccount account = new UserAccount();
                            account.setId(firebaseUser.getEmail());
                            account.setIdToken(firebaseUser.getUid());
                            account.setPassword(strPwd);
                            account.setName(strName);
                            //setValue : database에 insert(삽입)행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(SignupActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }
}