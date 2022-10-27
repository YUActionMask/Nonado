package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private EditText id;
    private EditText password;
    private Button buttonLogIn;
    private Button buttonSignUp;
    private Button buttonFindId;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private UserAccount userAccount;
    private String location = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignUp = findViewById(R.id.btn_signup);
        buttonLogIn = findViewById(R.id.btn_login);
        buttonFindId = findViewById(R.id.btn_sendEmail);

        id = findViewById(R.id.showid);
        password = findViewById(R.id.password);

        //데이터 베이스에서 사용자 받아오기


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        buttonLogIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                String strId = id.getText().toString().trim();
                String strPwd = password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(strId, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                //String user_id = user.getEmail().split(".")[0];
                                String user_id = strId.split("@")[0];
                                mDatabase = FirebaseDatabase.getInstance().getReference("User").child(user_id);

                                sendPushTokenToDB();
                                //Log.d("milky", "주소찾기");
                                ValueEventListener postListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //for (DataSnapshot userData : dataSnapshot.getChildren()) {
                                        location = dataSnapshot.child("location").getValue().toString();
                                        //Log.d("milky", location);

                                        if (location.equals("null")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle("동네인증이 필요합니다. ").setMessage("확인을 누르면 동네인증이 진행됩니다. ");

                                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(LoginActivity.this, NeighborhoodCertificationActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    mDatabase.child("User").child(user_id).child("location").setValue("현재위치를 설정해주세요.");
                                                    startActivity(intent);
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                            intent.putExtra("name", user_id);
                                            intent.putExtra("location", location);

                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message
                                        Log.w("milky", "loadPost:onCancelled", databaseError.toException());
                                    }
                                };
                                mDatabase.addValueEventListener(postListener);


                            } else {
                                Toast.makeText(LoginActivity.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });



            }
        });
        buttonFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindActivity.class);
                startActivity(intent);
            }
        });
    }
    private void sendPushTokenToDB(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    return;
                }

                UserAccount userAccount = new UserAccount();

                String token = task.getResult();
                Map<String, Object> map = new HashMap<>();
                map.put("fcmToken", token);
                mDatabase.child("User").child(user_id).child("token").setValue(token);

            }
        });

    }

}