package com.example.nonado;

import androidx.annotation.NonNull;
<<<<<<< Updated upstream
=======
import androidx.appcompat.app.ActionBar;
>>>>>>> Stashed changes
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
<<<<<<< Updated upstream

public class FindActivity extends AppCompatActivity {

    private Button buttonSnedEamil;
    private EditText editTextFindEmail;
    private FirebaseAuth firebaseAuth;
=======


public class FindActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonSendemail;
    private EditText editTextFindEmail;
>>>>>>> Stashed changes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

<<<<<<< Updated upstream
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
=======
        firebaseAuth = FirebaseAuth.getInstance();
        buttonSendemail = findViewById(R.id.btn_sendEmail);
        editTextFindEmail = findViewById(R.id.editTextFindEmail);


        }

    public void onClick(View view){
        if(view == buttonSendemail){
            String emailAddress = editTextFindEmail.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(FindActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(FindActivity.this, "해당 Email로 발송 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FindActivity.this, "해당 Email을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
            });
        }
>>>>>>> Stashed changes
    }


    private void startToast(String msg){Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();}
}