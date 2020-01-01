package com.example.team_os;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class popImg extends Activity {
    ImageView imgView;
    Intent i;
    String path;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.img_popup);
        i = getIntent();
        getImg();
        imgView = (ImageView)findViewById(R.id.imageView);
        setImgView();

    }

    public void onClick(View v){
        finish();
    }

    public void setImgView(){
        Log.i("info_img_path" ,path);
        if(path.equals("")){
            imgView.setImageResource(R.drawable.non_img);
        }
        else{
            imgView.setImageURI(Uri.parse(path));
        }
    }

    public void getImg(){
        path = i.getStringExtra("path");
    }
}
