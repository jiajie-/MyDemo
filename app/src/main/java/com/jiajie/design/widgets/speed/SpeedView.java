package com.jiajie.design.widgets.speed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    //一般在直接New一个View的时候调用。
    public SpeedView(Context context) {
        this(context, null);
    }

    //一般在layout文件中使用的时候会调用，关于它的所有属性(包括自定义属性)都会包含在attrs中传递进来。
    public SpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
        super.onLayout(changed, left, top, right, bottom);//这里不需要，因为只是一个view
        /**
         * 确定布局的函数是onLayout，它用于确定子View的位置，在自定义ViewGroup中会用到，他调用的是子View的layout函数。
         * 在自定义ViewGroup中，onLayout一般是循环取出子View，然后经过计算得出各个子View位置的坐标值，然后用以下函数设置子View位置。
         * child.layout(l, t, r, b);
         */
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * Q: 为什么要测量View大小？
         * A: View的大小不仅由自身所决定，同时也会受到父控件的影响，为了我们的控件能更好的适应各种情况，一般会自己进行测量。
         * 测量View大小使用的是onMeasure函数，我们可以从onMeasure的两个参数中取出宽高的相关数据：
         * 从上面可以看出 onMeasure 函数中有 widthMeasureSpec 和 heightMeasureSpec 这两个 int 类型的参数， 毫无疑问他们是和宽高相关的， 但它们其实不是宽和高， 而是由宽、高和各自方向上对应的测量模式来合成的一个值：
         * 测量模式一共有三种， 被定义在 Android 中的 View 类的一个内部类View.MeasureSpec中：
         * 如果对View的宽高进行修改了，不要调用super.onMeasure(widthMeasureSpec,heightMeasureSpec);要调用setMeasuredDimension(widthsize,heightsize); 这个函数。
         */
//        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式
//        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = (width > height) ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 这个函数在视图大小发生改变时调用。
         Q: 在测量完View并使用setMeasuredDimension函数之后View的大小基本上已经确定了，那么为什么还要再次确定View的大小呢？

         A: 这是因为View的大小不仅由View本身控制，而且受父控件的影响，所以我们在确定View大小的时候最好使用系统提供的onSizeChanged回调函数。
         *
         */
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

    protected int getTextHeight(String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        return textBound.bottom - textBound.top;
    }

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
