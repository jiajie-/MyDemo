package com.jiajie.design.widgets.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * YinYang
 * Created by jiajie on 16/9/9.
 */
public class YinYangView extends View {

    private static final String TAG = "YinYangView";
    private static final int DEFAULT_SIZE = 300;

    private int centerX;
    private int centerY;

    private Paint mPaint;

    private Path p1, p2, p3, p4;

    public YinYangView(Context context) {
        this(context, null);
    }

    public YinYangView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YinYangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);

        p1 = new Path();
        p2 = new Path();
        p3 = new Path();
        p4 = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(centerX, centerY);

        drawCoordinateSystem(canvas);

        p1.addCircle(0, 0, 200, Path.Direction.CW);
        p2.addRect(0, -200, 200, 200, Path.Direction.CW);
        p3.addCircle(0, -100, 100, Path.Direction.CW);
        p4.addCircle(0, 100, 100, Path.Direction.CCW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            p1.op(p2, Path.Op.DIFFERENCE);
            p1.op(p3, Path.Op.UNION);
            p1.op(p4, Path.Op.DIFFERENCE);
        }
        canvas.drawPath(p1, mPaint);
    }

    /**
     * 绘制坐标系
     *
     * @param canvas canvas
     */
    private void drawCoordinateSystem(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0, -2000, 0, 2000, paint);
        canvas.drawLine(-2000, 0, 2000, 0, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(chooseDimension(widthMode, widthSize), chooseDimension(heightMode, heightSize));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
    }


    private int chooseDimension(final int mode, final int size) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
            default:
                return DEFAULT_SIZE;
        }
    }


}
