package com.example.chenlian.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chenlian.adapter.WriteAdapter;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.edit_linear)
    LinearLayout editLinear;
    @ViewInject(R.id.fam)
    FloatingActionsMenu fam;
    @ViewInject(R.id.fab_write)
    FloatingActionButton fabWrite;
    @ViewInject(R.id.fab_picture)
    FloatingActionButton fabPicture;
    @ViewInject(R.id.rv_text)
    RecyclerView rvText;

    CardView editCard;
    List<MaterialEditText> mEdts = new ArrayList<>();
    WriteAdapter writeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initToolbar();
        initView();

        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MaterialEditText mEdT = new MaterialEditText(EditActivity.this);
//                initCard(mEdT);
//                mEdts.add(mEdT);
                writeAdapter.addItem(writeAdapter.mEdTs.size());
                rvText.scrollToPosition(0);
            }
        });

        fabPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Utils.RESULT_LOAD_IMAGE);
            }
        });

//        FloatingActionButton fam = (FloatingActionButton) findViewById(R.id.fam);
//        final FloatingActionButton fab_write = (FloatingActionButton) findViewById(R.id.fab_write);
//        final FloatingActionButton fab_picture = (FloatingActionButton) findViewById(R.id.fab_picture);
//        fam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                fab_write.show();
//                fab_picture.show();
//            }
//        });
    }

    private void initView(){
        rvText.setLayoutManager(new LinearLayoutManager(this));
        writeAdapter = new WriteAdapter(this);
        rvText.setAdapter(writeAdapter);
        rvText.setItemAnimator(new DefaultItemAnimator());
    }

    private void initCard(View view) {
        editCard = new CardView(this);
        editCard.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        editCard.addView(view);
        editLinear.addView(editCard);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ImageView imageView = (ImageView) findViewById(R.id.iv_picture);
                imageView.setImageBitmap(bitmap);
                CardView cardView = (CardView) findViewById(R.id.cv_medio);
                cardView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    TaskStackBuilder.create(this)
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
