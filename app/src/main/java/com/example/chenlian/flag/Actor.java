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
    int imgID;

    public Actor(){};

    public Actor(String str, int id){
        this.description = str;
        this.imgID = id;
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

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
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

}
