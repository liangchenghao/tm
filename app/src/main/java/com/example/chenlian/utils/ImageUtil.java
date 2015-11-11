package com.example.chenlian.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by chenlian on 10/16/2015.
 */
public class ImageUtil {
    /**
     * 旋转Bitmap
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }


    //bitmap缩小一半
    public static Bitmap zoomBitmap(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = 0.5f;
        float scaleHeight = 0.5f;
        //矩阵缩放，不会造成内存溢出
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
        return newBitmap;
    }
}
