package com.example.chenlian.flag;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by chenlian on 10/23/2015.
 */
public class Actor implements Serializable{
    int id;
    String description;
    int imgID;

    public Actor(){};

    public Actor(String str, int id){
        this.description = str;
        this.imgID = id;
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
}
