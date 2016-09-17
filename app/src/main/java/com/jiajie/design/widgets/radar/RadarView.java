package com.jiajie.design.widgets.radar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.jiajie.design.R;

/**
 * RadarView
 * Created by jiajie on 16/9/6.
 */
public class RadarView extends View {

    private static final String TAG = "RadarView";

    private static final int DEFAULT_SIZE = 300;
    private static final int DEFAULT_COUNT = 8;
    private static final float DEFAULT_MAX_VALUE = 10f;

    private int count = DEFAULT_COUNT;
    private float maxValue = DEFAULT_MAX_VALUE;
    private int textColor = Color.BLACK;
    private float textSize = 18f;
    private float perRadians = (float) (Math.PI * 2 / count);
    private RadarItem item;

    private float radius;
    private int centerX;
    private int centerY;

    private Paint mainPaint,
            valuePaint,
            textPaint;

    private Path polygonPath,
            regionPath,
            linePath;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttributeSet(context, attrs);
    }

    public void setRadarItem(RadarItem item) {
        if (item == null)
            throw new IllegalArgumentException("RadarItem should not be null!");
        if (item.getList() == null)
            throw new IllegalArgumentException("RadarItem list should not be null!");
        this.item = item;
        this.count = item.getList().size();
        this.perRadians = (float) (Math.PI * 2 / this.count);
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            invalidate();
        } else {
            postInvalidate();
        }
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
        radius = Math.min(h, w) / 2 * 0.6f;
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (item == null) return;
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    private void init() {
        //mainPaint
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(Color.BLACK);
        mainPaint.setStrokeWidth(2);
        mainPaint.setStyle(Paint.Style.STROKE);

        //textPaint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dp2px(textSize));

        //valuePaint
        valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //path
        polygonPath = new Path();
        regionPath = new Path();
        linePath = new Path();
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RadarView, 0, 0);
        textColor = a.getColor(R.styleable.RadarView_radarTextColor, textColor);
        textSize = a.getDimension(R.styleable.RadarView_radarTextSize, textSize);
        maxValue = a.getFloat(R.styleable.RadarView_radarMaxValue, maxValue);
        a.recycle();
    }

    /**
     * 绘制正多边形
     *
     * @param canvas canvas
     */
    private void drawPolygon(Canvas canvas) {
        float r = radius / (count - 1); //蜘蛛丝之间的间距
        for (int i = 1; i < count; i++) { //中心点不用绘制
            float currentRadius = r * i; //当前半径
            polygonPath.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    polygonPath.moveTo(centerX + currentRadius, centerY);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (centerX + currentRadius * Math.cos(perRadians * j));
                    float y = (float) (centerY + currentRadius * Math.sin(perRadians * j));
                    polygonPath.lineTo(x, y);
                }
            }
            polygonPath.close();
            canvas.drawPath(polygonPath, mainPaint);
        }
    }

    /**
     * 绘制从中心到末端的直线
     *
     * @param canvas canvas
     */
    private void drawLines(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            linePath.reset();
            linePath.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(perRadians * i));
            float y = (float) (centerY + radius * Math.sin(perRadians * i));
            linePath.lineTo(x, y);
            canvas.drawPath(linePath, mainPaint);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float centerToBaseline = -fontMetrics.top / 2 - fontMetrics.bottom / 2;

        canvas.save();
        canvas.scale(1.1f, 1.1f, centerX, centerY);
        for (int i = 0; i < count; i++) {
            float currentRadians = perRadians * i;
            String text = item.getList().get(i).name;
            float textWidth = textPaint.measureText(text);
            float x = centerX + (float) ((radius + textWidth / 2) * Math.cos(currentRadians));
            float y = centerY + (float) ((radius + textWidth / 2) * Math.sin(currentRadians) + centerToBaseline);
            canvas.drawText(text, x, y, textPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制区域
     *
     * @param canvas canvas
     */
    private void drawRegion(Canvas canvas) {
        regionPath.reset();
        valuePaint.setAlpha(255);
        for (int i = 0; i < count; i++) {
            int value = item.getList().get(i).value;
            double percent = value / maxValue;
            float x = (float) (centerX + radius * Math.cos(perRadians * i) * percent);
            float y = (float) (centerY + radius * Math.sin(perRadians * i) * percent);
            if (i == 0) {
                regionPath.moveTo(x, centerY);
            } else {
                regionPath.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 5, valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(regionPath, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(regionPath, valuePaint);
    }

    protected float dp2px(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
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
