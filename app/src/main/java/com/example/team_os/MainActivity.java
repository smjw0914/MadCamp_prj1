package com.example.team_os;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int flag = 0;
    ViewPager vp;
    TabLayout tl;
    public static Context context;
    private void init(){
        //뷰페이저 어댑터 추가
        pagerAdapter adapter = new pagerAdapter(this, getSupportFragmentManager());
        frag1 fg1 = new frag1();
        adapter.addItem(fg1);
        frag2 fg2 = new frag2();
        adapter.addItem(fg2);
        frag3 fg3 = new frag3();
        adapter.addItem(fg3);

        context=getApplicationContext();
        vp=(ViewPager)findViewById(R.id.vp);
        tl=(TabLayout)findViewById(R.id.tabLayout);
        vp.setAdapter(adapter);

        tl.addOnTabSelectedListener(pagerListener);

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl));



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 주소록,이미지 권한 확인하고 요청하는 부분
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            flag += 1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        } else {
            flag += 1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
        } else {
            flag += 1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 400);
        } else {
            flag += 1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 500);
        } else {
            flag += 1;
        }
        if (flag == 5) init();



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 4){
                        init();
                    }
                    else flag += 1;
                } else {
                    Log.i("dead","100");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 4){
                        init();
                    }
                    else flag += 1;
                } else {
                    Log.i("dead","200");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 300: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 4){
                        init();
                    }
                    else flag += 1;
                } else {
                    Log.i("dead","300");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 400: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 4){
                        init();
                    }
                    else flag += 1;
                } else {
                    Log.i("dead","400");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 500: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (flag == 4){
                        init();
                    }
                    else flag += 1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    class pagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public pagerAdapter(MainActivity mainActivity, FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    TabLayout.OnTabSelectedListener pagerListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            vp.setCurrentItem(tab.getPosition());
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }
        @Override
        public void onTabReselected(TabLayout.Tab tab) { }
    };
}
