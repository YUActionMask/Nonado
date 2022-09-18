package com.example.nonado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class ChangeinfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button mBtnLogout, mBtnSignout;
    private SignupActivity name = new SignupActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfo);

        mBtnLogout = findViewById(R.id.logout_btn);
        mBtnSignout = findViewById(R.id.signout_btn);

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
    }


}