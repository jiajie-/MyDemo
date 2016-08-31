package com.jiajie.design.widgets.speed;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * A Base SpeedView
 * Created by jiajie on 16/8/29.
 */
abstract public class SpeedView extends View {

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
    private int maxSpeed = 220;

    //color
    private int indicatorColor = Color.RED//指针颜色
            , centerCircleColor = Color.DKGRAY//中间圆颜色
            , markColor = Color.WHITE//速度位置标记颜色
            , lowSpeedColor = Color.GREEN//低速颜色
            , mediumSpeedColor = Color.YELLOW//中速颜色
            , highSpeedColor = Color.RED//高速颜色
            , textColor = Color.BLACK//文字颜色
            , backgroundCircleColor = Color.TRANSPARENT;//背景圆颜色


    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected float dp2px(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    abstract public void speedToDef();

    /**
     * <p>change speed to correct {@code int},</p>
     * <p>if {@code speed > maxSpeed} speed will be maxSpeed,</p>
     * if {@code speed < 0} speed will be 0.
     * @param speed correct speed to move.
     */
    abstract public void speedTo(int speed);

    /**
     * <p>change speed to correct {@code int},</p>
     * <p>if {@code speed > maxSpeed} speed will be maxSpeed,</p>
     * if {@code speed < 0} speed will be 0.
     * @param speed correct speed to move.
     * @param moveDuration The length of the animation, in milliseconds.
     *                     This value cannot be negative.
     */
    abstract public void speedTo(int speed, long moveDuration);

    /**
     * change speed to percent value.
     * @param percent percent value to change, should be between [0,100].
     */
    abstract public void speedPercentTo(int percent);

    /**
     * what speed is now
     * @return the last speed which you set by {@link #speedTo(int)}
     * or {@link #speedTo(int, long)} or {@link #speedPercentTo(int)}.
     */
    abstract public int getSpeed();

    /**
     * what percent speed is now
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
