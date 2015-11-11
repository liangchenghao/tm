package com.example.chenlian.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.FileUtil;
import com.example.chenlian.utils.ImageUtil;
import com.example.chenlian.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenlian on 10/20/2015.
 */
public class ResActivity extends Activity {

    public static final int RESULT_PICTURE = 0;

    @ViewInject(R.id.iv_result_image)
    ImageView img;

    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private Uri imageFileUri;
    public Actor actor = new Actor();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewUtils.inject(this);

        intent = getIntent();
        selectResFrom(intent.getIntExtra("chose_code", 9));
    }

    private void selectResFrom(int flag) {
        switch (flag) {
            case EditActivity.CHOSE_GALLERY:
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Utils.RESULT_LOAD_IMAGE);
                break;
            case EditActivity.CHOSE_IMAGE:
                captureImages();
                break;
            case EditActivity.CHOSE_VIDEO:
                break;
            default:
                break;
        }
    }

    private void captureImages() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageFileUri = FileUtil.getOutputMediaFileUri(FileUtil.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void putMedio(View v) {
        if (v.getId() == R.id.btn_ok && img.getDrawable() != null && img.getVisibility() == View.VISIBLE) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("actor", actor);
            intent.putExtras(bundle);
            setResult(RESULT_PICTURE, intent);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == Utils.RESULT_LOAD_IMAGE || requestCode == FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK && data != null) {
//                Toast.makeText(this, "Image saved to:\n" +
//                        data.getData(), Toast.LENGTH_SHORT).show();
//                Uri selectedImage = data.getData();
//                actor.setMediaPathUri(selectedImage.toString());
////                    InputStream is = getContentResolver().openInputStream(selectedImage);
////                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//
//                Bitmap bitmap = BitmapFactory.decodeFile(selectedImage.getPath());
//                if ()
//                bitmap.recycle();
//                img.setVisibility(View.VISIBLE);
//                img.setImageBitmap(bitmap);
//                if (requestCode == FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
//
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                // User cancelled the image capture
//            } else {
//                // Image capture failed, advise user
//            }
//        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.RESULT_LOAD_IMAGE:
                    ContentResolver resolver = getContentResolver();

                    Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_SHORT).show();

                    //照片的原始资源路径地址
                    Uri selectedImage = data.getData();
                    actor.setMediaPathUri(selectedImage.toString());
                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,selectedImage);
                        if (photo != null){
                            Bitmap smallBitmap = ImageUtil.zoomBitmap(photo);
                            photo.recycle();

                            img.setVisibility(View.VISIBLE);
                            img.setImageBitmap(smallBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:

                    Uri captureImage = imageFileUri;
                    actor.setMediaPathUri(captureImage.toString());
                    Bitmap bitmap = BitmapFactory.decodeFile(captureImage.getPath());
                    Bitmap newBitmap = ImageUtil.zoomBitmap(bitmap);
                    bitmap.recycle();

                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(newBitmap);
                    FileUtil.savePhotoToSDCard(captureImage.getPath(),newBitmap);
                    break;
            }
        }else {
            Toast.makeText(this, "get result failed", Toast.LENGTH_SHORT).show();
        }

    }
}
