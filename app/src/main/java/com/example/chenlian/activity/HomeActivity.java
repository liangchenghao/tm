package com.example.chenlian.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenlian.adapter.HomeAdapter;
import com.example.chenlian.adapter.LeftMenuAdapter;
import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.OnItemClickListener;
import com.example.chenlian.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

//    private ImageView ivRunningMan;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RecyclerView lvLeftMenu;
    private RecyclerView homeMain;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView tv_bar;
    List<Actor> showDates = new ArrayList<>();
    private HomeAdapter homeAdapter = new HomeAdapter(this,showDates);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);

        findViews();
        initToolbar();

//        toolbar.setTitle("");
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

//        setSupportActionBar(toolbar);
        initDrawerlayout();
    }

    private void initDrawerlayout() {
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open
                , R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        initLeftMenu();
        initHomeMain();
    }

    protected void initLeftMenu() {
        //设置菜单列表
        lvLeftMenu.setLayoutManager(new LinearLayoutManager(this));
        LeftMenuAdapter menuAdapter = new LeftMenuAdapter();
        menuAdapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(HomeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    //启动图库
                    case 1:
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, Utils.RESULT_LOAD_IMAGE);
                        break;
                    case 2://启动设置
                        startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        break;
                    case 3://启动相机
                        startActivity(new Intent(HomeActivity.this, CameraActivity.class));
                        break;
                }
                mDrawerLayout.closeDrawers();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        lvLeftMenu.setAdapter(menuAdapter);
        lvLeftMenu.setItemAnimator(new DefaultItemAnimator());
    }

    protected void initHomeMain() {
        homeMain.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        homeMain.setAdapter(homeAdapter);
        homeMain.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
    }

    //初始化
    private void findViews() {
//        ivRunningMan = (ImageView) findViewById(R.id.iv_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (RecyclerView) findViewById(R.id.lv_left_menu);
        homeMain = (RecyclerView) findViewById(R.id.rv_main);
//        tv_bar = (TextView) toolbar.findViewById(R.id.tv_toolbar);

        showDates.add(new Actor("sdafjdasfaskdf", R.drawable.p42));
    }

    //回调加载图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                ImageView imageView = (ImageView) findViewById(R.id.iv_main);
//                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //导航栏按钮添加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivity(new Intent(this, EditActivity.class));
                break;
            case R.id.action_add:
                    homeAdapter.addItem(0);
                homeMain.scrollToPosition(0);
//                homeAdapter.notifyDataSetChanged();

                break;
            case R.id.action_del:
                if (homeAdapter.getItemCount() > 0){
                    homeAdapter.remoteItem(0);
//                    homeMain.scrollToPosition(1);
//                    homeAdapter.notifyDataSetChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
