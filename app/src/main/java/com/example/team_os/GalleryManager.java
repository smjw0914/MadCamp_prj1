package com.example.team_os;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryManager {
    private Context mContext;

    public  double Mylatitude=-100;
    public  double Mylongitude=-100;

    public boolean flag = false;
    public boolean gallery_manager_for_frag3=false;

    public String selectedImg="";

    public double ImgLatitude;
    public double ImgLongitude;
    GeoDegree geoDegree;

    public GalleryManager(Context context) {
        mContext = context;
    }
    public GalleryManager(Context context,double lat,double lon) {
        mContext = context;
        Mylatitude=lat;
        Mylongitude=lon;
        Log.i("info","galleryManager_생성자");
    }
    /**
     * 갤러리 이미지 반환
     *
     * @return
     */
    public List<PhotoVO> getAllPhotoPathList() {

        ArrayList<PhotoVO> photoList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        String path="";
        // distanceTo distance = new distanceTo();

        while (cursor.moveToNext()) {
            PhotoVO photoVO = new PhotoVO(cursor.getString(columnIndexData),false);
            path=photoVO.getImgPath();
            if(gallery_manager_for_frag3) searchImg(path);
            photoList.add(photoVO);
        }

        //이미지 찾은 경우 동작설정
        if(flag == true) {
            Log.i("info","이미지를 찾았습니다");
        }
        //이미지 못찾은 경우 임시로 target위치 설정
        else if (flag == false){
            if(gallery_manager_for_frag3){
                Log.i("info","이미지를 찾지못했습니다11111111");
                Log.i("info",Double.toString(Mylatitude));
                ImgLongitude = Mylongitude+0.0003;
                ImgLatitude = Mylatitude+0.0004;
                flag=true;
                Log.i("info","마커등록");
            }
            else Log.i("info","이미지를 찾지못했습니다33333");
        }
        else  Log.i("info","이미지를 찾지못했습니다2222");

        cursor.close();
        return photoList;
    }

    public  void searchImg(String path){
        try {
            if(flag == false){
                ExifInterface exif = new ExifInterface(path);
                if(exif.getAltitude(0) == 0) return;
                geoDegree = new GeoDegree(exif);
                Log.i("geoDegree_info",Double.toString(geoDegree.getLatitude())+","+Double.toString(geoDegree.getLongitude())); //우리나라 위도 37
                checkImg(geoDegree,path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //내 근방의 이미지 찾기
    public  void checkImg(GeoDegree gd, String path){

        double dis = 0;
        Location location1 = new Location("point 1"); //내위치
        Location location2 = new Location("point 2"); //사진위치
        location1.setLatitude(Mylatitude);
        location1.setLongitude(Mylongitude);
        Log.i("myLocation_info",location1.toString());
        location2.setLongitude(gd.getLongitude());
        location2.setLatitude(gd.getLatitude());

        dis = location1.distanceTo(location2);

        Log.i("info_dis",Double.toString(dis));

        //근방의 사진을 찾은 경우
        if(dis >= 50 && dis <= 100) {
            flag = true;
            selectedImg = path;
            ImgLatitude = gd.getLatitude();
            ImgLongitude = gd.getLongitude();
            Log.i("info_imgLatitude","근방의 사진을 찾은 경우");
            Log.i("info_imgLatitude",Double.toString(ImgLatitude));
        }

    }
}
