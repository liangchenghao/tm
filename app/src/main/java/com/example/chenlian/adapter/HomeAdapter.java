package com.example.chenlian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlian.flag.Actor;
import com.example.chenlian.myapplication.R;

import java.util.List;

/**
 * Created by chenlian on 10/23/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Actor> actors;
    private Context context;

    public HomeAdapter(Context context, List<Actor> actors) {
        this.context = context;
        this.actors = actors;
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
//        //获取图片资源
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.p42);
//        BitmapDrawable draw = new BitmapDrawable(context.getResources(),bitmap);
//        //设置imageview高度适应图片高度
////        int height = holder.img.getWidth() / draw.getMinimumWidth() * draw.getMinimumHeight();
//        int width = draw.getMinimumWidth();
//        int height = draw.getMinimumHeight();
//        holder.img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

//        String imgUrl = "http://www.personal.psu.edu/jul229/mini.jpg";
//        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build();
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(imgUrl,holder.img,imageOptions);
        holder.txt.setText(actor.getDescription());
    }

    public void addItem(int position) {
        actors.add(position, new Actor("hainfakgna;lka", R.drawable.g18));
        notifyItemInserted(position);
    }

    public void remoteItem(int position) {
        actors.remove(position);
        notifyItemRemoved(position);
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
