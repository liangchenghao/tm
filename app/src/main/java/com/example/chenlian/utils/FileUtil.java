package com.example.chenlian.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenlian on 10/16/2015.
 */
public class FileUtil {
    private static final  String TAG = "FileUtil";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = "PlayCamera";

    /**初始化保存路径
     * @return
     */
    private static String initPath(){
        if(storagePath.equals("")){
            storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    public static void saveBitmap(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:失败");
            e.printStackTrace();
        }

    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "TimeMachine");
//Environment.getExternalStoragePublicDirectory(
//            Environment.DIRECTORY_PICTURES)
//        Log.v("TimeMachine", "success to create directory:" + mediaStorageDir.toString());
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                mediaStorageDir.mkdirs();
//                if (! mediaStorageDir.mkdirs()){
//                    Log.v("TimeMachine", "failed to create directory");
//                    return null;
//                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");
                Log.i("getOutputMediaFile",""+ mediaFile);
            } else if(type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_"+ timeStamp + ".mp4");
                Log.i("getOutputMediaFile",""+ mediaFile);
            } else {
                return null;
            }
            if (!mediaFile.exists()){
                try {
                    mediaFile.createNewFile();
                    Log.i("getOutputMediaFile","mediaFile create!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mediaFile;
        }else {
            Log.v("TimeMachine", "SD card is unable!");
            return null;
        }
    }

    public static void savePhotoToSDCard(String photoPath,Bitmap photoBitmap){
        File photoFile = new File(photoPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null){
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream)){
                    fileOutputStream.flush();
                    Log.i("savePhotoToSDCard", "to SD success");
                }
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}