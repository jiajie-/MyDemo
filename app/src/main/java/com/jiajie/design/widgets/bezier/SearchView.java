package com.jiajie.design.widgets.bezier;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * SearchView with path animation.
 * Created by jiajie on 16/9/11.
 */
public class SearchView extends View {

    private static final String TAG = "SearchView";

    private Paint mPaint;

    private int width;
    private int height;

    private int backgroundColor = Color.parseColor("#0082D7");

    // 放大镜与外部圆环
    private Path searchPath,
            circlePath,
            startingPath,
            searchingPath,
            endingPath;

    // 测量Path 并截取部分的工具
    private PathMeasure mMeasure;

    // 默认的动效周期 2s
    private static final int defaultDuration = 2000;

    // 动画
    private ValueAnimator mStartingAnimator,
            mSearchingAnimator,
            mEndingAnimator;

    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;

    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    // 用于控制动画状态转换
    private Handler mAnimatorHandler;

    // 判断是否已经搜索结束
    private boolean isOver = false;

    private int count = 0;

    public enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private State mCurrentState = State.NONE;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        initPath();
        initListener();
        initHandler();
        initAnimator();

        // 进入开始动画
        mCurrentState = State.STARTING;
        mStartingAnimator.start();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initPath() {
        searchPath = new Path();
        circlePath = new Path();
        startingPath = new Path();
        searchingPath = new Path();
        endingPath = new Path();

        mMeasure = new PathMeasure();
    }

    private void initListener() {

        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // getHandle发消息通知动画状态更新
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };
    }

    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurrentState) {

                    case STARTING:
                        // 从开始动画转换好搜索动画
                        isOver = false;
                        mCurrentState = State.SEARCHING;
                        mStartingAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;

                    case SEARCHING:
                        if (!isOver) { // 如果搜索未结束 则继续执行搜索动画
                            mSearchingAnimator.start();
                            Log.e(TAG, "RESTART");
                            count++;
                            if (count > 2) { // count大于2则进入结束状态
                                isOver = true;
                            }
                        } else { // 如果搜索已经结束 则进入结束动画
                            mCurrentState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;

                    case ENDING:
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE;
                        break;

                }
            }
        };
    }

    private void initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);

        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);

        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(width / 2, height / 2);
        canvas.drawColor(backgroundColor);
        startingPath.reset();
        searchingPath.reset();
        endingPath.reset();

        switch (mCurrentState) {
            case NONE:
                canvas.drawPath(searchPath, mPaint);
                break;

            case STARTING:
                mMeasure.setPath(searchPath, false);
                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue,
                        mMeasure.getLength(), startingPath, true);
                canvas.drawPath(startingPath, mPaint);
                break;

            case SEARCHING:
                mMeasure.setPath(circlePath, false);
                float stop = mMeasure.getLength() * mAnimatorValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
                mMeasure.getSegment(start, stop, searchingPath, true);
                canvas.drawPath(searchingPath, mPaint);
                break;

            case ENDING:
                mMeasure.setPath(searchPath, false);
                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue,
                        mMeasure.getLength(), endingPath, true);
                canvas.drawPath(endingPath, mPaint);
                break;

        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        searchPath.reset();
        circlePath.reset();

        float size = Math.min(width, height) * 0.8f;
        float insideRadius = size / 4;
        float outsideRadius = size / 2;

        // 注意,不要到360度,否则内部会自动优化,测量不能取到需要的数值
        RectF oval1 = new RectF(-insideRadius, -insideRadius, insideRadius, insideRadius); // 放大镜圆环
        searchPath.addArc(oval1, 45, 359.9f);

        RectF oval2 = new RectF(-outsideRadius, -outsideRadius, outsideRadius, outsideRadius); // 外部圆环
        circlePath.addArc(oval2, 45, -359.9f);

        float[] pos = new float[2];

        mMeasure.setPath(circlePath, false); // 放大镜把手的位置
        //获取circlePath起点坐标，保存到pos[]
        mMeasure.getPosTan(0, pos, null);

        searchPath.lineTo(pos[0], pos[1]); // 放大镜把手

        Log.i(TAG, "pos=" + pos[0] + ":" + pos[1]);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
