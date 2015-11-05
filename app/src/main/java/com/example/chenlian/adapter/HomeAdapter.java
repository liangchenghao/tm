package com.example.chenlian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.flag.MultiSelector;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlian on 10/23/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Actor> actors;
    private Context context;
    private OnItemClickListener itemClickListener;

    public MultiSelector selector = new MultiSelector();

    public HomeAdapter(Context context, List<Actor> actors) {
        this.context = context;
        this.actors = actors;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview, parent, false);
        HomeViewHolder holder = new HomeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        Actor actor = actors.get(position);
        holder.img.setImageResource(actor.getImgID());
        holder.txt.setText(actor.getDescription());
        final CardView itemView = (CardView) holder.itemView;

        if (!selector.isSelectable()){
            itemView.setCardBackgroundColor(Color.WHITE);
        }

        if (itemClickListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selector.isSelectable()){
                    }else {
                        int pos = holder.getLayoutPosition();
                        if (selector.isItemChecked(pos)){
                            itemView.setCardBackgroundColor(Color.WHITE);
                            selector.removeItemChecked(pos);
                        }else {
                            itemView.setCardBackgroundColor(Color.GRAY);
                            selector.setItemChecked(pos, true);
                            Log.v("isClick", pos + "");
                        }
                        itemClickListener.onItemClick(itemView,pos);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //判断是否已经进入选择模式
                    if (!selector.isSelectable()) {
                        itemView.setCardBackgroundColor(Color.GRAY);
                        int pos = holder.getLayoutPosition();
                        selector.setItemChecked(pos, true);
                        Log.v("isClick",pos+"");
                        selector.setIsSelected(true);
                        itemClickListener.onItemLongClick(itemView, pos);
                    }
                    return true;
                }
            });
        }
    }

    public void addItem(int position) {
        actors.add(position, new Actor("hainfakgna;lka", R.drawable.g18));
        notifyItemInserted(position);
    }

    //删除选中的item
    public void remoteItems() {
        Integer[] positions = selector.hasSelected();
        List<Actor> buff = new ArrayList<>();
        for (int i =0; i < positions.length;i++) {
            Actor actor = actors.get(positions[i]);
            buff.add(actor);
        }
        actors.removeAll(buff);
        selector.getSelectedPositions().clear();
        notifyDataSetChanged();
    }

    public void restoreBackgroundColor(){
        Integer[] positions = selector.hasSelected();
        for(int i = 0;i<positions.length;i++){

        }
    }

    @Override
    public int getItemCount() {
        return actors == null ? 0 : actors.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView txt;

        public HomeViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.cv_img);
            txt = (TextView) itemView.findViewById(R.id.cv_text);
        }
    }
}
