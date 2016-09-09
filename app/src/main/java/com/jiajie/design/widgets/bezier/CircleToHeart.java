package com.jiajie.design.widgets.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * CircleToHeart
 * Created by jiajie on 16/9/8.
 */
public class CircleToHeart extends View {

    private static final String TAG = "CircleToHeart";

    private static final float C = 0.551915024494f;// 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置

    private Paint mPaint;
    private int mCenterX, mCenterY;

    private PointF mCenter = new PointF(0, 0);
    private float mCircleRadius = 200;//圆的半径
    private float mDifference = mCircleRadius * C;//圆形的控制点与数据点的差值

    private float[] mData = new float[8];//顺时针记录绘制圆形的 四个[数据点]
    private float[] mControl = new float[16];//顺时针记录绘制圆形的 八个[控制点]

    private float mDuration = 1000;//变化总时长
    private float mCurrent = 0;//当前已进行时长
    private float mCount = 100;//将时长总共划分多少份
    private float mPiece = mDuration / mCount;//每一份的时长


    public CircleToHeart(Context context) {
        this(context, null);
    }

    public CircleToHeart(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        init();
    }

    private void init() {
        mCurrent = 0;

        //初始化数据点
        mData[0] = 0;
        mData[1] = mCircleRadius;

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = -mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;

        //初始化控制点
        mControl[0] = mData[0] + mDifference;
        mControl[1] = mData[1];

        mControl[2] = mData[2];
        mControl[3] = mData[3] + mDifference;

        mControl[4] = mData[2];
        mControl[5] = mData[3] - mDifference;

        mControl[6] = mData[4] + mDifference;
        mControl[7] = mData[5];

        mControl[8] = mData[4] - mDifference;
        mControl[9] = mData[5];

        mControl[10] = mData[6];
        mControl[11] = mData[7] - mDifference;

        mControl[12] = mData[6];
        mControl[13] = mData[7] + mDifference;

        mControl[14] = mData[0] - mDifference;
        mControl[15] = mData[1];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);//坐标系移动到canvas中央
        canvas.scale(1, -1);//翻转Y轴

//        drawCoordinateSystem(canvas);
//        drawAuxiliaryLine(canvas);
        drawBezier(canvas);

        mCurrent += mPiece;
        if (mCurrent < mDuration) {

            mData[1] -= 120 / mCount;

            mControl[7] += 80 / mCount;
            mControl[9] += 80 / mCount;

            mControl[4] -= 20 / mCount;
            mControl[10] += 20 / mCount;

            postInvalidateDelayed((long) mPiece);
        } else if (mCurrent == mDuration) {
            init();
            postInvalidateDelayed((long) mPiece);
        }
    }

    /**
     * 绘制贝塞尔曲线
     *
     * @param canvas canvas
     */
    private void drawBezier(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);

        Path path = new Path();
        path.moveTo(mData[0], mData[1]);

        path.cubicTo(mControl[0], mControl[1], mControl[2], mControl[3], mData[2], mData[3]);
        path.cubicTo(mControl[4], mControl[5], mControl[6], mControl[7], mData[4], mData[5]);
        path.cubicTo(mControl[8], mControl[9], mControl[10], mControl[11], mData[6], mData[7]);
        path.cubicTo(mControl[12], mControl[13], mControl[14], mControl[15], mData[0], mData[1]);

        canvas.drawPath(path, mPaint);

    }

    /**
     * 绘制坐标系
     *
     * @param canvas canvas
     */
    private void drawCoordinateSystem(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0, -2000, 0, 2000, paint);
        canvas.drawLine(-2000, 0, 2000, 0, paint);
    }

    /**
     * 绘制辅助线
     *
     * @param canvas canvas
     */
    private void drawAuxiliaryLine(Canvas canvas) {
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);

        for (int i = 0; i < mData.length; i += 2) {
            canvas.drawPoint(mData[i], mData[i + 1], mPaint);
        }

        for (int i = 0; i < mControl.length; i += 2) {
            canvas.drawPoint(mControl[i], mControl[i + 1], mPaint);
        }

        //绘制辅助线
        mPaint.setStrokeWidth(4);
        for (int i = 2, j = 2; i < 8; i += 2, j += 4) {
            canvas.drawLine(mData[i], mData[i + 1], mControl[j], mControl[j + 1], mPaint);
            canvas.drawLine(mData[i], mData[i + 1], mControl[j + 2], mControl[j + 3], mPaint);
        }
        canvas.drawLine(mData[0], mData[1], mControl[0], mControl[1], mPaint);
        canvas.drawLine(mData[0], mData[1], mControl[14], mControl[15], mPaint);
    }


}
