package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    public EditText name, id, password;
    private int point = 0;
    private String location = null;
    private String number = "";
    private String token = "";
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        id = findViewById(R.id.signup_id);
        name = findViewById(R.id.signup_name);
        password = findViewById(R.id.signup_password);


        mBtnRegister = findViewById(R.id.signupRes);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //회원가입 처리 시작

                String strId = id.getText().toString().trim();
                String strPwd = password.getText().toString().trim();
                String strName = name.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(strId, strPwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        showDialog();


                                        sendPushTokenToDB();

                                        HashMap result = new HashMap<>();
                                        result.put("name", strName);
                                        result.put("id", strId);
                                        result.put("password", strPwd);
                                        result.put("point", point);
                                        result.put("location", location);
                                        result.put("number", number);
                                        result.put("token", token);

                                        wirteUser(strId.split("@")[0], strId, strPwd,strName,point,location, number, token);
                                        Toast.makeText(SignupActivity.this, "해당 이메일로 인증 링크를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(SignupActivity.this, "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

                            Toast.makeText(SignupActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SignupActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

    }
    private void wirteUser(String userid, String id , String password, String name, int point, String location, String number, String token) {
        if(location==null) {

            /**by재은,수정 **/
            UserAccount user = new UserAccount(id, password, name, point, "null", number, token);

            mDatabase.child("User").child(userid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(SignupActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignupActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            mDatabase.child("User-Post").child(userid).setValue("");
        }
    }
    private void showDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(SignupActivity.this)
                .setTitle("이메일 인증 후 확인버튼을 눌러주세요.")
                .setMessage("인증 후 로그인이 가능하니 유의해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignupActivity.this, "이메일 인증해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void sendPushTokenToDB(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(SignupActivity.this, new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    return;
                }

                String token = task.getResult();
                Map<String, Object> map = new HashMap<>();
                map.put("fcmToken", token);
                mDatabase.child("User").child(user_id).child("token").setValue(token);

            }
        });

    }

}
