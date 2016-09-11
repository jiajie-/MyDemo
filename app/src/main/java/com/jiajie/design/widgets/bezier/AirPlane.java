package com.jiajie.design.widgets.bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.jiajie.design.R;

/**
 * AirPlane
 * Created by jiajie on 16/9/11.
 */
public class AirPlane extends View {

    private static final String TAG = "AirPlane";

    private static final int DEFAULT_SIZE = 300;

    private float centerX;
    private float centerY;

    private float currentValue = 0; // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private float[] pos;            // 当前点的实际位置
    private float[] tan;            // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap bitmap;          // 箭头图片
    private Matrix matrix;          // 矩阵,用于对图片进行一些操作
    private Path path;
    private PathMeasure measure;

    private Paint mPaint;


    public AirPlane(Context context) {
        this(context, null);
    }

    public AirPlane(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirPlane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//缩放图片
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_content_send, options);
        matrix = new Matrix();

        path = new Path();
        measure = new PathMeasure();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(centerX, centerY);

        path.reset();
        path.addCircle(0, 0, 200, Path.Direction.CW);

        measure.setPath(path, false);

        // 计算当前的位置在总长度上的比例[0,1]
        currentValue += 0.005;
        if (currentValue >= 1) {
            currentValue = 0;
        }

        // 以下两种实现效果一致

        // 获取当前位置的坐标以及趋势
//        measure.getPosTan(measure.getLength() * currentValue, pos, tan);
        // 获取当前位置的坐标以及趋势的矩阵
//        matrix.reset();
        // 计算图片旋转角度 atan2->弧度
//        float degrees = (float) Math.toDegrees(Math.atan2(tan[1], tan[0]));
        // 旋转图片
//        matrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        // 将图片绘制中心调整到与当前点重合
//        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);

        measure.getMatrix(measure.getLength() * currentValue, matrix,
                PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);

        // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)
        matrix.preTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);

        canvas.drawPath(path, mPaint);
        canvas.drawBitmap(bitmap, matrix, mPaint);

        invalidate();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
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
