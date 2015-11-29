package com.example.chenlian.activity;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.manager.PlayerManager;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.FileUtil;
import com.example.chenlian.utils.ImageUtil;
import com.example.chenlian.utils.Utils;
import com.example.chenlian.view.RecordButton;
import com.example.chenlian.view.RecorderButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends BaseActivity {

    public static final int MEDIA_CHOICE = 0123;
    public static final int CHOSE_GALLERY = 0;
    public static final int CHOSE_IMAGE = 1;
    public static final int CHOSE_VIDEO = 2;
    public static final int CONFIRM_EDIT = 666;

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    //    @ViewInject(R.id.iv_media)
//    ImageView ivMedia;
    @ViewInject(R.id.iv_picture)
    ImageView ivPicture;
    //    @ViewInject(R.id.vv_video)
//    VideoView vvVideo;
    @ViewInject(R.id.fab)
    RecordButton fab;
    @ViewInject(R.id.et_write)
    EditText et_content;
    @ViewInject(R.id.show_record_time)
    TextView txt_showRecordTime;
    @ViewInject(R.id.cancel_record)
    ImageView cancelRecord;

    private Camera mCamera;
    public Actor actor = new Actor();
    private Uri imageFileUri;
    private Intent intent;
    PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initToolbar();

        intent = getIntent();
        playerManager = new PlayerManager();

        fab.setOnRecordButtonListener(new RecordButton.RecordButtonListener() {
            @Override
            public void onFinish(int time, String filePath) {
                StringBuffer length = txtShowPlayerTime(time);
                txt_showRecordTime.setText(length);
                playerManager.fileDelete();
                playerManager.setFilePath(filePath);
                actor.setRecoderPath(filePath);
            }
        });

        txt_showRecordTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerManager.start();
            }
        });

        cancelRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerManager.fileDelete();
                txt_showRecordTime.setText("00:00");
            }
        });
        if (intent.getIntExtra("go_camera", 9) == 543) {
            captureImages();
        }

        if (intent.getExtras() != null){
            if (!intent.getExtras().isEmpty()){
                actor = (Actor) intent.getExtras().getSerializable("edit_actor");
                if (actor.getMediaPath() != null){
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源路径地址
                    Uri selectedImage = Uri.parse(actor.getMediaPath());

                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, selectedImage);
                        if (photo != null) {
                            Bitmap smallBitmap = ImageUtil.zoomBitmap(photo);
                            photo.recycle();

                            ivPicture.setImageBitmap(smallBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (actor.getDescription() != null){
                    et_content.setText(actor.getDescription());
                }

                if (actor.getRecoderPath() != null){
                    playerManager.setFilePath(actor.getRecoderPath());
                    StringBuffer length = txtShowPlayerTime(playerManager.player.getDuration());
                    txt_showRecordTime.setText(length);
                }
            }
        }
    }

    public StringBuffer txtShowPlayerTime(int time){
        StringBuffer length = new StringBuffer();
        if (time / 60 < 10) {
            length.append("0" + time / 60);
        } else {
            length.append(time / 60);
        }
        length.append(":");
        if (time % 60 < 10) {
            length.append("0" + time % 60);
        } else {
            length.append(time % 60);
        }
        return length;
    }

    @OnClick(R.id.iv_picture)
    public void showChoseDialog(View v) {
        String[] items = new String[]{"Gallery", "Image"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                Intent goGallery = new Intent(EditActivity.this, ResActivity.class);
//                                goGallery.putExtra("chose_code", CHOSE_GALLERY);
//                                startActivityForResult(goGallery, MEDIA_CHOICE);
                                Intent goGallery = new Intent(
                                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(goGallery, Utils.RESULT_LOAD_IMAGE);
                                break;
                            case 1:
//                                Intent goImage = new Intent(EditActivity.this, ResActivity.class);
//                                goImage.putExtra("chose_code", CHOSE_IMAGE);
//                                startActivityForResult(goImage, MEDIA_CHOICE);
                                captureImages();
                                break;
                        }
                    }
                });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void captureImages() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageFileUri = FileUtil.getOutputMediaFileUri(FileUtil.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_edit;
    }


    @Override
    protected void onPause() {
        super.onPause();   // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (Utils.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        //传递touchevent给别的组件
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                confirmEditor();
                return true;
            case android.R.id.home:
                showFinishDialog(this, "确定退出编辑吗？");
                return true;
            default:
                return false;
        }
    }

    private void confirmEditor() {
        if (!et_content.getText().toString().isEmpty()) {
            actor.setDescription(et_content.getText().toString());
        }

        LogUtils.v("editactivity>>>>>>>>>>>" + actor.toString());

        if (newBitmap != null){
            handler.sendEmptyMessage(SAVE_PHOTO_TO_SDCARD);
        }

        if (actor.getMediaPath() != null || actor.getDescription() != null || actor.getRecoderPath() != null) {

            String timeStamp = new SimpleDateFormat("yyyyMMdd HH:mm").format(new Date());
            actor.setTime(timeStamp);

            Bundle bundle = new Bundle();
            bundle.putSerializable("actor", actor);
            intent.putExtras(bundle);
            setResult(CONFIRM_EDIT, intent);
            LogUtils.v(">>>>>>>>>>edit actor is no null");
            finish();
        } else {
            showFinishDialog(this, "你没有编辑内容，确定退出吗？");
        }
    }

    private void showFinishDialog(Context context, String des) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(des)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditActivity.this.finish();
                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    private Uri captureImage;//拍摄的照片存放路径
    private Bitmap newBitmap;//拍摄后压缩的照片

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == MEDIA_CHOICE) {
//            if (resultCode == ResActivity.RESULT_PICTURE && data != null) {
//                LogUtils.v("editactivity onActivityResult picture");
//                Bundle mediaDate = data.getExtras();
//                Actor actor = (Actor) mediaDate.getSerializable("actor");
//                String picPath = actor.getMediaPathUri();
//                if (picPath != null) {
////                    try {
////                        LogUtils.v("" + Uri.parse(picPath));
////                        InputStream is = getContentResolver().openInputStream(Uri.parse(picPath));
////                        Bitmap bitmap = BitmapFactory.decodeStream(is);
////                        ImageView imageView = (ImageView) findViewById(R.id.iv_picture);
////                        imageView.setImageBitmap(bitmap);
////                        ivMedia.setVisibility(View.GONE);
////                        imageView.setVisibility(View.VISIBLE);
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    }
//                    ContentResolver resolver = getContentResolver();
//
//                    //照片的原始资源路径地址
//                    Uri returnPicture = Uri.parse(picPath);
//                    try {
//                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,returnPicture);
//                        if (photo != null){
//                            Bitmap smallBitmap = ImageUtil.zoomBitmap(photo);
//                            photo.recycle();
//
//
//                            ImageView imageView = (ImageView) findViewById(R.id.iv_picture);
//                            imageView.setImageBitmap(smallBitmap);
////                            ivMedia.setVisibility(View.GONE);
////                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.RESULT_LOAD_IMAGE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源路径地址
                    Uri selectedImage = data.getData();
                    actor.setMediaPath(selectedImage.toString());

                    LogUtils.v(">>>>>>>>>>edit actor.mediapath is no null" + selectedImage.toString());

                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, selectedImage);
                        if (photo != null) {
                            Bitmap smallBitmap = ImageUtil.zoomBitmap(photo);
                            photo.recycle();

                            ivPicture.setImageBitmap(smallBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case FileUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:

                    captureImage = imageFileUri;
                    actor.setMediaPath(captureImage.toString());

                    LogUtils.v(">>>>>>>>>>edit actor.mediapath is no null" + captureImage.toString());

                    Bitmap bitmap = BitmapFactory.decodeFile(captureImage.getPath());
                    newBitmap = ImageUtil.zoomBitmap(bitmap);
                    bitmap.recycle();

                    ivPicture.setImageBitmap(newBitmap);
                    break;
            }
        }
    }

    public static final int SAVE_PHOTO_TO_SDCARD = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_PHOTO_TO_SDCARD:
                    new Thread(runnable).start();
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            FileUtil.savePhotoToSDCard(captureImage.getPath(), newBitmap);
        }
    };
}
