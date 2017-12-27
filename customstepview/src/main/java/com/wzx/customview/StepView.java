package com.wzx.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class StepView extends View {
    private int mOuterColor = Color.BLUE;
    private int mInnerColor = Color.RED;
    private int mStepTextColor = Color.RED;
    private int mStepTextSize = 20;
    private int mBoderWidth = 16;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;
    private int mMaxStep = 100 ;
    private int mCurrentStep ;


    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOuterColor = arr.getColor(R.styleable.StepView_outerColor, mOuterColor);
        mInnerColor = arr.getColor(R.styleable.StepView_innerColor, mInnerColor);
        mStepTextColor = arr.getColor(R.styleable.StepView_stepTextColor, mStepTextColor);
        mBoderWidth = (int) arr.getDimension(R.styleable.StepView_boderWidth, mBoderWidth);
        mStepTextSize = (int) arr.getDimension(R.styleable.StepView_stepTextSize, mStepTextSize);
        mMaxStep = (int)arr.getInteger(R.styleable.StepView_maxStep,mMaxStep);
        arr.recycle();
        setStepMax(mMaxStep);
        initPaint();

    }

    public void initPaint() {
        //外圆画笔
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mBoderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStyle(Paint.Style.STROKE);

        //内圆画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBoderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.绘制外圆弧、2.绘制内圆弧、3.绘制文字
        RectF rectF = new RectF(mBoderWidth / 2, mBoderWidth / 2, getWidth() - mBoderWidth / 2, getHeight() - mBoderWidth / 2);
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);
        float sweepAngle = (float) mCurrentStep / mMaxStep;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);

        String stepText = mCurrentStep + "";
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textRect);
        //文字起点
        int dx = getWidth() / 2 - textRect.width() / 2;
        /*Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;
        int baseLine = getHeight()/2+dy;*/
        int baseLine = getHeight() / 2 + textRect.height() / 2;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }


    public synchronized void setStepMax(int maxStep) {
        this.mMaxStep = maxStep;
    }

    public synchronized void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        //c重新绘制
        invalidate();
    }
}
