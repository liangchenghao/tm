package com.example.chenlian.utils;

import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by chenlian on 10/15/2015.
 */
public class Utils {
    public static final int RESULT_LOAD_IMAGE = 0;


    public static final String CAMERA_TAG = "camera_error";

    public static boolean isShouldHideInput(View v,MotionEvent event){
        if (v != null && (v instanceof EditText)){
            int[] leftTop = {0,0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top){
                //点击的是输入框区域，保留edittext的点击事件
                return false;
            }else {
                return true;
            }
        }
        return false;
    }


    public static void showSnackbar(View view,String str){
        Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

}
