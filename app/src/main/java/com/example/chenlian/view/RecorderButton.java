package com.example.chenlian.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.chenlian.manager.RecorderManager;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;

import static com.example.chenlian.utils.Utils.showSnackbar;

/**
 * Created by Administrator on 2015/11/12.
 */
public class RecorderButton extends FloatingActionButton implements RecorderManager.RecoderManagerListener {

    //按钮的三个状态
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECODING = 2;
    private static final int STATE_CANCLE = 3;

    private int mCurState = STATE_NORMAL;//记录当前状态

    private int Y = 50;//限定手指移动的上下宽度

    private RecorderManager mRecodierManager;

    private boolean isRecoding = false;
    private boolean isLongClick = false;//是否长按

    private float mTime = 0;//记录录音时长

    private RecoderbuttonListener mListener;//用来传递数据的实体

    public interface RecoderbuttonListener {
        void onFinish(int time, String filePath);
    }

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        File recorderDir = new File(Environment.getExternalStorageDirectory(), "TimeMachine");
        if (!recorderDir.exists()) {
            recorderDir.mkdirs();
        }

        String path = recorderDir.getPath();

        mRecodierManager = RecorderManager.getRecoderManager(path);
        mRecodierManager.setOnRecoderManagerListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isLongClick = true;
                mRecodierManager.recoderPrepared();
                showSnackbar(getRootView(),"start to record");
                return false;
            }
        });
    }

    public void setOnRecorderButtonListener(RecoderbuttonListener listener) {
        this.mListener = listener;
    }

    private static final int CHANGE_VOICE = 0X110;
    private static final int DIALOG_DISS = 0X111;
    private static final int MEDIA_PREPARED = 0X112;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecoding) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTime += 0.1f;

                mHandler.sendEmptyMessage(CHANGE_VOICE);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MEDIA_PREPARED:
                    isRecoding = true;
                    new Thread(mRunnable).start();
                    break;
                case CHANGE_VOICE:

                    break;
                case DIALOG_DISS:
                    break;
            }
        }
    };

    //回调方法，开始录音
    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MEDIA_PREPARED);
    }

    private void reset() {
        isRecoding = false;
        isLongClick = false;
        mTime = 0;
        mCurState = STATE_NORMAL;
    }

    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (mCurState) {
                case STATE_NORMAL:
                    break;
                case STATE_RECODING:
                    break;
                case STATE_CANCLE:
                    break;
            }
        }
    }

    private boolean wantCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            LogUtils.v(">>>>>>>>划得太远了");
            return true;
        }

        if (y < -Y || y > getHeight() + Y) {
            LogUtils.v(">>>>>>>>划得太远了");
            return true;
        }
        return false;
//        int[] leftTop = {0,0};
//        //获取输入框当前的location位置
//        getLocationInWindow(leftTop);
//        int left = leftTop[0] - Y;
//        int top = leftTop[1] - Y;
//        int bottom = top + getHeight() + 2 * Y;
//        int right = left + getWidth() + 2 * Y;
//        if (x > left && x < right
//                && y > top && y < bottom){
//            return false;
//        }else {
//            LogUtils.v(">>>>>>>>划得太远了");
//            return true;
//        }
    }

    //捕捉按钮点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecoding) {
                    if (wantCancel(x, y)) {
                        changeState(STATE_CANCLE);
                    } else {
                        changeState(STATE_RECODING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //没有触发长按按钮
                if (!isLongClick){
                    reset();
                    return super.onTouchEvent(event);
                }else if (mTime < 0.6f){ //没有准备好录音器或录音时间太短
                    mRecodierManager.cancel();
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISS,2000);
                    showSnackbar(getRootView(),"cancel record cause by no prepared or short");
                }else if (mCurState == STATE_RECODING){//正常录音结束
                    mRecodierManager.release();
                    //返回录音的时长和文件路径
                    if (mListener != null){
                       mListener.onFinish((int) mTime,mRecodierManager.getPath());
                    }
                    showSnackbar(getRootView(),"finish recording");
                }else if (mCurState == STATE_CANCLE){//取消录音
                    mRecodierManager.cancel();
                    showSnackbar(getRootView(),"cancel record");
                }
                //各种设置复位
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }
}
