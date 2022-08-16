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
import com.google.firebase.auth.FirebaseAuth;

public class FindActivity extends AppCompatActivity {

    private Button buttonFindId;
    private EditText editTextfindid;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        buttonFindId = findViewById(R.id.btn_findid);
        editTextfindid = findViewById(R.id.editTextFindEmail);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void onClick(View view){
        if(view == buttonFindId){
            String emailAddress = editTextfindid.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(FindActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(FindActivity.this, "해당 Email로 메일을 전송하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FindActivity.this, "해당 Email을 찾을 수 없습니다..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
    }
}