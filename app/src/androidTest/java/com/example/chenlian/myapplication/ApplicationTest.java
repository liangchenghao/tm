package com.example.chenlian.myapplication;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.chenlian.activity.EditActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public static void tryTest(){
        EditActivity editActivity = new EditActivity();
    }
}