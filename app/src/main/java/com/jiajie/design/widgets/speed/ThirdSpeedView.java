package com.jiajie.design.widgets.speed;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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

    private int speedBackgroundColor = Color.TRANSPARENT,
            speedTextColor = Color.WHITE,
            rotateTextColor = Color.RED,
            rotateSpeedColor1 = Color.parseColor("#19FFFFFF"),
            rotateSpeedColor2 = Color.parseColor("#FF0000"),
            smallMarkColor1 = Color.parseColor("#80FFFFFF"),
            smallMarkColor2 = Color.parseColor("#12C3F5"),
            insideCircleColor1 = Color.parseColor("#51FFFFFF"),
            insideCircleColor2 = Color.parseColor("#1BE4FE");

    private boolean canceled = false;
    private final float MIN_DEGREE = 150f,
            MAX_DEGREE = MIN_DEGREE + 240f;
    private final float MIN_ROTATE_DEGREE = 0f,
            MAX_ROTATE_DEGREE = 120f;
    /** to rotate indicator */
    private float degree = MIN_DEGREE;
    private float rotateDegree = MIN_ROTATE_DEGREE;
    private int speed = 0;
    private int rotateSpeed = 0;
    private ValueAnimator speedAnimator, rotateSpeedAnimator,
            trembleAnimator;

    protected Bitmap dashboardBitmap;
    protected Paint dashboardBitmapPaint;


    private boolean withEffects = true;

    public ThirdSpeedView(Context context) {
        super(context);
        init();
    }

    public ThirdSpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttributeSet(context, attrs);
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
        Log.d(TAG, "onSizeChanged: ");

        initDraw();


        //inside circle
        float scale = 84 / 204f;
        insideCircleRect.set((scale) * w / 2f, scale * h / 2f,
                w - scale * w / 2f, h - scale * h / 2f);

        //point path
        RectF pointRect = new RectF(insideCircleRect.left + 20, insideCircleRect.top + 20,
                insideCircleRect.right - 20, insideCircleRect.bottom - 20);
        pointPath.addArc(pointRect, MIN_DEGREE, MAX_DEGREE - MIN_DEGREE);

        //indicatorPath
        float indicatorWidth = w / 64f;
        indicatorPath.moveTo(w / 2f, 0f);
        indicatorPath.lineTo(w / 2f - indicatorWidth, h * 3f / 5f);
        indicatorPath.lineTo(w / 2f + indicatorWidth, h * 3f / 5f);
        RectF indicatorRectF = new RectF(w / 2f - indicatorWidth, h * 3f / 5f - indicatorWidth,
                w / 2f + indicatorWidth, h * 3f / 5f + indicatorWidth);
        indicatorPath.addArc(indicatorRectF, 0f, 180f);
        indicatorPath.moveTo(0f, 0f);

        //markPath markPaint
        float markHeight = h / 20f;
        markPath.moveTo(w / 2f, 0f);
        markPath.lineTo(w / 2f, markHeight);
        markPath.moveTo(0f, 0f);
        markPaint.setStrokeWidth(markHeight / 4f);

        //smallMarkPath smallMarkPaint
        float smallMarkHeight = h / 40f;
        smallMarkPath.moveTo(w / 2f, smallMarkHeight);
        smallMarkPath.lineTo(w / 2f, smallMarkHeight * 2);
        smallMarkPath.moveTo(0f, 0f);
        smallMarkPaint.setStrokeWidth(smallMarkHeight / 3f);

        //rotateSpeedRectF rotateSpeedPaint
        float rotateWidth = h / 65f;
        rotateSpeedRect.set(h / 40f, h / 40f, w - smallMarkHeight, h - smallMarkHeight);
        rotateSpeedPaint.setStrokeWidth(rotateWidth);

        if (dashboardBitmap != null) {
            dashboardBitmap.recycle();
        }
        dashboardBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dashboardBitmap);
        drawIntoBitmapForCache(canvas);

    }

    private void drawIntoBitmapForCache(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        float centerX = width / 2f;
        float centerY = height / 2f;

        float backgroundCircleRadius = width / 2f;
        float indicatorCircleRadius = width / 55f;

        //background circle
        if (isWithBackgroundCircle()) {
            canvas.drawCircle(centerX, centerY, backgroundCircleRadius, backgroundCirclePaint);
        }
        Log.d(TAG, "drawIntoBitmapForCache: background circle");

        //inside circle
        canvas.drawArc(insideCircleRect, MIN_DEGREE, (MAX_DEGREE - MIN_DEGREE),
                false, insideCirclePaint);
        Log.d(TAG, "drawIntoBitmapForCache: inside circle");


        //points
        canvas.drawPath(pointPath, pointPaint);
        Log.d(TAG, "drawIntoBitmapForCache: points");


        //mark
        canvas.save();
        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
        float markPer = (MAX_DEGREE - MIN_DEGREE) / 11f;
        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += markPer) {
            canvas.drawPath(markPath, markPaint);
            canvas.rotate(markPer, centerX, centerY);
        }
        canvas.restore();

        //small mark
        canvas.save();
        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
        float smallMarkPer = (MAX_DEGREE - MIN_DEGREE) / 44f;
        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += smallMarkPer) {
            canvas.drawPath(smallMarkPath, smallMarkPaint);
            canvas.rotate(smallMarkPer, centerX, centerY);
        }
        canvas.restore();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDraw();

        //如果仪表盘已经存在了，则直接绘制出来
        if (null != dashboardBitmap) {
            Log.e(TAG, "onDraw: 如果仪表盘已经存在了，则直接绘制出来");
            canvas.drawBitmap(dashboardBitmap, 0, 0, dashboardBitmapPaint);
        }

        int width = getWidth();
        int height = getHeight();

        float centerX = width / 2f;
        float centerY = height / 2f;

        float backgroundCircleRadius = width / 2f;
        float indicatorCircleRadius = width / 55f;

        //background circle
