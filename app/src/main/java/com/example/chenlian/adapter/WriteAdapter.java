package com.example.chenlian.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Scroller;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;
import com.lidroid.xutils.util.LogUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlian on 10/26/2015.
 */
public class WriteAdapter extends RecyclerView.Adapter<WriteAdapter.WriteViewHolder> {

    public List<MaterialEditText> mEdTs = new ArrayList<>();
    public List<CardView> writeCards = new ArrayList<>();
    private Context context;
    private CardView writeCard;
    private MaterialEditText itemTxt;

    //手指按下X的坐标
    private int downY;
    //手指按下Y的坐标
    private int downX;
    // 屏幕宽度
    private int screenWidth;
    //滑动类
    private Scroller scroller;
    private static final int SNAP_VELOCITY = 600;
    // 速度追踪对象
    private VelocityTracker velocityTracker;
    //是否响应滑动，默认为不响应
    private boolean isSlide = false;
    //认为是用户滑动的最小距离
    private int mTouchSlop;
    //移除item后的回调接口
    private RemoveListener mRemoveListener;
    //用来指示item滑出屏幕的方向,向左或者向右,用一个枚举值来标记
    private RemoveDirection removeDirection;

    /**
     * @param context 传入使用该容器的View的Context
     */
    public WriteAdapter(Context context) {
        this.context = context;
//        this.mEdTs = mEdTDates;
    }

    @Override
    public WriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_write_card, parent, false);
        return new WriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WriteViewHolder holder, int position) {
        holder.mEdT.setText("jasdldf;ajg;agkajdkfj;a");
        writeCard = holder.cardView;
        itemTxt = holder.mEdT;
    }

    @Override
    public int getItemCount() {
//        return writeCards == null ? 0 : writeCards.size();
        return 3;
    }


    public void addItem(int position) {

        writeCards.add(position, writeCard);
        notifyItemInserted(position);
    }

    public void remoteItem(int position) {
        mEdTs.remove(position);
        notifyItemRemoved(position);
    }

    public static class WriteViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        MaterialEditText mEdT;

        public WriteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_write_item);
            mEdT = (MaterialEditText) itemView.findViewById(R.id.mdt_write);
        }
    }

    // 滑动删除方向的枚举值
    public enum RemoveDirection {
        RIGHT, LEFT;
    }

    public interface RemoveListener {
        public void removeItem(RemoveDirection direction, int position);
    }
}
