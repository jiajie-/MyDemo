package com.jiajie.design.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiajie.design.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {

    private static Set<String> mSelectedImage = new HashSet<>();

    private String mDirPath;
    private List<String> mImagePaths;
    private LayoutInflater mInflater;
    private OnImageEventListener listener;

    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath = dirPath;
        this.mImagePaths = mDatas;
        this.mInflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.item_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //重置状态
        viewHolder.mImageView.setImageResource(R.drawable.pic_no);
        viewHolder.mImageView.setColorFilter(null);
        viewHolder.mSelect.setImageResource(R.drawable.pic_unselected);

        final String filePath = mDirPath + "/" + mImagePaths.get(position);

        Glide.with(convertView.getContext())
                .load(filePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.mImageView);

        viewHolder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImage.contains(filePath)) {
                    //已经被选择
                    mSelectedImage.remove(filePath);
                    viewHolder.mImageView.setColorFilter(null);
                    viewHolder.mSelect.setImageResource(R.drawable.pic_unselected);
                } else {
                    //未被选择
                    mSelectedImage.add(filePath);
                    viewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.mSelect.setImageResource(R.drawable.pic_selected);
                }
            }
        });

        if (listener != null) {
            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageClick(mImagePaths.get(position));
                }
            });
        }

        if (mSelectedImage.contains(filePath)) {
            viewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mSelect.setImageResource(R.drawable.pic_selected);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView mImageView;
        ImageButton mSelect;
    }

    public interface OnImageEventListener {
        void onImageClick(String file);
    }

    public void setOnImageEventListener(OnImageEventListener listener) {
        this.listener = listener;
    }
}

