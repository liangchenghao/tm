package com.example.chenlian.flag;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by chenlian on 10/23/2015.
 */
public class Actor implements Serializable{
    int id;
    String mediaPathUri;
    CharSequence description;
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

    public String getMediaPathUri() {
        return mediaPathUri;
    }

    public void setMediaPathUri(String mediaPathUri) {
        this.mediaPathUri = mediaPathUri;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }
}
