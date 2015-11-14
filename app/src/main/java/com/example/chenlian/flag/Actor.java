package com.example.chenlian.flag;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by chenlian on 10/23/2015.
 */
public class Actor implements Serializable{
    int id;
    String mediaPath;
    String recoderPath;
    String description;
    String time;

    public Actor(){}

    public Actor(String str){
        this.description = str;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecoderPath() {
        return recoderPath;
    }

    public void setRecoderPath(String recoderPath) {
        this.recoderPath = recoderPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", mediaPath='" + mediaPath + '\'' +
                ", recoderPath='" + recoderPath + '\'' +
                ", description='" + description + '\'' +
                ", imgID=" +
                '}';
    }
}
