package com.example.chenlian.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.FileUtil;
import com.example.chenlian.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditActivity extends BaseActivity {

    public static final int MEDIA_CHOICE = 0123;
    public static final int CHOSE_GALLERY = 0;
    public static final int CHOSE_IMAGE = 1;
    public static final int CHOSE_VIDEO = 2;

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.iv_media)
    ImageView ivMedia;
    @ViewInject(R.id.iv_picture)
    ImageView ivPicture;
    @ViewInject(R.id.vv_video)
    VideoView vvVideo;
    @ViewInject(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initToolbar();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick({R.id.iv_media, R.id.iv_picture, R.id.vv_video})
    public void showChoseDialog(View v) {
        String[] items = new String[]{"Gallery", "Image", "video"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent i = new Intent(EditActivity.this, ResActivity.class);
                                i.putExtra("chose_code", CHOSE_GALLERY);
                                startActivityForResult(i, MEDIA_CHOICE);
//                                Intent i = new Intent(
//                                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(i, Utils.RESULT_LOAD_IMAGE);
                                break;
                        }
                    }
                });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
//                setResult();
                break;
            case R.id.home:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("确定退出编辑吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditActivity.this.finish();
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_CHOICE) {
            if (resultCode == ResActivity.RESULT_PICTURE && data != null) {
                LogUtils.v("onresultgetpic");
                Bundle mediaDate = data.getExtras();
                Actor actor = (Actor) mediaDate.getSerializable("actor");
                String picPath = actor.getMediaPathUri();
                if (picPath != null) {
                    try {
                        LogUtils.v("" + Uri.parse(picPath));
                        InputStream is = getContentResolver().openInputStream(Uri.parse(picPath));
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        ImageView imageView = (ImageView) findViewById(R.id.iv_picture);
                        imageView.setImageBitmap(bitmap);
                        ivMedia.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
