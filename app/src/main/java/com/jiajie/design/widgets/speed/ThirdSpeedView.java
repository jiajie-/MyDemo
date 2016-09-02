package com.jiajie.design.widgets.speed;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.jiajie.design.R;

import java.util.Locale;
import java.util.Random;

/**
 * ThirdSpeedView
 * Created by jiajie on 16/8/30.
 */
public class ThirdSpeedView extends SpeedView {

    private static final String TAG = "ThirdSpeedView";

    private Path indicatorPath,
            markPath,
            smallMarkPath,
            pointPath;

    private Paint indicatorPaint,
            markPaint,
            smallMarkPaint,
            insideCirclePaint,
            radialGradientPaint,
            pointPaint,
            rotateSpeedPaint,
            backgroundCirclePaint,
            centerCirclePaint,
            speedBackgroundPaint;

    private TextPaint speedTextPaint,
            markTextPaint,
            rotateSpeedTextPaint;

    private RectF insideCircleRect,
            rotateSpeedRect,
            speedBackgroundRect;

    private RadialGradient radialGradient;

    private int speedBackgroundColor = Color.TRANSPARENT,
            speedTextColor = Color.WHITE,
            rotateTextColor = Color.RED,
            gradientStartColor = Color.parseColor("#000F4E61"),
            gradientEndColor = Color.parseColor("#FF08555C"),
            rotateSpeedColor1 = Color.parseColor("#19FFFFFF"),
            rotateSpeedColor2 = Color.parseColor("#FF0000"),
            smallMarkColor1 = Color.parseColor("#80FFFFFF"),
            smallMarkColor2 = Color.parseColor("#12C3F5"),
            insideCircleColor1 = Color.parseColor("#51FFFFFF"),
            insideCircleColor2 = Color.parseColor("#1BE4FE");

    private float mInsideCircleRadius,
            mMarkTextRadius,
            mWidth,
            mHeight,
            centerX,
            centerY;

    private static final float MIN_DEGREE = 150f,
            MAX_DEGREE = MIN_DEGREE + 240f;
    private static final float MIN_ROTATE_DEGREE = 0f,
            MAX_ROTATE_DEGREE = 120f;

    private static final float SMALL_MARK_DEGREE = (MAX_DEGREE - MIN_DEGREE) / 44f;
    private static final float MARK_DEGREE = (MAX_DEGREE - MIN_DEGREE) / 11f;
    private static final float INSIDE_CIRCLE_SCALE = 120f / 204f;
    private static final float MARK_SCALE = 166f / 204f;

    /** to rotate indicator */
    private float mDegree = MIN_DEGREE;
    private float mRotateDegree = MIN_ROTATE_DEGREE;
    private int mSpeed = 0;
    private int mRotateSpeed = 0;
    private boolean mWithEffects = true;
    private boolean mCanceled = false;
    private int mMaxRotateSpeed = 8000;

    private ValueAnimator speedAnimator,
            rotateSpeedAnimator,
            trembleAnimator;

    public ThirdSpeedView(Context context) {
        this(context, null);
    }

    public ThirdSpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    @Override
    protected void init() {//init path,paint,textPaint,rectF,animator
        //paths
        indicatorPath = new Path();//指针
        markPath = new Path();//大刻度
        smallMarkPath = new Path();//小刻度
        pointPath = new Path();//点

        //paints
        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        insideCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rotateSpeedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        radialGradientPaint = new Paint(insideCirclePaint);

        //text paints
        speedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        markTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        speedBackgroundPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        rotateSpeedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        //rectF
        insideCircleRect = new RectF();
        speedBackgroundRect = new RectF();
        rotateSpeedRect = new RectF();

        //set style
        pointPaint.setStyle(Paint.Style.STROKE);
        centerCirclePaint.setStyle(Paint.Style.STROKE);
        markPaint.setStyle(Paint.Style.STROKE);
        smallMarkPaint.setStyle(Paint.Style.STROKE);
        rotateSpeedPaint.setStyle(Paint.Style.STROKE);
        radialGradientPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //text align
        speedTextPaint.setTextAlign(Paint.Align.CENTER);
        rotateSpeedTextPaint.setTextAlign(Paint.Align.CENTER);

        //animator
        speedAnimator = ValueAnimator.ofFloat(0f, 1f);
        rotateSpeedAnimator = ValueAnimator.ofFloat(0f, 1f);
        trembleAnimator = ValueAnimator.ofFloat(0f, 1f);

        //set point effects,to draw a dash line
        pointPaint.setPathEffect(new DashPathEffect(new float[]{3f, 97f}, 0));
        //set radial effect
        radialGradientPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWithEffects(mWithEffects);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ThirdSpeedView, 0, 0);

