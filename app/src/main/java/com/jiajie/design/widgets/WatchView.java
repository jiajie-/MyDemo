package com.jiajie.design.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * WatchView
 * Created by jiajie on 16/8/11.
 */
public class WatchView extends View implements Runnable {

    private static final String TAG = "WatchView";

    private static final double ROUND = 2d * Math.PI;
    private static final double QUARTER = 1d / 4d;
    private static final double SIX = 1d / 12d;

    private final Calendar calendar = new GregorianCalendar();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Paint mSecond = new Paint();
    private final Paint mMinute = new Paint();
    private final Paint mHour = new Paint();
    private final Paint mText = new Paint();
    private final Path mTextPath = new Path();
    RectF rectF;
    private Bitmap background;

    public WatchView(Context context) {
        this(context, null);
    }

    public WatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaints(mSecond, Color.RED, 2f);
        initPaints(mMinute, Color.BLACK, 3f);
        initPaints(mHour, Color.BLUE, 5f);

        mText.setTextAlign(Paint.Align.CENTER);
        mText.setTextSize(30);
        mText.setAntiAlias(true);
        mText.setColor(Color.BLACK);
        mText.setStyle(Paint.Style.FILL);
        mText.setStrokeWidth(2f);
        rectF = new RectF();

    }

    private void initPaints(Paint paint, int color, float widthDp) {
        float density = getResources().getDisplayMetrics().density;
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(widthDp * density);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize < heightSize) {
            setMeasuredDimension(widthSize, widthSize);
            rectF.set(0, 0, widthSize, widthSize);
        } else {
            setMeasuredDimension(heightSize, heightSize);
            rectF.set(0, 0, heightSize, heightSize);
        }
        mTextPath.addCircle(widthSize / 2f, heightSize / 2f, widthSize / 2f, Path.Direction.CCW);
//        mTextPath.addOval(rectF, Path.Direction.CCW);


        Log.d(TAG, "onMeasure: widthSize:" + widthSize + " heightSize:" + heightSize);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");

//        if (background != null) {
//            background.recycle();
//        }
//        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas=new Canvas(background);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //圆心的坐标
        int x = canvas.getWidth() / 2;
        int y = canvas.getHeight() / 2;

        //半径
        int radius = Math.min(x, y);

        // 为了让秒针连续转动，所以秒针的角度肯定不是从「秒」这个整数里来
        // 刚好秒针走一圈是 1 分钟，那么，就用分乘以「一圈（2π）」就是秒针要走的弧度数了
        float mills = calendar.get(Calendar.MILLISECOND) / 1000f;
        float second = (calendar.get(Calendar.SECOND) + mills) / 60f;
        float minute = (calendar.get(Calendar.MINUTE) + second) / 60f;
        float hour = (calendar.get(Calendar.HOUR) + minute) / 12f;

        drawPointer(canvas, mHour, x, y, radius * 0.5f, hour);
        drawPointer(canvas, mMinute, x, y, radius * 0.7f, minute);
        drawPointer(canvas, mSecond, x, y, radius * 0.9f, second);

        mText.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mTextPath, mText);//画圆
        mText.setStyle(Paint.Style.FILL);

        for (int i = 0; i < 13; i++) {
            double currentRadians = (SIX * i) * ROUND;
            canvas.drawText(String.valueOf(i),
                    x - (float) Math.sin(currentRadians) * radius,
                    y - (float) Math.cos(currentRadians) * radius - 20f,
                    mText);
        }

    }

    private void drawPointer(Canvas canvas, Paint paint, float x, float y, float length, float round) {
        // 三角函数的坐标轴是以 3 点方向为 0 的，所以记得要减去四分之一个圆周哦
        double currentRadians = (round - QUARTER) * ROUND;

        canvas.drawLine(x, y,
                x + (float) Math.cos(currentRadians) * length,
                y + (float) Math.sin(currentRadians) * length,
                paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.post(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void run() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        invalidate();
        handler.postDelayed(this, 1000 / 60);
    }
}
