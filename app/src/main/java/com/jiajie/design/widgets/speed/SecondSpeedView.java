//package com.jiajie.design.widgets.speed;
//
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.BlurMaskFilter;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.RectF;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.view.animation.DecelerateInterpolator;
//
//import com.jiajie.design.R;
//
//import java.util.Locale;
//import java.util.Random;
//
///**
// * SecondSpeedView
// * Created by jiajie on 16/8/30.
// */
//public class SecondSpeedView extends SpeedView {
//
//    private static final String TAG = "SecondSpeedView";
//
//    private Path indicatorPath,
//            markPath,
//            smallMarkPath;
//
//    private Paint circlePaint,
//            centerCirclePaint,
//            indicatorPaint,
//            speedometerPaint,
//            markPaint,
//            smallMarkPaint,
//            speedBackgroundPaint;
//
//    private TextPaint speedTextPaint,
//            textPaint;
//
//    private RectF speedometerRect,
//            speedBackgroundRect;
//
//    private int speedBackgroundColor = Color.WHITE,
//            speedTextColor = Color.BLACK;
//
//    private boolean canceled = false;
//    private final int MIN_DEGREE = 135,
//            MAX_DEGREE = 135 + 270;
//    /** to rotate indicator */
//    private float degree = MIN_DEGREE;
//    private int speed = 0;
//    private ValueAnimator speedAnimator,
//            trembleAnimator;
//
//    private boolean withEffects = true;
//
//    public SecondSpeedView(Context context) {
//        super(context);
//        init();
//    }
//
//    public SecondSpeedView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//        initAttributeSet(context, attrs);
//    }
//
//    public SecondSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//        initAttributeSet(context, attrs);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
//        int size = (width > height) ? height : width;
//        setMeasuredDimension(size, size);
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        float risk = getSpeedometerWidth() / 2f;
//        speedometerRect.set(risk, risk, w - risk, h - risk);
//
//        //indicatorPath
//        float indicatorWidth = w / 32f;
//        indicatorPath.moveTo(w / 2f, h / 5f);
//        indicatorPath.lineTo(w / 2f - indicatorWidth, h * 3f / 5f);
//        indicatorPath.lineTo(w / 2f + indicatorWidth, h * 3f / 5f);
//        RectF rectF = new RectF(w / 2f - indicatorWidth, h * 3f / 5f - indicatorWidth,
//                w / 2f + indicatorWidth, h * 3f / 5f + indicatorWidth);
//        indicatorPath.addArc(rectF, 0f, 180f);
//        indicatorPath.moveTo(0f, 0f);
//
//        //markPath markPaint
//        float markHeight = h / 28f;
//        markPath.moveTo(w / 2f, 0f);
//        markPath.lineTo(w / 2f, markHeight);
//        markPath.moveTo(0f, 0f);
//        markPaint.setStrokeWidth(markHeight / 3f);
//
//        //smallMarkPath smallMarkPaint
//        float smallMarkHeight = h / 20f;
//        smallMarkPath.moveTo(w / 2f, getSpeedometerWidth());
//        smallMarkPath.lineTo(w / 2f, getSpeedometerWidth() + smallMarkHeight);
//        smallMarkPath.moveTo(0f, 0f);
//        smallMarkPaint.setStrokeWidth(3f);
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        initDraw();
//
//        int width = getWidth();
//        int height = getHeight();
//        float centerX = width / 2f;
//        float centerY = height / 2f;
//        float backgroundCircleRadius = width / 2f;
//        float indicatorCircleRadius = width / 12f;
//
//        float lowArc = 135f;
//        float mediumArc = lowArc + 160f;
//        float highArc = mediumArc + 75f;
//
//        //background circle
//        if (isWithBackgroundCircle()) {
//            canvas.drawCircle(centerX, centerY, backgroundCircleRadius, circlePaint);
//        }
//
//        //low speed
//        speedometerPaint.setColor(getLowSpeedColor());
//        canvas.drawArc(speedometerRect, lowArc, 160f, false, speedometerPaint);
//        //medium speed
//        speedometerPaint.setColor(getMediumSpeedColor());
//        canvas.drawArc(speedometerRect, mediumArc, 75f, false, speedometerPaint);
//        //high speed
//        speedometerPaint.setColor(getHighSpeedColor());
//        canvas.drawArc(speedometerRect, highArc, 35f, false, speedometerPaint);
//
//        //mark 8
//        canvas.save();
//        canvas.rotate(135f + 90f, centerX, centerY);
//        for (int i = 135; i <= 345; i += 30) {
//            canvas.rotate(30f, centerX, centerY);
//            canvas.drawPath(markPath, markPaint);
//        }
//        canvas.restore();
//
//        //small mark 26
//        canvas.save();
//        canvas.rotate(135f + 90f, centerX, centerY);
//        for (int i = 135; i < 395; i += 10) {
//            canvas.rotate(10f, centerX, centerY);
//            canvas.drawPath(smallMarkPath, smallMarkPaint);
//        }
//        canvas.restore();
//
//        //indicator
//        canvas.save();
//        canvas.rotate(90f + degree, centerX, centerY);
//        canvas.drawPath(indicatorPath, indicatorPaint);
//        canvas.restore();
//        //indicator circle
//        canvas.drawCircle(centerX, centerY, indicatorCircleRadius, centerCirclePaint);
//
//        //text and background
//        textPaint.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("00", width / 5f, height * 6f / 7f, textPaint);
//        textPaint.setTextAlign(Paint.Align.RIGHT);
//        canvas.drawText(String.format(Locale.getDefault(), "%d", getMaxSpeed()),
//                width * 4 / 5f, height * 6 / 7f, textPaint);
//        String speed = String.format(Locale.getDefault(), "%.1f",
//                (degree - MIN_DEGREE) * getMaxSpeed() / (MAX_DEGREE - MIN_DEGREE)) + getUnit();
//
//        speedBackgroundRect.set(centerX - (speedTextPaint.measureText(speed) / 2f) - 5,//left
//                speedometerRect.bottom - speedTextPaint.getTextSize(),//top
//                centerX + (speedTextPaint.measureText(speed) / 2f) + 5,//right
//                speedometerRect.bottom + 4);//bottom
//
//        canvas.drawRect(speedBackgroundRect, speedBackgroundPaint);
//        canvas.drawText(String.format(Locale.getDefault(), "%.1f",
//                (degree - MIN_DEGREE) * getMaxSpeed() / (MAX_DEGREE - MIN_DEGREE)) + getUnit(),
//                getWidth() / 2f, speedometerRect.bottom, speedTextPaint);
//
//    }
//
//    private void init() {
//        indicatorPath = new Path();
//        markPath = new Path();
//        smallMarkPath = new Path();
//
//        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        speedometerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        smallMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        speedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        speedBackgroundPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//
//        speedometerRect = new RectF();
//        speedBackgroundRect = new RectF();
//
//        speedometerPaint.setStyle(Paint.Style.STROKE);
//        markPaint.setStyle(Paint.Style.STROKE);
//        smallMarkPaint.setStyle(Paint.Style.STROKE);
//        speedTextPaint.setTextAlign(Paint.Align.CENTER);
//
//        speedAnimator = ValueAnimator.ofFloat(0f, 1f);
//        trembleAnimator = ValueAnimator.ofFloat(0f, 1f);
//
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
//        setWithEffects(withEffects);
//    }
//
//    private void initAttributeSet(Context context, AttributeSet attrs) {
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SecondSpeedView, 0, 0);
//
//        setIndicatorColor(a.getColor(R.styleable.SecondSpeedView_indicatorColor, Color.parseColor("#00ffec")));
//        setCenterCircleColor(a.getColor(R.styleable.SecondSpeedView_centerCircleColor, Color.parseColor("#e0e0e0")));
//        setMarkColor(a.getColor(R.styleable.SecondSpeedView_markColor, getMarkColor()));
//        setLowSpeedColor(a.getColor(R.styleable.SecondSpeedView_lowSpeedColor, Color.parseColor("#37872f")));
//        setMediumSpeedColor(a.getColor(R.styleable.SecondSpeedView_mediumSpeedColor, Color.parseColor("#a38234")));
//        setHighSpeedColor(a.getColor(R.styleable.SecondSpeedView_highSpeedColor, Color.parseColor("#9b2020")));
//        setTextColor(a.getColor(R.styleable.SecondSpeedView_textColor, Color.WHITE));
//        setBackgroundCircleColor(a.getColor(R.styleable.SecondSpeedView_backgroundCircleColor, Color.parseColor("#212121")));
//        speedTextColor = a.getColor(R.styleable.SecondSpeedView_speedTextColor, speedTextColor);
//        speedBackgroundColor = a.getColor(R.styleable.SecondSpeedView_speedBackgroundColor, speedBackgroundColor);
//        setSpeedometerWidth(a.getDimension(R.styleable.SecondSpeedView_speedometerWidth, getSpeedometerWidth()));
//        setMaxSpeed(a.getInt(R.styleable.SecondSpeedView_maxSpeed, getMaxSpeed()));
//        setWithTremble(a.getBoolean(R.styleable.SecondSpeedView_withTremble, isWithTremble()));
//        setWithBackgroundCircle(a.getBoolean(R.styleable.SecondSpeedView_withBackgroundCircle, isWithBackgroundCircle()));
//        withEffects = a.getBoolean(R.styleable.SecondSpeedView_withEffects, withEffects);
//        setSpeedTextSize(a.getDimension(R.styleable.SecondSpeedView_speedTextSize, getSpeedTextSize()));
//        String unit = a.getString(R.styleable.SecondSpeedView_unit);
//        a.recycle();
//        setUnit((unit != null) ? unit : getUnit());
//        setWithEffects(withEffects);
//    }
//
//    private void initDraw() {
//        indicatorPaint.setColor(getIndicatorColor());
//        speedometerPaint.setStrokeWidth(getSpeedometerWidth());
//        markPaint.setColor(getMarkColor());
//        smallMarkPaint.setColor(getMarkColor());
//        speedTextPaint.setColor(speedTextColor);
//        speedTextPaint.setTextSize(getSpeedTextSize());
//        textPaint.setColor(getTextColor());
//        speedBackgroundPaint.setColor(speedBackgroundColor);
//        centerCirclePaint.setColor(getCenterCircleColor());
//        circlePaint.setColor(getBackgroundCircleColor());
//    }
//
//    private void cancel() {
//        cancelSpeedMove();
//        cancelTremble();
//    }
//
//    private void cancelTremble() {
//        canceled = true;
//        trembleAnimator.cancel();
//        canceled = false;
//    }
//
//    private void cancelSpeedMove() {
//        canceled = true;
//        speedAnimator.cancel();
//        canceled = false;
//    }
//
//    @Override
//    public void speedToDef() {
//        speedTo(speed, 2000);
//    }
//
//    @Override
//    public void speedTo(int speed) {
//        speedTo(speed, 2000);
//    }
//
//    @Override
//    public void speedTo(int speed, long moveDuration) {
//        //0~maxSpeed
//        speed = (speed > getMaxSpeed()) ? getMaxSpeed() : (speed < 0) ? 0 : speed;
//        this.speed = speed;
//
//        //135~405
//        float newDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;
//
//        if (newDegree == degree) return;
//
//        //取消上次动画效果
//        cancel();
//
//        speedAnimator = ValueAnimator.ofFloat(degree, newDegree);
//        speedAnimator.setInterpolator(new DecelerateInterpolator());
//        speedAnimator.setDuration(moveDuration);
//        speedAnimator.addUpdateListener(speedListener);
//        speedAnimator.addListener(animatorListener);
//        speedAnimator.start();
//    }
//
//    //颤抖动画
//    private void tremble() {
//        cancelTremble();
//        if (!isWithTremble()) return;
//
//        Random random = new Random();
//        float mad = 4 * random.nextFloat() * ((random.nextBoolean()) ? -1 : 1);
//        float originalDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / getMaxSpeed() + MIN_DEGREE;
//        mad = (originalDegree + mad > MAX_DEGREE) ? MAX_DEGREE - originalDegree
//                : (originalDegree + mad < MIN_DEGREE) ? MIN_DEGREE - originalDegree : mad;
//        trembleAnimator = ValueAnimator.ofFloat(degree, originalDegree + mad);
//        trembleAnimator.setInterpolator(new DecelerateInterpolator());
//        trembleAnimator.setDuration(1000);
//        trembleAnimator.addUpdateListener(trembleListener);
//        trembleAnimator.addListener(animatorListener);
//        trembleAnimator.start();
//    }
//
//    private ValueAnimator.AnimatorUpdateListener speedListener = new ValueAnimator.AnimatorUpdateListener() {
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            degree = (float) speedAnimator.getAnimatedValue();
//            postInvalidate();
//        }
//    };
//
//    private ValueAnimator.AnimatorUpdateListener trembleListener = new ValueAnimator.AnimatorUpdateListener() {
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            degree = (float) trembleAnimator.getAnimatedValue();
//            postInvalidate();
//        }
//    };
//
//    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
//        @Override
//        public void onAnimationStart(Animator animation) {
//        }
//
//        @Override
//        public void onAnimationEnd(Animator animation) {
//            if (!canceled) tremble();
//        }
//
//        @Override
//        public void onAnimationCancel(Animator animation) {
//        }
//
//        @Override
//        public void onAnimationRepeat(Animator animation) {
//        }
//    };
//
//    @Override
//    public void setSpeedometerWidth(float speedometerWidth) {
//        super.setSpeedometerWidth(speedometerWidth);
//        float risk = speedometerWidth / 2f;
//        speedBackgroundRect.set(risk, risk, getWidth() - risk, getHeight() - risk);
//        invalidate();
//    }
//
//    @Override
//    public void setWithTremble(boolean withTremble) {
//        super.setWithTremble(withTremble);
//        tremble();
//    }
//
//    @Override
//    public void speedPercentTo(int percent) {
//        percent = (percent > 100) ? 100 : (percent < 0) ? 0 : percent;
//        speedTo(percent * getMaxSpeed() / 100);
//    }
//
//    @Override
//    public int getSpeed() {
//        return speed;
//    }
//
//    @Override
//    public int getPercentSpeed() {
//        return speed * 100 / getMaxSpeed();
//    }
//
//    public boolean isWithEffects() {
//        return withEffects;
//    }
//
//    public void setWithEffects(boolean withEffects) {
//        this.withEffects = withEffects;
//        if (withEffects) {
//            indicatorPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
//            markPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));
//            speedBackgroundPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));
//            centerCirclePaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
//        } else {
//            indicatorPaint.setMaskFilter(null);
//            markPaint.setMaskFilter(null);
//            speedBackgroundPaint.setMaskFilter(null);
//            centerCirclePaint.setMaskFilter(null);
//        }
//        invalidate();
//    }
//
//    public int getSpeedBackgroundColor() {
//        return speedBackgroundColor;
//    }
//
//    public void setSpeedBackgroundColor(int speedBackgroundColor) {
//        this.speedBackgroundColor = speedBackgroundColor;
//        invalidate();
//    }
//
//    public int getSpeedTextColor() {
//        return speedTextColor;
//    }
//
//    public void setSpeedTextColor(int speedTextColor) {
//        this.speedTextColor = speedTextColor;
//        invalidate();
//    }
//
//}
