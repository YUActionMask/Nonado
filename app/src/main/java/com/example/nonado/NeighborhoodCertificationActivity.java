package com.example.nonado;
import net.daum.mf.map.api.MapView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

public class NeighborhoodCertificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_certification);
        MapView kakaoMapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.kakaoMapView);
        mapViewContainer.addView(kakaoMapView);
    }
}