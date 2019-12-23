package com.common.esimrfid.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {

    Paint textPaint;
    float mDrawTextStart;
    float mDrawTextEnd;
    float mOffset = 3;
    public TextProgressBar(Context context) {
        this(context, null);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);

    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getProgress() == 0) {
            mDrawTextStart = getPaddingLeft();
        } else {
            float right = 0.0f;
            //进度条大于四分之一显示在进度条内白色，否则显示在进度条外绿色
            if (getProgress() > getMax() / 4) {
                textPaint.setColor(Color.WHITE);
                textPaint.setTextAlign(Paint.Align.RIGHT);
                right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() - mOffset + getPaddingLeft();
                mDrawTextStart = (right - mOffset);
            } else {
                textPaint.setColor(0xFF28A701);
                textPaint.setTextAlign(Paint.Align.LEFT);
                right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + mOffset + getPaddingLeft();
                mDrawTextStart = right;
            }
            mDrawTextEnd = (int) ((getHeight() / 2.0f) - ((textPaint.descent() + textPaint.ascent()) / 2.0f));
            String progress =  "已经盘点" + getProgress();
            canvas.drawText(progress, mDrawTextStart, mDrawTextEnd, textPaint);
        }

    }

    private void initData(Context context) {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(4);
        textPaint.setTextSize(25);
        final float scale = context.getResources().getDisplayMetrics().density;
        mOffset = (int) (mOffset * scale + 0.5f);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        invalidate();
    }



}
