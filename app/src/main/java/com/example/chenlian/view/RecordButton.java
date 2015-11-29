package com.example.chenlian.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.example.chenlian.manager.RecorderManager;
import com.example.chenlian.myapplication.R;

import java.io.File;

import static com.example.chenlian.utils.Utils.showSnackbar;

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

    public void setOnRecordButtonListener(RecordButtonListener listener) {
        this.mListener = listener;
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
                    RecordButton.this.setImageResource(R.drawable.ic_info_black_24dp);
                    isRecording = true;
                }else if (mTime < 0.6f){ //没有准备好录音器或录音时间太短
                    mRecorderManager.cancel();
                    RecordButton.this.setImageResource(R.drawable.ic_add_white_24dp);
                    isRecording = false;
                    showSnackbar(getRootView(),"cancel record cause by no prepared or short");
                }else {
                    mRecorderManager.release();
                    RecordButton.this.setImageResource(R.drawable.ic_add_white_24dp);
                    //返回录音的时长和文件路径
                    if (mListener != null){
                        mListener.onFinish((int) mTime,mRecorderManager.getPath());
                    }
                    mTime = 0;
                    isRecording = false;
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
