package com.example.team_os;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class myGPS {
    Activity mActivity;
    public double myLat, myLon;
    public double targetLat=-100, targetLon=-100;
    public boolean setTarget = false;
    public boolean arrive = false;
    GoogleMap googleMap;
    Marker m;

    public myGPS(Activity activity){
        mActivity = activity;
        myLat= -100;
        myLon = -100;
        getMyGps();
    }

    public double getLatitude(){
        return myLat;
    }

    public double getLongitude(){
        return myLon;
    }

    protected void getMyGps() {

        LocationManager lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        Location location;

        // 권한확인
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);

        } //else {
        Log.i("myGPS_info", "현재위치찾기시작");
        //GPS 프로바이더 사용 가능
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                myLat = location.getLatitude();
                myLon = location.getLongitude();
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,gpsLocationListener);
            } else{
                Log.i("info_myGPS", "위치찾기실패1");
                // 네트워크 프로바이더 사용 가능
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        myLat = location.getLatitude();
                        myLon = location.getLongitude();
                        Log.i("myGPS",Double.toString(myLat)+","+Double.toString(myLon));
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10,gpsLocationListener);
                    } else Log.i("info_myGPS", "위치찾기실패2");
                } else Log.i("info_myGPS", "위치찾기실패3");

            }
        }
        // }
    }

    public LocationListener gpsLocationListener = new LocationListener() {

        //내 위치가 변할때마다
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            //내위치 마커 업데이트

            setMarker();

            if(setTarget){
                //거리계산을 위해
                Location myLocation = new Location("my");
                Location targetLocation = new Location("target");
                myLocation.setLongitude(longitude); myLon=longitude;
                myLocation.setLatitude(latitude); myLat=latitude;
                targetLocation.setLatitude(targetLat);
                targetLocation.setLongitude(targetLon);

                if(myLocation.distanceTo(targetLocation) <= 20) {
                    arrive=true;
                    Toast.makeText(mActivity.getApplicationContext(), "목적지 도착", Toast.LENGTH_SHORT).show();
                }else {
                    Float dis = myLocation.distanceTo(targetLocation);
                    Toast.makeText(mActivity.getApplicationContext(), Float.toString(dis),Toast.LENGTH_LONG).show();
                }
            } else  Toast.makeText(mActivity.getApplicationContext(), "setTarget==false", Toast.LENGTH_SHORT).show();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public boolean checkArrive(Location from, Location to){
        if(from.distanceTo(to) <= 15) return true;
        return false;
    }

    public void setTargetLat(Double d){
        targetLat = d;
    }

    public void setTargetLon(Double d){
        targetLon = d;
    }

    public Marker setMarker(){
        m.remove();
        LatLng me = new LatLng(myLat,myLon);
        m = googleMap.addMarker(new MarkerOptions().position(me).title("현재 위치"));
        return m;
    }


    public void setMyMarker(Marker me,GoogleMap g){
        m = me;
        googleMap = g;
    }

}
