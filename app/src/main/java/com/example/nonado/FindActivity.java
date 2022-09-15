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

    private Button buttonSnedEamil;
    private EditText editTextFindEmail;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        buttonSnedEamil = findViewById(R.id.btn_sendEmail);
        editTextFindEmail = findViewById(R.id.editTextFindEmail);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonSnedEamil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(view == buttonSnedEamil){
                    String emailAddress = editTextFindEmail.getText().toString().trim();
                    if(emailAddress.length()>0) {
                        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startToast("해당 Email로 mail을 전송했습다.");
                                }
                                else {
                                    startToast("Email을 입력해주세요.");
                                    return;
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    private void startToast(String msg){Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();}
}