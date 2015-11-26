package com.example.chenlian.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.example.chenlian.manager.RecorderManager;

import java.io.File;

/**
 * Created by Administrator on 2015/11/26.
 */
public class RecordButton extends ImageButton implements RecorderManager.RecoderManagerListener{


    private RecorderManager mRecorderManager;
    private RecordButtonListener mListener;

    private boolean isRecording = false;
    private float mTime = 0;//记录录音时长

    //回调方法，执行开始录音时的操作
    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MEDIA_PREPARED);
    }

    //用来传递数据的实体
    public interface RecordButtonListener {
        void onFinish(int time, String filePath);
    }

    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        File recorderDir = new File(Environment.getExternalStorageDirectory(), "TimeMachine");
        if (!recorderDir.exists()) {
            recorderDir.mkdirs();
        }

        String path = recorderDir.getPath();

        mRecorderManager = RecorderManager.getRecoderManager(path);
        mRecorderManager.setOnRecoderManagerListener(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording){
                    mRecorderManager.recoderPrepared();
                    isRecording = true;
                }else {
                }
            }
        });
    }

    private static final int MEDIA_PREPARED = 0;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTime += 0.1f;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MEDIA_PREPARED:
                    isRecording = true;
                    new Thread(mRunnable).start();
                    break;
            }
        }
    };
}