//        if (isWithBackgroundCircle()) {
//            canvas.drawCircle(centerX, centerY, backgroundCircleRadius, backgroundCirclePaint);
//        }

        //scale
        int scaleValue = 0;
        for (int i = 0; i < 12; i++) {
            double angle = 240f / 11f * i + 60f;
            String value = String.valueOf(scaleValue);
            float scaleValuePositionX = (float) (centerX - Math.sin(Math.toRadians(angle)) * width / 2);
            float scaleValuePositionY = (float) (centerY + Math.cos(Math.toRadians(angle)) * height / 2);
            canvas.drawText(value, scaleValuePositionX,
                    scaleValuePositionY, markTextPaint);
            scaleValue += 20;
        }

        //inside circle
//        canvas.drawArc(insideCircleRect, MIN_DEGREE, (MAX_DEGREE - MIN_DEGREE),
//                false, insideCirclePaint);

        //points
//        canvas.drawPath(pointPath, pointPaint);

        //rotate speed arc
        canvas.drawArc(rotateSpeedRect, 30f, rotateDegree, false, rotateSpeedPaint);

//        //mark
//        canvas.save();
//        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
//        float markPer = (MAX_DEGREE - MIN_DEGREE) / 11f;
//        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += markPer) {
//            canvas.drawPath(markPath, markPaint);
//            canvas.rotate(markPer, centerX, centerY);
//        }
//        canvas.restore();
//
//        //small mark
//        canvas.save();
//        canvas.rotate(MIN_DEGREE + 90f, centerX, centerY);
//        float smallMarkPer = (MAX_DEGREE - MIN_DEGREE) / 44f;
//        for (float i = MIN_DEGREE; i <= MAX_DEGREE; i += smallMarkPer) {
//            canvas.drawPath(smallMarkPath, smallMarkPaint);
//            canvas.rotate(smallMarkPer, centerX, centerY);
//        }
//        canvas.restore();

        //indicator
        canvas.save();
        canvas.rotate(90f + degree, centerX, centerY);
        canvas.drawPath(indicatorPath, indicatorPaint);
        canvas.restore();
        //indicator circle
        canvas.drawCircle(centerX, centerY, indicatorCircleRadius, centerCirclePaint);

        //speed text
        String speedText = String.format(Locale.getDefault(), "%.1f",
                (degree - MIN_DEGREE) * getMaxSpeed() / (MAX_DEGREE - MIN_DEGREE)) + getUnit();
        speedBackgroundRect.set(centerX - (speedTextPaint.measureText(speedText) / 2f) - 5,//left
                insideCircleRect.bottom - speedTextPaint.getTextSize(),//top
                centerX + (speedTextPaint.measureText(speedText) / 2f) + 5,//right
                centerY * 1.4f + 4);//bottom
        canvas.drawRect(speedBackgroundRect, speedBackgroundPaint);
        canvas.drawText(speedText, centerX, centerY * 1.4f, speedTextPaint);

        //rotate speed text
        String rotateSpeedText = String.format(Locale.getDefault(), "%d",
                (int) ((rotateDegree - MIN_ROTATE_DEGREE) * 5000 / (MAX_ROTATE_DEGREE - MIN_ROTATE_DEGREE))) + " RPM";
        canvas.drawText(rotateSpeedText, centerX, centerY * 1.8f, rotateSpeedTextPaint);

    }


    private void init() {

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

        Paint paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND); //平滑效果
        paint.setDither(true); //防抖动
        paint.setAntiAlias(true);  //消除锯齿
        paint.setStyle(Paint.Style.STROKE);  //设置样式


        dashboardBitmapPaint = new Paint(paint);

        dashboardBitmapPaint.setFilterBitmap(true);

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

        //text align
        speedTextPaint.setTextAlign(Paint.Align.CENTER);
        rotateSpeedTextPaint.setTextAlign(Paint.Align.CENTER);

        //animator
        speedAnimator = ValueAnimator.ofFloat(0f, 1f);
        rotateSpeedAnimator = ValueAnimator.ofFloat(0f, 1f);
        trembleAnimator = ValueAnimator.ofFloat(0f, 1f);

        //set point effects,to draw a dash line
        pointPaint.setPathEffect(new DashPathEffect(new float[]{3f, 97f}, 0));

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWithEffects(withEffects);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
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
        withEffects = a.getBoolean(R.styleable.ThirdSpeedView_withEffects, withEffects);
        String unit = a.getString(R.styleable.ThirdSpeedView_unit);
        a.recycle();
        setUnit((unit != null) ? unit : getUnit());
        setWithEffects(withEffects);
    }

    private void initDraw() {
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

    private void cancel() {
        cancelSpeedMove();
        cancelTremble();
    }

    private void cancelTremble() {
        canceled = true;
        trembleAnimator.cancel();
        canceled = false;
    }

    private void cancelSpeedMove() {
        canceled = true;
        speedAnimator.cancel();
        canceled = false;
    }

    public void rotateSpeedTo(int rotateSpeed) {
        rotateSpeedTo(rotateSpeed, 2000);
    }

    public void rotateSpeedTo(int rotateSpeed, long moveDuration) {
        int maxRotateSpeed = 5000;
        //0~maxRotateSpeed
        rotateSpeed = (rotateSpeed > maxRotateSpeed) ? maxRotateSpeed : (rotateSpeed < 0) ? 0 : rotateSpeed;
        this.rotateSpeed = rotateSpeed;

        //30~150
        float newDegree = (float) rotateSpeed * (MAX_ROTATE_DEGREE - MIN_ROTATE_DEGREE) /
                maxRotateSpeed + MIN_ROTATE_DEGREE;

        if (newDegree == rotateDegree) return;

        //取消上次动画效果
        rotateSpeedAnimator.cancel();

        rotateSpeedAnimator = ValueAnimator.ofFloat(rotateDegree, newDegree);
        rotateSpeedAnimator.setInterpolator(new DecelerateInterpolator());
        rotateSpeedAnimator.setDuration(moveDuration);
        rotateSpeedAnimator.addUpdateListener(rotateListener);
        rotateSpeedAnimator.start();
    }

    @Override
    public void speedToDef() {
        speedTo(speed, 2000);
    }

    @Override
    public void speedTo(int speed) {
        speedTo(speed, 2000);
    }

    @Override
    public void speedTo(int speed, long moveDuration) {
        //0~maxSpeed
        speed = (speed > getMaxSpeed()) ? getMaxSpeed() : (speed < 0) ? 0 : speed;
        this.speed = speed;

        //135~270
        float newDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;

        if (newDegree == degree) return;

        //取消上次动画效果
        cancel();

        speedAnimator = ValueAnimator.ofFloat(degree, newDegree);
        speedAnimator.setInterpolator(new DecelerateInterpolator());
        speedAnimator.setDuration(moveDuration);
        speedAnimator.addUpdateListener(speedListener);
        speedAnimator.addListener(animatorListener);
        speedAnimator.start();
    }

    //颤抖动画
    private void tremble() {
        cancelTremble();
        if (!isWithTremble()) return;

        Random random = new Random();
        float mad = 4 * random.nextFloat() * ((random.nextBoolean()) ? -1 : 1);
        float originalDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;
        mad = (originalDegree + mad > MAX_DEGREE) ? MAX_DEGREE - originalDegree
                : (originalDegree + mad < MIN_DEGREE) ? MIN_DEGREE - originalDegree : mad;
        trembleAnimator = ValueAnimator.ofFloat(degree, originalDegree + mad);
        trembleAnimator.setInterpolator(new DecelerateInterpolator());
        trembleAnimator.setDuration(1000);
        trembleAnimator.addUpdateListener(trembleListener);
        trembleAnimator.addListener(animatorListener);
        trembleAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener rotateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            rotateDegree = (float) rotateSpeedAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private ValueAnimator.AnimatorUpdateListener speedListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            degree = (float) speedAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private ValueAnimator.AnimatorUpdateListener trembleListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            degree = (float) trembleAnimator.getAnimatedValue();
            postInvalidate();
        }
    };

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!canceled) tremble();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    @Override
    public void setSpeedometerWidth(float speedometerWidth) {
        super.setSpeedometerWidth(speedometerWidth);
        float risk = speedometerWidth / 2f;
        speedBackgroundRect.set(risk, risk, getWidth() - risk, getHeight() - risk);
        invalidate();
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
        return speed;
    }

    @Override
    public int getPercentSpeed() {
        return speed * 100 / getMaxSpeed();
    }

    public void setWithEffects(boolean withEffects) {
        this.withEffects = withEffects;
        if (withEffects) {
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

}
