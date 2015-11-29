package com.example.chenlian.manager;

import android.media.MediaRecorder;

import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/12.
 */

//录音类
public class RecorderManager {
    private MediaRecorder mMediaRecoder;//录音器

    private static RecorderManager mRecorderManager;
    private RecoderManagerListener mListener;
    private static String dir;//录音存放的文件夹
    private String mCurPath;//录音即时存放的路径名

    private boolean isPrepared;//判断录音是否已经准备好了

    private RecorderManager(String dir){
        this.dir = dir;//转入保存音频的文件夹的地址
    }

    public static RecorderManager getRecoderManager(String dir){
        if (mRecorderManager == null){
            synchronized (RecorderManager.class){
                if (mRecorderManager == null){
                    mRecorderManager = new RecorderManager(dir);
                }
            }
        }
        return mRecorderManager;
    }

    //提供一个毁掉接口，当录音准备好了以后，调用该接口的方法，录音正式开始，此时就可以获取声音等级等
    public interface RecoderManagerListener{
        void wellPrepared();//录音准备好了调用该方法
    }

    public void setOnRecoderManagerListener(RecoderManagerListener mListener) {
        this.mListener = mListener;
    }

    //随机生成文件名
    private  String generateName(){
//        return UUID.randomUUID().toString() + ".amr";
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".amr";
    }

    public void recoderPrepared(){
        isPrepared = false;

        File mDir = new File(dir);

        if (!mDir.exists()){
            mDir.mkdirs();
        }

        String fileName = generateName();//录下的声音所输出的文件名
        File file = new File(mDir, fileName);//在文件夹mDir生成文件
        mCurPath = file.getAbsolutePath();//记录即时存放录音文件的完整路径

        mMediaRecoder = new MediaRecorder();
        //设置音频源为麦克风
        mMediaRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频格式
        mMediaRecoder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        //设置编码格式
        mMediaRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //输出文件
        mMediaRecoder.setOutputFile(mCurPath);
        LogUtils.v(">>>>>>>>>>>录音输出：" + mCurPath);

        try {
            mMediaRecoder.prepare();
            mMediaRecoder.start();

            isPrepared = true;

            if (isPrepared){
                mListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通过音频获得声音的级别，转化为1~maxLevel之间
    public int getVoiceLevel(int maxLevel){
        if (mMediaRecoder != null){
            try{
                return maxLevel*mMediaRecoder.getMaxAmplitude()/32768 + 1;
            }catch (IllegalStateException e){
                //避免影响程序进行
            }
        }

        return 1;
    }

    //释放资源
    public void release(){
        if (mMediaRecoder != null){
            mMediaRecoder.reset();
            mMediaRecoder.release();
            mMediaRecoder = null;
        }
    }

    //录音取消时的操作
    public void cancel(){
        release();

        if (mCurPath != null){
            File file = new File(mCurPath);
            if (file.exists()){
                file.delete();
                mCurPath = null;
            }
        }
    }

    //提供一个获取录音存放路径的方法
    public String getPath(){
        return mCurPath;
    }
}