        //default attributes
        setIndicatorColor(a.getColor(R.styleable.ThirdSpeedView_indicatorColor, Color.parseColor("#1BE4FE")));
        setCenterCircleColor(a.getColor(R.styleable.ThirdSpeedView_centerCircleColor, Color.parseColor("#51FFFFFF")));
        setMarkColor(a.getColor(R.styleable.ThirdSpeedView_markColor, Color.WHITE));
        setTextColor(a.getColor(R.styleable.ThirdSpeedView_textColor, Color.WHITE));
        setBackgroundCircleColor(a.getColor(R.styleable.ThirdSpeedView_backgroundCircleColor, Color.TRANSPARENT));
        setSpeedometerWidth(a.getDimension(R.styleable.ThirdSpeedView_speedometerWidth, getSpeedometerWidth()));
        setMaxSpeed(a.getInt(R.styleable.ThirdSpeedView_maxSpeed, 220));
        setWithTremble(a.getBoolean(R.styleable.ThirdSpeedView_withTremble, true));
        setWithBackgroundCircle(a.getBoolean(R.styleable.ThirdSpeedView_withBackgroundCircle, true));
        setSpeedTextSize(a.getDimension(R.styleable.ThirdSpeedView_speedTextSize, getSpeedTextSize()));
        //expand attributes
        speedTextColor = a.getColor(R.styleable.ThirdSpeedView_speedTextColor, speedTextColor);
        speedBackgroundColor = a.getColor(R.styleable.ThirdSpeedView_speedBackgroundColor, speedBackgroundColor);
        mWithEffects = a.getBoolean(R.styleable.ThirdSpeedView_withEffects, mWithEffects);
        String unit = a.getString(R.styleable.ThirdSpeedView_unit);
        a.recycle();
        setUnit((unit != null) ? unit : getUnit());
        setWithEffects(mWithEffects);
    }

    @Override
    protected void refreshDraw() {
        //color
        indicatorPaint.setColor(getIndicatorColor());
        markPaint.setColor(getMarkColor());
        smallMarkPaint.setColor(smallMarkColor1);//TODO 根据情况
        insideCirclePaint.setColor(insideCircleColor1);//TODO 根据情况
        pointPaint.setColor(insideCircleColor1);
        backgroundCirclePaint.setColor(getBackgroundCircleColor());
        centerCirclePaint.setColor(getCenterCircleColor());
        speedBackgroundPaint.setColor(speedBackgroundColor);
        speedTextPaint.setColor(speedTextColor);
        markTextPaint.setColor(getTextColor());
        rotateSpeedTextPaint.setColor(rotateTextColor);
        rotateSpeedPaint.setColor(rotateSpeedColor2);//TODO 根据情况
        //style
        insideCirclePaint.setStyle(Paint.Style.STROKE);
        //stroke width
        insideCirclePaint.setStrokeWidth(4f);
        pointPaint.setStrokeWidth(4f);
        //text size
        speedTextPaint.setTextSize(getSpeedTextSize());
        markTextPaint.setTextSize(getSpeedTextSize());
        rotateSpeedTextPaint.setTextSize(getSpeedTextSize());
    }

    @Override
    protected void calculateSize(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "calculateSize() called with: " + "w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");

        mWidth = w;
        mHeight = h;
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        mMarkTextRadius = centerY - getTextHeight("0", markTextPaint);//0,20,220 :the same height

        //inside circle
        mInsideCircleRadius = INSIDE_CIRCLE_SCALE * centerX;
        insideCircleRect.set(centerX - mInsideCircleRadius, centerY - mInsideCircleRadius,
                centerX + mInsideCircleRadius, centerY + mInsideCircleRadius);

        //radialGradient
        radialGradient = new RadialGradient(centerX, centerY, mInsideCircleRadius, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
        radialGradientPaint.setShader(radialGradient);

        //point path
        RectF pointRect = new RectF(insideCircleRect.left + 20, insideCircleRect.top + 20,
                insideCircleRect.right - 20, insideCircleRect.bottom - 20);
        pointPath.addArc(pointRect, MIN_DEGREE, MAX_DEGREE - MIN_DEGREE);

        //markPath markPaint
        float markHeight = mHeight / 20f;
        markPath.moveTo(centerX, 0f);
        markPath.lineTo(centerX, markHeight);
        markPath.moveTo(0f, 0f);
        markPaint.setStrokeWidth(markHeight / 4f);

        //smallMarkPath smallMarkPaint
        float smallMarkHeight = mHeight / 40f;
        smallMarkPath.moveTo(centerX, smallMarkHeight);
        smallMarkPath.lineTo(centerX, smallMarkHeight * 2);
        smallMarkPath.moveTo(0f, 0f);
        smallMarkPaint.setStrokeWidth(smallMarkHeight / 3f);

        //indicatorPath
        float indicatorHeight = mHeight / 64f;
        float indicatorLength = mHeight * 0.5f;
        indicatorPath.moveTo(centerX, markHeight);//real indicator length = indicatorLength-markHeight
        indicatorPath.lineTo(centerX - indicatorHeight, indicatorLength);
        indicatorPath.lineTo(centerX + indicatorHeight, indicatorLength);
        RectF indicatorRectF = new RectF(centerX - indicatorHeight, indicatorLength - indicatorHeight,
                centerY + indicatorHeight, indicatorLength + indicatorHeight);
        indicatorPath.addArc(indicatorRectF, 0f, 180f);
        indicatorPath.moveTo(0f, 0f);

        //rotateSpeedRectF rotateSpeedPaint
        float rotateWidth = mHeight / 65f;
        rotateSpeedRect.set(mWidth / 40f, mHeight / 40f, mWidth - rotateWidth, mHeight - rotateWidth);
        rotateSpeedPaint.setStrokeWidth(rotateWidth);
    }

    @Override
    protected void drawStaticSpeedView(Canvas canvas) {
        Log.d(TAG, "drawStaticSpeedView: ");
        float backgroundCircleRadius = mWidth / 2f;

        //background circle
        if (isWithBackgroundCircle()) {
            canvas.drawCircle(centerX, centerY, backgroundCircleRadius, backgroundCirclePaint);
        }

        //mark text
        int scaleValue = 0;
        for (int i = 0; i < 12; i++) {
            double angle = MARK_DEGREE * i + 60f;
            String value = String.valueOf(scaleValue);
            float textWidth = markTextPaint.measureText(value);//20,40,60
            float scaleValuePositionX = (float) (centerX - Math.sin(Math.toRadians(angle)) * mMarkTextRadius);
            float scaleValuePositionY = (float) (centerY + Math.cos(Math.toRadians(angle)) * mMarkTextRadius);
            canvas.drawText(value, scaleValuePositionX - textWidth / 2,
                    scaleValuePositionY + getTextHeight(value, markTextPaint) / 2, markTextPaint);
            scaleValue += 20;
        }

        //inside circle
        canvas.drawArc(insideCircleRect, MIN_DEGREE, (MAX_DEGREE - MIN_DEGREE),
                false, insideCirclePaint);

        //points
        canvas.drawPath(pointPath, pointPaint);

        //scale down canvas
        canvas.scale(MARK_SCALE, MARK_SCALE, centerX, centerY);

        //mark
        canvas.save();
        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += MARK_DEGREE) {
            canvas.drawPath(markPath, markPaint);
            canvas.rotate(MARK_DEGREE, centerX, centerY);
        }
        canvas.restore();

        //small mark
        canvas.save();
        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += SMALL_MARK_DEGREE) {
            canvas.drawPath(smallMarkPath, smallMarkPaint);
            canvas.rotate(SMALL_MARK_DEGREE, centerX, centerY);
        }
        canvas.restore();

    }

    @Override
    protected void drawActiveSpeedView(Canvas canvas) {
        float indicatorCircleRadius = mWidth / 45f;

        //radial area
        if (mDegree - MIN_DEGREE != 0) {
            canvas.drawArc(insideCircleRect, MIN_DEGREE, mDegree - MIN_DEGREE, true, radialGradientPaint);
        }

        //indicator
        canvas.save();
        canvas.rotate(mDegree + 90f, centerX, centerY);
        canvas.drawPath(indicatorPath, indicatorPaint);
        canvas.restore();
        //indicator circle
        canvas.drawCircle(centerX, centerY, indicatorCircleRadius, centerCirclePaint);

        //speed text
        String speedText = String.format(Locale.getDefault(), "%.1f",
                (mDegree - MIN_DEGREE) * getMaxSpeed() / (MAX_DEGREE - MIN_DEGREE));
//        speedBackgroundRect.set(centerX - (speedTextPaint.measureText(speedText) / 2f) - 5,//left
//                insideCircleRect.bottom - speedTextPaint.getTextSize(),//top
//                centerX + (speedTextPaint.measureText(speedText) / 2f) + 5,//right
//                centerY * 1.4f + 4);//bottom
//        canvas.drawRect(speedBackgroundRect, speedBackgroundPaint);
        canvas.drawText(speedText, centerX, centerY * 1.2f, speedTextPaint);
        canvas.drawText(getUnit(), centerX, centerY * 1.3f, speedTextPaint);

        //rotate speed text
        String rotateSpeedText = String.format(Locale.getDefault(), "%d",
                (int) ((mRotateDegree - MIN_ROTATE_DEGREE) * mMaxRotateSpeed / (MAX_ROTATE_DEGREE - MIN_ROTATE_DEGREE))) + " RPM";
        canvas.drawText(rotateSpeedText, centerX, centerY * 1.7f, rotateSpeedTextPaint);

        canvas.scale(MARK_SCALE, MARK_SCALE, centerX, centerY);
        //rotate speed arc
        canvas.drawArc(rotateSpeedRect, 30f, mRotateDegree, false, rotateSpeedPaint);
    }

    @Override
    public void speedToDef() {
        speedTo(mSpeed, 2000);
    }

    @Override
    public void speedTo(int speed) {
        speedTo(speed, 2000);
    }

    @Override
    public void speedTo(int speed, long moveDuration) {
        //0~maxSpeed
        speed = (speed > getMaxSpeed()) ? getMaxSpeed() : (speed < 0) ? 0 : speed;
        this.mSpeed = speed;

        //135~270
        float newDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;

        if (newDegree == mDegree) return;

        //取消上次动画效果
        cancel();

        speedAnimator = ValueAnimator.ofFloat(mDegree, newDegree);
        speedAnimator.setInterpolator(new DecelerateInterpolator());
        speedAnimator.setDuration(moveDuration);
        speedAnimator.addUpdateListener(speedListener);
        speedAnimator.addListener(animatorListener);
        speedAnimator.start();
    }

    @Override
    public void setWithTremble(boolean withTremble) {
        super.setWithTremble(withTremble);
        tremble();
    }

    @Override
    public void speedPercentTo(int percent) {
        percent = (percent > 100) ? 100 : (percent < 0) ? 0 : percent;
        speedTo(percent * getMaxSpeed() / 100);
    }

    @Override
    public int getSpeed() {
        return mSpeed;
    }

    @Override
    public int getPercentSpeed() {
        return mSpeed * 100 / getMaxSpeed();
    }

    private void cancel() {
        cancelSpeedMove();
        cancelTremble();
    }

    private void cancelTremble() {
        mCanceled = true;
        trembleAnimator.cancel();
        mCanceled = false;
    }

    private void cancelSpeedMove() {
        mCanceled = true;
        speedAnimator.cancel();
        mCanceled = false;
    }

    public void rotateSpeedTo(int rotateSpeed) {
        rotateSpeedTo(rotateSpeed, 2000);
    }

    public void rotateSpeedTo(int rotateSpeed, long moveDuration) {
        //0~maxRotateSpeed
        rotateSpeed = (rotateSpeed > mMaxRotateSpeed) ? mMaxRotateSpeed : (rotateSpeed < 0) ? 0 : rotateSpeed;
        this.mRotateSpeed = rotateSpeed;

        //30~150
        float newDegree = (float) rotateSpeed * (MAX_ROTATE_DEGREE - MIN_ROTATE_DEGREE) /
                mMaxRotateSpeed + MIN_ROTATE_DEGREE;

        if (newDegree == mRotateDegree) return;

        //取消上次动画效果
        rotateSpeedAnimator.cancel();

        rotateSpeedAnimator = ValueAnimator.ofFloat(mRotateDegree, newDegree);
        rotateSpeedAnimator.setInterpolator(new DecelerateInterpolator());
        rotateSpeedAnimator.setDuration(moveDuration);
        rotateSpeedAnimator.addUpdateListener(rotateListener);
        rotateSpeedAnimator.start();
    }

    public void setWithEffects(boolean withEffects) {
        this.mWithEffects = withEffects;
        if (mWithEffects) {
            indicatorPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
            markPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));
            speedBackgroundPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));
            centerCirclePaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        } else {
            indicatorPaint.setMaskFilter(null);
            markPaint.setMaskFilter(null);
            speedBackgroundPaint.setMaskFilter(null);
            centerCirclePaint.setMaskFilter(null);
        }
        invalidate();
    }

    //颤抖动画
    private void tremble() {
        cancelTremble();
        if (!isWithTremble()) return;

        Random random = new Random();
        float mad = 4 * random.nextFloat() * ((random.nextBoolean()) ? -1 : 1);
        float originalDegree = (float) mSpeed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;
        mad = (originalDegree + mad > MAX_DEGREE) ? MAX_DEGREE - originalDegree
                : (originalDegree + mad < MIN_DEGREE) ? MIN_DEGREE - originalDegree : mad;
        trembleAnimator = ValueAnimator.ofFloat(mDegree, originalDegree + mad);
        trembleAnimator.setInterpolator(new DecelerateInterpolator());
        trembleAnimator.setDuration(1000);
        trembleAnimator.addUpdateListener(trembleListener);
        trembleAnimator.addListener(animatorListener);
        trembleAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener rotateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mRotateDegree = (float) rotateSpeedAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private ValueAnimator.AnimatorUpdateListener speedListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mDegree = (float) speedAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private ValueAnimator.AnimatorUpdateListener trembleListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mDegree = (float) trembleAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!mCanceled) tremble();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

}
