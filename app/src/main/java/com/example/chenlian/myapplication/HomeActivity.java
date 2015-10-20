package com.example.chenlian.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenlian.utils.OnItemClickListener;
import com.example.chenlian.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivRunningMan;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RecyclerView lvLeftMenu;
    private ActionBarDrawerToggle mDrawerToggle;

    private int[] lvImg = {R.drawable.g8, R.drawable.g17, R.drawable.g18, R.drawable.g20};
    private String[] lvs = {"时光机", "相册", "设置", "ListItem"};
    private TextView tv_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();

        toolbar.setTitle("");
        tv_bar.setText("有声相册");//设置标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open
                , R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(HomeActivity.this, "open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(HomeActivity.this, "close", Toast.LENGTH_SHORT).show();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
                        //启动设置
                    case 2:startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
                        //启动相机
                    case 3:startActivity(new Intent(HomeActivity.this,CameraActivity.class));
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

    //初始化
    private void findViews() {
        ivRunningMan = (ImageView) findViewById(R.id.iv_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (RecyclerView) findViewById(R.id.lv_left_menu);
        tv_bar = (TextView) toolbar.findViewById(R.id.tv_toolbar);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    //侧滑适配器
    class LeftMenuAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickLitener(OnItemClickListener OnItemClickListener) {
            this.mOnItemClickListener = OnItemClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_ivtv, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.img.setImageResource(lvImg[position]);
            holder.txt.setText(lvs[position]);
            holder.txt.setTextSize(20);
            holder.txt.setTextColor(Color.BLACK);

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return lvImg.length;
        }
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
                ImageView imageView = (ImageView) findViewById(R.id.iv_main);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
