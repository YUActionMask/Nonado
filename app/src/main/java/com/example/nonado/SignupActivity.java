package com.example.nonado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText name, id, password;
    final private int point = 0;
    private Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

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
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            HashMap result = new HashMap<>();
                            result.put("name", strName);
                            result.put("id", strId);
                            result.put("password", strPwd);
                            result.put("point", point);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("User");
                            reference.child(strId).setValue(result);

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignupActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }
    private void wirteUser(String userid, String id , String password, String name, int point, DatabaseReference reference){
        UserAccount user = new UserAccount(id, password, name, point);


    }
    public boolean onSupportNavigateUp(){
        onBackPressed();;
        return super.onSupportNavigateUp();
    }
}
