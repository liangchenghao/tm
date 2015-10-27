package com.example.chenlian.utils;

import android.support.v7.widget.Toolbar;

import com.example.chenlian.activity.BaseActivity;
import com.example.chenlian.myapplication.R;

public class ToolbarUtils {

    public static void initToolbar(Toolbar toolbar, BaseActivity activity) {
        if (toolbar == null || activity == null)
            return;
//        TextView tv_bar = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        toolbar.setBackgroundColor(activity.getColorPrimary());
        toolbar.setTitle("");
        toolbar.setTitleTextColor(activity.getResColor(R.color.action_bar_title_color));
//        tv_bar.setText("One");
        toolbar.collapseActionView();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
//            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
