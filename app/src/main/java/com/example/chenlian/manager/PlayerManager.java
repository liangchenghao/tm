package com.example.chenlian.manager;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/11/16.
 */
public class PlayerManager {
    public MediaPlayer player; // 定义多媒体对象
    private String filePath;
    public PlayerManager(){
        player = new MediaPlayer();
    }

    //设置播放文件的绝对路径
    public void setFilePath(String path){
        this.filePath = path;
        try {
            player.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放音乐
    public void start() {
        player.reset();// 重置播放器
        try {
            player.prepare();
            player.start();
            // setOnCompletionListener 当当前多媒体对象播放完成时发生的事件
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    player.stop();
//                }
//            });
        } catch (IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stop(){
        if (player.isPlaying()) {
            player.stop();
        }
    }

    public void fileDelete(){
        if (filePath != null){
            File file = new File(filePath);
            if (file.exists()){
                file.delete();
            }
        }
    }
}
