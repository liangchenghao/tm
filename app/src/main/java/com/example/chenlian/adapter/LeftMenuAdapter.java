package com.example.chenlian.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlian on 10/23/2015.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.MyViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private int[] lvImg = {R.drawable.ic_insert_photo_black_24dp, R.drawable.ic_insert_photo_black_24dp
            , R.drawable.ic_insert_photo_black_24dp, R.drawable.ic_insert_photo_black_24dp};
    private String[] lvs = {"时光机", "相册", "设置", "相机"};
    private List<Actor> mDate = new ArrayList<>();

    public void setOnItemClickLitener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ivtv, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.img.setImageResource(lvImg[position]);
        holder.txt.setText(lvs[position]);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lvImg.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

}
