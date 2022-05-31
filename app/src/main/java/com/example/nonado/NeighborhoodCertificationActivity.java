package com.example.nonado;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.List;

public class NeighborhoodCertificationActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };
//    private static final String LOG_TAG = "NeighborhoodCertificationActivity";
//    private static final String TAG = "[MainA]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_certification);


//        LocationManager locationManager = getApplicationContext().getSystemService(LocationManager.GPS_PROVIDER);



        MapView kakaoMapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.kakaoMapView);
        mapViewContainer.addView(kakaoMapView);

        kakaoMapView.setMapViewEventListener((MapView.MapViewEventListener) this);
        kakaoMapView.setMapViewEventListener(this);

        if(!checkLocationServiceStatus()){
            //Log.d("fdf","errors");
            showDialogForLocationServiceSetting();
        }else{
            checkRuntTimePermission();;
        }
       kakaoMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapVeiwContainer.removeAllViews();
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG)
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint  ) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length){
            boolean check_result = true;

            for(int result : grantResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    check_result = false;
                    break;
                }
            }

            if(check_result){
                //Log.d("@@@", "Start");
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                    Toast.makeText(NeighborhoodCertificationActivity.this, "권한이 거부되었습니다. 앱을 다시 실행하여 권한을 허용하십시오.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(NeighborhoodCertificationActivity.this, "권한이 거부되었습니다. 설정에서 권한을 허용하십시오.", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
    public void checkRuntTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(NeighborhoodCertificationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            //Log.d(TAG, "checkRuntTimePermission: ");
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(NeighborhoodCertificationActivity.this, REQUIRED_PERMISSIONS[0])){
                Toast.makeText(NeighborhoodCertificationActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(NeighborhoodCertificationActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(NeighborhoodCertificationActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    private  void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NeighborhoodCertificationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage( "앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSettingIntent = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
                startActivityForResult(callGPSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServiceStatus()){
                    if(checkLocationServiceStatus()){
                        //Log.d("@@@", "gps활성화 됨");
                        checkRuntTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServiceStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void onFinishReverseGeoCoding(String result){

    }

}