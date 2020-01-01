package com.example.team_os;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class frag3 extends Fragment implements OnMapReadyCallback {
    public frag3() {
    }

    //도영쓰
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;
    // Calendar 객체 생성

    //추가
    myGPS MyGps;
    double myLat;
    double myLon;
    GalleryManager galleryManager;
    String imgPath="";

    //MapView mapView;
    public GoogleMap mMap;
    SupportMapFragment mapFragment;

    Marker myMarker;
    Button imgBtn;

    // 알람리시버 intent 생성

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_tab3, container, false);
        this.context=getActivity().getApplicationContext();//이곳 다시 보기

        final Calendar calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        // final ImageView imageview = rootView.findViewById(R.id.changeImage);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        imgBtn = rootView.findViewById(R.id.btn_img);

        // mapView = (MapView) rootView.findViewById(R.id.map);
        // mapView.onCreate(savedInstanceState);
        // mapView.getMapAsync(this);

        //추가
        MyGps = new myGPS(this.getActivity());
        myLat=MyGps.myLat;
        myLon=MyGps.myLon;
        galleryManager = new GalleryManager(getContext(),myLat,myLon);
        galleryManager.gallery_manager_for_frag3=true;
        galleryManager.getAllPhotoPathList();
        //frag3에서 GalleryManager를 호출한 경우임을 표시


        //이미지를 찾은 경우 이미지의 절대경로 설정---------------------------------------------------------------------------
        if(galleryManager.flag) {
            imgPath=galleryManager.selectedImg;
        }

        //도영쓰
        alarm_manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        // 타임피커 설정
        alarm_timepicker = (TimePicker) rootView.findViewById(R.id.time_picker);
        // calendar에 시간 셋팅
        // 알람 시작 버튼
        Button alarm_on = null;
        alarm_on = (Button) rootView.findViewById(R.id.btn_start);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                // 시간 가져옴
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                Toast.makeText(context, "Alarm 예정 " + hour + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state", "alarm on");
                pendingIntent = PendingIntent.getBroadcast(context, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람셋팅
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                MyGps.setTargetLat(galleryManager.ImgLatitude);
                MyGps.setTargetLon(galleryManager.ImgLongitude);

                MyGps.setTarget = true;
                Toast.makeText(context, "setTarget==true,frag3", Toast.LENGTH_SHORT).show();

                LatLng toLatLng = new LatLng(galleryManager.ImgLatitude,galleryManager.ImgLongitude);
                mMap.addMarker(new MarkerOptions().position(toLatLng).title("도착지"));
            }
        });


        // 알람 정지 버튼
        Button alarm_off = null;
        alarm_off = rootView.findViewById(R.id.btn_finish);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Alarm 종료버튼누름", Toast.LENGTH_SHORT).show();
                // 알람매니저 취소----------------------------------------------------------------------------내위치랑 비교해서 취소버튼 실제로 실행되게....
                if(MyGps.arrive)
                {
                    alarm_manager.cancel(pendingIntent);
                    my_intent.putExtra("state", "alarm off");
                    // 알람취소
                    context.sendBroadcast(my_intent);
                    Toast.makeText(context, "Alarm 종료 성공", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Alarm 종료 실패", Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgBtn.setOnClickListener(new View.OnClickListener() {//---------------------------------------------------------
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, popImg.class);
                i.putExtra("path",galleryManager.selectedImg);
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public double myLat(){
        return MyGps.getLatitude();
    }

    public double myLon(){
        return MyGps.getLongitude();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LatLng mMe = new LatLng(myLat,myLon);

        myMarker = mMap.addMarker(new MarkerOptions().position(mMe).title("현재위치"));
        MyGps.setMyMarker(myMarker, mMap);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMe, 18);
        mMap.moveCamera(cameraUpdate);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
}
