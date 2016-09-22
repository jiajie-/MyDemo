package com.jiajie.design.ui.photopicker;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiajie.design.R;

import java.util.List;

/**
 * ListImageDirPopupWindow
 * Created by jiajie on 16/9/12.
 */
class ListImageDirPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;

    private View mConvertView;
    private ListView mListView;

    private List<Folder> mFolderList;

    private OnDirSelectedListener mListener;

    ListImageDirPopupWindow(Context context, List<Folder> folderList) {
        super(context);
        calculateSize(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_selector, null);
        mFolderList = folderList;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        init(context);
    }

    private void init(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.dir_list);
        mListView.setAdapter(new ListDirAdapter(context, mFolderList));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onSelected(mFolderList.get(position));
                }
            }
        });
    }

    private void calculateSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7);
    }

    private static class ListDirAdapter extends ArrayAdapter<Folder> {

        private LayoutInflater mInflater;

        ListDirAdapter(Context context, List<Folder> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup, parent, false);
                holder.mImage = (ImageView) convertView.findViewById(R.id.dir_item_image);
                holder.mDirName = (TextView) convertView.findViewById(R.id.dir_item_name);
                holder.mDirCount = (TextView) convertView.findViewById(R.id.dir_item_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Folder bean = getItem(position);

            //reset
            holder.mImage.setImageResource(R.drawable.pic_no);

            Glide.with(convertView.getContext())
                    .load(bean.getFirstImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.mImage);

            holder.mDirName.setText(bean.getName());
            holder.mDirCount.setText(String.valueOf(bean.getCount()) + "张");

            return convertView;
        }

        private static class ViewHolder {
            ImageView mImage;
            TextView mDirName;
            TextView mDirCount;
        }
    }

    interface OnDirSelectedListener {
        void onSelected(Folder folder);
    }

    void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

}
