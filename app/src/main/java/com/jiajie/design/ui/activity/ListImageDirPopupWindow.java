package com.jiajie.design.ui.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
import com.jiajie.design.R;

import java.util.List;

/**
 * ListImageDirPopupWindow
 * Created by jiajie on 16/9/12.
 */
public class ListImageDirPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;

    private View mConvertView;
    private ListView mListView;

    private List<FolderBean> mDatas;

    public interface OnDirSelectedListener {
        void onSelected(FolderBean folderBean);
    }

    OnDirSelectedListener mListener;

    public OnDirSelectedListener getOnDirSelectedListener() {
        return mListener;
    }

    public void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

    public ListImageDirPopupWindow(Context context, List<FolderBean> datas) {
        super(context);
        calculateSize(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_selector, null);
        mDatas = datas;

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

        initViews(context);
        initEvent();

    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onSelected(mDatas.get(position));
                }
            }
        });

    }

    private void initViews(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.dir_list);
        mListView.setAdapter(new ListDirAdapter(context, mDatas));
    }

    private void calculateSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7);
    }

    private class ListDirAdapter extends ArrayAdapter<FolderBean> {

        private LayoutInflater mInflater;

        private List<FolderBean> mDatas;

        public ListDirAdapter(Context context, List<FolderBean> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

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

            FolderBean bean = getItem(position);
            //reset

            holder.mImage.setImageResource(R.drawable.pic_no);

            Glide.with(convertView.getContext())
                    .load(bean.getFirstImagePath())
                    .into(holder.mImage);
//            ImageLoader.getInstance().loadImage(bean.getFirstImagePath(), holder.mImage);

            holder.mDirName.setText(bean.getName());
            holder.mDirCount.setText(String.valueOf(bean.getCount()));

            return convertView;
        }


        private class ViewHolder {
            ImageView mImage;
            TextView mDirName;
            TextView mDirCount;
        }
    }


}
