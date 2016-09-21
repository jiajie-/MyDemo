package com.jiajie.design.widgets.loopview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiajie.design.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * LoopView for banner
 * Created by jiajie on 16/9/21.
 */
public class LoopView extends RelativeLayout implements ViewPager.OnPageChangeListener
        , View.OnTouchListener {

    private static final String TAG = "LoopView";

    private int mRate;
    private int mBottomStyle;
    private static final int DEF_RATE = 5;
    private static final int DEF_BOTTOM_STYLE = 1;

    //widgets
    private ViewPager mPager;
    private LinearLayout mLinearCircle;
    private LinearLayout mLinearCircleNo;
    private LinearLayout mLinearLayout;
    private TextView mDesc;

    //
    private LoopViewAdapter mAdapter;
    private boolean isChanged;
    private int mCurrentPos;
    private ScheduledExecutorService mScheduledService;
    private LoopViewAdapter.OnItemClickListener listener;

    private MyHandler mHandler;

    public LoopView(Context context) {
        this(context, null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttributeSet(context, attrs);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopView);
        mRate = a.getInteger(R.styleable.LoopView_rate, DEF_RATE);
        mBottomStyle = a.getInteger(R.styleable.LoopView_bottomStyle, DEF_BOTTOM_STYLE);
        a.recycle();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_loopview, this, true);
        mPager = (ViewPager) findViewById(R.id.banner);
        mLinearCircle = (LinearLayout) findViewById(R.id.linear_circle);
        mLinearCircleNo = (LinearLayout) findViewById(R.id.linear_circle_no);
        mDesc = (TextView) findViewById(R.id.desc);
        mPager.addOnPageChangeListener(this);
        mPager.setOnTouchListener(this);

        mHandler = new MyHandler(this);
    }

    public void setLoopData(List<LoopViewItem> data) {
        if (data == null || data.isEmpty()) return;

        int circleCount = data.size();
        //add views
        for (int i = 0; i < circleCount; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.loop_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 2;
            if (mBottomStyle != DEF_BOTTOM_STYLE) {
                mDesc.setVisibility(GONE);
            }

            if (mBottomStyle == getResources().getInteger(R.integer.loop_no_desc_center)) {
                mLinearCircle.setVisibility(GONE);
                mLinearCircleNo.setVisibility(VISIBLE);
                mLinearLayout = mLinearCircleNo;
            } else {
                mLinearLayout = mLinearCircle;
            }
            imageView.setLayoutParams(params);
            mLinearLayout.addView(imageView);
        }

        //adapter
        mAdapter = new LoopViewAdapter(data);
        mAdapter.setOnItemClickListener(new LoopViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.e(TAG, "onItemClick: " + position);
            }
        });
        mPager.setAdapter(mAdapter);
        mLinearLayout.getChildAt(0).setSelected(true);
        mDesc.setText(data.get(0).getDesc());
        startLoop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //对界面中 圆点选中切换 与 描述文本的内容变更
        mCurrentPos = position;

        if (mBottomStyle == getResources().getInteger(R.integer.loop_have_desc)) {
            //代表有描述的布局
            mDesc.setText(mAdapter.getItemAt(position).getDesc());
        }
        int childCount = mLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == position) {
                mLinearLayout.getChildAt(i).setSelected(true);
            } else {
                mLinearLayout.getChildAt(i).setSelected(false);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://滑动完成
                if (mPager.getCurrentItem() == mAdapter.getCount() - 1 && !isChanged) {
                    mPager.setCurrentItem(0);
                }
                if (mPager.getCurrentItem() == 0 && !isChanged) {
                    mPager.setCurrentItem(mAdapter.getCount() - 1);
                }
                break;

            case ViewPager.SCROLL_STATE_DRAGGING:
                isChanged = false;
                break;

            case ViewPager.SCROLL_STATE_SETTLING:
                isChanged = true;
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouch: mCurrentPos " + mCurrentPos);
                pauseLoop();
                break;
            case MotionEvent.ACTION_UP:
                if (mScheduledService.isShutdown()) {
                    startLoop();
                }
                break;
        }
        return false;
    }

    private static class AutoRunnable implements Runnable {

        private WeakReference<LoopView> loopViewWeakReference;


        public AutoRunnable(LoopView loopView) {
            this.loopViewWeakReference = new WeakReference<>(loopView);
        }

        @Override
        public void run() {
            LoopView loopView = loopViewWeakReference.get();
            if (loopView != null) {
                synchronized (this) {
                    loopView.mCurrentPos = (loopView.mCurrentPos + 1) % loopView.mAdapter.getCount();
                    loopView.mHandler.obtainMessage().sendToTarget();
                }
            }
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<LoopView> loopViewWeakReference;

        public MyHandler(LoopView loopView) {
            this.loopViewWeakReference = new WeakReference<>(loopView);
        }

        @Override
        public void handleMessage(Message msg) {
            LoopView loopView = loopViewWeakReference.get();

            if (loopView != null) {
                loopView.mPager.setCurrentItem(loopView.mCurrentPos, true);
            }

        }
    }

    private void startLoop() {
        Log.e(TAG, "startLoop: ");
        mScheduledService = Executors.newSingleThreadScheduledExecutor();
        mScheduledService.scheduleAtFixedRate(new AutoRunnable(this), mRate, mRate, TimeUnit.SECONDS);
    }

    private void pauseLoop() {
        if (!mScheduledService.isShutdown()) {
            Log.e(TAG, "pauseLoop: shut down");
            mScheduledService.shutdown();
        }
    }

    public void setOnItemClickListener(LoopViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
