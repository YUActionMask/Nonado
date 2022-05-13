package com.example.nonado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<<< HEAD:app/src/main/java/com/example/nonado/SignupActivity.java
import android.os.Bundle;

public class SignupActivity extends AppCompatActivity {
========
public class detailActivity extends AppCompatActivity {
>>>>>>>> jjeongjae:app/src/main/java/com/example/nonado/detailActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<<< HEAD:app/src/main/java/com/example/nonado/SignupActivity.java
        setContentView(R.layout.activity_signup);
========
        setContentView(R.layout.activity_detail);
>>>>>>>> jjeongjae:app/src/main/java/com/example/nonado/detailActivity.java
    }
}