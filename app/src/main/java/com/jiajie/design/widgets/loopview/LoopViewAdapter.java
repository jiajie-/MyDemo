package com.jiajie.design.widgets.loopview;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiajie.design.R;

import java.util.List;

/**
 * LoopViewAdapter for LoopView
 * Created by jiajie on 16/9/21.
 */
public class LoopViewAdapter extends PagerAdapter {

    private List<LoopViewItem> items;
    private OnItemClickListener listener;

    LoopViewAdapter(List<LoopViewItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    LoopViewItem getItemAt(int position) {
        return items.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_loopview, null);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.loop_image);
        Glide.with(container.getContext())
                .load(items.get(position).getImageUrl())
                .centerCrop()
                .into(imageView);

        if (listener != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
