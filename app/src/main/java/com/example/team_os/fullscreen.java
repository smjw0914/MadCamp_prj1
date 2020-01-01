package com.example.team_os;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class fullscreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_photo);
        ImageView fullscreen = (ImageView) findViewById(R.id.fullscreen);
        String img_path =getIntent().getStringExtra("img_path");
        Log.i("img_path",img_path);
        Bitmap myBitmap = BitmapFactory.decodeFile(img_path);
        fullscreen.setImageBitmap(myBitmap);
    }
}
