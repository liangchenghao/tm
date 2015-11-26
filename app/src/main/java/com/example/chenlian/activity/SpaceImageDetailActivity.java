package com.example.chenlian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.chenlian.view.SmoothImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/23.
 */
public class SpaceImageDetailActivity extends Activity {

    String imgUri;
//    int mPosition;
    int mLocationX,mLocationY;
    int mWidth,mHeight;

    SmoothImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        imgUri = getIntent().getStringExtra("image");
//        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        ImageLoader.getInstance().displayImage(imgUri, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.transformOut();
            }
        });
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == SmoothImageView.STATE_TRANSFORM_OUT){
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                imageView.transformOut();
                imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
                    @Override
                    public void onTransformComplete(int mode) {
                        if (mode == SmoothImageView.STATE_TRANSFORM_OUT){
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    }
                });
                break;
        }
        return false;
    }
}
