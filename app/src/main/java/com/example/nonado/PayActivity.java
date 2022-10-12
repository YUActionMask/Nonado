package com.example.nonado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PayActivity extends AppCompatActivity {

    private Button kakaoPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        kakaoPayBtn = (Button) findViewById(R.id.kakaoPayBtn);

        kakaoPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "name";
                String price = "price";


            }
        });
    }
}