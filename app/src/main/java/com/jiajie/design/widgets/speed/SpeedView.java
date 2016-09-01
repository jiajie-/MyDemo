package com.jiajie.design.widgets.speed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * A Base SpeedView
 * Created by jiajie on 16/8/29.
 */
abstract public class SpeedView extends View {

    private static final String TAG = "SpeedView";

    //速度计
    private float speedometerWidth = dp2px(30f);
    //速度文字大小
    private float speedTextSize = dp2px(18f);
    //表盘文字
    private String unit = "Km/h";
    //是否带抖动动画、背景圆圈
    private boolean withTremble = true,
            withBackgroundCircle = true;
    //最高速度
    private int maxSpeed = 100;

    //color
    private int indicatorColor = Color.RED//指针颜色
            , centerCircleColor = Color.DKGRAY//中间圆颜色
            , markColor = Color.WHITE//速度位置标记颜色
            , lowSpeedColor = Color.GREEN//低速颜色
            , mediumSpeedColor = Color.YELLOW//中速颜色
            , highSpeedColor = Color.RED//高速颜色
            , textColor = Color.BLACK//文字颜色
            , backgroundCircleColor = Color.TRANSPARENT;//背景圆颜色

    protected Bitmap speedViewBitmap;
    protected Paint speedViewBitmapPaint;

    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initSpeedViewPaint();
    }

    public void initSpeedViewPaint() {
        speedViewBitmapPaint = new Paint();
        speedViewBitmapPaint.setStrokeJoin(Paint.Join.ROUND); //平滑效果
        speedViewBitmapPaint.setDither(true); //防抖动
        speedViewBitmapPaint.setAntiAlias(true);  //消除锯齿
        speedViewBitmapPaint.setStyle(Paint.Style.STROKE);  //设置样式
        speedViewBitmapPaint.setFilterBitmap(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //refresh draw parameters
        refreshDraw();
        //draw static value
        if (speedViewBitmap != null) {
            canvas.drawBitmap(speedViewBitmap, 0, 0, speedViewBitmapPaint);
        }
        //draw active value
        drawActiveSpeedView(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = (width > height) ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //refresh draw parameters
        refreshDraw();
        //calculate size change
        calculateSize(w, h, oldw, oldh);
        //draw static speed view
        if (speedViewBitmap != null) {
            speedViewBitmap.recycle();
        }
        speedViewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(speedViewBitmap);
        drawStaticSpeedView(canvas);
    }

    protected float dp2px(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * init Paint,TextPaint,Path,RectF etc...
     */
    abstract protected void init();

    /**
     * refresh draw parameters...
     */
    abstract protected void refreshDraw();

    /**
     * <p>calculate view size to correct {@code int},</p>
     * <p>set Path,Paint,RectF size etc ...</p>
     *
     * @param w    width
     * @param h    height
     * @param oldw old width
     * @param oldh old height
     */
    abstract protected void calculateSize(int w, int h, int oldw, int oldh);

    /**
     * draw a static speed view into bitmap,reduce canvas draw work
     *
     * @param canvas canvas to draw static speed view
     */
    abstract protected void drawStaticSpeedView(Canvas canvas);

    /**
     * draw a active speed view in {@link View#onDraw(Canvas canvas)
     *
     * @param canvas canvas to draw active speed view
     */
    abstract protected void drawActiveSpeedView(Canvas canvas);

    abstract public void speedToDef();

    /**
     * <p>change speed to correct {@code int},</p>
     * <p>if {@code speed > maxSpeed} speed will be maxSpeed,</p>
     * if {@code speed < 0} speed will be 0.
     *
     * @param speed correct speed to move.
     */
    abstract public void speedTo(int speed);

    /**
     * <p>change speed to correct {@code int},</p>
     * <p>if {@code speed > maxSpeed} speed will be maxSpeed,</p>
     * if {@code speed < 0} speed will be 0.
     *
     * @param speed        correct speed to move.
     * @param moveDuration The length of the animation, in milliseconds.
     *                     This value cannot be negative.
     */
    abstract public void speedTo(int speed, long moveDuration);

    /**
     * change speed to percent value.
     *
     * @param percent percent value to change, should be between [0,100].
     */
    abstract public void speedPercentTo(int percent);

    /**
     * what speed is now
     *
     * @return the last speed which you set by {@link #speedTo(int)}
     * or {@link #speedTo(int, long)} or {@link #speedPercentTo(int)}.
     */
    abstract public int getSpeed();

    /**
     * what percent speed is now
     *
     * @return the last speed which you set by {@link #speedTo(int)}
     * or {@link #speedTo(int, long)} or {@link #speedPercentTo(int)}.
     */
    abstract public int getPercentSpeed();

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getCenterCircleColor() {
        return centerCircleColor;
    }

    public void setCenterCircleColor(int centerCircleColor) {
        this.centerCircleColor = centerCircleColor;
        invalidate();
    }

    public int getMarkColor() {
        return markColor;
    }

    public void setMarkColor(int markColor) {
        this.markColor = markColor;
        invalidate();
    }

    public int getLowSpeedColor() {
        return lowSpeedColor;
    }

    public void setLowSpeedColor(int lowSpeedColor) {
        this.lowSpeedColor = lowSpeedColor;
        invalidate();
    }

    public int getMediumSpeedColor() {
        return mediumSpeedColor;
    }

    public void setMediumSpeedColor(int mediumSpeedColor) {
        this.mediumSpeedColor = mediumSpeedColor;
        invalidate();
    }

    public int getHighSpeedColor() {
        return highSpeedColor;
    }

    public void setHighSpeedColor(int highSpeedColor) {
        this.highSpeedColor = highSpeedColor;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public int getBackgroundCircleColor() {
        return backgroundCircleColor;
    }

    public void setBackgroundCircleColor(int backgroundCircleColor) {
        this.backgroundCircleColor = backgroundCircleColor;
        invalidate();
    }

    public float getSpeedTextSize() {
        return speedTextSize;
    }

    public void setSpeedTextSize(float speedTextSize) {
        this.speedTextSize = speedTextSize;
        invalidate();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        invalidate();
    }

    public boolean isWithTremble() {
        return withTremble;
    }

    public void setWithTremble(boolean withTremble) {
        this.withTremble = withTremble;
    }

    public float getSpeedometerWidth() {
        return speedometerWidth;
    }

    public void setSpeedometerWidth(float speedometerWidth) {
        this.speedometerWidth = speedometerWidth;
        invalidate();
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        if (maxSpeed <= 0)
            return;
        this.maxSpeed = maxSpeed;
        speedToDef();
        invalidate();
    }

    public boolean isWithBackgroundCircle() {
        return withBackgroundCircle;
    }

    public void setWithBackgroundCircle(boolean withBackgroundCircle) {
        this.withBackgroundCircle = withBackgroundCircle;
        invalidate();
    }

}
