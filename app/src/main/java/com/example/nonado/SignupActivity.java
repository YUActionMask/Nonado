package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    public EditText name, id, password;
    private int point = 0;
    private String location = null;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference reference = database.getReference();


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
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            HashMap result = new HashMap<>();
                            result.put("name", strName);
                            result.put("id", strId);
                            result.put("password", strPwd);
                            result.put("point", point);
                            result.put("location", location);



                            wirteUser(strName, strId, strPwd, strName, point,location);
                            //Toast.makeText(SignupActivity.this, "회원가입에 성공하였습니다..", Toast.LENGTH_SHORT).show();

                            //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            //startActivity(intent);


                        } else {
                            Toast.makeText(SignupActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                });
            }
        });

    }
    private void wirteUser(String userid, String id , String password, String name, int point, String location){
        UserAccount user = new UserAccount(id, password, name, point, location);

        mDatabase.child("User").child(userid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(SignupActivity.this, "회원가입에 성공하였습니다..", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
