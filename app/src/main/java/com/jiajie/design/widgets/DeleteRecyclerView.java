package com.jiajie.design.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiajie.design.R;

/**
 * 在 RecyclerView 上滑动就可以显示出一个删除按钮，点击按钮就会删除相应数据
 * Created by jiajie on 16/8/7.
 */
public class DeleteRecyclerView extends ListView implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final String TAG = "DeleteRecyclerView";

    private GestureDetector mGestureDetector;

    private OnDeleteListener listener;

    private View delete;
    private ViewGroup itemLayout;
    private int selectedItem;
    private boolean isDeleteShown;

    public DeleteRecyclerView(Context context) {
        super(context);
    }

    public DeleteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDeleteShown) {
            itemLayout.removeView(delete);
            delete = null;
            isDeleteShown = false;
            return false;
        } else {
            return mGestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShown) {
            selectedItem = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e(TAG, "onFling: ");
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            delete = LayoutInflater.from(getContext()).inflate(R.layout.delete_button, null);
            delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(delete);
                    delete = null;
                    isDeleteShown = false;
                    if (listener != null) {
                        listener.onDelete(selectedItem);
                    }
                }
            });
            itemLayout = (ViewGroup) getChildAt(selectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(delete, params);
            isDeleteShown = true;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    interface OnDeleteListener {
        void onDelete(int index);
    }

}
