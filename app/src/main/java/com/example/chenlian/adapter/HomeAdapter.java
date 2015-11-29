package com.example.chenlian.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.flag.MultiSelector;
import com.example.chenlian.manager.PlayerManager;
import com.example.chenlian.myapplication.R;
import com.example.chenlian.utils.ImageUtil;
import com.example.chenlian.utils.OnItemClickListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 * Created by chenlian on 10/23/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Actor> actors;
    private Context context;
    private OnItemClickListener itemClickListener;
    private PlayerManager playerManager;

    private RecyclerView mRecyclerView;

    public MultiSelector selector = new MultiSelector();

    public HomeAdapter(Context context, List<Actor> actors) {
        playerManager = new PlayerManager();
        this.context = context;
        this.actors = actors;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview, parent, false);
        HomeViewHolder holder = new HomeViewHolder(view);

        //实例化recyclerview
        if (mRecyclerView == null){
            mRecyclerView = (RecyclerView) parent;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        final Actor actor = actors.get(position);
        LogUtils.v(">>>>>>>>>" + actor.toString());

        if (actor.getMediaPath() != null) {
//            try {
//                InputStream is = context.getContentResolver().openInputStream(Uri.parse(actor.getMediaPath()));
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//
//                LogUtils.v(">>>>>>>>>" + bitmap.toString());
//
//                Bitmap newBitmap = ImageUtil.zoomBitmap(bitmap);
//                bitmap.recycle();
//
//                holder.img.setVisibility(View.VISIBLE);
//                holder.img.setTag(actor.getMediaPath());
//                holder.img.setImageBitmap(newBitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            holder.img.setTag(actor.getMediaPath());
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(actor.getMediaPath());
//            if ()
        }else {
            holder.img.setVisibility(View.GONE);
        }
//        holder.img.setImageResource(actor.getImgID());

        if (actor.getDescription() != null) {
            holder.txt.setVisibility(View.VISIBLE);
            holder.txt.setTag(actor.getDescription());
            holder.txt.setText(actor.getDescription());
        }else {
            holder.txt.setVisibility(View.GONE);
        }

        if(actor.getRecoderPath() != null){
            holder.ibtn.setVisibility(View.VISIBLE);
            holder.ibtn.setTag(actor.getRecoderPath());
            holder.ibtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    handler.sendEmptyMessage(STOP_PLAYER);
//                    playerManager.setFilePath(actor.getRecoderPath());
//                    handler.sendEmptyMessage(START_PLATER);
                    playerManager.stop();
                    playerManager.setFilePath(actor.getRecoderPath());
                    playerManager.start();
                }
            });
        }else {
            holder.ibtn.setVisibility(View.GONE);
        }

        final CardView itemView = (CardView) holder.itemView;

//        if (!selector.isSelectable() && selector.getSelectedPositions().size() > 0) {
//            if (selector.isItemChecked(position)){
//                itemView.setCardBackgroundColor(Color.WHITE);
//                selector.removeItemChecked(position);
//            }
//            selector.getSelectedPositions().clear();
//        }

        if (!selector.isSelectable()){
            itemView.setCardBackgroundColor(Color.WHITE);
        }

        if (itemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selector.isSelectable()) {
                        if (holder.img.getVisibility() == View.VISIBLE){
                            itemClickListener.onItemClick(holder.img,holder.getLayoutPosition());
                        }
                    } else {
                        int pos = holder.getLayoutPosition();
                        if (selector.isItemChecked(pos)) {
                            itemView.setCardBackgroundColor(Color.WHITE);
                            selector.removeItemChecked(pos);
                        } else {
                            itemView.setCardBackgroundColor(Color.GRAY);
                            selector.setItemChecked(pos, true);
                            Log.v("isClick", pos + "");
                        }
                        itemClickListener.onItemClick(itemView, pos);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    //判断是否已经进入选择模式
//                    if (selector.isSelectable()) {
//                        itemView.setCardBackgroundColor(Color.GRAY);
//                        selector.setItemChecked(pos, true);
//                        Log.v("isClick", pos + "");
//                        selector.setIsSelected(true);
//                    }
                    itemClickListener.onItemLongClick(itemView, pos);
                    return true;
                }
            });
        }
    }

    public void addItem(Actor actor) {
        actors.add(0, actor);
        notifyItemInserted(0);
    }

    //删除item
    public void remoteItem(int position){
        actors.remove(position);
        notifyDataSetChanged();
    }
    //删除选中的item
    public void remoteItems(DbUtils db) {
        Integer[] positions = selector.hasSelected();
        List<Actor> buff = new ArrayList<>();
        for (int i = 0; i < positions.length; i++) {
            Actor actor = actors.get(positions[i]);
            buff.add(actor);
        }
        actors.removeAll(buff);
        try {
            db.deleteAll(buff);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.getSelectedPositions().clear();
        notifyDataSetChanged();
    }

    public void restoreBackgroundColor() {
        Integer[] positions = selector.hasSelected();
        for (int i = 0; i < positions.length; i++) {
            notifyItemChanged(positions[i]);
            selector.removeItemChecked(positions[i]);
        }
//        notifyDataSetChanged();
//        selector.getSelectedPositions().clear();
    }

    @Override
    public int getItemCount() {
        return actors == null ? 0 : actors.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView txt;
        public ImageButton ibtn;

        public HomeViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.cv_img);
            txt = (TextView) itemView.findViewById(R.id.cv_text);
            ibtn = (ImageButton) itemView.findViewById(R.id.ib_recorder_player);
        }
    }

    class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap> {

        String imgUri;

        @Override
        protected Bitmap doInBackground(String... strings) {
            imgUri = strings[0];
            //在后台加载图片
            InputStream is= null;
            try {
                is = context.getContentResolver().openInputStream(Uri.parse(imgUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            LogUtils.v(">>>>>>>>>task doinbackground bitmap");

            Bitmap newBitmap = ImageUtil.zoomBitmap(bitmap);
            bitmap.recycle();

            return newBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) mRecyclerView.findViewWithTag(imgUri);
            if (imageView != null && bitmap != null){
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
