//package com.jiajie.design.widgets.speed;
//
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
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
// * FirstSpeedView
// * Created by jiajie on 16/8/29.
// */
//public class FirstSpeedView extends SpeedView {
//
//    private static final String TAG = "FirstSpeedView";
//
//    private Path indicatorPath,//指针路径
//            markPath;//速度位置标记路径
//
//    private Paint circlePaint,//背景圆 画笔
//            indicatorPaint,
//            speedometerPaint,//圆弧指示器 画笔
//            markPaint;//速度位置标记 画笔
//
//    private TextPaint speedTextPaint,//速度文字画笔
//            textPaint;//表盘名称画笔
//
//    private RectF speedometerRect;//指针区域
//
//    private boolean canceled = false;
//
//    private final int MIN_DEGREE = 135,
//            MAX_DEGREE = 135 + 270;
//
//    /** to rotate indicator */
//    private float degree = MIN_DEGREE;
//    private int maxSpeed = 100;
//    private int speed = 0;
//
//    private ValueAnimator speedAnimator,//速度动画
//            trembleAnimator;//颤抖动画
//
//    public FirstSpeedView(Context context) {
//        super(context);
//        init();
//    }
//
//    public FirstSpeedView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//        initAttributeSet(context, attrs);
//    }
//
//    public FirstSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
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
//    /**
//     * 初始化 Path Paint RectF Animator
//     */
//    private void init() {
//        indicatorPath = new Path();
//        markPath = new Path();
//
//        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        speedometerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        speedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//
//        speedometerRect = new RectF();
//
//        speedometerPaint.setStyle(Paint.Style.STROKE);
//        markPaint.setStyle(Paint.Style.STROKE);
//        speedTextPaint.setTextAlign(Paint.Align.CENTER);
//
//        speedAnimator = ValueAnimator.ofFloat(0f, 1f);
//        trembleAnimator = ValueAnimator.ofFloat(0f, 1f);
//    }
//
//    /**
//     * 加载自定义属性
//     *
//     * @param context context
//     * @param attrs   attrs
//     */
//    private void initAttributeSet(Context context, AttributeSet attrs) {
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FirstSpeedView, 0, 0);
//
//        setIndicatorColor(a.getColor(R.styleable.FirstSpeedView_indicatorColor, getIndicatorColor()));
//        setCenterCircleColor(a.getColor(R.styleable.FirstSpeedView_centerCircleColor, getCenterCircleColor()));
//        setMarkColor(a.getColor(R.styleable.FirstSpeedView_markColor, getMarkColor()));
//        setLowSpeedColor(a.getColor(R.styleable.FirstSpeedView_lowSpeedColor, getLowSpeedColor()));
//        setMediumSpeedColor(a.getColor(R.styleable.FirstSpeedView_mediumSpeedColor, getMediumSpeedColor()));
//        setHighSpeedColor(a.getColor(R.styleable.FirstSpeedView_highSpeedColor, getHighSpeedColor()));
//        setTextColor(a.getColor(R.styleable.FirstSpeedView_textColor, getTextColor()));
//        setBackgroundCircleColor(a.getColor(R.styleable.FirstSpeedView_backgroundCircleColor, getBackgroundCircleColor()));
//        setSpeedometerWidth(a.getDimension(R.styleable.FirstSpeedView_speedometerWidth, getSpeedometerWidth()));
//        setMaxSpeed(a.getInt(R.styleable.FirstSpeedView_maxSpeed, getMaxSpeed()));
//        setWithTremble(a.getBoolean(R.styleable.FirstSpeedView_withTremble, isWithTremble()));
//        setWithBackgroundCircle(a.getBoolean(R.styleable.FirstSpeedView_withBackgroundCircle, isWithBackgroundCircle()));
//        setSpeedTextSize(a.getDimension(R.styleable.FirstSpeedView_speedTextSize, getSpeedTextSize()));
//        String unit = a.getString(R.styleable.FirstSpeedView_unit);
//        a.recycle();
//        setUnit((unit != null) ? unit : getUnit());
//    }
//
//    /**
//     * 获取自定义属性的值，为Paint设置属性
//     */
//    private void initDraw() {
//        speedometerPaint.setStrokeWidth(getSpeedometerWidth());
//
//        markPaint.setColor(getMarkColor());
//
//        speedTextPaint.setColor(getTextColor());
//        speedTextPaint.setTextSize(getSpeedTextSize());
//
//        textPaint.setColor(getTextColor());
//        circlePaint.setColor(getBackgroundCircleColor());
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        float risk = getSpeedometerWidth() / 2f;
//        speedometerRect.set(risk, risk, w - risk, h - risk);
//
//        float indicatorWidth = w / 32f;
//        indicatorPath.moveTo(w / 2f, 0f);
//        indicatorPath.lineTo(w / 2f - indicatorWidth, h * 2f / 3f);
//        indicatorPath.lineTo(w / 2f + indicatorWidth, h * 2f / 3f);
//        RectF rectF = new RectF(w / 2f - indicatorWidth, h * 2f / 3f - indicatorWidth,
//                w / 2f + indicatorWidth, h * 2f / 3f + indicatorWidth);
//        indicatorPath.addArc(rectF, 0f, 180f);
//        indicatorPath.moveTo(0f, 0f);
//
//        float markHeight = h / 28f;
//        markPath.moveTo(w / 2f, 0f);
//        markPath.lineTo(w / 2f, markHeight);
//        markPath.moveTo(0f, 0f);
//        markPaint.setStrokeWidth(markHeight / 3f);
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
//
//        //画出背景圆
//        if (isWithBackgroundCircle()) {
//            canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint);
//        }
//
//        //画出低速
//        speedometerPaint.setColor(getLowSpeedColor());
//        canvas.drawArc(speedometerRect, 135f, 160f, false, speedometerPaint);
//        //画出中速
//        speedometerPaint.setColor(getMediumSpeedColor());
//        canvas.drawArc(speedometerRect, 135f + 160f, 75f, false, speedometerPaint);
//        //画出高速
//        speedometerPaint.setColor(getHighSpeedColor());
//        canvas.drawArc(speedometerRect, 135f + 160f + 75f, 35f, false, speedometerPaint);
//
//        //保存
//        canvas.save();
//        //旋转canvas 90度
//        canvas.rotate(135f + 90f, width / 2f, height / 2f);
//
//        //画8个mark 点
//        for (int i = 135; i <= 345; i += 30) {
//            canvas.rotate(30f, width / 2f, height / 2f);
//            canvas.drawPath(markPath, markPaint);
//        }
//        canvas.restore();
//
//        //画指针（指针和中间的圆）
//        indicatorPaint.setColor(getIndicatorColor());
//        canvas.save();
//
//        canvas.rotate(90f + degree, width / 2f, height / 2f);
//        canvas.drawPath(indicatorPath, indicatorPaint);
//        canvas.restore();
//
//        indicatorPaint.setColor(getCenterCircleColor());
//        canvas.drawCircle(width / 2f, height / 2f, width / 12f, indicatorPaint);
//
//        //画 显示速度(0.0Km/h)
//        textPaint.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("00", width / 6f, height * 7f / 8f, textPaint);
//        textPaint.setTextAlign(Paint.Align.RIGHT);
//        canvas.drawText(String.format(Locale.getDefault(), "%d", maxSpeed),
//                width * 5f / 6f, height * 7f / 8f, textPaint);
//        canvas.drawText(String.format(Locale.getDefault(), "%.1f",
//                (degree - MIN_DEGREE) * maxSpeed / (MAX_DEGREE - MIN_DEGREE)) + getUnit(),
//                width / 2f, speedometerRect.bottom, speedTextPaint);
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
//        speed = (speed > maxSpeed) ? maxSpeed : (speed < 0) ? 0 : speed;
//        this.speed = speed;
//
//        //135~270
//        float newDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / maxSpeed + MIN_DEGREE;
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
//        float originalDegree = (float) speed * (MAX_DEGREE - MIN_DEGREE) / maxSpeed + MIN_DEGREE;
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
//
//    @Override
//    public void speedPercentTo(int percent) {
//        percent = (percent > 100) ? 100 : (percent < 0) ? 0 : percent;
//        speedTo(percent * maxSpeed / 100);
//    }
//
//    @Override
//    public int getSpeed() {
//        return speed;
//    }
//
//    @Override
//    public int getPercentSpeed() {
//        return speed * 100 / maxSpeed;
//    }
//
//    @Override
//    public int getMaxSpeed() {
//        return maxSpeed;
//    }
//
//    @Override
//    public void setMaxSpeed(int maxSpeed) {
//        if (maxSpeed <= 0) return;
//        this.maxSpeed = maxSpeed;
//        speedTo(speed);
//        invalidate();
//    }
//
//    @Override
//    public void setSpeedometerWidth(float speedometerWidth) {
//        super.setSpeedometerWidth(speedometerWidth);
//        float risk = speedometerWidth / 2f;
//        speedometerRect.set(risk, risk, getWidth() - risk, getHeight() - risk);
//        invalidate();
//    }
//
//    @Override
//    public void setWithTremble(boolean withTremble) {
//        super.setWithTremble(withTremble);
//        tremble();
//    }
//
//
//}
